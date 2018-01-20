package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class LogisticsMessageActivity extends BaseActivity {
    private String name;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_message_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getExtras().getString("delivery_name");
            code = intent.getExtras().getString("delivery_no");
            wuliu_gongs.setText(name);
            wuliu_code.setText(code);
        }
    }

    //显示物流公司名称
    @InjectView(R.id.wuliu_gongs)
    TextView wuliu_gongs;

    //输入物流单号控件
    @InjectView(R.id.wuliu_code)
    TextView wuliu_code;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }
}