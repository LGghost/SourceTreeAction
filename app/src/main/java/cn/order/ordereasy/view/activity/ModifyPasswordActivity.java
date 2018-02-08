package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * 修改密码activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class ModifyPasswordActivity extends BaseActivity implements OrderEasyView {
    private int userid;
    private String tel;
    //    private String password;
    private OrderEasyPresenter orderEasyPresenter;
    private boolean isOld = false;
    private boolean isNew = false;
    private boolean isQueren = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modify_password);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

        userid = this.getIntent().getIntExtra("user_id", 0);
        tel = this.getIntent().getStringExtra("tel");

        phone_number.setText(tel);

        SharedPreferences sp = this.getSharedPreferences("user", 0);
//        password=sp.getString("user_pwd","1");
//        old_password.setText(password);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //手机号码显示
    @InjectView(R.id.phone_number)
    TextView phone_number;
    //原密码输入框
    @InjectView(R.id.old_password)
    EditText old_password;

    //新密码输入框
    @InjectView(R.id.new_password)
    EditText new_password;

    //确认密码输入框
    @InjectView(R.id.queren_password)
    EditText queren_password;

    @InjectView(R.id.old_image)
    ImageView old_image;
    @InjectView(R.id.new_image)
    ImageView new_image;
    @InjectView(R.id.queren_image)
    ImageView queren_image;
    //确认按钮
    @InjectView(R.id.queren)
    Button queren;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        ModifyPasswordActivity.this.finish();
    }

    @OnClick(R.id.old_image)
    void old_image() {
        if (isOld) {
            isOld = false;
            old_image.setImageResource(R.drawable.icon_guanbimima);
            old_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            isOld = true;
            old_image.setImageResource(R.drawable.icon_xianshimima);
            old_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            old_password.setSelection(old_password.getText().length());
        }

    }

    @OnClick(R.id.new_image)
    void new_image() {
        if (isNew) {
            isNew = false;
            new_image.setImageResource(R.drawable.icon_guanbimima);
            new_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            isNew = true;
            new_image.setImageResource(R.drawable.icon_xianshimima);
            new_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            new_password.setSelection(new_password.getText().length());
        }
    }

    @OnClick(R.id.queren_image)
    void queren_image() {
        if (isQueren) {
            isQueren = false;
            queren_image.setImageResource(R.drawable.icon_guanbimima);
            queren_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            isQueren = true;
            queren_image.setImageResource(R.drawable.icon_xianshimima);
            queren_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            queren_password.setSelection(queren_password.getText().length());
        }
    }

    //确认按钮
    @OnClick(R.id.queren)
    void queren() {
        String new_password1 = new_password.getText().toString();
        String queren_password2 = queren_password.getText().toString();
        String old_password1 = old_password.getText().toString();

        if (new_password1.trim().length() < 6 || new_password1.trim().length() > 20) {
            ToastUtil.show("密码的长度在6-20位!");
            return;
        }
        if (queren_password2.trim().length() < 6 || queren_password2.trim().length() > 20) {
            ToastUtil.show("密码的长度在6-20位!");
            return;
        }
        if (old_password1.trim().length() < 6 || old_password1.trim().length() > 20) {
            ToastUtil.show("密码的长度在6-20位!");
            return;
        }

        if (!new_password1.equals(queren_password2)) {
            ToastUtil.show("新密码和确认密码输入不一致");
            return;
        }
        Log.e("ModifyPasswordActivity", old_password1);

        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderEasyPresenter.updateUserPass(userid, old_password1, new_password1);
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("ModifyPasswordActivity", data.toString());
        if (data.get("code").getAsInt() == 1) {
            ToastUtil.show("修改密码成功!");

            SharedPreferences sp = this.getSharedPreferences("user", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user_pwd", new_password.getText().toString());
//           editor.putString("user_pwd",old_password.getText().toString());
            editor.commit();
            ModifyPasswordActivity.this.finish();
        } else {
            ToastUtil.show(data.get("message").getAsString());
        }
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
}
