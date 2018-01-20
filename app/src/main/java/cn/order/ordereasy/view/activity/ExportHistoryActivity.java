package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ExportHistoryAdapter;
import cn.order.ordereasy.bean.Statement;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DownloadFile;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.RequestUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class ExportHistoryActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, ExportHistoryAdapter.DownloadListener, DownloadFile.DownloadFileClickLister {
    private OrderEasyPresenter orderEasyPresenter;
    private ExportHistoryAdapter adapter;
    private AlertDialog alertDialog;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.export_history_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        adapter = new ExportHistoryAdapter(this);
        adapter.setDownloadListener(this);
        list_view.setAdapter(adapter);
        refreshData(true);
    }

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    @OnClick(R.id.return_click)
    void return_click() {//返回
        finish();
    }

    @OnClick(R.id.history_refresh)
    void history_refresh() {//刷新
        refreshData(true);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
        store_refresh.setRefreshing(false);
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        Log.e("ExportHistoryActivity", "data:" + data.toString());
        if (data != null) {
            int code = data.get("code").getAsInt();
            if (code == 1) {
                JsonArray jsonArray = data.get("result").getAsJsonArray();
                List<Statement> mapList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    Statement statement = (Statement) GsonUtils.getEntity(jsonArray.get(i).toString(), Statement.class);
                    mapList.add(statement);
                }
                adapter.setData(mapList);
                if (mapList.size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }

        }
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.exportLog();
    }

    @Override
    public void Download(int log_id, String title) {
        this.title = title;
        ProgressUtil.showDialog(this);
        DownloadFile downloadFile = new DownloadFile(this, RequestUtils.BASE_URL_V1 + Config.export_download_url + "?log_id=" + log_id,title);
        downloadFile.setDownloadFileClickLister(this);
    }

    @Override
    public void downloadComplete(File file) {
        ProgressUtil.dissDialog();
        showUpdatadialogs(file);
    }

    @Override
    public void downloadFail() {
        ToastUtil.show("下载失败");
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
        text_conten.setText("是否导出" + title + "报表？");


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
                FileUtils.openFile(ExportHistoryActivity.this, file);
                alertDialog.dismiss();
            }
        });
    }
}
