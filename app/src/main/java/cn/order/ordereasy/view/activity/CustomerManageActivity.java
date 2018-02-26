package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import cn.order.ordereasy.adapter.CustomerManageAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.DiscountCustomer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class CustomerManageActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, BGAOnItemChildClickListener, AdapterView.OnItemClickListener {
    private OrderEasyPresenter orderEasyPresenter;
    private List<DiscountCustomer> list = new ArrayList<>();
    private CustomerManageAdapter adapter;
    private String flag = "manage";
    private int index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_manage_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            index = bundle.getInt("rank_id");
        }
        store_refresh.setOnRefreshListener(this);
        adapter = new CustomerManageAdapter(this, flag);
        list_view.setAdapter(adapter);
        adapter.setOnItemChildClickListener(this);
        list_view.setOnItemClickListener(this);
        if (DataStorageUtils.getInstance().getDiscountCustomers().size() > 0) {
            list = DataStorageUtils.getInstance().getDiscountCustomers();
            adapter.setIndex(index);
            adapter.setData(list);
        } else {
            refreshData(true);
        }
    }

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.customer_add)
    void customer_add() {
        Intent intent = new Intent(this, AddCustomerSortActivity.class);
        startActivityForResult(intent, 1001);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DataStorageUtils.getInstance().isAddCustomer()) {//根据isAddCustomer字段来判断刷新数据（添加分类完成isAddCustomer设置为true）
            DataStorageUtils.getInstance().setAddCustomer(false);
            orderEasyPresenter.getCustomerList1();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            refreshData(false);
        }
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        if (type == 0) {
            Log.e("CustomerManageActivity", "data:" + data.toString());
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //处理返回的数据
                JsonArray stocks = data.getAsJsonArray("result");
                list.clear();
                for (int i = 0; i < stocks.size(); i++) {
                    //循环遍历获取的数据，并转成实体
                    DiscountCustomer dCustomer = (DiscountCustomer) GsonUtils.getEntity(stocks.get(i).toString(), DiscountCustomer.class);
                    list.add(dCustomer);
                }
                adapter.setIndex(index);
                adapter.setData(list);
                if (list.size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }
        }
        if (type == 4) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    Log.e("FragmentStore", "result:" + data.toString());
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
                        datas.add(customer);
                    }
                    DataStorageUtils.getInstance().setCustomerLists(datas);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.customerRankList();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            orderEasyPresenter.customerdelRank(adapter.getData().get(position).getRank_id());
            list.remove(position);
            adapter.closeOpenedSwipeItemLayout();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (flag.equals("newCustomer")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", adapter.getData().get(position));
            intent.putExtras(bundle);
            setResult(1001, intent);
            finish();
        } else {
            Intent intent = new Intent(this, AddCustomerSortActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("flag", "edit");
            bundle.putSerializable("data", adapter.getData().get(position));
            intent.putExtras(bundle);
            startActivityForResult(intent, 1001);
        }
    }
}
