package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ArrearsAdapter;
import cn.order.ordereasy.bean.ArrearsBean;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.LoadMoreListView;

public class PurchaseRecordActivity extends BaseActivity implements AdapterView.OnItemClickListener, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, OrderEasyView {
    private List<ArrearsBean> list = new ArrayList<>();
    private ArrearsAdapter adapter;
    private SupplierBean bean;
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_record_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);//网络请求
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (SupplierBean) bundle.getSerializable("data");
            purchase_name.setText(bean.getName());
            refreshData(true);
        }
        listView.setOnItemClickListener(this);
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        adapter = new ArrearsAdapter(this);
        listView.setAdapter(adapter);
    }

//    private void setData() {
//
//        ArrearsBean arrearsBean = new ArrearsBean();
//        arrearsBean.setCreate_time("1516867571");
//        arrearsBean.setCustomer_id(1);
//        arrearsBean.setCustomer_name("北京供应商");
//        arrearsBean.setIs_adjustment(1);
//        arrearsBean.setLog_id(0);
//        arrearsBean.setMoney(150.00);
//        arrearsBean.setRemark("调整欠供应商款项");
//        arrearsBean.setTotal_debt(150.00);
//
//        ArrearsBean arrearsBean2 = new ArrearsBean();
//        arrearsBean2.setCreate_time("1516867571");
//        arrearsBean2.setCustomer_id(1);
//        arrearsBean2.setCustomer_name("长沙供应商");
//        arrearsBean2.setIs_adjustment(0);
//        arrearsBean2.setLog_id(0);
//        arrearsBean2.setMoney(60.0);
//        arrearsBean2.setTotal_debt(152.00);
//        arrearsBean2.setType(4);
//        arrearsBean2.setUser_name("员工1");
//
//        ArrearsBean arrearsBean3 = new ArrearsBean();
//        arrearsBean3.setCreate_time("1516867571");
//        arrearsBean3.setCustomer_id(1);
//        arrearsBean3.setCustomer_name("深圳供应商");
//        arrearsBean3.setIs_adjustment(0);
//        arrearsBean3.setLog_id(0);
//        arrearsBean3.setMoney(200.0);
//        arrearsBean3.setTotal_debt(92.00);
//        arrearsBean3.setType(3);
//        arrearsBean3.setUser_name("员工2");
//
//        ArrearsBean arrearsBean4 = new ArrearsBean();
//        arrearsBean4.setCreate_time("1516867571");
//        arrearsBean4.setCustomer_id(1);
//        arrearsBean4.setCustomer_name("上海供应商");
//        arrearsBean4.setIs_adjustment(0);
//        arrearsBean4.setLog_id(0);
//        arrearsBean4.setMoney(1290.0);
//        arrearsBean4.setTotal_debt(292.00);
//        arrearsBean4.setType(2);
//        arrearsBean4.setUser_name("员工3");
//        arrearsBean4.setDelete_time("212312334125");
//
//        ArrearsBean arrearsBean5 = new ArrearsBean();
//        arrearsBean5.setCreate_time("1516867571");
//        arrearsBean5.setCustomer_id(1);
//        arrearsBean5.setCustomer_name("北京供应商");
//        arrearsBean5.setIs_adjustment(0);
//        arrearsBean5.setLog_id(0);
//        arrearsBean5.setMoney(1582.0);
//        arrearsBean5.setTotal_debt(1582.00);
//        arrearsBean5.setType(1);
//        arrearsBean5.setUser_name("员工4");
//        arrearsBean5.setDelete_time("1234123414");
//
//        list.add(arrearsBean);
//        list.add(arrearsBean2);
//        list.add(arrearsBean3);
//        list.add(arrearsBean4);
//        list.add(arrearsBean5);
//    }

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.purchase_name)
    TextView purchase_name;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrearsBean arrearsBean = adapter.getData().get(position);
        if (arrearsBean.getIs_adjustment() == 1) {
            ToastUtil.show("调整记录不可点击");
            return;
        }
        switch (arrearsBean.getType()) {
            case 1:
            case 2:
                Intent intent = new Intent(this, ProcurementDetailsActivity.class);
                Bundle bundle1 = new Bundle();
                intent.putExtra("id",arrearsBean.getOrder_id());
                intent.putExtra("order_no","0");
                intent.putExtras(bundle1);
                startActivity(intent);
                break;
            case 3:
            case 4:
                Intent intent1 = new Intent(this, CashierDetailsActivity.class);
                Bundle bundle = new Bundle();
                Money money = new Money();
                money.setCustomer_name(bean.getName());
                money.setCustomer_id(bean.getSupplier_id());
                if (arrearsBean.getType() == 3) {
                    money.setPayment_type(1);
                } else {
                    money.setPayment_type(0);
                }
                money.setMoney(arrearsBean.getMoney());
                money.setCreate_time(arrearsBean.getCreate_time());
                money.setUser_name(arrearsBean.getUser_name());
                money.setPayment_way(0);
                bundle.putSerializable("data", money);
                bundle.putString("tel", bean.getMobile());
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onloadMore() {
        ToastUtil.show("没有更多数据了");
        listView.setLoadCompleted();
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.suplierAccountLog(bean.getSupplier_id());
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("PurchaseRecordActivity", "data:" + data.toString());
        if (data != null) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //成功
                list.clear();
                JsonArray jsonArray = data.getAsJsonObject("result").getAsJsonArray("list");
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        ArrearsBean arrearsBean = (ArrearsBean) GsonUtils.getEntity(jsonArray.get(i).toString(), ArrearsBean.class);
                        list.add(arrearsBean);
                    }
                    adapter.setData(list);
                }
                if (jsonArray.size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}