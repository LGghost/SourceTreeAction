package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Fahuo;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/17.
 * <p>
 * 收款
 */

public class ReceivablesActivity extends BaseActivity implements OrderEasyView, TextWatcher {

    private Customer customer;
    OrderEasyPresenter orderEasyPresenter;
    int type = 1;
    String flag = "default";
    double cash = 0, alipay = 0, wechat = 0, card = 0, other = 0;
    double amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.receivables);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        ed_xianjin.addTextChangedListener(this);
        ed_zhifubao.addTextChangedListener(this);
        ed_bankcard.addTextChangedListener(this);
        ed_weixin.addTextChangedListener(this);
        ed_qita.addTextChangedListener(this);
        if (bundle != null) {
            customer = (Customer) bundle.getSerializable("data");
            kehu_name.setText(customer.getName());
            flag = bundle.getString("flag");
            zong_qiankuan.setText(String.valueOf(customer.getReceivable()));
        }

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.queren)
    LinearLayout queren;

    @InjectView(R.id.zongji)
    TextView zongji;
    @InjectView(R.id.title)
    TextView title;

    //右上角去退款按钮
    @InjectView(R.id.tuikuan)
    TextView tuikuan;

    //客户名称
    @InjectView(R.id.kehu_name)
    TextView kehu_name;

    //总欠款数
    @InjectView(R.id.zong_qiankuan)
    TextView zong_qiankuan;
    @InjectView(R.id.text)
    TextView text;

    //输入现金控件
    @InjectView(R.id.ed_xianjin)
    EditText ed_xianjin;

    //输入支付宝控件
    @InjectView(R.id.ed_zhifubao)
    EditText ed_zhifubao;

    //输入微信控件
    @InjectView(R.id.ed_weixin)
    EditText ed_weixin;

    //输入银行卡控件
    @InjectView(R.id.ed_bankcard)
    EditText ed_bankcard;

    //输入其他控件
    @InjectView(R.id.ed_qita)
    EditText ed_qita;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        ReceivablesActivity.this.finish();
    }

    @OnClick(R.id.tuikuan)
    void click_tuikuan() {
       /* Intent intent=new Intent(ReceivablesActivity.this,TuiKuanActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",customer);
        intent.putExtras(bundle);
        ReceivablesActivity.this.finish();
        startActivity(intent);*/
        if (type == 1) {
            tuikuan.setText("去收款");
            text.setText("确认退款¥");
            title.setText("退款");
            type = 2;
        } else if (type == 2) {
            tuikuan.setText("去退款");
            text.setText("确认收款¥");
            title.setText("收款");
            type = 1;
        }

    }

    @OnClick(R.id.queren)
    void save() {
        if (type == 1) {
            orderEasyPresenter.addOrderPay(customer.getCustomer_id(), 1, cash, wechat, alipay, card, other);
        } else if (type == 2) {
            orderEasyPresenter.addOrderPay(customer.getCustomer_id(), 2, cash, wechat, alipay, card, other);
        }
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Message message = new Message();
        switch (type) {
            case 0:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;

                }
                break;
            case 2:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;
                }
                break;
            case 3:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1004;

                }

                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //处理返回的数据
                            if (type == 1) {
                                showToast("收款成功");
                            } else {
                                showToast("退款成功");
                            }
                            if (flag != null && flag.equals("order")) {
                                setResult(1004);
                            } else {
                                setResult(1001);
                            }
                            ReceivablesActivity.this.finish();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);

                        }

                    }
                    Log.e("订单信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //处理返回的数据

                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);

                        }
                    }
                    Log.e("收银信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //处理返回的数据

                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);

                        }
                    }
                    Log.e("发货信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);

                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1007:
                    ToastUtil.show("出错了哟~");
                    break;
                case 9999:
                    ToastUtil.show("网络有问题哟~");
                    break;
            }
        }
    };


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        calcData();
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        calcData();
    }

    @Override
    public void afterTextChanged(Editable s) {
        calcData();
    }

    void calcData() {
        String cash_str = ed_xianjin.getText().toString();
        if (TextUtils.isEmpty(cash_str)) {
            cash_str = "0";
        }
        String alipay_str = ed_zhifubao.getText().toString();
        if (TextUtils.isEmpty(alipay_str)) {
            alipay_str = "0";
        }
        String wechat_str = ed_weixin.getText().toString();
        if (TextUtils.isEmpty(wechat_str)) {
            wechat_str = "0";
        }
        String card_str = ed_bankcard.getText().toString();
        if (TextUtils.isEmpty(card_str)) {
            card_str = "0";
        }
        String other_str = ed_qita.getText().toString();
        if (TextUtils.isEmpty(other_str)) {
            other_str = "0";
        }
        cash = Double.parseDouble(cash_str);
        alipay = Double.parseDouble(alipay_str);
        wechat = Double.parseDouble(wechat_str);
        card = Double.parseDouble(card_str);
        other = Double.parseDouble(other_str);
        amount = cash + alipay + wechat + card + other;
        zongji.setText(String.valueOf(amount));
    }
}
