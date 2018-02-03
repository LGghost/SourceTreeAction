package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGANormalRefreshViewHolder;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.order.ordereasy.BuildConfig;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;
import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Administrator on 2017/9/5.
 * 设置店铺信息
 */

public class StoreSettingsActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {

    private OrderEasyPresenter orderEasyPresenter;
    private static final int REQUEST_CODE_SELECT_IMG_WX = 1;
    private static final int REQUEST_CODE_CAMERA_IMG_WX = 2;
    private static final int REQUEST_CODE_SELECT_IMG_LG = 3;
    private static final int REQUEST_CODE_CAMERA_IMG_LG = 4;
    private String path = "";

    private String qrcode = "", logo = "";

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.store_settings);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        initRefreshLayout();
        refreshData(true);
    }


    private void initRefreshLayout() {
        store_refresh.setOnRefreshListener(this);
    }


    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //保存按钮
    @InjectView(R.id.baocun)
    TextView baocun;
    //店铺图片
    @InjectView(R.id.store_image)
    ImageView store_image;
    //店铺名称
    @InjectView(R.id.store_name)
    TextView store_name;

    //店铺地址
    @InjectView(R.id.tx_store_add)
    TextView tx_store_add;
    //店铺二维码
    @InjectView(R.id.store_erweima)
    ImageView store_erweima;
    //店铺联系号码
    @InjectView(R.id.phone_number)
    TextView phone_number;
    //店铺微信号
    @InjectView(R.id.wx_num)
    TextView wx_num;
    //输入店铺名称
    @InjectView(R.id.ed_store_name)
    EditText ed_store_name;

    //选择所在地区
    @InjectView(R.id.addres)
    LinearLayout addres;
    //填写详细地址
    @InjectView(R.id.store_add)
    EditText store_add;
    //填写联系号码
    @InjectView(R.id.ed_phone)
    EditText ed_phone;
    //填写电话号码
    @InjectView(R.id.ed_call)
    EditText ed_call;
    //选择店铺logo图片
    @InjectView(R.id.store_logo_img)
    LinearLayout store_logo_img;
    //展示店铺logo图片
    @InjectView(R.id.store_logo)
    ImageView store_logo;
    //选择店铺客服二维码图片
    @InjectView(R.id.wx_img)
    LinearLayout wx_img;
    //展示客服二维码图片
    @InjectView(R.id.wx_logo)
    ImageView wx_logo;
    //输入客服微信号
    @InjectView(R.id.wx_ed)
    EditText wx_ed;
    //输入店铺公告
    @InjectView(R.id.ed_gonggao)
    EditText ed_gonggao;

    //省市县
    @InjectView(R.id.id_province)
    TextView id_province;
    @InjectView(R.id.id_city)
    TextView id_city;
    @InjectView(R.id.id_district)
    TextView id_district;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        StoreSettingsActivity.this.finish();
    }

    //点击选取店铺图片
    @OnClick(R.id.store_logo_img)
    void store_logo_img() {
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setTitle("设置店铺LOGO")
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        onPhoto(REQUEST_CODE_CAMERA_IMG_LG);
                    }
                })
                .addSheetItem("去相册选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        ImageSelector.show(StoreSettingsActivity.this, REQUEST_CODE_SELECT_IMG_LG, 1);
                    }
                });

        actionSheet.show();

    }

    //点击选取店铺二维码图片
    @OnClick(R.id.wx_img)
    void select_wx() {
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setTitle("店铺二维码")
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        onPhoto(REQUEST_CODE_CAMERA_IMG_WX);
                    }
                })
                .addSheetItem("去相册选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        ImageSelector.show(StoreSettingsActivity.this, REQUEST_CODE_SELECT_IMG_WX, 1);
                    }
                });

        actionSheet.show();

    }

    @OnClick(R.id.addres)
    void select_addr() {
        CityPickerView cityPicker = new CityPickerView.Builder(StoreSettingsActivity.this)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#dcdcdc")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //返回结果
                //ProvinceBean 省份信息
                //CityBean     城市信息
                //DistrictBean 区县信息
                id_city.setText(city.getName());
                id_district.setText(district.getName());
                id_province.setText(province.getName());
            }

            @Override
            public void onCancel() {

            }
        });
    }

    //保存按钮
    @OnClick(R.id.baocun)
    void baocun() {
        String mobile = ed_phone.getText().toString();
        String name = ed_store_name.getText().toString();
        String province = id_province.getText().toString();
        String city = id_city.getText().toString();
        String distrct = id_district.getText().toString();
        String addr = store_add.getText().toString();
        String telephone = ed_call.getText().toString();
        String notice = ed_gonggao.getText().toString();
        String wechat = wx_ed.getText().toString();
        orderEasyPresenter.setupStore(mobile, name, province, city, distrct, addr, telephone, wechat, notice);

        showToast("保存成功");
        StoreSettingsActivity.this.finish();
    }

    @Override
    public void showProgress(int type) {
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
        Message message = new Message();
        switch (type) {
            case 0:

                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;

                }
                break;
            case 2:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;
                }
                break;
            case 3:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;

                }

                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            JsonObject shop = result.getAsJsonObject("result").getAsJsonObject("shop_info");

                            store_name.setText(shop.get("name").getAsString());
                            tx_store_add.setText(shop.get("address").getAsString());
                            phone_number.setText(shop.get("mobile").getAsString());
                            ed_phone.setText(shop.get("mobile").getAsString());
                            ed_store_name.setText(shop.get("name").getAsString());
                            store_add.setText(shop.get("address").getAsString());
                            wx_ed.setText(shop.get("wechat").getAsString());
                            wx_num.setText(shop.get("wechat").getAsString());
                            id_province.setText(shop.get("province").getAsString());
                            id_city.setText(shop.get("city").getAsString());
                            id_district.setText(shop.get("district").getAsString());
                            ed_gonggao.setText(shop.get("notice").getAsString());
                            String logo = shop.get("logo").getAsString();
                            String wx_qrcode = shop.get("wx_qrcode").getAsString();
                            ImageLoader.getInstance().clearDiskCache();
                            ImageLoader.getInstance().clearMemoryCache();
                            if (!TextUtils.isEmpty(logo)) {
                                ImageLoader.getInstance().displayImage(Config.URL_HTTP + logo, store_image);
                                ImageLoader.getInstance().displayImage(Config.URL_HTTP + logo, store_logo);
                            }
                            if (!TextUtils.isEmpty(wx_qrcode)) {
                                ImageLoader.getInstance().displayImage(Config.URL_HTTP + wx_qrcode, store_erweima);
                                ImageLoader.getInstance().displayImage(Config.URL_HTTP + wx_qrcode, wx_logo);
                            }

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(StoreSettingsActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("店铺信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            ImageLoader.getInstance().clearMemoryCache();
                            ImageLoader.getInstance().clearDiskCache();
                        } else {

                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

                        }
                    }
                    Log.e("StoreSettingsActivity", "图片：" + result.toString());
                    break;
                case 1007:
                    ToastUtil.show("出错了哟~");
                    break;
                case 9999:
                    ToastUtil.show("网络有问题哟~");
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> yourSelectImgPaths = null;
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_IMG_LG:
                try {
                    Bitmap bm = BitmapUtils.revitionImageSize(path);
                    if (bm == null) {
                        return;
                    }
                    logo = path;
                    //logo展示
                    store_image.setImageBitmap(bm);
                    store_logo.setImageBitmap(bm);
                    if (!TextUtils.isEmpty(logo)) {
                        Log.e("StoreSettingsActivity", "logo:" + logo);
                        orderEasyPresenter.uploadStoreImg("logo", logo);
                        logo = "";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_SELECT_IMG_LG:
                yourSelectImgPaths = ImageSelector.getImagePaths(data);
                if (yourSelectImgPaths.size() > 0) {
                    Bitmap bm = null;
                    try {
                        bm = BitmapUtils.revitionImageSize(yourSelectImgPaths.get(0));
                        logo = yourSelectImgPaths.get(0);
                        //orderEasyPresenter.uploadStoreImg("logo",yourSelectImgPaths.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //微信二维码展示
                    if (bm == null) {
                        return;
                    }
                    store_image.setImageBitmap(bm);
                    store_logo.setImageBitmap(bm);
                    if (!TextUtils.isEmpty(logo)) {
                        Log.e("StoreSettingsActivity", "logo:" + logo);
                        orderEasyPresenter.uploadStoreImg("logo", logo);
                        logo = "";
                    }
                }
                break;
            case REQUEST_CODE_CAMERA_IMG_WX:
                try {
                    Bitmap bm = BitmapUtils.revitionImageSize(path);
                    qrcode = path;
                    if (bm == null) {
                        return;
                    }
                    //orderEasyPresenter.uploadStoreImg("wx_qrcode",path);
                    //微信二维码展示
                    store_erweima.setImageBitmap(bm);
                    wx_logo.setImageBitmap(bm);
                    if (!TextUtils.isEmpty(qrcode)) {
                        Log.e("StoreSettingsActivity", "qrcode:" + qrcode);
                        orderEasyPresenter.uploadStoreImg("wx_qrcode", qrcode);
                        qrcode = "";
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_CODE_SELECT_IMG_WX:
                yourSelectImgPaths = ImageSelector.getImagePaths(data);
                if (yourSelectImgPaths.size() > 0) {
                    Bitmap bm = null;
                    try {
                        bm = BitmapUtils.revitionImageSize(yourSelectImgPaths.get(0));
                        qrcode = yourSelectImgPaths.get(0);
                        //orderEasyPresenter.uploadStoreImg("wx_qrcode",yourSelectImgPaths.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (bm == null) {
                        return;
                    }
                    //微信二维码展示
                    store_erweima.setImageBitmap(bm);
                    wx_logo.setImageBitmap(bm);
                    if (!TextUtils.isEmpty(qrcode)) {
                        Log.e("StoreSettingsActivity", "qrcode:" + qrcode);
                        orderEasyPresenter.uploadStoreImg("wx_qrcode", qrcode);
                        qrcode = "";
                    }
                }
                break;
        }
       /* if (requestCode == REQUEST_CODE_SELECT_IMG_WX) {
            List<String> yourSelectImgPaths = ImageSelector.getImagePaths(data);
            if(yourSelectImgPaths.size()>0){
                Bitmap bm = null;
                try {
                    bm = BitmapUtils.revitionImageSize(yourSelectImgPaths.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //微信二维码展示
                store_erweima.setImageBitmap(bm);
                wx_logo.setImageBitmap(bm);

            }

            return;
        }else if(requestCode == REQUEST_CODE_CAMERA_IMG_WX){
            try {
                Bitmap bm = BitmapUtils.revitionImageSize(path);
                orderEasyPresenter.uploadStoreImg();
                //微信二维码展示
                store_erweima.setImageBitmap(bm);
                wx_logo.setImageBitmap(bm);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        Log.e("StoreSetting", "requestCode" + requestCode);
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //photo();
                } else {
                    // Permission Denied
                    Toast.makeText(this, "请打开拍照权限！", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    public void onPhoto(int type) {
        if (SystemfieldUtils.isCameraUseable()) {
            Log.e("StoreSetting", "type:" + type);
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                Log.e("StoreSetting", "checkCallPhonePermission:" + checkCallPhonePermission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_CALL_PHONE);
                    return;
                } else {
                    photo(type);
                }
            } else {
                photo(type);
            }
        } else {
            ToastUtil.show("请去设置中打开拍照权限");
        }
    }

    //拍照
    public void photo(int type) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", photoFile));
                } else {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                }
            }
        }
        startActivityForResult(takePictureIntent, type);//跳转界面传回拍照所得数据
    }

    private File createImageFile() {
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File image = null;
        try {
            image = File.createTempFile(
                    generateFileName(),  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        path = image.getAbsolutePath();
        return image;
    }

    public static String generateFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        return imageFileName;
    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    @Override
    public void onRefresh() {
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        refreshData(false);
    }

    private void refreshData(boolean isFrist) {
        if (isFrist) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getStoreInfo();
    }
}
