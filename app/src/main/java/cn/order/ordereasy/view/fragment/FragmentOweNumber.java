package cn.order.ordereasy.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.BookingAdapter;
import cn.order.ordereasy.adapter.BookingAdapter2;
import cn.order.ordereasy.adapter.TradeCustomerAdapter;
import cn.order.ordereasy.bean.Mylist1;
import cn.order.ordereasy.bean.TradeCustomer;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;
import cn.order.ordereasy.widget.LoadMoreListView;

//欠数
public class FragmentOweNumber extends Fragment implements OrderEasyView, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenterImp orderEasyPresenter;
    private BookingAdapter adapter = null;
    private TradeCustomerAdapter tradeAdapter = null;
    private int type = 1, pageCurrent = 1, pageTotal = 1;
    private List<Mylist1> lists1 = new ArrayList<>();
    private List<TradeCustomer> mylist2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.the_books_view_two, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        listview1.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        refreshData(true);
    }

    @InjectView(R.id.id1)
    TextView id1;
    @InjectView(R.id.id2)
    TextView id2;
    @InjectView(R.id.id3)
    TextView id3;
    @InjectView(R.id.text_type)
    TextView text_type;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listview1;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;


    @OnClick(R.id.lay_click)
    void lay_click() {//货品
        ActionSheetDialog actionSheet = new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("货品", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        text_type.setText("货品");
                        type = 1;
                        pageCurrent = 1;
                        if (mylist2.size() > 0) {
                            mylist2.clear();
                        }
                        tradeAdapter = null;
                        refreshData(true);
                    }
                })
                .addSheetItem("客户", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        type = 2;
                        pageCurrent = 1;
                        text_type.setText("客户");
                        Log.e("FragmentOweNumber", "111111");
                        if (lists1.size() > 0) {
                            lists1.clear();
                        }
                        adapter = null;
                        Log.e("FragmentOweNumber", "22222");
                        refreshData(true);
                    }
                });

        actionSheet.show();
    }

    @Override
    public void showProgress(int type) {
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
            no_data_view.setVisibility(View.VISIBLE);
        }
        listview1.setLoadCompleted();
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        int dataSize = 0;
        ProgressUtil.dissDialog();
        listview1.setLoadCompleted();
        store_refresh.setRefreshing(false);
        Log.e("FragmentOweNumber", "loadData:" + type);
        if (data != null) {
            if (data.get("code").getAsInt() == 1) {
                //分页处理
                JsonObject page = data.getAsJsonObject("result").getAsJsonObject("page");
                Log.e("FragmentOweNumber", "page:" + page.toString());
                if (pageCurrent == 1) {
                    Log.e("FragmentOweNumber", "3333333");
                    if (lists1.size() > 0) {
                        lists1.clear();
                    }
                    Log.e("FragmentOweNumber", "444444");
                    if (mylist2.size() > 0) {
                        mylist2.clear();
                    }
                    Log.e("FragmentOweNumber", "55555555");
                }
                pageTotal = page.get("page_total").getAsInt();
                Log.e("FragmentOweNumber", "pageTotal:" + pageTotal);
                id1.setText("¥" + data.getAsJsonObject("result").getAsJsonObject("total").get("receivable").getAsDouble());
                id2.setText("¥" + data.getAsJsonObject("result").getAsJsonObject("total").get("payable").getAsDouble());
                id3.setText("¥" + data.getAsJsonObject("result").getAsJsonObject("total").get("owe_num").getAsInt());
                JsonArray array = data.getAsJsonObject("result").getAsJsonArray("page_list");
                if (this.type == 1) {
                    if (pageCurrent != 1) {
                        dataSize = lists1.size();
                    }
                    if (array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            Mylist1 mylist1 = new Mylist1();
                            mylist1.title = array.get(i).getAsJsonObject().get("title").getAsString();
                            mylist1.goods_id = array.get(i).getAsJsonObject().get("goods_id").getAsInt();
                            mylist1.cover = array.get(i).getAsJsonObject().get("cover").getAsString();
                            mylist1.store_num = array.get(i).getAsJsonObject().get("store_num").getAsString();
                            mylist1.goods_no = array.get(i).getAsJsonObject().get("goods_no").getAsString();
                            mylist1.owe_num = array.get(i).getAsJsonObject().get("owe_num").getAsString();
                            lists1.add(mylist1);
                        }
                        if (adapter == null) {
                            adapter = new BookingAdapter(getActivity());
                            adapter.setData(lists1);
                            listview1.setAdapter(adapter);
                        } else {
                            adapter.setData(lists1);
                            listview1.setAdapter(adapter);
                        }
                        if (pageCurrent != 1) {
                            listview1.setSelection(dataSize - 1);
                        } else {
                            listview1.setSelection(0);
                        }
                        no_data_view.setVisibility(View.GONE);
                    } else {
                        ToastUtil.show("没有更多数据了");
                        listview1.setIsLoading(true);
                    }
                } else {
                    if (pageCurrent != 1) {
                        dataSize = mylist2.size();
                    }
                    if (array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            TradeCustomer tradeCustomer = new TradeCustomer();
                            tradeCustomer.setCustomer_id(array.get(i).getAsJsonObject().get("customer_id").getAsInt());
                            tradeCustomer.setTrade_sum(array.get(i).getAsJsonObject().get("receivable").getAsDouble());
                            tradeCustomer.setSale_num(array.get(i).getAsJsonObject().get("owe_num").getAsInt());
                            tradeCustomer.setCustomer_name(array.get(i).getAsJsonObject().get("customer_name").getAsString());
                            tradeCustomer.setLevel(array.get(i).getAsJsonObject().get("level").getAsInt());
                            mylist2.add(tradeCustomer);
                        }
                        if (tradeAdapter == null) {
                            tradeAdapter = new TradeCustomerAdapter(getActivity(), 2);
                            tradeAdapter.setData(mylist2);
                            listview1.setAdapter(tradeAdapter);
                        } else {
                            tradeAdapter.setData(mylist2);
                            listview1.setAdapter(tradeAdapter);
                        }
                        if (pageCurrent != 1) {
                            listview1.setSelection(dataSize - 1);
                        } else {
                            listview1.setSelection(0);
                        }
                        store_refresh.setVisibility(View.VISIBLE);
                        no_data_view.setVisibility(View.GONE);
                    } else {
                        mylist2.clear();
                        tradeAdapter.setData(mylist2);
                        store_refresh.setVisibility(View.GONE);
                        no_data_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(getActivity());
        }
        orderEasyPresenter.booking2(type, pageCurrent);
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
        pageCurrent++;
        refreshData(false);

    }
}