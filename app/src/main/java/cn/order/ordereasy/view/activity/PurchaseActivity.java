package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class PurchaseActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

    }

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    //开单
    @OnClick(R.id.kaidan_text)
    void kaidan_text() {
        Intent intent = new Intent(this, BillingActivity.class);
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