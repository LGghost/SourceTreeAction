package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SupplierBean;

public class SupplierDetailsActivity extends BaseActivity {
    private SupplierBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_details_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (SupplierBean) bundle.getSerializable("data");
        }
        initData();
    }

    private void initData() {
        supplier_name.setText(bean.getName());
        user_name.setText(bean.getUser());
        phone_number.setText(bean.getPhone());
        call_number.setText(bean.getCall());
        address.setText(bean.getAddress());
        money_num.setText(bean.getArrears() + "");
    }

    @InjectView(R.id.supplier_name)
    TextView supplier_name;
    @InjectView(R.id.user_name)
    TextView user_name;
    @InjectView(R.id.phone_number)
    TextView phone_number;
    @InjectView(R.id.call_number)
    TextView call_number;
    @InjectView(R.id.address)
    TextView address;
    @InjectView(R.id.money_num)
    TextView money_num;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    //编辑
    @OnClick(R.id.supplier_edit)
    void supplier_edit() {
    }

    //修改
    @OnClick(R.id.modify)
    void modify() {
    }

    //对账记录
    @OnClick(R.id.account_record_layout)
    void account_record_layout() {
    }

    //采购记录
    @OnClick(R.id.procurement_records_layout)
    void procurement_records_layout() {
    }

    //付款
    @OnClick(R.id.payment_layout)
    void payment_layout() {

    }

    //付款
    @OnClick(R.id.call_up_layout)
    void call_up_layout() {

    }

}
