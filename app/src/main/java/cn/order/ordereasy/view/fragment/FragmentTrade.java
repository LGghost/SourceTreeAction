package cn.order.ordereasy.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import cn.order.ordereasy.adapter.BookingAdapter2;
import cn.order.ordereasy.adapter.TradeCustomerAdapter;
import cn.order.ordereasy.adapter.popuAdapter;
import cn.order.ordereasy.bean.Mylist2;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.bean.TradeCustomer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;
import cn.order.ordereasy.widget.LoadMoreListView;

//交易
public class FragmentTrade extends Fragment implements OrderEasyView, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenter orderEasyPresenter;
    private AlertDialog alertDialog;
    private String begindate = "";
    private String enddate = "";
    private String startTime = "";
    private String endTime = "";
    private List<TopicLabelObject> mapList = new ArrayList<>();
    private BookingAdapter2 adapter2 = null;
    private TradeCustomerAdapter tradeAdapter = null;
    private PopupWindow popupWindow;
    private int day = 0;//日期 全部 0，
    private int pageCurrent = 1, pageTotal = 1;
    private int type = 1;
    private String user_id = "0";
    private List<Mylist2> mylist1 = new ArrayList<>();
    private List<TradeCustomer> mylist2 = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.the_books_view_one, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        if (DataStorageUtils.getInstance().getYuangongLists().size() > 0) {
            mapList = DataStorageUtils.getInstance().getYuangongLists();
        } else {
            orderEasyPresenter.getEmployee(5);
        }
        day = 0;
        refreshData(true);
        lay_2_text.setText("全部员工");
    }

    @InjectView(R.id.trade_text)
    TextView trade_text;
    @InjectView(R.id.lay_2_text)
    TextView lay_2_text;
    @InjectView(R.id.lay_2)
    LinearLayout lay_2;
    @InjectView(R.id.lay_3_text)
    TextView lay_3_text;
    @InjectView(R.id.in)
    TextView in;
    @InjectView(R.id.out)
    TextView out;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.mask)
    View mask;

    @OnClick(R.id.btn_trade)
    void btn_trade() {//全部交易
        showDialog();
    }


    @OnClick(R.id.lay_2)
    void lay_2() {//全店账本
        mask.setVisibility(View.VISIBLE);
        showPopupWindow();
    }

    @OnClick(R.id.lay_3)
    void lay_3() {//货品
        ActionSheetDialog actionSheet = new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("货品", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        lay_3_text.setText("货品");
                        type = 1;
                        tradeAdapter = null;
                        refreshData(true);
                    }
                }).addSheetItem("客户", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        lay_3_text.setText("客户");
                        type = 2;
                        adapter2 = null;
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
        Log.e("FragmentTrade", "hideProgress:" + type);
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        listView.setLoadCompleted();
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        int dataSize = 0;
        ProgressUtil.dissDialog();
        listView.setLoadCompleted();
        store_refresh.setRefreshing(false);

        if (type == 4) {//全部交易数据
            if (data.get("code").getAsInt() == 1)//表示请求成功
            {
                //分页处理
                JsonObject page = data.getAsJsonObject("result").getAsJsonObject("page");
                if (pageCurrent == 1) {
                    Log.e("LOG", "----3");
                    if (mylist1.size() > 0) {
                        mylist1.clear();
                    }
                    Log.e("LOG", "----4");
                    if (mylist2.size() > 0) {
                        mylist2.clear();
                    }
                }
                pageTotal = page.get("page_total").getAsInt();

                out.setText(data.getAsJsonObject("result").getAsJsonObject("total").get("trade_sum").getAsString());
                in.setText(data.getAsJsonObject("result").getAsJsonObject("total").get("sale_num").getAsString());
                JsonArray array = data.getAsJsonObject("result").getAsJsonArray("page_list");
                if (this.type == 1) {
                    if (pageCurrent != 1) {
                        dataSize = mylist1.size();
                    }
                    if (array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            Mylist2 list = new Mylist2();
                            list.goods_id = array.get(i).getAsJsonObject().get("goods_id").getAsInt();
                            list.goods_no = array.get(i).getAsJsonObject().get("goods_no").getAsString();
                            list.title = array.get(i).getAsJsonObject().get("title").getAsString();
                            list.cover = array.get(i).getAsJsonObject().get("cover").getAsString();
                            list.sale_num = array.get(i).getAsJsonObject().get("sale_num").getAsString();
                            list.trade_sum = array.get(i).getAsJsonObject().get("trade_sum").getAsString();
                            mylist1.add(list);
                        }

                        if (adapter2 == null) {
                            adapter2 = new BookingAdapter2(getActivity());
                            adapter2.setData(mylist1);
                            listView.setAdapter(adapter2);
                        } else {
                            adapter2.setData(mylist1);
                            listView.setAdapter(adapter2);
                        }
                        Log.e("FragmentTrade", "pageCurrent：" + pageCurrent);
                        if (pageCurrent != 1) {
                            listView.setSelection(dataSize - 1);
                        } else {
                            listView.setSelection(0);
                        }
                        store_refresh.setVisibility(View.VISIBLE);
                        no_data_view.setVisibility(View.GONE);
                    } else {
                        if (mylist1.size() > 0) {
                            mylist1.clear();
                        }
                        adapter2.setData(mylist1);
                        store_refresh.setVisibility(View.GONE);
                        no_data_view.setVisibility(View.VISIBLE);

                    }
                } else {
                    Log.e("LOG", "----5");
                    if (pageCurrent != 1) {
                        dataSize = mylist2.size();
                    }
                    Log.e("LOG", "----6");
                    if (array.size() > 0) {
                        for (int i = 0; i < array.size(); i++) {
                            TradeCustomer tradeCustomer = new TradeCustomer();
                            tradeCustomer.setCustomer_id(array.get(i).getAsJsonObject().get("customer_id").getAsInt());
                            tradeCustomer.setTrade_sum(array.get(i).getAsJsonObject().get("trade_sum").getAsDouble());
                            tradeCustomer.setSale_num(array.get(i).getAsJsonObject().get("sale_num").getAsInt());
                            tradeCustomer.setCustomer_name(array.get(i).getAsJsonObject().get("customer_name").getAsString());
                            tradeCustomer.setLevel(array.get(i).getAsJsonObject().get("level").getAsInt());
                            mylist2.add(tradeCustomer);
                        }
                        Log.e("LOG", "----7");
                        if (tradeAdapter == null) {
                            tradeAdapter = new TradeCustomerAdapter(getActivity(), 1);
                            tradeAdapter.setData(mylist2);
                            listView.setAdapter(tradeAdapter);
                            Log.e("LOG", "----8");
                        } else {
                            Log.e("LOG", "----9");
                            tradeAdapter.setData(mylist2);
                            listView.setAdapter(tradeAdapter);
                            Log.e("LOG", "----10");
                        }
                        if (pageCurrent != 1) {
                            Log.e("LOG", "----11");
                            listView.setSelection(dataSize - 1);
                        } else {
                            Log.e("LOG", "----12");
                            listView.setSelection(0);
                        }
                        store_refresh.setVisibility(View.VISIBLE);
                        no_data_view.setVisibility(View.GONE);
                    } else {
                        if (mylist2.size() > 0) {
                            mylist2.clear();
                        }
                        tradeAdapter.setData(mylist2);
                        store_refresh.setVisibility(View.GONE);
                        no_data_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else if (type == 5) {//员工数据
            JsonArray jsonArray = data.get("result").getAsJsonArray();
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    String name = jsonObject.get("name").getAsString();
                    if (jsonObject.get("is_boss").getAsInt() == 1) {
                        name = name + "(老板)";
                    }
                    TopicLabelObject topicLabelObject1 = new TopicLabelObject(jsonObject.get("user_id").getAsInt(), 1, name, 0);
                    mapList.add(topicLabelObject1);
                }
                DataStorageUtils.getInstance().setYuangongLists(mapList);
            }
        }
    }


    private void showdialogs(final int type) {
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        View view = View.inflate(getActivity(), R.layout.tanchuang_view_time, null);
        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_time);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("选择时间");

        //所有时间点击事件
        TextView all_time = (TextView) window.findViewById(R.id.all_time);
        all_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageCurrent = 1;
                day = 0;
                trade_text.setText("全部交易");
                begindate = "";
                enddate = "";
                refreshData(true);
                alertDialog.dismiss();
            }
        });
        //开始时间点击事件
        final TextView kaishi_time = (TextView) window.findViewById(R.id.kaishi_time);
        kaishi_time.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        kaishi_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        kaishi_time.setText(year + "-" + month + "-" + dayOfMonth);
