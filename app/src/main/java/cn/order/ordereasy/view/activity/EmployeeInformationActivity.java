package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import cn.order.ordereasy.bean.MyEmployee;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/28.
 */

public class EmployeeInformationActivity extends BaseActivity implements OrderEasyView {

    AlertDialog alertDialog;
    OrderEasyPresenter orderEasyPresenter;
    MyEmployee employee;
    private List<Integer> auths = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.employee_information);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            employee = (MyEmployee) bundle.getSerializable("data");
            yuangong_name.setText(employee.getName());
            yuangong_phonenum.setText(employee.getTelephone());
            yuangong_mima.setText("******");
            auths = employee.getAuth_group_ids();
        }
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        String shop_keeper = "", salesperson = "", godown_man = "";
        for (int m = 0; m < auths.size(); m++) {
            if (auths.get(m) == 0) {
            } else if (auths.get(m) == 1) {
                shop_keeper = getString(R.string.shop_keeper) + "/";
            } else if (auths.get(m) == 2) {
                salesperson = getString(R.string.salesperson) + "/";
            } else if (auths.get(m) == 3) {
                godown_man = getString(R.string.godown_man);
            }
        }
        String power = shop_keeper + salesperson + godown_man;
        if (power.endsWith("/")) {
            power = power.substring(0, power.length() - 1);
        }
        role_name.setText(power);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    //修改密码
    @InjectView(R.id.xiugai_password)
    TextView xiugai_password;
    //员工姓名
    @InjectView(R.id.yuangong_name)
    TextView yuangong_name;
    //员工手机号码
    @InjectView(R.id.yuangong_phonenum)
    TextView yuangong_phonenum;
    //员工密码
    @InjectView(R.id.yuangong_mima)
    TextView yuangong_mima;
    @InjectView(R.id.role_name)
    TextView role_name;

    //q确定
    @InjectView(R.id.zhuce_but)
    Button zhuce_but;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        EmployeeInformationActivity.this.finish();
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

    @OnClick(R.id.xiugai_save)
    void xiugai_save() {//保存

        String name = yuangong_name.getText().toString();

        if (name == null || name.trim().length() < 2 || name.trim().length() > 15) {
            ToastUtil.show("姓名输入不合格!");
            return;
        }

        if (auths.size() == 0) {
            ToastUtil.show("请选择员工角色!");
            return;
        }
        orderEasyPresenter.updateUserInfo(employee.user_id, name, auths);

    }

    @OnClick(R.id.zhuce_but)
    void zhuce_but() {
        //删除
        showdialogs1();
    }

    //修改密码换气弹窗
    @OnClick(R.id.xiugai_password)
    void xiugai_password() {
        showdialogs();
    }


    private void showdialogs1() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("您确认要删除该员工吗？");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderEasyPresenter.deleteEmployee(employee.getUser_id() + "");
                alertDialog.dismiss();
            }
        });
    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入新密码");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
        //hint内容
        ed_type_name.setHint("");
        //限制输入长度
        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderEasyPresenter.updateUserPass(employee.getUser_id(), "", ed_type_name.getText().toString());
                alertDialog.dismiss();
            }
        });
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
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("EmployeeInformation", "loadData" + data.toString());
        if (type == 2) {
            if (data.get("code").getAsInt() == 1) {
                ToastUtil.show("用户信息修改成功!");
                setResult(1001);
                finish();
            } else {
                ToastUtil.show("修改用户信息失败!");
            }
        }
        if (type == 1) {
            if (data.get("code").getAsInt() == 1) {
                ToastUtil.show("修改密码成功!");
                setResult(1001);
                finish();
            } else {
                ToastUtil.show("修改密码失败!");
            }
        } else if (type == 0) {
            if (data.get("code").getAsInt() == 1) {
                ToastUtil.show("删除员工成功!");
                setResult(1001);
                finish();
            } else {
                ToastUtil.show("删除员工失败!");
            }
        }
    }
}
