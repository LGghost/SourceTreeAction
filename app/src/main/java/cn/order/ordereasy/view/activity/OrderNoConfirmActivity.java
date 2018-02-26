package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.ArrayRes;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ConfirmBillingAdapter;
import cn.order.ordereasy.adapter.OrderSelectGoodsListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ListUtils;
import cn.order.ordereasy.utils.ListUtilsHook;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/17.
 * <p>
 * 订单确认
 */

public class OrderNoConfirmActivity extends BaseActivity implements OrderEasyView {

    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;
    Order order = new Order();
    private String flag = "";

    ConfirmBillingAdapter confirmBillingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.orderno_confirm);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();


        confirmBillingAdapter = new ConfirmBillingAdapter(this);
        order_kehu_listview.setAdapter(confirmBillingAdapter);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        if (bundle != null) {
            flag = bundle.getString("flag");
            if (flag.equals("tuiqianhuo")) {
                pay_type.setText("应退");
                title_name.setText("退欠货订单确定");
            } else if (flag.equals("tuihuo")) {
                pay_type.setText("应退");
                title_name.setText("退货订单确定");
            }
            order = (Order) bundle.getSerializable("data");
            initData();
        }
    }

    private void initData() {
        if (TextUtils.isEmpty(order.getCustomer_name())) {
            order.setCustomer_name("无");
        }
        order_kehu_name.setText(order.getCustomer_name());
        if (TextUtils.isEmpty(order.getTelephone())) {
            order.setTelephone("无");
        }
        if (TextUtils.isEmpty(order.getAddress())) {
            order.setAddress("不需要发货地址");
        }
        Log.e("BillingActivity", "备注：" + order.getRemark());
        order_kehu_phone.setText(order.getTelephone());
        order_kehu_addr.setText(order.getAddress());
        List<Goods> goods = order.getGoods_list();
        List<Goods> goodsList = new ArrayList<>();
        if (goods == null) goods = new ArrayList<>();
        for (Goods good : goods) {
            Log.e("OrderNoConfirm", "goods:" + good.getPrice());
            List<Product> products = new ArrayList<>();
            products = ListUtils.filter(good.getProduct_list(), new ListUtilsHook<Product>() {
                @Override
                public boolean compare(Product order) {
                    return order.getNum() > 0;
                }
            });
            good.setProduct_list(products);
            if (good.getProduct_list().size() > 0) {
                goodsList.add(good);
            }
        }
        order.setGoods_list(goodsList);
        confirmBillingAdapter.setData(goodsList);
        confirmBillingAdapter.notifyDataSetChanged();
        double price = 0;
        int num = 0;
        for (Goods good : confirmBillingAdapter.getData()) {
            price += good.getPrice();
            num += good.getNum();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        yingfu_money.setText("" + df.format(price));

        if (order.getIs_wechat() == 1) {
            if (order.getOrder_status() == 1) {
                queren_tijiao.setText("确认修改");
            }
        }
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;


    @InjectView(R.id.title_name)
    TextView title_name;
    //订单人姓名
    @InjectView(R.id.order_kehu_name)
    TextView order_kehu_name;
    //订单人手机号码
    @InjectView(R.id.order_kehu_phone)
    TextView order_kehu_phone;
    //订单人地址
    @InjectView(R.id.order_kehu_addr)
    TextView order_kehu_addr;

    //应付金额
    @InjectView(R.id.yingfu_money)
    TextView yingfu_money;

    //应付金额
    @InjectView(R.id.xiugai_jine)
    ImageView xiugai_jine;

    //确认提交
    @InjectView(R.id.queren_tijiao)
    Button queren_tijiao;

    @InjectView(R.id.pay_type)
    TextView pay_type;
    /***
     * 底部控件
     * **/
    //ListView
    @InjectView(R.id.order_kehu_listview)
    ListView order_kehu_listview;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        OrderNoConfirmActivity.this.finish();
    }

    //修改价格
    @OnClick(R.id.xiugai_jine)
    void xiugai_jine() {
        showdialogs();
    }

    @OnClick(R.id.queren_tijiao)
    void queren_tijiao() {
        double price = 0;
        int num = 0;
        for (Goods good : confirmBillingAdapter.getData()) {
            price += good.getPrice();
            num += good.getNum();
        }
        order.setGoods_list(confirmBillingAdapter.getData());
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String userStr = spPreferences.getString("userinfo", "");
        JsonObject user = (JsonObject) GsonUtils.getObj(userStr, JsonObject.class);
        order.setUser_id(user.get("user_id").getAsInt());
        order.setUser_name(user.get("name").getAsString());
        order.setDiscount_price(price);
        order.setOperate_num(num);
        if (flag.equals("tuiqianhuo")) {
            order.setOrder_type(3);
        } else if (flag.equals("tuihuo")) {
            order.setOrder_type(2);
        } else {
            order.setOrder_type(1);
        }
        order.setSubtotal(price);
        order.setPayable(Double.parseDouble(yingfu_money.getText().toString()));
        Log.e("BillingActivity", "备注1：" + order.getRemark());
        if (order.getIs_wechat() == 1) {
            if (order.getOrder_status() == 1) {
                orderEasyPresenter.orderConfirm(order);
            } else {
                orderEasyPresenter.Add_Odder(order);
            }
        } else {
            orderEasyPresenter.Add_Odder(order);
        }
        ProgressUtil.showDialog(this);
    }


    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入应付金额");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
        //只能输入数字和小数点
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        //hint内容
        ed_type_name.setText(yingfu_money.getText().toString());
        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                ed_type_name.setSelection(yingfu_money.getText().toString().length());
            }
        });
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yingfu_money.setText("" + ed_type_name.getText().toString());
                orderEasyPresenter.addCategoryInfo(ed_type_name.getText().toString());
                alertDialog.dismiss();
            }
        });

        //监听edittext
        ed_type_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    queren.setTextColor(getResources().getColor(R.color.lanse));
                    queren.setEnabled(true);
                } else {
                    queren.setTextColor(getResources().getColor(R.color.touzi_huise));
                    queren.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
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
                    Log.e("OrderNoConfirm", "" + result);
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //showToast("开单成功");
                            JsonObject goodsResult = result.getAsJsonObject("result");

                            Intent intent = new Intent(OrderNoConfirmActivity.this, BillingSuccessActivity.class);
                            Bundle bundle = new Bundle();
                            Customer customer = new Customer();
                            customer.setCustomer_id(order.getCustomer_id());
                            customer.setCustomer_name(order.getCustomer_name());
                            customer.setTelephone(order.getTelephone());
                            customer.setAddress(Arrays.asList(new String[]{order.getAddress()}));
                            customer.setTrade_money(order.getPayable());
                            customer.setOrder_id(Integer.parseInt(goodsResult.get("order_id").toString()));
                            customer.setOwe_sum(Integer.parseInt(goodsResult.get("owe_num").toString()));
                            order.setCreate_time(goodsResult.get("create_time").toString());
                            bundle.putString("flag", flag);
                            bundle.putSerializable("data", customer);
                            bundle.putSerializable("order", order);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            if (flag.equals("tuihuo")) {
                                DataStorageUtils.getInstance().setBilling(true);
                                setResult(1003);
                            }
                            finish();

                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    Log.e("OrderNoConfirm", result.toString());
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            setResult(1007);
                            finish();
                        }
                    }
                    break;

            }
        }
    };
}
