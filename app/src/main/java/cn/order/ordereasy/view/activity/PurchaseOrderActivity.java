package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
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
import cn.order.ordereasy.adapter.CustomerOrderListAdapter;
import cn.order.ordereasy.adapter.PurchaseOrderAdapter;
import cn.order.ordereasy.bean.ArrearsBean;
import cn.order.ordereasy.bean.DropdownItemObject;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.bean.SupplierBean;
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
import cn.order.ordereasy.view.fragment.FragmentOrder;
import cn.order.ordereasy.view.fragment.FragmentStore;
import cn.order.ordereasy.widget.DownListView;
import cn.order.ordereasy.widget.DropdownButton;
import cn.order.ordereasy.widget.DropdownListItemView;
import cn.order.ordereasy.widget.DropdownListView;
import cn.order.ordereasy.widget.LoadMoreListView;

public class PurchaseOrderActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, AdapterView.OnItemClickListener {
    private static final int ID_TYPE_ALL = 0;
    private static final String TYPE_ALL = "所有时间段";

    private static final int ID_LABEL_ALL = -1;
    private static final String LABEL_ALL = "全部员工";

    private static final String ORDER_REPLY_TIME = "全部订单";
    private static final int ID_ORDER_REPLY_TIME = 51;

    View mask;
    DropdownButton chooseType, chooseLabel, chooseOrder;
    DropdownListView dropdownType, dropdownLabel, dropdownOrder;
    Animation dropdown_in, dropdown_out, dropdown_mask_out;
    AlertDialog alertDialog;
    private List<TopicLabelObject> labels = new ArrayList<>();
    private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
    private static final String TAG = FragmentStore.class.getSimpleName();
    OrderEasyPresenter orderEasyPresenter;
    List<OrderList> orders = new ArrayList<>();
    PurchaseOrderAdapter adapter;
    //当前页数
    private int currentPage = 1;
    private String begindate, enddate;
    private String beginTime, endTime, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_order_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        //listView = (ListView) getActivity().findViewById(R.id.listView);
        mask = findViewById(R.id.mask);
        chooseType = (DropdownButton) findViewById(R.id.chooseType);
        chooseLabel = (DropdownButton) findViewById(R.id.chooseLabel);
        chooseOrder = (DropdownButton) findViewById(R.id.chooseOrder);
        dropdownType = (DropdownListView) findViewById(R.id.dropdownType);
        dropdownLabel = (DropdownListView) findViewById(R.id.dropdownLabel);
        dropdownOrder = (DropdownListView) findViewById(R.id.dropdownOrder);
        chooseLabel.setText(LABEL_ALL);
        dropdown_in = AnimationUtils.loadAnimation(this, R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(this, R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(this, R.anim.dropdown_mask_out);

        dropdownButtonsController.init();


        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
            }
        });
        chooseType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogs();
            }
        });

        dropdownButtonsController.flushCounts();
        dropdownButtonsController.flushAllLabels();
        //dropdownButtonsController.flushMyLabels();

        View view = (DropdownListItemView) LayoutInflater.from(this).inflate(R.layout.dropdown_manage_btn, dropdownType, false);
        dropdownType.addView(view);
        initRefreshLayout();
        adapter = new PurchaseOrderAdapter(this);
        //if(listView)
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        if (DataStorageUtils.getInstance().getOrderLists().size() > 0) {
            dropdownButtonsController.addType(DataStorageUtils.getInstance().getYuangongLists());
        } else {
            orderEasyPresenter.getEmployee(1);
        }
        refreshData(1, true);
    }


    @OnClick(R.id.sousuo)
    void sousuo() {
        Intent intent = new Intent(this, SearchOrderActivity.class);
        intent.putExtra("flag","purchase");
        startActivity(intent);
    }

    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("PurchaseOrderActivity", "onResume");
        if (DataStorageUtils.getInstance().isPurchaseBilling()) {//根据isPurchaseBilling字段来判断刷新数据（采购开单完成isPurchaseBilling设置为true）
            DataStorageUtils.getInstance().setPurchaseBilling(false);
            refreshData(1, false);
        }
    }
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.listView)
    LoadMoreListView listView;

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
                time = "所有时间段";
                chooseType.setText(time);
                refreshData(1, false);
                alertDialog.dismiss();
            }
        });
        //开始时间点击事件
        final TextView kaishi_time = (TextView) window.findViewById(R.id.kaishi_time);
        kaishi_time.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        kaishi_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PurchaseOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        beginTime = month + "." + dayOfMonth;
                        String monthDate;
                        String day;
                        if (month < 10) {
                            monthDate = "0" + month;
                        } else {
                            monthDate = month + "";
                        }
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = dayOfMonth + "";
                        }
                        kaishi_time.setText(year + "-" + monthDate + "-" + day);
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
                new DatePickerDialog(PurchaseOrderActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
                        endTime = month + "." + dayOfMonth;
                        String monthDate;
                        String day;
                        if (month < 10) {
                            monthDate = "0" + month;
                        } else {
                            monthDate = month + "";
                        }
                        if (dayOfMonth < 10) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = dayOfMonth + "";
                        }
                        jieshu_time.setText(year + "-" + monthDate + "-" + day);
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
                currentPage = 1;
                begindate = kaishi_time.getText().toString();
                enddate = jieshu_time.getText().toString();
                if (beginTime == null || endTime == null) {
                    beginTime = TimeUtil.getDate(begindate);
                    endTime = TimeUtil.getDate(enddate);
                }
                time = beginTime + "-" + endTime;
                chooseType.setText(time);
                refreshData(1, false);
                alertDialog.dismiss();
            }
        });
    }

    private void initRefreshLayout() {
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        listView.setLoadCompleted();
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
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
                    DataStorageUtils.getInstance().setYuangongLists(mapList);
                    dropdownButtonsController.addType(mapList);
                }
            }
        } else if (type == 0) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    Log.e("PurchaseOrderActivity", "data：" + data.toString());
                    //列表显示
                    JsonArray jsonArray = data.getAsJsonObject("result").getAsJsonArray("list");
                    if (jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            OrderList good = (OrderList) GsonUtils.getEntity(jsonArray.get(i).toString(), OrderList.class);
                            orders.add(good);
                        }
                        adapter.setData(orders);
                        if (adapter.getData().size() > 0) {
                            no_data_view.setVisibility(View.GONE);
                        } else {
                            no_data_view.setVisibility(View.VISIBLE);
                        }
                    } else {
                        ToastUtil.show("没有更多数据了");
                        listView.setIsLoading(true);
                    }
                }
            }
        }
    }

    @Override
    public void onloadMore() {
        Log.e("FragmentOrder", "正在加载更多");
        listView.setLoadCompleted();
        currentPage++;
        refreshData(currentPage, false);
    }

    @Override
    public void onRefresh() {
        //下拉刷新
        refreshData(1, false);
    }


    private class DropdownButtonsController implements DropdownListView.Container {
        private DropdownListView currentDropdownList;
        private List<DropdownItemObject> datasetType = new ArrayList<>();//全部分类
        private List<DropdownItemObject> datasetAllLabel = new ArrayList<>();//全部类型
        private List<DropdownItemObject> datasetOrder = new ArrayList<>(3);//全部排序

        @Override
        public void show(DropdownListView view) {
            if (currentDropdownList != null) {
                currentDropdownList.clearAnimation();
                currentDropdownList.startAnimation(dropdown_out);
                currentDropdownList.setVisibility(View.GONE);
                currentDropdownList.button.setChecked(false);
            }
            currentDropdownList = view;
            mask.clearAnimation();
            mask.setVisibility(View.VISIBLE);
            currentDropdownList.clearAnimation();
            currentDropdownList.startAnimation(dropdown_in);
            currentDropdownList.setVisibility(View.VISIBLE);
            currentDropdownList.button.setChecked(true);
        }

        @Override
        public void hide() {
            if (currentDropdownList != null) {
                currentDropdownList.clearAnimation();
                currentDropdownList.startAnimation(dropdown_out);
                currentDropdownList.button.setChecked(false);
                mask.clearAnimation();
                mask.startAnimation(dropdown_mask_out);
            }
            currentDropdownList = null;
        }

        @Override
        public void onSelectionChanged(DropdownListView view) {

            refreshData(1, false);
        }

        void reset() {
            chooseType.setChecked(false);
            chooseLabel.setChecked(false);
            chooseOrder.setChecked(false);

            dropdownType.setVisibility(View.GONE);
            dropdownLabel.setVisibility(View.GONE);
            dropdownOrder.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownType.clearAnimation();
            dropdownLabel.clearAnimation();
            dropdownOrder.clearAnimation();
            mask.clearAnimation();
        }

        void init() {
            reset();
            //时间段
            datasetType.add(new DropdownItemObject(TYPE_ALL, ID_TYPE_ALL, "all"));
            //datasetType.add(new DropdownItemObject(TYPE_MY, ID_TYPE_MY, "my"));
            dropdownType.bind(datasetType, chooseType, this, ID_ORDER_REPLY_TIME);

            //员工
            datasetAllLabel.add(new DropdownItemObject(LABEL_ALL, ID_LABEL_ALL, "0"));

            //排序
            datasetOrder.add(new DropdownItemObject(ORDER_REPLY_TIME, ID_ORDER_REPLY_TIME, "0"));
            datasetOrder.add(new DropdownItemObject("欠货订单", 1, "1"));
            datasetOrder.add(new DropdownItemObject("已完成", 2, "2"));
            datasetOrder.add(new DropdownItemObject("已关闭", 3, "3"));
            datasetOrder.add(new DropdownItemObject("退货单", 4, "4"));

            dropdownOrder.bind(datasetOrder, chooseOrder, this, ID_ORDER_REPLY_TIME);


            dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (currentDropdownList == null) {
                        reset();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }

        private List<DropdownItemObject> getCurrentLabels() {
            return datasetAllLabel;
        }


        public void flushCounts() {
            datasetType.get(ID_TYPE_ALL).setSuffix(" (" + "5" + ")");
            dropdownType.flush();
            dropdownLabel.flush();
        }

        void flushAllLabels() {
            flushLabels(datasetAllLabel);
        }

        void addType(List<TopicLabelObject> items) {
            for (TopicLabelObject object : items) {
                int id = object.getId();
                String name = object.getName();
                DropdownItemObject item = new DropdownItemObject(name, id, String.valueOf(id));
                datasetAllLabel.add(item);
            }

            dropdownLabel.bind(datasetAllLabel, chooseLabel, this, ID_TYPE_ALL);
        }

        private void flushLabels(List<DropdownItemObject> targetList) {

            while (targetList.size() > 1) targetList.remove(targetList.size() - 1);
            for (int i = 0, n = labels.size(); i < n; i++) {
                if (labels.size() < 1) continue;
                int id = labels.get(i).getId();
                String name = labels.get(i).getName();
                if (TextUtils.isEmpty(name)) continue;
                int topicsCount = labels.get(i).getCount();
                DropdownItemObject item = new DropdownItemObject(name, id, String.valueOf(id));
                targetList.add(item);
                datasetAllLabel.add(item);
            }
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, ProcurementDetailsActivity.class);
        OrderList order1 = adapter.getData().get(position);
        intent.putExtra("id",order1.getOrder_id());
        intent.putExtra("order_no",order1.getOrder_no());
        startActivity(intent);
    }


    public void refreshData(int page, boolean isRefres) {
        if (isRefres) {
            ProgressUtil.showDialog(this);
        }

        String filter_type = "";
        if (dropdownOrder.current != null) {
            filter_type = dropdownOrder.current.value;
        }

        String user_id = "";
        if (dropdownLabel.current != null) {
            user_id = dropdownLabel.current.value;
        }
        if (page == 1) {
            orders.clear();
        }
        orderEasyPresenter.supplierOrderList(page, filter_type, user_id, begindate, enddate);

    }

}
