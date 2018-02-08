package cn.order.ordereasy.view.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
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
import cn.order.ordereasy.view.activity.CashierDetailsActivity;
import cn.order.ordereasy.view.activity.CashierRecordActivity;
import cn.order.ordereasy.view.activity.CustomerHomepageActivity;
import cn.order.ordereasy.widget.ActionSheetDialog;

//收支
public class FragmentReceivables extends Fragment implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenterImp orderEasyPresenter;
    private AlertDialog alertDialog;
    private String begindate = "";
    private String enddate = "";
    private int day = 1;
    private String startTime;
    private String endTime;
    private String dialogStart, dialogEnd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.the_books_view_three, container, false);
        ButterKnife.inject(this, view);
        intData();
        return view;
    }

    private void intData() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        refreshData();
        time_text.setText("今日");
    }

    @InjectView(R.id.in)
    TextView in;
    @InjectView(R.id.out)
    TextView out;
    @InjectView(R.id.tv1in)
    TextView tv1in;
    @InjectView(R.id.tv1out)
    TextView tv1out;
    @InjectView(R.id.tv2in)
    TextView tv2in;
    @InjectView(R.id.tv2out)
    TextView tv2out;
    @InjectView(R.id.tv3in)
    TextView tv3in;
    @InjectView(R.id.tv3out)
    TextView tv3out;
    @InjectView(R.id.tv4in)
    TextView tv4in;
    @InjectView(R.id.tv4out)
    TextView tv4out;
    @InjectView(R.id.tv5in)
    TextView tv5in;
    @InjectView(R.id.tv5out)
    TextView tv5out;
    @InjectView(R.id.time_text)
    TextView time_text;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    @OnClick(R.id.data_click)
    void data_click() {//时间
        showDialog();
    }

    @OnClick(R.id.layout)
    void layout() {
        Intent intent= new Intent(getActivity(), CashierRecordActivity.class);
        startActivity(intent);
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(getActivity());
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
        store_refresh.setRefreshing(false);
        Log.e("FragmentReceivables", "loadData:" + type);
        if (data != null) {
            if (data.get("code").getAsInt() == 1) {

                double d1in = data.getAsJsonObject("result").getAsJsonObject("cash").get("in_money").getAsDouble();
                double d1out = data.getAsJsonObject("result").getAsJsonObject("cash").get("out_money").getAsDouble();
                Log.e("TheBooks", "" + d1in);
                tv1in.setText(d1in + "");
                tv1out.setText(d1out + "");
                double d2in = data.getAsJsonObject("result").getAsJsonObject("alipay").get("in_money").getAsDouble();
                double d2out = data.getAsJsonObject("result").getAsJsonObject("alipay").get("out_money").getAsDouble();
                tv2in.setText(d2in + "");
                tv2out.setText(d2out + "");
                double d3in = data.getAsJsonObject("result").getAsJsonObject("wechat").get("in_money").getAsDouble();
                double d3out = data.getAsJsonObject("result").getAsJsonObject("wechat").get("out_money").getAsDouble();
                tv3in.setText(d3in + "");
                tv3out.setText(d3out + "");
                double d4in = data.getAsJsonObject("result").getAsJsonObject("bank_card").get("in_money").getAsDouble();
                double d4out = data.getAsJsonObject("result").getAsJsonObject("bank_card").get("out_money").getAsDouble();
                tv4in.setText(d4in + "");
                tv4out.setText(d4out + "");
                double d5in = data.getAsJsonObject("result").getAsJsonObject("other").get("in_money").getAsDouble();
                double d5out = data.getAsJsonObject("result").getAsJsonObject("other").get("out_money").getAsDouble();
                tv5in.setText(d5in + "");
                tv5out.setText(d5out + "");
                in.setText("" + (d1in + d2in + d3in + d4in + d5in));
                out.setText("" + (d1out + d2out + d3out + d4out + d5out));
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
                time_text.setText("全部");
                begindate = "";
                enddate = "";
                dialogStart = "";
                dialogEnd = "";
                day = 0;
                refreshData();
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
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

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
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

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
        ActionSheetDialog actionSheet = new ActionSheetDialog(getActivity())
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
        orderEasyPresenter.booking3(day, begindate, enddate);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }
}