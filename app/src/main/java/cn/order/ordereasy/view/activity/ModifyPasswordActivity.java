package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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

    //确认按钮
    @InjectView(R.id.queren)
    Button queren;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        ModifyPasswordActivity.this.finish();
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
}
