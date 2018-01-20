package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.CustomerMoneyListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.LoadMoreListView;

public class CashierRecordActivity extends BaseActivity implements OrderEasyView, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    OrderEasyPresenter orderEasyPresenter;
    CustomerMoneyListAdapter customerMoneyListAdapter;
    private int pageCurrent = 1, pageTotal = 1;
    List<Money> moneys = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.cashier_record_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        customerMoneyListAdapter = new CustomerMoneyListAdapter(this);
        listView.setAdapter(customerMoneyListAdapter);
        initData();
    }

    private void initData() {
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        listView.setOnItemClickListener(this);
        refreshData(true);
    }

    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        Log.e("FragmentTrade", "hideProgress:" + type);
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        listView.setLoadCompleted();
        if (data != null) {
            int status = data.get("code").getAsInt();
            //String message=result.get("message").getAsString();
            if (status == 1) {
                //分页处理
                JsonObject page = data.getAsJsonObject("result").getAsJsonObject("page");
                MyLog.e("page", page.toString());
                pageCurrent = page.get("cur_page").getAsInt();
                pageTotal = page.get("page_total").getAsInt();
                if (pageCurrent == 1) moneys = new ArrayList<>();
                //处理返回的数据
                JsonArray stocks = data.getAsJsonObject("result").getAsJsonArray("page_list");
                Log.e("CashierRecordActivity", "stocks:" + data.getAsJsonObject("result").toString());
                for (int i = 0; i < stocks.size(); i++) {
                    //循环遍历获取的数据，并转成实体
                    Money money = (Money) GsonUtils.getEntity(stocks.get(i).toString(), Money.class);
                    moneys.add(money);
                }
                customerMoneyListAdapter.setData(moneys);
                customerMoneyListAdapter.notifyDataSetChanged();

                if (customerMoneyListAdapter.getData().size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            } else {
                if (status == -7) {
                    ToastUtil.show(getString(R.string.landfall_overdue));
                    Intent intent = new Intent(CashierRecordActivity.this, LoginActity.class);
                    startActivity(intent);
                }
            }
        }
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getOrderRecordLlist(-1, pageCurrent + "");
    }

    @Override
    public void onloadMore() {
        //上拉加载更多 完成时关闭
        if (pageTotal == pageCurrent) {
            ToastUtil.show("没有更多数据了");
            listView.setIsLoading(true);
        } else {
            pageCurrent++;
            refreshData(false);
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新 完成时关闭
        pageCurrent = 1;
        refreshData(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Money money = customerMoneyListAdapter.getData().get(position);
        List<Customer> list = DataStorageUtils.getInstance().getCustomerLists();
        Customer customer = null;
        if (list.size() > 0) {
            for (Customer customer1 : list) {
                if (money.getCustomer_id() == customer1.getCustomer_id()) {
                    customer = customer1;
                    break;
                }
            }
        }
        Intent intent = new Intent(this, CashierDetailsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", customerMoneyListAdapter.getData().get(position));
        bundle.putString("tel", customer.getTelephone() + "");
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
