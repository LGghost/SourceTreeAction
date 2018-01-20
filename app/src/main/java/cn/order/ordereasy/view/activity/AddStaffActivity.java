package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/13.
 * <p>
 * 添加员工
 */

public class AddStaffActivity extends BaseActivity implements OrderEasyView {

    private OrderEasyPresenter orderEasyPresenter;
    private List<Integer> auths = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_staff);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    //员工姓名
    @InjectView(R.id.yuangong_name)
    ClearEditText yuangong_name;
    //员工手机号码
    @InjectView(R.id.yuangong_phonenum)
    ClearEditText yuangong_phonenum;
    //员工密码
    @InjectView(R.id.password_number)
    ClearEditText password_number;
    //确定按钮
    @InjectView(R.id.zhuce_but)
    Button zhuce_but;
    @InjectView(R.id.role_name)
    TextView role_name;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        AddStaffActivity.this.finish();
    }

    @OnClick(R.id.role_layout)
    void role_layout() {//所属角色
        String roleName = "";
        Intent intent = new Intent(this, RoleSelectionActivity.class);
        if (auths.size() > 0) {
            for (int i = 0; i < auths.size(); i++) {
                if (i == 0) {
                    roleName += auths.get(i);
                } else {
                    roleName += "/" + auths.get(i);
                }
            }
        }
        intent.putExtra("roleName", roleName);
        startActivityForResult(intent, 1001);
    }

    //点击添加员工
    @OnClick(R.id.zhuce_but)
    void zhuce_but() {
        String name = yuangong_name.getText().toString();
        String tel = yuangong_phonenum.getText().toString();
        String pass = password_number.getText().toString();

        if (name == null || name.trim().length() < 2 || name.trim().length() > 15) {
            ToastUtil.show("姓名输入不合格!");
            return;
        }
        if (tel == null || tel.trim().length() != 11) {
            ToastUtil.show("手机号码格式有误!");
            return;
        }
        if (pass == null || pass.trim().length() < 6 || pass.trim().length() > 30) {
            ToastUtil.show("密码输入不合格!");
            return;
        }
        if (auths.size() == 0) {
            ToastUtil.show("请选择员工角色!");
            return;
        }
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderEasyPresenter.addEmployee(name, tel, pass, auths);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            auths.clear();
            String shop_keeper_str = "", salesperson_str = "", godown_man_str = "";
            int shop_keeper = data.getExtras().getInt("shop_keeper");
            if (shop_keeper != -1) {
                auths.add(shop_keeper);
                shop_keeper_str = getString(R.string.shop_keeper) + "/";
            }
            int salesperson = data.getExtras().getInt("salesperson");
            if (salesperson != -1) {
                auths.add(salesperson);
                salesperson_str = getString(R.string.salesperson) + "/";
            }
            int godown_man = data.getExtras().getInt("godown_man");
            if (godown_man != -1) {
                auths.add(godown_man);
                godown_man_str = getString(R.string.godown_man);
            }
            String power = shop_keeper_str + salesperson_str + godown_man_str;
            if (power.endsWith("/")) {
                power = power.substring(0, power.length() - 1);
            }
            role_name.setText(power);
        }
    }


    @Override
    public void loadData(JsonObject data, int type) {
        if (data.get("code").getAsInt() == 1) {
            ToastUtil.show("添加员工成功");
            setResult(1001);
            AddStaffActivity.this.finish();
        } else {
            ToastUtil.show(data.get("message").getAsString());
        }
    }
}
