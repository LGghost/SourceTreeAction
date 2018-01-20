package cn.order.ordereasy.view.fragment.gooddetailsfragment;

import android.content.SharedPreferences;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;

public class FragmentPreview extends Fragment {
    private WebView webview;
    private Goods goods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goods_details_right, container, false);
        webview = (WebView) view.findViewById(R.id.detail_webview);
        initData();
        return view;
    }

    private void initData() {
        if(goods == null){
            return;
        }
        WebSettings webSettings = webview.getSettings();
        //设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //设置支持缩放
        webSettings.setBuiltInZoomControls(false);
        //加载需要显示的网页
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        SharedPreferences spPreferences = getActivity().getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String key = shop.get("shop_key").getAsString();
            String url = "https://m.dinghuo5u.com/wx/" + key + "/goods?id=" + goods.getGoods_id() + "&preview=true";
            webview.loadUrl(url);
            MyLog.e("店铺网址信息", url);
        }

        //设置Web视图
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // 接受网站证书
            }
        });

    }

    public void setData(Goods goods) {
        this.goods = goods;
    }
}