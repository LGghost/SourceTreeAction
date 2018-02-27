package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.DatePicker;
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
import cn.order.ordereasy.adapter.CustomerThingsListAdapter;
import cn.order.ordereasy.bean.Fahuo;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.MainActivity;
import cn.order.ordereasy.widget.DownListView;
import cn.order.ordereasy.widget.LoadMoreListView;

public class InventoryChangeRecordActivity extends BaseActivity implements OrderEasyView, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    OrderEasyPresenter orderEasyPresenter;
    private int pageCurrent = 1, pageTotal = 1;
    private AlertDialog alertDialog;
    private String begindate = "", enddate = "";
    private String beginTime, endTime, time;
    private String dialogStart, dialogEnd;
    private int type = 0, user_id = -1;
    private List<Fahuo> fahuos = new ArrayList<>();
    private CustomerThingsListAdapter customerThingsListAdapter;
    private int is_boss;
    private JsonArray arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_change_record);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String userinfo = spPreferences.getString("userinfo", "");
        if (!TextUtils.isEmpty(userinfo)) {
            JsonObject user = (JsonObject) GsonUtils.getObj(userinfo, JsonObject.class);
            is_boss = user.get("is_boss").getAsInt();
            arr = user.getAsJsonArray("auth_group_ids");

        }
        initSetOnListener();
    }

    private void initSetOnListener() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        listview.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        listview.setOnItemClickListener(this);
        customerThingsListAdapter = new CustomerThingsListAdapter(this);
        listview.setAdapter(customerThingsListAdapter);
        inventory_employee.setHigh(false);
        inventory_record.setHigh(false);
        inventory_employee.setText("全部员工");
        if (DataStorageUtils.getInstance().getYuangongLists().size() > 0) {
            if (!DataStorageUtils.getInstance().getYuangongLists().get(0).getName().equals("全部员工")) {
                TopicLabelObject topicLabelObject = new TopicLabelObject(-1, 0, "全部员工", 0);
                DataStorageUtils.getInstance().getYuangongLists().add(0, topicLabelObject);
            }
            inventory_employee.setItemsData(DataStorageUtils.getInstance().getYuangongLists(), 0);
        } else {
            if (!isSalesperson() && !isStockman()) {
                orderEasyPresenter.getEmployee(1);
            }
        }

        inventory_record.setItemsData(getList(this.getResources().getStringArray(R.array.stock_array)), 0);
        inventory_employee.setDownItemClickListener(new DownListView.DownItemClickListener() {
            @Override
            public void selected(TopicLabelObject topic) {//员工
                user_id = topic.getId();
                pageCurrent = 1;
                refreshData(false);
            }

            @Override
            public void onClick(boolean isShow) {
                if (isShow) {
                    mask.setVisibility(View.VISIBLE);
                } else {
                    mask.setVisibility(View.GONE);
                }
            }
        });
        inventory_record.setDownItemClickListener(new DownListView.DownItemClickListener() {
            @Override
            public void selected(TopicLabelObject topic) {//全部记录
                type = topic.getId();
                pageCurrent = 1;
                refreshData(false);
            }

            @Override
            public void onClick(boolean isShow) {
                if (isShow) {
                    mask.setVisibility(View.VISIBLE);
                } else {
                    mask.setVisibility(View.GONE);
                }
            }
        });
        refreshData(true);
    }

    private List<TopicLabelObject> getList(String[] list) {
        List<TopicLabelObject> topicList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            TopicLabelObject object;
            if (i == 0) {
                object = new TopicLabelObject(i, -1, list[i], 1);
            } else {
                object = new TopicLabelObject(i, -1, list[i], 0);
            }
            topicList.add(object);
        }
        return topicList;
    }

    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.time_text)
    TextView time_text;
    @InjectView(R.id.inventory_employee)
    DownListView inventory_employee;
    @InjectView(R.id.inventory_record)
    DownListView inventory_record;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listview;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.mask)
    View mask;

    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.inventory_time_layout)
    void inventory_time_layout() {//所有时间段
        showdialogs();
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
        store_refresh.setRefreshing(false);
    }

    @Override
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
        listview.setLoadCompleted();
        store_refresh.setRefreshing(false);
        if (type == 2) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                //String message=result.get("message").getAsString();
                if (status == 1) {
                    //分页处理
                    JsonObject page = data.getAsJsonObject("result").getAsJsonObject("page");
                    Log.e("page", page.toString());
                    pageCurrent = page.get("cur_page").getAsInt();
                    pageTotal = page.get("page_total").getAsInt();
                    if (pageCurrent == 1) fahuos = new ArrayList<>();
                    //处理返回的数据
                    JsonArray stocks = data.getAsJsonObject("result").getAsJsonArray("page_list");
                    Log.e("Inventory", "data:" + data.getAsJsonObject("result").toString());
                    if (stocks.size() != 0) {
                        for (int i = 0; i < stocks.size(); i++) {
                            //循环遍历获取的数据，并转成实体
                            Fahuo fahuo = (Fahuo) GsonUtils.getEntity(stocks.get(i).toString(), Fahuo.class);
                            fahuos.add(fahuo);
                        }
                        customerThingsListAdapter.setData(fahuos);
                        customerThingsListAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.show("没有更多数据了");
                        listview.setIsLoading(true);
                    }
                    if (customerThingsListAdapter.getData().size() > 0) {
                        no_data_view.setVisibility(View.GONE);
                    } else {
                        no_data_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
        if (type == 1) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                //String message=result.get("message").getAsString();
                if (status == 1) {
                    JsonArray jsonArray = data.get("result").getAsJsonArray();
                    List<TopicLabelObject> mapList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        String name = jsonObject.get("name").getAsString();
                        if (jsonObject.get("is_boss").getAsInt() == 1) {
                            name = name + "(老板)";
                        }
                        TopicLabelObject topicLabelObject1 = new TopicLabelObject(jsonObject.get("user_id").getAsInt(), 1, name, 0);
                        mapList.add(topicLabelObject1);
                    }
                    if (!mapList.get(0).getName().equals("全部员工")) {
                        TopicLabelObject topicLabelObject = new TopicLabelObject(-1, 0, "全部员工", 0);
                        mapList.add(0, topicLabelObject);
                    }
                    DataStorageUtils.getInstance().setYuangongLists(mapList);
                    inventory_employee.setItemsData(mapList, 0);
                }
            }
        }
    }

    @Override
    public void onloadMore() {
        //上拉加载更多 完成时关闭

        if (pageTotal == pageCurrent) {
            ToastUtil.show("没有更多数据了");
            listview.setIsLoading(true);
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
        int type = customerThingsListAdapter.getData().get(position).getOperate_type();

        if (type == 4 || type == 5) {
            Intent intent = new Intent(this, OperationRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", customerThingsListAdapter.getData().get(position).getOperate_id());
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, AdjustingRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", customerThingsListAdapter.getData().get(position).getOperate_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view_time, null);
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
                begindate = "";
                enddate = "";
                dialogStart = "";
                dialogEnd = "";
                time = "所有时间段";
                time_text.setText(time);
                refreshData(false);
                alertDialog.dismiss();
            }
        });
        //开始时间点击事件
        final TextView kaishi_time = (TextView) window.findViewById(R.id.kaishi_time);
        if (TextUtils.isEmpty(dialogStart)) {
            kaishi_time.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        } else {
            kaishi_time.setText(dialogStart);
        }
        kaishi_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InventoryChangeRecordActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        dialogStart = year + "-" + TimeUtil.getDate(month) + "-" + TimeUtil.getDate(dayOfMonth);
                        kaishi_time.setText(dialogStart);
                    }
                }, TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDay()).show();
            }
        });

        //结束时间点击事件
        final TextView jieshu_time = (TextView) window.findViewById(R.id.jieshu_time);
        if (TextUtils.isEmpty(dialogEnd)) {
            jieshu_time.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        } else {
            jieshu_time.setText(dialogEnd);
        }
        jieshu_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(InventoryChangeRecordActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        dialogEnd = year + "-" + TimeUtil.getDate(month) + "-" + TimeUtil.getDate(dayOfMonth);
                        jieshu_time.setText(dialogEnd);
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
                pageCurrent = 1;
                begindate = kaishi_time.getText().toString();
                enddate = jieshu_time.getText().toString();
                beginTime = TimeUtil.getDate(begindate);
                endTime = TimeUtil.getDate(enddate);
                time = beginTime + "-" + endTime;
                time_text.setText(time);
                refreshData(false);
                alertDialog.dismiss();
            }
        });
    }

    private void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getOperationRecordList(pageCurrent, type, user_id, begindate, enddate);
    }

    public boolean isSalesperson() {
        if (is_boss != 1) {
            if (arr.size() == 1) {
                for (int i = 0; i < arr.size(); i++) {
                    if (!arr.get(i).getAsString().equals("")) {
                        if (arr.get(i).getAsInt() == 2) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    public boolean isStockman() {
        if (is_boss != 1) {
            if (arr.size() == 1) {
                for (int i = 0; i < arr.size(); i++) {
                    if (!arr.get(i).getAsString().equals("")) {
                        if (arr.get(i).getAsInt() == 3) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }
}
