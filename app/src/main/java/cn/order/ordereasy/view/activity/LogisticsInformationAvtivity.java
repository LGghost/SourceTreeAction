package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;

/**
 * Created by Administrator on 2017/9/21.
 * <p>
 * 去发货
 */

public class LogisticsInformationAvtivity extends BaseActivity {
    private String company = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_information);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //点击选择物流公司
    @InjectView(R.id.wuliu_gongsi)
    LinearLayout wuliu_gongsi;

    //显示物流公司名称
    @InjectView(R.id.wuliu_gongs)
    TextView wuliu_gongs;

    //输入物流单号控件
    @InjectView(R.id.wuliu_code)
    ClearEditText wuliu_code;

    //扫一扫按钮
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;

    //短信开关控件
    @InjectView(R.id.mTogBtn)
    ToggleButton mTogBtn;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(this, ScanActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    @OnClick(R.id.wuliu_gongsi)
    void wuliu_gongsi() {
        Intent intent = new Intent(this, LogisticsCompanyActivity.class);
        startActivityForResult(intent, 1001);
    }

    //返回按钮
    @OnClick(R.id.queren_fahuo)
    void queren_fahuo() {
        Intent intent = new Intent();
        intent.putExtra("addrs", wuliu_gongs.getText().toString());
        intent.putExtra("code", wuliu_code.getText().toString());
        if (mTogBtn.isChecked()) {
            intent.putExtra("message", 1);
        } else {
            intent.putExtra("message", 0);
        }
        setResult(1001, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            company = data.getExtras().getString("addrs");
            wuliu_gongs.setText(company);
        }
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            boolean isExist = false;
            if (TextUtils.isEmpty(res)) {
                ToastUtil.show("没有识别到二维码信息");
            } else {
                wuliu_code.setText(res);
            }
            MyLog.e("扫一扫返回数据", res);
        }
    }
}
