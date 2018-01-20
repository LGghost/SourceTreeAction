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
 *
 * 使用帮助activity
 */

public class UseHelpActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ues_help);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.call_phone)
    TextView call_phone;

    @InjectView(R.id.net)
    TextView net;

    @InjectView(R.id.shouce)
    TextView shouce;

    @InjectView(R.id.video)
    TextView video;

    @InjectView(R.id.video2)
    TextView video2;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        UseHelpActivity.this.finish();
    }

    //拨打客服电话
    @OnClick(R.id.call_phone)
    void  call_phone() {
        Intent intent = new Intent();
        Uri data = Uri.parse("tel:" + "13974977597");
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(data);
        this.startActivity(intent);
    }

    //官网
    @OnClick(R.id.net)
    void  net() {

        Uri uri = Uri.parse("https://www.dinghuo5u.com");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
         startActivity(it);
    }
    //手册
    @OnClick(R.id.shouce)
    void  shouce() {

        Uri uri = Uri.parse("https://www.dinghuo5u.com/images/manual.pdf");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //视频1
    @OnClick(R.id.video)
    void  video() {

        Uri uri = Uri.parse("https://v.qq.com/x/page/x05388l2wxj.html");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //视频2
    @OnClick(R.id.video2)
    void  video2() {

        Uri uri = Uri.parse("https://v.youku.com/v_show/id_XMjk3NDAyOTg3Ng==.html?spm=a2hzp.8244740.0.0");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }


}
