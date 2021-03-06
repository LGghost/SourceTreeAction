package cn.order.ordereasy.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 * Created by Administrator on 2017/9/12.
 * <p>
 * 商品描述
 */

public class ShangpinDescribeAvtivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shangpin_describe);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        String data = bundle.getString("data");
        description.setText(data);
        description.setFocusable(true);
        description.setFocusableInTouchMode(true);
        description.requestFocus();
        description.setSelection(data.length());
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.desc_queren)
    TextView queren;

    @InjectView(R.id.description)
    EditText description;

    private static final int REQUEST_CODE_DESCRIPTION_PREVIEW = 1003;

    @OnClick(R.id.desc_queren)
    void click_queren() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        String desc = description.getText().toString();
        bundle.putString("desc", desc);
        intent.putExtras(bundle);
        setResult(REQUEST_CODE_DESCRIPTION_PREVIEW, intent);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        finish();
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        ShangpinDescribeAvtivity.this.finish();
    }

}
