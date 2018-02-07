package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.BuildConfig;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.UserInfo;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;

/**
 * 个人信息activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */
public class PersonalInformationActivity extends BaseActivity implements OrderEasyView {
    private int userid;
    private String username;
    private OrderEasyPresenter orderEasyPresenter;
    private String path = "";
    private boolean isChange = false;
    private List<Integer> auths = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_information);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        userid = this.getIntent().getIntExtra("user_id", 0);
        username = this.getIntent().getStringExtra("user_name");
        String avatar = this.getIntent().getStringExtra("avatar");
        if (!TextUtils.isEmpty(avatar)) {
            ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + avatar, touxiang);
        }
        name.setText(username);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.huan_touxiang)
    LinearLayout huan_touxiang;

    //头像显示
    @InjectView(R.id.touxiang)
    ImageView touxiang;

    //姓名显示
    @InjectView(R.id.name)
    TextView name;

    private static final int REQUEST_CODE_SELECT_IMG_LG = 3;
    private static final int REQUEST_CODE_CAMERA_IMG_LG = 4;

    @OnClick(R.id.huan_touxiang)
    void huan_touxiang() {
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .setTitle("上传头像")
                .addSheetItem("拍照", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        onPhoto(REQUEST_CODE_CAMERA_IMG_LG);
                    }
                })
                .addSheetItem("去相册选择", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        ImageSelector.show(PersonalInformationActivity.this, REQUEST_CODE_SELECT_IMG_LG, 1);
                    }
                });

        actionSheet.show();
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        if (isChange) {
            setResult(1001);
        }
        PersonalInformationActivity.this.finish();
    }

    @OnClick(R.id.gai_name)
    void gai_name() {
        Intent intent = new Intent(this, NameActivity.class);
        intent.putExtra("user_id", userid);
        intent.putExtra("username", username);
        startActivityForResult(intent, 1001);
    }
//    @OnClick(R.id.send)
//    void send_click() {
//        String user_name = name.getText().toString();
//        if (user_name == null || user_name.trim().length() < 2 || user_name.trim().length() > 15) {
//            ToastUtil.show("姓名输入不合格!");
//            return;
//        }
//        orderEasyPresenter.updateUserInfo(userid, user_name, auths);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        if (type == 3) {
            MyLog.e("修改头像信息", data.toString());
            if (data.get("code").getAsInt() == 1) {
                isChange = true;
                showToast("操作成功");
                JsonObject res = data.get("result").getAsJsonObject();
                ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + res.get("file").getAsString(), touxiang);
            }
        }
    }

    public void onPhoto(int type) {
        if (SystemfieldUtils.isCameraUseable()) {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
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
       /* Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Config.DIR_IMAGE_PATH+"take/", String.valueOf(System.currentTimeMillis())
                + ".jpg");
        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        openCameraIntent.putExtra(path, file);
        startActivityForResult(openCameraIntent, REQUEST_CODE_CAMERA_IMG);*/
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            photoFile = createImageFile();
            // Continue only if the File was successfully created
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<String> yourSelectImgPaths = null;
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_IMG_LG:
                try {

                    Bitmap bm = BitmapUtils.revitionImageSize(path);
                    if (!TextUtils.isEmpty(path)) {
                        if (bm == null) {
                            return;
                        }
                        orderEasyPresenter.uploadUserAvatar(path);
                        //logo展示
                        touxiang.setImageBitmap(bm);
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
                        orderEasyPresenter.uploadUserAvatar(yourSelectImgPaths.get(0));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    touxiang.setImageBitmap(bm);
                }
                break;
            case 1001:
                if (data != null) {
                    String username = data.getExtras().getString("name");
                    name.setText(username);
                    isChange = true;
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isChange) {
                setResult(1001);
                finish();
            } else {
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
