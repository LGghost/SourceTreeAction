package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.view.fragment.MainActivity;

/**
 * Created by Administrator on 2017/9/17.
 * <p>
 * 开单成功
 */

public class BillingSuccessActivity extends BaseActivity {

    private Customer customer;
    private Order order;
    List<OrderList> orders = new ArrayList<>();
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_success);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            customer = (Customer) bundle.getSerializable("data");
            order = (Order) bundle.getSerializable("order");
            if (flag.equals("tuihuo")) {
                type_text.setText("应退金额");
            }
            MyLog.e("金额", customer.getTrade_money() + "," + customer.getCustomer_name());
            kehu_name.setText(customer.getCustomer_name());

            yingfu_money.setText(String.valueOf(customer.getTrade_money()));
            Log.e("BillingSuccessActivity", order.getCreate_time());
        }
    }

    //找到控件ID


    //完成按钮
    @InjectView(R.id.wancheng)
    TextView wancheng;

    //客户名称
    @InjectView(R.id.kehu_name)
    TextView kehu_name;

    @InjectView(R.id.type_text)
    TextView type_text;

    //应付金额
    @InjectView(R.id.yingfu_money)
    TextView yingfu_money;

    //打印
    @InjectView(R.id.dayin)
    LinearLayout dayin;

    //收银按钮
    @InjectView(R.id.shouyin)
    LinearLayout shouyin;

    //去发货按钮
    @InjectView(R.id.fahuo)
    LinearLayout fahuo;


    //继续开单
    @InjectView(R.id.jixu_kaidan)
    Button jixu_kaidan;


    //需要的点击事件

    //完成按钮
    @OnClick(R.id.wancheng)
    void wancheng() {
        DataStorageUtils.getInstance().setBilling(true);
        if (flag.equals("tuihuo")) {
            finish();
        } else {
            Intent intent = new Intent(BillingSuccessActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
    }

    //打印按钮
    @OnClick(R.id.dayin)
    void dayin() {
        Intent intent = new Intent(BillingSuccessActivity.this, PrintOrderNoSetUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", order);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //收银按钮
    @OnClick(R.id.shouyin)
    void shouyin() {
        Intent intent = new Intent(BillingSuccessActivity.this, ReceivablesActivity.class);
        Bundle bundle = new Bundle();
        customer.setName(customer.getCustomer_name());
        customer.setReceivable(customer.getTrade_money());
        bundle.putSerializable("data", customer);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //发货按钮
    @OnClick(R.id.fahuo)
    void fahuo() {
        if (customer.getOwe_sum() > 0) {
            Intent intent = new Intent(BillingSuccessActivity.this, DeliverGoodsActivity.class);
            Bundle bundle = new Bundle();
            customer.setName(customer.getCustomer_name());
            customer.setReceivable(customer.getTrade_money());
            bundle.putString("flag", "cust");
            bundle.putSerializable("data", customer);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            showToast("暂无发货！");
        }


    }

    //继续开单按钮
    @OnClick(R.id.jixu_kaidan)
    void jixu_kaidan() {
        Intent intent = new Intent(BillingSuccessActivity.this, BillingActivity.class);
        startActivity(intent);
        finish();
    }

    //屏蔽当前界面手机自带返回按键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}
