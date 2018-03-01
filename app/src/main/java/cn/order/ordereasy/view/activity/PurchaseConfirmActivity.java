package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ConfirmBillingAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.ListUtils;
import cn.order.ordereasy.utils.ListUtilsHook;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class PurchaseConfirmActivity extends BaseActivity implements OrderEasyView {
    AlertDialog alertDialog;
    Order order = new Order();
    private String flag = "";
    private OrderEasyPresenter orderEasyPresenter;
    ConfirmBillingAdapter confirmBillingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.purchase_confirm_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        confirmBillingAdapter = new ConfirmBillingAdapter(this, 1);
        order_kehu_listview.setAdapter(confirmBillingAdapter);
        if (bundle != null) {
            order = (Order) bundle.getSerializable("data");
            flag = bundle.getString("flag");
            initData();
        }
    }

    private void initData() {
        if (TextUtils.isEmpty(order.getCustomer_name())) {
            order.setCustomer_name("无");
        }
        order_kehu_name.setText(order.getCustomer_name());
        Log.e("BillingActivity", "备注：" + order.getRemark());
        order_remark.setText(order.getRemark());
        List<Goods> goods = order.getGoods_list();
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
        }
        confirmBillingAdapter.setData(goods);
        confirmBillingAdapter.notifyDataSetChanged();
        double price = 0;
        int num = 0;
        for (Goods good : confirmBillingAdapter.getData()) {
            price += good.getPrice();
            num += good.getNum();
        }
        DecimalFormat df = new DecimalFormat("0.00");
        yingfu_money.setText("" + df.format(price));
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
    @InjectView(R.id.order_remark)
    TextView order_remark;

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
        finish();
    }

    //修改价格
    @OnClick(R.id.xiugai_jine)
    void xiugai_jine() {
        showdialogs();
    }

    @OnClick(R.id.queren_tijiao)
    void queren_tijiao() {
        order.setGoods_list(confirmBillingAdapter.getData());
        if (flag.equals("tuihuo")) {
            order.setOrder_type(2);
        } else {
            order.setOrder_type(1);
        }
        order.setPayable(Double.parseDouble(yingfu_money.getText().toString()));
        orderEasyPresenter.supplierAddOrder(order);
    }

    @OnClick(R.id.remark_layout)
    void remark_layout() {
        Intent intent = new Intent(this, RemarksActivity.class);
        intent.putExtra("content", order.getRemark());
        intent.putExtra("type", 0);
        startActivityForResult(intent, 1005);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1005) {
            Bundle bundle = data.getExtras();
            String content = bundle.getString("desc");
            Log.e("BillingActivity", "content:" + content);
            if (content != null && !content.equals("")) {
                order_remark.setText(content);
                order.setRemark(content);
            } else {
                order_remark.setText("");
                order.setRemark("");
            }
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
        if (data != null) {
            Log.e("PurchaseConfirmActivity", "data:" + data.toString());
            int status = data.get("code").getAsInt();
            if (status == 1) {
                if (flag.equals("tuihuo")) {
                    DataStorageUtils.getInstance().setPurchaseBilling(true);
                    setResult(1003);
                }
                finish();
            }
        }
    }
}
