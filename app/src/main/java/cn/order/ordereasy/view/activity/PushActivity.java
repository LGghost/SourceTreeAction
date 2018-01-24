package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class PushActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
    }

    //规格管理
    @InjectView(R.id.text_conten)
    TextView text_conten;

    @OnClick(R.id.cancel)
    void cancel() {
        finish();
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
    }

    @OnClick(R.id.confirm)
    void confirm() {
        finish();
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
    }
}