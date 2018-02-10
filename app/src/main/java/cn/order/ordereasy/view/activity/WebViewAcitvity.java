package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.utils.UmengUtils;
import cn.order.ordereasy.view.fragment.MainActivity;

import static cn.order.ordereasy.R.id.webView;

/**
 * Created by Administrator on 2017/9/28.
 * 微店
 */

public class WebViewAcitvity extends BaseActivity {
    private AlertDialog alertDialog;

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
        showdialogs();
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    TextView return_click;
    //关闭按钮
    @InjectView(R.id.textView23)
    TextView textView23;

    //更多
    @InjectView(R.id.more_text)
    TextView more_text;

    //WebView
    @InjectView(R.id.web_view_content)
    WebView web_view_content;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        web_view_content.goBack();//返回上个页
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.more_text)
    void more_text() {
        showPopWindow();
    }

    //关闭按钮
    @OnClick(R.id.textView23)
    void textView23() {
        WebViewAcitvity.this.finish();
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

    private void showPopWindow() {
        final PopupWindow popupWindow;
        View contentView = LayoutInflater.from(this).inflate(R.layout.supplier_add_popuwindow, null, false);
        LinearLayout add_supplier = (LinearLayout) contentView.findViewById(R.id.add_supplier);
        LinearLayout supplier_import = (LinearLayout) contentView.findViewById(R.id.supplier_import);
        ImageView imageView = (ImageView) contentView.findViewById(R.id.title_image);
        ImageView imageView1 = (ImageView) contentView.findViewById(R.id.title_image1);
        TextView title = (TextView) contentView.findViewById(R.id.title_text);
        TextView title1 = (TextView) contentView.findViewById(R.id.title_text1);
        imageView.setImageResource(R.drawable.icon_share1);
        imageView1.setImageResource(R.drawable.icon_qrcode);
        title.setText("分享店铺");
        title1.setText("店铺二维码");
        popupWindow = new PopupWindow(contentView, ScreenUtil.getWindowsW(this) / 3 + 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(more_text);

        add_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//分享店铺
                UmengUtils.getInstance().share(WebViewAcitvity.this, "欢迎光临本店", shareListener);
                popupWindow.dismiss();
            }
        });
        supplier_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//店铺二维码

                Intent intent = new Intent(WebViewAcitvity.this, PrintActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("flag", "web");
                intent.putExtras(bundle);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });

    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        View view1 = window.findViewById(R.id.view1);
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        text_conten.setText("微信店铺仅用于在手机微信端下单，在App内仅供预览，请勿执行下单操作");
        quxiao.setVisibility(View.GONE);
        view1.setVisibility(View.GONE);

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
                alertDialog.dismiss();
            }
        });
    }

    /**
     * 使点击回退按钮不会直接退出整个应用程序而是返回上一个页面
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && web_view_content.canGoBack()) {
            web_view_content.goBack();//返回上个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);//退出整个应用程序
    }
}
