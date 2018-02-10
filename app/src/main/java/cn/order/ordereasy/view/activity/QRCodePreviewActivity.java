package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.DownImage;
import cn.order.ordereasy.utils.HttpsUtil;
import cn.order.ordereasy.utils.ImgUtils;
import cn.order.ordereasy.utils.Pos;
import cn.order.ordereasy.utils.ToastUtil;

/**
 * Created by Administrator on 2017/9/14.
 * <p>
 * 二维码预览
 */

public class QRCodePreviewActivity extends BaseActivity implements DownImage.CompletionListener {
    private Pos pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.qr_code_preview);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String n = bundle.getString("no");
            String title = bundle.getString("name");
            String img = bundle.getString("img");
            DownImage downImage = new DownImage(img);
            downImage.setCompletionListener(this);
            Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode(n, 256, Color.BLACK);
            erweima_img.setImageBitmap(bitmap);
            no.setText("货品编号：" + n);
            name.setText("货品名称：" + title);
        }
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.erweima_img)
    ImageView erweima_img;
    @InjectView(R.id.huopin_img)
    ImageView huopin_img;
    @InjectView(R.id.name)
    TextView name;
    @InjectView(R.id.no)
    TextView no;

    //打印跳转
    @InjectView(R.id.dayin)
    TextView dayin;

    //分享按钮
    @InjectView(R.id.fenxiang)
    LinearLayout fenxiang;

    //保存按钮
    @InjectView(R.id.baocun)
    LinearLayout baocun;
    @InjectView(R.id.img_view)
    LinearLayout img_view;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        QRCodePreviewActivity.this.finish();
    }

    //打印按钮
    @OnClick(R.id.dayin)
    void dayin() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            print();
//        } else {
//            ToastUtil.show("系统不支持打印功能");
//        }
    }

    private void print() {
        Bitmap bitmap = BitmapUtils.getViewBitmap(img_view);
        String path = BitmapUtils.savePhotoToSDCard(bitmap);
        Intent intent = new Intent(this, PrintSetUpActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);
    }


    //分享点击事件
    @OnClick(R.id.fenxiang)
    void fenxiang() {
        final View view = img_view;
        UMImage thumb = new UMImage(this, ImgUtils.screenShot(view));

        new ShareAction(this)
                .withMedia(thumb)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(shareListener)
                .open();
    }

    //保存点击事件
    @OnClick(R.id.baocun)
    void baocun() {
        final View view = img_view;
        ImgUtils.saveImageToGallery(this, ImgUtils.screenShot(view));
        showToast("已经保存到相册！");
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
            Toast.makeText(QRCodePreviewActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(QRCodePreviewActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(QRCodePreviewActivity.this, "取消了", Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public void getCompletion(Bitmap bitmap) {
        huopin_img.setImageBitmap(bitmap);
    }
}
