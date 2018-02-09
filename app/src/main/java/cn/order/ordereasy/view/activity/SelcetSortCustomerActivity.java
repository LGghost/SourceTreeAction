package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SelcetSortAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.DiscountCustomer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class SelcetSortCustomerActivity extends BaseActivity implements BGAOnItemChildClickListener, OrderEasyView {
    private String flag = "";
    private SelcetSortAdapter adapter;
    private int rank_id;
    private List<Customer> customerList = new ArrayList<>();
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selcet_sort_customer_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            rank_id = bundle.getInt("rank_id");
        }
        adapter = new SelcetSortAdapter(this);
        list_view.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        if (flag.equals("edit")) {
            initData();
        }else{
            adapter.setData(DataStorageUtils.getInstance().getSelectCustomer());
        }
    }

    private void initData() {
        ProgressUtil.showDialog(this);
        orderEasyPresenter.getCustomerList1();
    }


    @InjectView(R.id.list_view)
    ListView list_view;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    @OnClick(R.id.return_click)
    void return_click() {
        if (!flag.equals("edit")) {
            String str = "";
            for (int i = 0; i < adapter.getData().size(); i++) {
                if (i == adapter.getData().size() - 1) {
                    str += adapter.getData().get(i).getCustomer_id();
                } else {
                    str += adapter.getData().get(i).getCustomer_id() + ",";
                }
            }
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putString("list", str);
            intent.putExtras(bundle);
            setResult(1002, intent);
        }
        finish();
    }

    @OnClick(R.id.customer_add)
    void customer_add() {
        Intent intent = new Intent(this, SelectCustomerListViewActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            List<Customer> cusData = DataStorageUtils.getInstance().getSelectCustomer();
            if (flag.equals("edit")) {
                List<String> customer_ids = new ArrayList<>();
                for (Customer customer : cusData) {
                    customer_ids.add(customer.getCustomer_id() + "");
                }
                ProgressUtil.showDialog(this);
                orderEasyPresenter.customertoRank(customer_ids, rank_id);
            }
            customerList.removeAll(cusData);
            customerList.addAll(cusData);
            adapter.setData(customerList);
            if(customerList.size() > 0){
                no_data_view.setVisibility(View.GONE);
            }else{
                no_data_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            Customer customer = customerList.get(position);
            customer.setRank_id(0);
            orderEasyPresenter.updateCustomer(customer);
            customerList.remove(position);
            adapter.closeOpenedSwipeItemLayout();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
        if (type == 0) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                DataStorageUtils.getInstance().cleanSelectCustomer();
                ToastUtil.show("添加成功");
            }
        }
        if (type == 1) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                ToastUtil.show("修改成功");
            }
        }
        if (type == 4) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    Log.e("FragmentStore","result:"+ data.toString());
                    List<Customer> datas = new ArrayList<>();
                    JsonArray jsonArray = data.get("result").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(), Customer.class);
                        String name = "";
                        if (TextUtils.isEmpty(customer.getName())) {
                            name = "-";
                        } else {
                            name = customer.getName();
                        }
                        customer.setName(name);
                        if(customer.getIs_retail() == 1){
                            DataStorageUtils.getInstance().setRetailCustomer(customer);
                        }
                        datas.add(customer);
                    }
                    DataStorageUtils.getInstance().setCustomerLists(datas);
                    for (Customer customer : datas) {
                        if (customer.getRank_id() == rank_id) {
                            customerList.add(customer);
                        }
                    }
                    adapter.setData(customerList);
                    if(customerList.size() > 0){
                        no_data_view.setVisibility(View.GONE);
                    }else{
                        no_data_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }
}