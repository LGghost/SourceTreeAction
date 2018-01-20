package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OweAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Trade;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.LoadMoreListView;

public class OweActivity extends BaseActivity implements OrderEasyView, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenter orderEasyPresenter;
    private Customer customer;
    private OweAdapter adapter;
    private List<Trade> oweTrade = new ArrayList<>();
    private int pageCurrent = 1, pageTotal = 1;
    private String number;
    private boolean isChang = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owe_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customer = (Customer) bundle.getSerializable("data");
            refreshData(true);
        }
        adapter = new OweAdapter(this);
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
    }

    //找到控件ID
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        if (isChang) {
            setResult(1003);
        }
        finish();
    }

    @OnClick(R.id.owe_fahuo)
    void owe_fahuo() {
        if (oweTrade.size() > 0) {
            Intent intent = new Intent(this, DeliverGoodsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("flag", "cust");
            bundle.putSerializable("data", customer);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1003);
        } else {
            showToast("暂无发货！");
            return;
        }

    }

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;


    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1003) {
            isChang = true;
            number = data.getExtras().getString("number");
            refreshData(false);
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        int dataSize = 0;
        ProgressUtil.dissDialog();
        listView.setLoadCompleted();
        store_refresh.setRefreshing(false);
        if (data.get("code").getAsInt() == 1)//表示请求成功
        {
            //分页处理
            JsonObject page = data.getAsJsonObject("result").getAsJsonObject("page");
            if (pageCurrent == 1) {
                oweTrade.clear();
            }
            pageTotal = page.get("page_total").getAsInt();
            JsonArray array = data.getAsJsonObject("result").getAsJsonArray("page_list");
            if (pageCurrent != 1) {
                dataSize = oweTrade.size();
            }
            if (array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    String object = array.get(i).getAsJsonObject().toString();
                    Trade trade = (Trade) GsonUtils.getEntity(object, Trade.class);
                    if (trade != null) {
                        oweTrade.add(trade);
                    }
                }
                adapter.setData(oweTrade);
                listView.setAdapter(adapter);
                if (pageCurrent != 1) {
                    listView.setSelection(dataSize - 1);
                } else {
                    listView.setSelection(0);
                }
                if (oweTrade.size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }else {
                no_data_view.setVisibility(View.VISIBLE);
            }
        }
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.customerTransactionRecord(customer.getCustomer_id(), 0, 1, pageCurrent);
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isChang) {
                setResult(1003);
            }
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}