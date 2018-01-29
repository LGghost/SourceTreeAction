package cn.order.ordereasy.view.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import cn.order.ordereasy.R;

public class SupplierPaymentActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_payment_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

}