package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scrat.app.selectorlibrary.ImageSelector;

import java.io.IOException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ToastUtil;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by mrpan on 2017/9/10.
 */

public class ScanActivity extends BaseActivity implements QRCodeView.Delegate, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private int flag = 0;

    @InjectView(R.id.zxingview)
    ZXingView mQRCodeView;
    @InjectView(R.id.photo)
    TextView photo;

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        ScanActivity.this.finish();
    }

    @OnClick(R.id.photo)
    void take_photo() {
        ImageSelector.show(ScanActivity.this, REQUEST_CODE_SELECT_IMG, 1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zxingview);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        requestCodeQRCodePermissions();
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getInt("code");
        }
        mQRCodeView.setDelegate(this);
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.e("ScanActivity", result);
        Log.e("ScanActivity", flag + "");
        vibrate();
        mQRCodeView.startCamera();
        mQRCodeView.startSpot();
        //获取结果并返回
        if (!TextUtils.isEmpty(result) && flag != 0) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", result);
            intent.putExtras(bundle);
            setResult(flag, intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        ToastUtil.show("打开相机出错");
        finish();
        Log.e("ScanActivity", "打开相机出错");
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("ScanActivity", "requestCode:" + requestCode);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.e("ScanActivity", "onPermissionsGranted:");
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        finish();
        Log.e("ScanActivity", "onPermissionsDenied:");
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        Log.e("ScanActivity", "requestCodeQRCodePermissions:");
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> yourSelectImgPaths = null;
        switch (requestCode) {
            case REQUEST_CODE_SELECT_IMG:
                mQRCodeView.showScanRect();
                yourSelectImgPaths = ImageSelector.getImagePaths(data);
                if (yourSelectImgPaths.size() > 0) {
                    String path = yourSelectImgPaths.get(0);
                    String result = QRCodeDecoder.syncDecodeQRCode(path);
                    if (TextUtils.isEmpty(result)) {
                        showToast("暂未发现二维码");
                    } else {
                        showToast(result);
                    }

                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}