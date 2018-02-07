package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class NameActivity extends BaseActivity implements OrderEasyView {
    private OrderEasyPresenter orderEasyPresenter;
    private List<Integer> auths = new ArrayList<>();
    private int userid;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.name_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        //控件查找
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userid = bundle.getInt("user_id");
            userName = bundle.getString("username");
            name.setText(userName);
        }
    }

    //用户名
    @InjectView(R.id.name)
    ClearEditText name;

    @OnClick(R.id.return_click)
    void return_click() {//返回
        finish();
    }

    @OnClick(R.id.save)
    void save() {//保存
        String user_name = name.getText().toString();
        if (user_name == null || user_name.trim().length() < 2 || user_name.trim().length() > 15) {
            ToastUtil.show("姓名输入不合格!");
            return;
        }
        orderEasyPresenter.updateUserInfo(userid, user_name, auths);
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
        if (type == 2) {
            if (data.get("code").getAsInt() == 1) {
                ToastUtil.show("用户信息修改成功!");
                Intent intent = new Intent();
                intent.putExtra("name", name.getText().toString());
                setResult(1001, intent);
                finish();
            } else {
                ToastUtil.show("修改用户信息失败!");
            }
        }
    }
}