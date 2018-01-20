package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class SupplierReconciliationActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_reconciliation_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

    }

    @InjectView(R.id.all_supplier)
    TextView all_supplier;
    @InjectView(R.id.yingfu_supplier)
    TextView yingfu_supplier;
    @InjectView(R.id.yingfu_money)
    TextView yingfu_money;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listview)
    ListView listview;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }


}
