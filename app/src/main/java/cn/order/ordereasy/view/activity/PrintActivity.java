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

import com.google.gson.JsonObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.HttpsUtil;
import cn.order.ordereasy.utils.Pos;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;

/**
 * Created by Administrator on 2017/9/13.
 * <p>
 * 打印
 */

public class PrintActivity extends BaseActivity {

    AlertDialog alertDialog;
    private LinearLayout imageLayout;
    private String path;

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
        initQR();
    }

    private void initQR() {
        ProgressUtil.showDialog(this);
        Bitmap bitmap = QRCodeEncoder.syncEncodeQRCode("uri:https:/www.dinghuo5u.com", 256);
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
    @InjectView(R.id.erweima_img)
    ImageView erweima_img;


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

    private void print() {
        // 获取ImageView这个用于显示图片的控件里的图片
        Bitmap bitmap = BitmapUtils.getViewBitmap(imageLayout);
        path = BitmapUtils.savePhotoToSDCard(bitmap);
        Intent intent = new Intent(this, PrintSetUpActivity.class);
        intent.putExtra("path",path);
        startActivity(intent);

    }


}
