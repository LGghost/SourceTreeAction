package cn.order.ordereasy.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 * Created by Administrator on 2017/9/23.
 */

public class AgreementActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.agreement);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        xieyi.loadUrl("file:///android_asset/user_protocol.html");
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.xieyi)
    WebView xieyi;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        AgreementActivity.this.finish();
    }

}
