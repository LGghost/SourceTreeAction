package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.print.PrintHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.HttpsUtil;
import cn.order.ordereasy.utils.ImgUtils;
import cn.order.ordereasy.utils.Pos;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UmengUtils;

/**
 * Created by Administrator on 2017/9/13.
 * <p>
 * 打印
 */

public class PrintActivity extends BaseActivity {

    AlertDialog alertDialog;
    private LinearLayout imageLayout;
    private String path;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String name = shop.get("name").getAsString();
            String bossName = shop.get("boss_name").getAsString();
            store_name.setText(name);
        }
        imageLayout = (LinearLayout) findViewById(R.id.image_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            if (flag.equals("web")) {
                title_name.setText("微信店铺二维码");
                bottom_layout.setVisibility(View.VISIBLE);
            }
        } else {
            bottom_layout.setVisibility(View.GONE);
        }
        initQR();
    }

    private void initQR() {
        ProgressUtil.showDialog(this);
        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(UmengUtils.getInstance().shareUrl(this), 256);
        erweima_img.setImageBitmap(bitmap);
        ProgressUtil.dissDialog();
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //返回按钮
    @InjectView(R.id.dayin)
    TextView dayin;
    @InjectView(R.id.store_name)
    TextView store_name;

    @InjectView(R.id.title_name)
    TextView title_name;
    @InjectView(R.id.erweima_img)
    ImageView erweima_img;
    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        PrintActivity.this.finish();
    }

    @OnClick(R.id.dayin)
    void dayin() {
        print();
    }

    @OnClick(R.id.baocun)
    void baocun() {
        final View view = imageLayout;
        ImgUtils.saveImageToGallery(this, ImgUtils.screenShot(view));
        showToast("已经保存到相册！");
    }

    @OnClick(R.id.fenxiang)
    void fenxiang() {
        final View view = imageLayout;
        UMImage thumb = new UMImage(this, ImgUtils.screenShot(view));

        new ShareAction(this)
                .withMedia(thumb)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(shareListener)
                .open();
    }

    private void print() {
        // 获取ImageView这个用于显示图片的控件里的图片
        Bitmap bitmap = BitmapUtils.getViewBitmap(imageLayout);
        path = BitmapUtils.savePhotoToSDCard(bitmap);
        Intent intent = new Intent(this, PrintSetUpActivity.class);
        intent.putExtra("path", path);
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
            Toast.makeText(PrintActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(PrintActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(PrintActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

}
