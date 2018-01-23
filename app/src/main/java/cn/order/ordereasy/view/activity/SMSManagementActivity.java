package cn.order.ordereasy.view.activity;

import android.os.Bundle;

import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class SMSManagementActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms_management_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

    }

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }
}