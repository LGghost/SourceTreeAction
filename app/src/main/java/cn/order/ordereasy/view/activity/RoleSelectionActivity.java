package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.CheckBox;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class RoleSelectionActivity extends BaseActivity {
    private int shop_keeper = -1;
    private int salesperson = -1;
    private int godown_man = -1;
    private String roleName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_selection_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            roleName = bundle.getString("roleName");
        }
        if (!TextUtils.isEmpty(roleName)) {
            String[] str = roleName.split("/");
            for (int i = 0; i < str.length; i++) {
                if (Integer.parseInt(str[i]) == 1) {
                    shop_keeper = 1;
                } else if (Integer.parseInt(str[i]) == 2) {
                    salesperson = 2;
                } else if (Integer.parseInt(str[i]) == 3) {
                    godown_man = 3;
                }
            }
            if (shop_keeper != -1) {
                shop_keeper_checkbox.setChecked(true);
            }
            if (salesperson != -1) {
                salesperson_checkbox.setChecked(true);
            }
            if (godown_man != -1) {
                godown_man_checkbox.setChecked(true);
            }
        }
    }

    @InjectView(R.id.shop_keeper_checkbox)
    CheckBox shop_keeper_checkbox;
    @InjectView(R.id.salesperson_checkbox)
    CheckBox salesperson_checkbox;
    @InjectView(R.id.godown_man_checkbox)
    CheckBox godown_man_checkbox;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.role_save)
    void role_save() {//保存
        if (shop_keeper_checkbox.isChecked()) {
            shop_keeper = 1;
        }else{
            shop_keeper = -1;
        }
        if (salesperson_checkbox.isChecked()) {
            salesperson = 2;
        }else{
            salesperson = -1;
        }
        if (godown_man_checkbox.isChecked()) {
            godown_man = 3;
        }else{
            godown_man = -1;
        }
        Intent intent = new Intent();
        intent.putExtra("shop_keeper", shop_keeper);
        intent.putExtra("salesperson", salesperson);
        intent.putExtra("godown_man", godown_man);
        setResult(1001, intent);
        finish();
    }

    @OnClick(R.id.shop_keeper_layout)
    void shop_keeper_layout() {//店铺管理员
        shop_keeper_checkbox.setChecked(!shop_keeper_checkbox.isChecked());
    }

    @OnClick(R.id.salesperson_layout)
    void salesperson_layout() {//销售员
        salesperson_checkbox.setChecked(!salesperson_checkbox.isChecked());
    }

    @OnClick(R.id.godown_man_layout)
    void godown_man_layout() {//仓库管理员
        godown_man_checkbox.setChecked(!godown_man_checkbox.isChecked());
    }
}