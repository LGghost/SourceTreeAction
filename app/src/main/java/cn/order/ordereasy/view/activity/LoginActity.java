package cn.order.ordereasy.view.activity;

/**
 * 登录activity
 **/

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushManager;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.MainActivity;

public class LoginActity extends BaseActivity implements OrderEasyView {

    private OrderEasyPresenter orderEasyPresenter;
    SharedPreferences spPreferences;
    boolean isWelcome = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 判断是否从推送通知栏打开的
        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        if (click != null) {
            //从推送通知栏打开Service打开Activity会重新执行Laucher流程  //查看是不是全新打开的面板
            if (isTaskRoot()) {
                return;
            }
            finish();//如果有面板存在则关闭当前的面板
        }

        //绑定界面
        setContentView(R.layout.login);
        //控件查找
        ButterKnife.inject(this);
        setTranslucent(this);
        spPreferences = getSharedPreferences("user", 0);
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        String username = spPreferences.getString("user_name", "");
        String pwd = spPreferences.getString("user_pwd", "");
        user_name.setText(username);
        password.setText(pwd);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)

        {
            isWelcome = bundle.getBoolean("flag");
            if (isWelcome) {
                if (pwd != null && !pwd.equals("")) {
                    orderEasyPresenter.login(username, pwd);
                }
            }
        }
        //输入框光标始终显示在文本后面
        user_name.setSelection(user_name.getText().

                length());
        password.setSelection(password.getText().

                length());

        //光标不显示
        user_name.setCursorVisible(true);
        password.setCursorVisible(true);
    }


    //找到控件ID
    //登录手机号码输入框
    @InjectView(R.id.user_name)
    ClearEditText user_name;

    //登录密码输入框
    @InjectView(R.id.password)
    ClearEditText password;

//    //忘记密码按钮
//    @InjectView(R.id.forgot_password)
//    TextView forgot_password;

    //登录按钮
    @InjectView(R.id.login_but)
    Button login_but;

    //注册按钮
    @InjectView(R.id.registration)
    LinearLayout registration;

    //需要的点击事件
//    //点击忘记密码的操作
//    @OnClick(R.id.forgot_password)
//    void forgot_password() {
//        Intent intent = new Intent(LoginActity.this, ForgotPasswordActivity.class);
//        startActivity(intent);
//    }

    //登录按钮的操作
    @OnClick(R.id.login_but)
    void login_but() {
        /**点击事件**/
        String username = user_name.getText().toString();
        if (TextUtils.isEmpty(username)) {
            showToast("用户名不能为空！");
            return;
        }
        String pwd = password.getText().toString();
        if (TextUtils.isEmpty(pwd)) {
            showToast("密码不能为空！");
            return;
        }
        orderEasyPresenter.login(username, pwd);
        //Intent intent =new Intent(LoginActity.this,MainActivity.class);
        //startActivity(intent);
    }

    //注册按钮点击事件
    @OnClick(R.id.registration)
    void registration() {
        Intent intent = new Intent(LoginActity.this, RegisterActivity.class);
        startActivity(intent);
    }

    //体验一下
    @OnClick(R.id.experience)
    void experience() {
        Intent intent = new Intent(LoginActity.this, ForgotPasswordActivity.class);
        startActivity(intent);
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
                            //登录成功
                            String token = result.getAsJsonObject("result").get("token").getAsString();
                            SharedPreferences.Editor editor = spPreferences.edit();
                            editor.putInt("isLogin", 1);
                            editor.putString("user_name", user_name.getText().toString());
                            editor.putString("user_pwd", password.getText().toString());
                            //editor.putString("user",)
                            editor.putString("token", token);
                            editor.commit();
                            orderEasyPresenter.getStoreInfo();
                            //finish();
                        } else {
                            SharedPreferences.Editor editor = spPreferences.edit();
                            editor.putString("token", "").commit();
                        }
                    }
                    Log.e("登录信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        Log.e("LoginActity", "result:" + result.toString());
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            JsonObject shop = result.getAsJsonObject("result").getAsJsonObject("shop_info");
                            JsonObject user = result.getAsJsonObject("result").getAsJsonObject("user_info");
                            JsonObject retailInfo = result.getAsJsonObject("result").getAsJsonObject("retail_info");
                            Customer customer = (Customer) GsonUtils.getEntity(retailInfo.toString(), Customer.class);
                            //保存零售客
                            DataStorageUtils.getInstance().setRetailCustomer(customer);
                            String is_boss = user.get("is_boss").toString();
                            SharedPreferences.Editor editor = spPreferences.edit();
                            editor.putString("shopinfo", shop.toString());
                            editor.putString("userinfo", user.toString());
                            editor.putString("is_boss", is_boss);
                            editor.commit();
                            DataStorageUtils.getInstance().cleanData();
                            Intent mainIntent = new Intent(LoginActity.this, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    }
                    Log.e("登录获取基本信息", result.toString());
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
        Message message = new Message();
        switch (type) {
            case 1:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 0:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;
                }
                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}




