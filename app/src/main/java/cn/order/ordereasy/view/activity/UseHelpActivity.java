package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 * Created by Administrator on 2017/9/4.
 * <p>
 * 使用帮助activity
 */

public class UseHelpActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ues_help);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        UseHelpActivity.this.finish();
    }

    //拨打客服电话
    @OnClick(R.id.call_phone)
    void call_phone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "13974977597"));
        this.startActivity(intent);
    }

    //官网
    @OnClick(R.id.go_to_website)
    void go_to_website() {
        Uri uri = Uri.parse("https://www.dinghuo5u.com");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //查看本地手册
    @OnClick(R.id.local_manual_layout)
    void local_manual_layout() {
        Intent intent = new Intent(this, UserManualActivity.class);
        startActivity(intent);
    }

    //查看网络手册
    @OnClick(R.id.network_manual_layout)
    void network_manual_layout() {
        Uri uri = Uri.parse("https://www.dinghuo5u.com/images/manual.pdf");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //查看视频教程
    @OnClick(R.id.video_course_layout)
    void video_course_layout() {
        Intent intent = new Intent(this, VideoCourseActivity.class);
        startActivity(intent);

    }


}
