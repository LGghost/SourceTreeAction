package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 * 关于Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class AboutActivity extends BaseActivity {
    private String version;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = info.versionName;
        version_text.setText("订货无忧 " + version + " (20180130—11)");
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //返回按钮
    @InjectView(R.id.xieyi)
    LinearLayout xieyi;

    @InjectView(R.id.version_text)
    TextView version_text;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        AboutActivity.this.finish();
    }

    //返回按钮
    @OnClick(R.id.xieyi)
    void xieyi() {
        Intent intent = new Intent(AboutActivity.this, AgreementActivity.class);
        startActivity(intent);
    }
}
