package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.AllTradeAdapter;
import cn.order.ordereasy.bean.Trade;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;
import cn.order.ordereasy.widget.LoadMoreListView;

public class AllTradeActivity extends BaseActivity implements OrderEasyView, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenter orderEasyPresenter;
    private int day = 0;//日期 全部 0，
    private int pageCurrent = 1, pageTotal = 1;
    private int customer_id;
    private String customer_name;
    private List<Trade> tradeList = new ArrayList<>();
    private AllTradeAdapter allAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_trade_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customer_id = bundle.getInt("customer_id");
            customer_name = bundle.getString("customer_name");
            all_trade_frist_name.setText(customer_name.subSequence(0, 1));
            all_trade_name.setText(customer_name);
        }
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        allAdapter = new AllTradeAdapter(this);
        listView.setOnLoadMoreListener(this);
        listView.setAdapter(allAdapter);
        store_refresh.setOnRefreshListener(this);
        refreshData(true);
    }

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.all_trade_frist_name)
    TextView all_trade_frist_name;
    @InjectView(R.id.all_trade_name)
    TextView all_trade_name;
    @InjectView(R.id.all_trade_phone)
    TextView all_trade_phone;
    @InjectView(R.id.a_turnover)
    TextView a_turnover;
    @InjectView(R.id.sales_volume)
    TextView sales_volume;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    @OnClick(R.id.title)
    void title() {//全部交易
        showDialog();
    }

    @OnClick(R.id.return_click)
    void return_click() {//返回
        finish();
    }

    private void showDialog() {
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("今日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 1;
                        title.setText("今日");
                        refreshData(true);
                    }
                })
                .addSheetItem("昨日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 2;
                        title.setText("昨日");
                        refreshData(true);
                    }
                }).addSheetItem("7日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 7;
                        title.setText("7日");
                        refreshData(true);
                    }
                }).addSheetItem("30日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 30;
                        title.setText("30日");
                        refreshData(true);
                    }
                }).addSheetItem("全部交易", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 0;
                        refreshData(true);
                        title.setText("全部交易");
                    }
                });

        actionSheet.show();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        Log.e("AllTradeActivity", "hideProgress:" + type);
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
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
                tradeList.clear();
            }
            pageTotal = page.get("page_total").getAsInt();
            a_turnover.setText("总交易额：" + data.getAsJsonObject("result").getAsJsonObject("total").get("trade_sum").getAsString());
            sales_volume.setText("总销售量：" + data.getAsJsonObject("result").getAsJsonObject("total").get("sale_num").getAsString());
            JsonArray array = data.getAsJsonObject("result").getAsJsonArray("page_list");
            if (pageCurrent != 1) {
                dataSize = tradeList.size();
            }
            if (array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    String object = array.get(i).getAsJsonObject().toString();
                    Log.e("AllTradeActivity", "object:" + object.toString());
                    Trade trade = (Trade) GsonUtils.getEntity(object, Trade.class);
                    tradeList.add(trade);
                }
                Log.e("AllTradeActivity", "tradeList:" + tradeList.size());
                allAdapter.setData(tradeList);
                listView.setAdapter(allAdapter);
                if (pageCurrent != 1) {
                    listView.setSelection(dataSize - 1);
                } else {
                    listView.setSelection(0);
                }
                store_refresh.setVisibility(View.VISIBLE);
                no_data_view.setVisibility(View.GONE);
            } else {
                store_refresh.setVisibility(View.GONE);
                no_data_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新 完成时关闭
        pageCurrent = 1;
        refreshData(false);
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

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.customerTransactionRecord(customer_id, day, 0, pageCurrent);
    }
}