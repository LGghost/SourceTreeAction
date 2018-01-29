package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.order.ordereasy.R;
import cn.order.ordereasy.widget.LoadMoreListView;

public class PurchaseRecordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_record_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

    }
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.purchase_name)
    TextView purchase_name;
}