//                        begindate = year + "-" + month + "-" + dayOfMonth;
//                        startTime = month + "." + dayOfMonth;
                    }
                }, TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDay()).show();
            }
        });

        //结束时间点击事件
        final TextView jieshu_time = (TextView) window.findViewById(R.id.jieshu_time);
        jieshu_time.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        jieshu_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        jieshu_time.setText(year + "-" + month + "-" + dayOfMonth);
//                        enddate = year + "-" + month + "-" + dayOfMonth;
//                        endTime = month + "." + dayOfMonth;
                    }
                }, TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDay()).show();
            }
        });


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                begindate = kaishi_time.getText().toString();
                startTime = TimeUtil.getDate(begindate);
                enddate = jieshu_time.getText().toString();
                endTime = TimeUtil.getDate(enddate);
                if (!TextUtils.isEmpty(begindate) && !TextUtils.isEmpty(enddate)) {
                    trade_text.setText(startTime + "-" + endTime);
                    pageCurrent = 1;
                    day = -1;
                    refreshData(true);
                } else {
                    ToastUtil.show("请选择日期");
                }
                alertDialog.dismiss();
            }
        });
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(getActivity());
        }
        orderEasyPresenter.booking1(day, pageCurrent, type, user_id, begindate, enddate);
    }

    private void showDialog() {
        begindate = "";
        enddate = "";
        ActionSheetDialog actionSheet = new ActionSheetDialog(getActivity())
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("今日数据", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 1;
                        trade_text.setText("今日数据");
                        refreshData(true);
                    }
                })
                .addSheetItem("昨日数据", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 2;
                        trade_text.setText("昨日数据");
                        refreshData(true);
                    }
                }).addSheetItem("7日数据", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 7;
                        trade_text.setText("7日数据");
                        refreshData(true);
                    }
                }).addSheetItem("30日数据", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 30;
                        trade_text.setText("30日数据");
                        refreshData(true);
                    }
                }).addSheetItem("日历", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        showdialogs(0);
                        trade_text.setText("时间段数据");
                    }
                }).addSheetItem("全部交易", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        pageCurrent = 1;
                        day = 0;
                        begindate = "";
                        enddate = "";
                        refreshData(true);
                        trade_text.setText("全部交易");
                    }
                });

        actionSheet.show();
    }

    private void showPopupWindow() {
        View view = getActivity().getLayoutInflater().inflate(R.layout.popup_window, null);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtil.getWindowsH(getActivity()) / 2 - 100);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(lay_2, 0, 20, Gravity.CENTER_HORIZONTAL);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mask.setVisibility(View.GONE);
            }
        });
        ListView populist = (ListView) view.findViewById(R.id.popup_window_listview);
        popuAdapter adapter = new popuAdapter(getActivity(), mapList);
        populist.setAdapter(adapter);
        adapter.setMyItemClickListener(new popuAdapter.MyItemClickListener() {
            @Override
            public void selected(TopicLabelObject labelObject, int position) {
                lay_2_text.setText(labelObject.getName());
                pageCurrent = 1;
                if (position == 0) {
                    user_id = "0";
                    refreshData(true);
                } else {
                    user_id = labelObject.getId() + "";
                    refreshData(true);
                }
                popupWindow.dismiss();
            }
        });

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
}