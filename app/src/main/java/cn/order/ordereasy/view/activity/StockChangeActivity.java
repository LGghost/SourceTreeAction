package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;

public class StockChangeActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenterImp orderEasyPresenter;
    private AlertDialog alertDialog;
    private String begindate = "", enddate = "";
    private String startTime;
    private String endTime;
    private int day = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.the_books_view_four);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        top_layout.setVisibility(View.VISIBLE);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        refreshData();
        time_text.setText("今日");
    }

    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.top_layout)
    RelativeLayout top_layout;
    @InjectView(R.id.in)
    TextView in;
    @InjectView(R.id.out)
    TextView out;
    @InjectView(R.id.tv1)
    TextView tv1;
    @InjectView(R.id.tv2)
    TextView tv2;
    @InjectView(R.id.tv3)
    TextView tv3;
    @InjectView(R.id.tv4)
    TextView tv4;
    @InjectView(R.id.tv5)
    TextView tv5;
    @InjectView(R.id.tv6)
    TextView tv6;
    @InjectView(R.id.time_text)
    TextView time_text;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.data_click2)
    void data_click2() {//时间
        showDialog();
    }

    @OnClick(R.id.layout)
    void layout() {
        Intent intent = new Intent(this, InventoryChangeRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
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
        Log.e("FragmentStock", "loadData:" + type);
        store_refresh.setRefreshing(false);
        if (data != null) {
            if (data.get("code").getAsInt() == 1) {
                //绑定数据
                in.setText(data.getAsJsonObject("result").getAsJsonObject("total").get("in_number").getAsInt() + "");
                out.setText(data.getAsJsonObject("result").getAsJsonObject("total").get("out_number").getAsInt() + "");

                int i11 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("in_store").get("in_number").getAsInt();
                int i12 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("in_store").get("out_number").getAsInt();

                tv1.setText((i11 + i12) + "");

                int i21 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("out_store").get("in_number").getAsInt();
                int i22 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("out_store").get("out_number").getAsInt();

                tv2.setText((i21 + i22) + "");

                int i31 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("deliver").get("in_number").getAsInt();
                int i32 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("deliver").get("out_number").getAsInt();

                tv3.setText((i31 + i32) + "");

                int i41 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("return").get("in_number").getAsInt();
                int i42 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("return").get("out_number").getAsInt();

                tv4.setText((i41 + i42) + "");

                int i51 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("manual_adjust").get("in_number").getAsInt();
                int i52 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("manual_adjust").get("out_number").getAsInt();

                tv5.setText("+" + i51 + "(-" + i52 + ")");

                int i61 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("adjust").get("in_number").getAsInt();
                int i62 = data.getAsJsonObject("result").getAsJsonObject("list").getAsJsonObject("adjust").get("out_number").getAsInt();

                tv6.setText("+" + i61 + "(-" + i62 + ")");
            }
        }
    }

    private void showdialogs(final int type) {
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
                time_text.setText("全部");
                begindate = "";
                enddate = "";
                refreshData();
                alertDialog.dismiss();
            }
        });
        //开始时间点击事件
        final TextView kaishi_time = (TextView) window.findViewById(R.id.kaishi_time);
        kaishi_time.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        kaishi_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(StockChangeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
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
                new DatePickerDialog(StockChangeActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth) {
                        int month = monthOfYear + 1;
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
//
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
                    day = -1;
                    refreshData();
                    time_text.setText(startTime + "-" + endTime);
                } else {
                    ToastUtil.show("请选择日期");
                }
                alertDialog.dismiss();
            }
        });
    }

    private void showDialog() {
        begindate = "";
        enddate = "";
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("今日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        day = 1;
                        refreshData();
                        time_text.setText("今日");
                    }
                })
                .addSheetItem("昨日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        day = 2;
                        refreshData();
                        time_text.setText("昨日");
                    }
                }).addSheetItem("7日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        day = 7;
                        refreshData();
                        time_text.setText("7日");
                    }
                }).addSheetItem("30日", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        time_text.setText("30日");
                        day = 30;
                        refreshData();
                    }
                }).addSheetItem("日历", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        showdialogs(1);
                        time_text.setText("时间段");
                    }
                }).addSheetItem("全部", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        day = 0;
                        refreshData();
                        time_text.setText("全部");
                    }
                });

        actionSheet.show();
    }

    public void refreshData() {
        orderEasyPresenter.booking4(day, begindate, enddate);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }
}