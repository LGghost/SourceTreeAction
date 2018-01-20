package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.UmengUtils;
import cn.order.ordereasy.view.fragment.MainActivity;

/**
 * Created by Administrator on 2017/9/28.
 * 微店
 */

public class WebViewAcitvity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.web_view);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

        String key = getIntent().getStringExtra("key");

        WebSettings webSettings = web_view_content.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //加载需要显示的网页
        web_view_content.getSettings().setJavaScriptEnabled(true);
        web_view_content.getSettings().setDomStorageEnabled(true);


        web_view_content.loadUrl(key);

        Log.e("1111111", key);

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    TextView return_click;
    //关闭按钮
    @InjectView(R.id.textView23)
    TextView textView23;

    //分享
    @InjectView(R.id.fenxiang)
    TextView fenxiang;

    //WebView
    @InjectView(R.id.web_view_content)
    WebView web_view_content;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        WebViewAcitvity.this.finish();
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.fenxiang)
    void fenxiang() {
        UmengUtils.getInstance().share(WebViewAcitvity.this, "欢迎光临本店", shareListener);
    }

    //关闭按钮
    @OnClick(R.id.textView23)
    void textView23() {
        Intent intent = new Intent(WebViewAcitvity.this, MainActivity.class);
        startActivity(intent);
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(WebViewAcitvity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(WebViewAcitvity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(WebViewAcitvity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };
}
