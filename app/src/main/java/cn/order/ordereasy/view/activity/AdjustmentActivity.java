package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class AdjustmentActivity extends BaseActivity implements OrderEasyView {
    private OrderEasyPresenter orderEasyPresenter;
    private int customer_id;
    private String customer_name;
    private double money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adjustment_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        if (bundle != null) {
            customer_id = bundle.getInt("customer_id");
            customer_name = bundle.getString("customer_name");
            money = bundle.getDouble("Money");
        }
        initData();
    }

    private void initData() {
        String str = "将" + customer_name + "的欠款从" + money + "元调整为：";
        setTVColor(str, "将", "的", "从", "元", getResources().getColor(R.color.shouye_hongse), adjust_name);
    }

    private void setTVColor(String str, String ch1, String ch2, String ch3, String ch4, int color, TextView tv) {
        int a = str.indexOf(ch1) + 1; //从字符ch1的下标开始
        int b = str.indexOf(ch2); //到字符ch2的下标+1结束,因为SpannableStringBuilder的setSpan方法中区间为[ a,b )左闭右开
        int c = str.indexOf(ch3) + 1; //从字符ch1的下标开始
        int d = str.indexOf(ch4) + 1;
        SpannableStringBuilder builder = new SpannableStringBuilder(str);
        builder.setSpan(new ForegroundColorSpan(color), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(color), c, d, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.setText(builder);
    }

    //找到控件ID
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    //确定
    @OnClick(R.id.adjust_confirm)
    void adjust_confirm() {
        String remark = "调整客户应收款";
        double money1;
        if (!TextUtils.isEmpty(adjust_remarks.getText().toString())) {
            remark = adjust_remarks.getText().toString();
        }
        if (TextUtils.isEmpty(adjust_money.getText().toString())) {
            ToastUtil.show("请输入调整金额");
            return;
        }
        money1 = Double.parseDouble(adjust_money.getText().toString());
        orderEasyPresenter.adjustmentArrears(customer_id, money1, remark);
    }

    @InjectView(R.id.adjust_name)
    TextView adjust_name;
    @InjectView(R.id.adjust_money)
    EditText adjust_money;
    @InjectView(R.id.adjust_remarks)
    EditText adjust_remarks;

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        if (type == 0) {
            if (data.get("code").getAsInt() == 1)//表示请求成功
            {
                ToastUtil.show("调整成功");
                setResult(1001);
                finish();
            }
        }
    }
}