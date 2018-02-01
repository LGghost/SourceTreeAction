package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.UpdataApp;

public class PurchaseActivity extends BaseActivity {
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        showDialog();
    }

    private void showDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        //设置点击屏幕不让消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("采购功能为体验功能，仅供用户体验试用。所有数据不保存到服务器，正式版带体验活动结束即可上线");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        View view1 = window.findViewById(R.id.view1);
        view1.setVisibility(View.GONE);
        quxiao.setVisibility(View.GONE);
        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setText("我知道了");
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    //开单
    @OnClick(R.id.kaidan_text)
    void kaidan_text() {
        Intent intent = new Intent(this, BillingPurchaseActivity.class);
        startActivity(intent);
    }

    //供应商管理
    @OnClick(R.id.supplier_management_layout)
    void supplier_management_layout() {
        Intent intent = new Intent(this, SupplierManagementActivity.class);
        startActivity(intent);
    }

    //供应商对账
    @OnClick(R.id.supplier_reconciliation_layout)
    void supplier_reconciliation_layout() {
        Intent intent = new Intent(this, SupplierReconciliationActivity.class);
        startActivity(intent);
    }

    //采购订单
    @OnClick(R.id.purchase_order_layout)
    void purchase_order_layout() {
        Intent intent = new Intent(this, PurchaseOrderActivity.class);
        startActivity(intent);
    }
}