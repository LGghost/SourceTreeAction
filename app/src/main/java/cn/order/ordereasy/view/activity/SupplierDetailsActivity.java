package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.utils.ToastUtil;

public class SupplierDetailsActivity extends BaseActivity {
    private SupplierBean bean;
    private AlertDialog alertDialog;

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
        Intent intent = new Intent(this, AddSuppliersActivity.class);
        intent.putExtra("data", bean);
        startActivity(intent);
    }

    //修改
    @OnClick(R.id.modify)
    void modify() {
        //弹出框
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.supplier_dialog_layout, null);

        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.supplier_dialog_layout);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入欠供应商款数");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
        final EditText supplier_remarks = (EditText) window.findViewById(R.id.supplier_remarks);
        final TextView supplier_remarks_text = (TextView) window.findViewById(R.id.supplier_remarks_text);
        FrameLayout frame_layout = (FrameLayout) window.findViewById(R.id.remarks_frame_layout);
        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        supplier_remarks.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        ed_type_name.requestFocus();
        supplier_remarks.requestFocus();
        supplier_remarks.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                    supplier_remarks.setVisibility(View.VISIBLE);
                    supplier_remarks_text.setVisibility(View.GONE);
                    supplier_remarks.setSelection(supplier_remarks.getText().toString().length());
                } else {
                    // 此处为失去焦点时的处理内容
                    supplier_remarks.setVisibility(View.GONE);
                    supplier_remarks_text.setVisibility(View.VISIBLE);
                    String content = supplier_remarks.getText().toString();
                    if (!TextUtils.isEmpty(content)) {
                        supplier_remarks_text.setText(content);
                    } else {
                        supplier_remarks_text.setText("");
                        supplier_remarks.setVisibility(View.VISIBLE);
                        supplier_remarks_text.setVisibility(View.GONE);
                    }
                }
            }
        });
        //hint内容
        ed_type_name.setHint(bean.getArrears() + "");
        //限制输入长度
//        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

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
                String addr = ed_type_name.getText().toString();
                String remarks = supplier_remarks_text.getText().toString();
                if (!TextUtils.isEmpty(addr)) {
                    money_num.setText(addr);
                    bean.setArrears(Double.parseDouble(addr));
                } else {
                    ToastUtil.show("请输入欠供应商款数");
                }
                if (!TextUtils.isEmpty(remarks)) {
                    bean.setRemarks(remarks);
                }
                alertDialog.dismiss();
            }
        });


    }

    //对账记录
    @OnClick(R.id.account_record_layout)
    void account_record_layout() {
        Intent intent = new Intent(this, PurchaseRecordActivity.class);
        intent.putExtra("data", bean);
        startActivity(intent);
    }

    //采购记录
    @OnClick(R.id.procurement_records_layout)
    void procurement_records_layout() {
        Intent intent = new Intent(this, PurchaseOrderActivity.class);
        startActivity(intent);
    }

    //付款
    @OnClick(R.id.payment_layout)
    void payment_layout() {
        Intent intent = new Intent(this, SupplierPaymentActivity.class);
        intent.putExtra("data", bean);
        startActivity(intent);
    }

    //打电话
    @OnClick(R.id.call_up_layout)
    void call_up_layout() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + bean.getPhone()));
        startActivity(intent);
    }

}
