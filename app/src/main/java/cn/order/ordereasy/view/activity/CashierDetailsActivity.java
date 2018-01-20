package cn.order.ordereasy.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.utils.PinyinUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.widget.CharacterParser;

/**
 * Created by Administrator on 2017/9/21.
 * <p>
 * 收银详情
 */

public class CashierDetailsActivity extends BaseActivity {

    Money money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.cashier_details);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            money = (Money) bundle.getSerializable("data");
            String name = money.getCustomer_name();
            if (!TextUtils.isEmpty(name)) {
                name = PinyinUtil.getFirstStr(name) + "";
            }
            name_shou_zimu.setText(String.valueOf(name));
            kehu_name.setText(money.getCustomer_name());
            shoukuan_jine.setText(money.getMoney() + "");
            if (money.getPayment_type() == 1) {
                tuikuan_jine.setText("收款金额");
                shoukuan_jine.setText("+ " + String.valueOf(money.getMoney()) + "元");
                shoukuan_jine.setTextColor(getResources().getColor(R.color.lvse));
            } else {
                tuikuan_jine.setText("退款金额");
                shoukuan_jine.setText("- " + String.valueOf(money.getMoney()) + "元");
                shoukuan_jine.setTextColor(getResources().getColor(R.color.shouye_hongse));
            }
            phone_number.setText(bundle.getString("tel"));
            data_time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(money.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
            caozuoren.setText(money.getUser_name());
            switch (money.getPayment_way()) {
                case 1:
                    zhanghu.setText("现金");
                    break;
                case 2:
                    zhanghu.setText("支付宝");
                    break;
                case 3:
                    zhanghu.setText("微信");
                    break;
                case 4:
                    zhanghu.setText("银行卡");
                    break;
                default:
                    zhanghu.setText("其他");
                    break;

            }
        }
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //首字母
    @InjectView(R.id.name_shou_zimu)
    TextView name_shou_zimu;

    //客户名称
    @InjectView(R.id.kehu_name)
    TextView kehu_name;

    //手机号码
    @InjectView(R.id.phone_number)
    TextView phone_number;

    //收款金额
    @InjectView(R.id.shoukuan_jine)
    TextView shoukuan_jine;
    //收款金额
    @InjectView(R.id.tuikuan_jine)
    TextView tuikuan_jine;
    //时间
    @InjectView(R.id.data_time)
    TextView data_time;

    //操作人
    @InjectView(R.id.caozuoren)
    TextView caozuoren;

    //账户类型
    @InjectView(R.id.zhanghu)
    TextView zhanghu;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        CashierDetailsActivity.this.finish();
    }
}
