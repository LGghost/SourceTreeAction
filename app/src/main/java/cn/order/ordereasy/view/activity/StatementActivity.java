package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DownloadFile;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.RequestUtils;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class StatementActivity extends BaseActivity implements OrderEasyView, DownloadFile.DownloadFileClickLister {
    private int sign = 1;
    private String startTime;
    private String endTime;
    private OrderEasyPresenter orderEasyPresenter;
    private TestThread thread;
    private int log_id;
    private AlertDialog alertDialog;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statement_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        thread = new TestThread();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            sign = bundle.getInt("sign");
        }
        if (sign == 1) {
            fileName = getString(R.string.financial_statements);
        } else if (sign == 2) {
            fileName = getString(R.string.goods_sale);
        } else {
            fileName = getString(R.string.detailed_statement_of_goods);
            start_time_hint.setText(getString(R.string.selection_date));
            end_time_layout.setVisibility(View.GONE);
        }
        statement_name.setText(fileName);
        startTime = TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd");
        endTime = TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd");
        start_time_text.setText(startTime);
        end_time_text.setText(endTime);
        Log.e("StatementActivity", "time:" + TimeUtil.stringToLong(startTime));
    }

    @InjectView(R.id.statement_name)
    TextView statement_name;
    @InjectView(R.id.start_time_hint)
    TextView start_time_hint;
    @InjectView(R.id.start_time_text)
    TextView start_time_text;
    @InjectView(R.id.end_time_text)
    TextView end_time_text;
    @InjectView(R.id.end_time_layout)
    LinearLayout end_time_layout;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.start_time_text)
    void start_time_text() {//开始时间
        showdialogs(1);
    }

    @OnClick(R.id.end_time_text)
    void end_time_text() {//结束时间
        showdialogs(2);
    }

    @OnClick(R.id.start_exporting_data)
    void start_exporting_data() {//开始导出数据
        if (sign == 1) {
            if (TimeUtil.stringToLong(startTime) > TimeUtil.stringToLong(endTime)) {
                ToastUtil.show("开始时间不能大于结束时间");
                return;
            }
            orderEasyPresenter.exportPayList(startTime, endTime);
        } else if (sign == 2) {
            if (TimeUtil.stringToLong(startTime) > TimeUtil.stringToLong(endTime)) {
                ToastUtil.show("开始时间不能大于结束时间");
                return;
            }
            orderEasyPresenter.exportSaleProductCount(startTime, endTime);
        } else {
            if (TimeUtil.stringToLong(startTime) > TimeUtil.stringToLong(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"))) {
                ToastUtil.show("开始时间不能大于今天的日期");
                return;
            }
            orderEasyPresenter.exportSaleProductList(startTime);
        }
        ProgressUtil.showDialog(this);
    }

    @OnClick(R.id.export_history)
    void export_history() {//导出历史
        Intent intent = new Intent(this, ExportHistoryActivity.class);
        startActivity(intent);
    }

    private void showdialogs(final int type) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                int month = monthOfYear + 1;
                if (type == 1) {
                    startTime = year + "-" + month + "-" + dayOfMonth;
                    start_time_text.setText(startTime);
                } else {
                    endTime = year + "-" + month + "-" + dayOfMonth;
                    end_time_text.setText(endTime);
                }
            }
        }, TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDay()).show();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
            ProgressUtil.dissDialog();
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("StatementActivity", "type:" + type + "data:" + data.toString());
        if (type == 0) {
            if (data != null) {
                int code = data.get("code").getAsInt();
                if (code == 1) {
                    int status = data.getAsJsonObject("result").get("status").getAsInt();
                    if (status == 2) {
                        thread.setStop(true);
                        if (sign == 3) {
                            DownloadFile downloadFile = new DownloadFile(this, RequestUtils.BASE_URL_V1 + Config.export_download_url + "?log_id=" + log_id, fileName + "(" + startTime + ")");
                            downloadFile.setDownloadFileClickLister(this);
                        } else {
                            DownloadFile downloadFile = new DownloadFile(this, RequestUtils.BASE_URL_V1 + Config.export_download_url + "?log_id=" + log_id, fileName + "(" + startTime + "~" + endTime + ")");
                            downloadFile.setDownloadFileClickLister(this);
                        }
                    }
                }

            }
        }
        if (type == 1 || type == 3 || type == 2) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    log_id = data.getAsJsonObject("result").get("log_id").getAsInt();
                    thread.setStop(false);
                    thread.start();
                } else {
                    ProgressUtil.dissDialog();
                    String message = data.get("message").getAsString();
                    ToastUtil.show(message);
                }
            }
        }
    }

    @Override
    public void downloadComplete(File file) {
        ProgressUtil.dissDialog();
        showUpdatadialogs(file);
    }

    @Override
    public void downloadFail() {
        ProgressUtil.dissDialog();
        ToastUtil.show("下载失败");
    }

    class TestThread extends Thread {
        private boolean isStopped = false;

        @Override
        public void run() {

            for (int i = 0; i < 20; i++) {
                if (!isStopped) {
                    try {
                        orderEasyPresenter.exportStatus(log_id);
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    break;
                }
            }
        }

        public void setStop(boolean isStopped) {
            this.isStopped = isStopped;
        }
    }

    private void showUpdatadialogs(final File file) {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        //设置点击屏幕不让消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("下载完成");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("是否导出报表？");


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
                FileUtils.openFile(StatementActivity.this, file);
                alertDialog.dismiss();
            }
        });
    }
}