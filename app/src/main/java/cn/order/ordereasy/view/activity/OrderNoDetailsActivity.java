package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderDetailAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.PinyinUtil;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.FragmentCust;
import cn.order.ordereasy.widget.CustomExpandableListView;
import cn.order.ordereasy.widget.GuideDialog;

/**
 * Created by Administrator on 2017/9/16.
 * <p>
 * 订单详情
 */

public class OrderNoDetailsActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {

    OrderEasyPresenter orderEasyPresenter;
    Order order;
    OrderDetailAdapter adapter;
    AlertDialog alertDialog;
    private int id;
    private List<Customer> list = new ArrayList<>();
    private Customer detailsCustomer;
    private boolean isResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        ProgressUtil.showDialog(OrderNoDetailsActivity.this);
        setContentView(R.layout.orderno_details);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initRefreshLayout();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            Log.e("JJF", "id" + id);
            orderEasyPresenter.getOrderInfo(id);
        }

        orderno_list_view.setGroupIndicator(null);
        orderno_list_view.setFocusable(false);
        //新手引导
        new GuideDialog(5, this);
    }

    private void initRefreshLayout() {
        store_refresh.setOnRefreshListener(this);
    }

    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("您确认要关闭此订单吗？");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
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
                orderEasyPresenter.closeOrder(order.getOrder_id());
                alertDialog.dismiss();
            }
        });
    }

    //下拉刷新控件
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //右上角打印按钮
    @InjectView(R.id.dayin)
    TextView dayin;

    //订单号
    @InjectView(R.id.order_num)
    TextView order_num;
    @InjectView(R.id.tv_zhankai)
    TextView tv_zhankai;

    @InjectView(R.id.close_text)
    TextView close_text;

    //订单状态图片
    @InjectView(R.id.type_image)
    ImageView type_image;

    @InjectView(R.id.guanbi_image)
    ImageView guanbi_image;

    //姓名第一个字
    @InjectView(R.id.name_shou)
    TextView name_shou;

    //订单人姓名
    @InjectView(R.id.order_name)
    TextView order_name;

    //订单人欠款数
    @InjectView(R.id.qiankuan_money_num)
    TextView qiankuan_money_num;

    //订单人手机号
    @InjectView(R.id.phone_number)
    TextView phone_number;

    //点击去收款
    @InjectView(R.id.goto_shoukuan)
    TextView goto_shoukuan;

    //点击去选择修改地址
    @InjectView(R.id.xuanze_shouhuo_addr)
    LinearLayout xuanze_shouhuo_addr;
    //点击去选择修改地址
    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;
    @InjectView(R.id.qianhuo_layout)
    LinearLayout qianhuo_layout;
    @InjectView(R.id.order_record)
    LinearLayout order_record;
    @InjectView(R.id.order_remarks_layout)
    LinearLayout order_remarks_layout;
    //显示地址
    @InjectView(R.id.addres)
    TextView addres;

    @InjectView(R.id.order_remarks)
    TextView order_remarks;
    //开单人姓名
    @InjectView(R.id.kaidanren_name)
    TextView kaidanren_name;
    @InjectView(R.id.huopin_leixing)
    TextView huopin_leixing;

    //开单日期
    @InjectView(R.id.data_time)
    TextView data_time;

    //订单金额
    @InjectView(R.id.order_money_num)
    TextView order_money_num;

    //总欠货数量
    @InjectView(R.id.qianhuo_num)
    TextView qianhuo_num;

    //点击展开
    @InjectView(R.id.zhankai_view)
    LinearLayout zhankai_view;
    //点击展开
    @InjectView(R.id.view_switcher)
    ViewSwitcher view_switcher;

    //listview
    @InjectView(R.id.orderno_list_view)
    CustomExpandableListView orderno_list_view;
    @InjectView(R.id.scroll_view)
    ScrollView scroll_view;
    /**
     * 底部三个按钮
     **/
    //退欠货
    @InjectView(R.id.tuiqianhuo)
    LinearLayout tuiqianhuo;

    //关闭订单
    @InjectView(R.id.close_orderno)
    LinearLayout close_orderno;
    //发货
    @InjectView(R.id.fahuo)
    LinearLayout fahuo;
    //优惠
    @InjectView(R.id.youhui_layout)
    LinearLayout youhui_layout;
    //优惠金额
    @InjectView(R.id.youhui_money_num)
    TextView youhui_money_num;
    //
    @InjectView(R.id.jine_text)
    TextView jine_text;

    @InjectView(R.id.yiguanbi)
    ImageView yiguanbi;

    @InjectView(R.id.yaohuo_num)
    TextView yaohuo_num;
    //修改微信订单
    @InjectView(R.id.xiugai)
    LinearLayout xiugai;

    //关闭微信订单
    @InjectView(R.id.wechat_close)
    LinearLayout wechat_close;

    //确定微信订单
    @InjectView(R.id.queding)
    LinearLayout queding;

    boolean isExpand = true;

    @OnClick(R.id.zhankai_view)
    void zhankai() {
        if (!isExpand) {
            int groupCount = orderno_list_view.getCount();
            if (groupCount < 1) return;
            for (int i = 0; i < groupCount; i++) {
                isExpand = true;
                orderno_list_view.expandGroup(i);
                tv_zhankai.setText("统一收起");
            }
        } else {
            int groupCount = orderno_list_view.getCount();
            if (groupCount < 1) return;
            for (int i = 0; i < groupCount; i++) {
                isExpand = false;
                orderno_list_view.collapseGroup(i);
                tv_zhankai.setText("统一展开");
            }
        }


    }

    /*****需要的点击事件****/
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        Intent intent = new Intent();
        intent.putExtra("isResult", isResult);
        setResult(1001, intent);
        finish();
    }

    //点击去收款
    @OnClick(R.id.goto_shoukuan)
    void goto_shoukuan() {
        Intent intent = new Intent(OrderNoDetailsActivity.this, ReceivablesActivity.class);
        Bundle bundle = new Bundle();
        Customer customer = new Customer();
        customer.setName(order.getCustomer_name());
        customer.setCustomer_id(order.getCustomer_id());
        customer.setReceivable(Double.parseDouble(qiankuan_money_num.getText().toString()));
        Log.e("JJFa", "Name:" + order.getCustomer_name() + "Receivable:" + order.getPayable());
        bundle.putSerializable("data", customer);
        intent.putExtra("flag", "order");
        intent.putExtras(bundle);
        startActivityForResult(intent, 1004);
    }

    //点击选择地址
    @OnClick(R.id.xuanze_shouhuo_addr)
    void xuanze_shouhuo_addr() {
        Intent intent = new Intent(OrderNoDetailsActivity.this, HarvestAddressActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", detailsCustomer);
        bundle.putString("flag", "order");
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }

    //打印
    @OnClick(R.id.dayin)
    void dayin() {
//        Intent intent = new Intent(OrderNoDetailsActivity.this, PrintPreviewActivity.class);
        Intent intent = new Intent(OrderNoDetailsActivity.this, PrintOrderNoSetUpActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", order);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //去发货
    @OnClick(R.id.fahuo)
    void fahuo_click() {
        if (order.getIs_close() == 1) {
            ToastUtil.show("已关闭订单不能进行此操作");
            return;
        }
//        int owe = 0;
//        if (order != null && order.getGoods_list() != null) {
//            for (Goods goods : order.getGoods_list()) {
//                for (Product product : goods.getProduct_list()) {
//                    owe += product.getOwe_num();
//                }
//            }
//        }
//        if (owe <= 0) {
//            showToast("没有欠货的订单无法进行发货");
//            return;
//        }
        Intent intent = new Intent(OrderNoDetailsActivity.this, DeliverGoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        bundle.putSerializable("data", order);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    //退欠货
    @OnClick(R.id.tuiqianhuo)
    void tuiqianhuo() {
        if (order.getIs_close() == 1) {
            ToastUtil.show("已关闭订单不能进行此操作");
            return;
        }
        int owe = 0;
        if (order != null && order.getGoods_list() != null) {
            for (Goods goods : order.getGoods_list()) {
                for (Product product : goods.getProduct_list()) {
                    owe += product.getOwe_num();
                }
            }
        }
        if (owe <= 0) {
            showToast("没有欠货的订单无法进行退欠货");
            return;
        }
        Intent intent = new Intent(OrderNoDetailsActivity.this, OutOfDebtActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        bundle.putSerializable("data", order);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    //修改微信订单
    @OnClick(R.id.xiugai)
    void xiugai() {
        Intent intent = new Intent(OrderNoDetailsActivity.this, BillingActivity.class);
        Bundle bundle = new Bundle();
        order.setTelephone(detailsCustomer.getTelephone());
        bundle.putDouble("receivable", detailsCustomer.getReceivable());
        bundle.putSerializable("Order", order);
        bundle.putString("flag", "details");
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    //关闭微信订单
    @OnClick(R.id.wechat_close)
    void wechat_close() {
        showdialogs();
    }

    //确定微信订单
    @OnClick(R.id.queding)
    void queding() {
        order.setAct("done");
        orderEasyPresenter.orderConfirm(order);
        ProgressUtil.showDialog(this);
    }

    @OnClick(R.id.order_record)
//定单操作记录
    void order_record() {
        Intent intent = new Intent(OrderNoDetailsActivity.this, OrderOperationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("order_id", order.getOrder_id());
        bundle.putInt("order_type", order.getOrder_type());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.order_remarks_layout)
//备注
    void order_remarks_layout() {
        Intent intent = new Intent(this, RemarksActivity.class);
        intent.putExtra("content", order.getRemark());
        intent.putExtra("type", 1);
        startActivity(intent);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Message message = new Message();
        ProgressUtil.dissDialog();
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
        store_refresh.setRefreshing(false);
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
                            //成功
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            SharedPreferences sp = getSharedPreferences("customers", 0);
                            if (jsonArray.size() > 0) {
                                sp.edit().putString("customers", result.get("result").toString()).commit();
                            }
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(), Customer.class);
                                String name = "";
                                if (TextUtils.isEmpty(customer.getName())) {
                                    name = "-";
                                } else {
                                    name = customer.getName();
                                }
                                customer.setName(name);
                                list.add(customer);
                            }
                            DataStorageUtils.getInstance().setCustomerLists(list);
                            for (Customer customer : list) {
                                if (order.getCustomer_id() == customer.getCustomer_id()) {
                                    detailsCustomer = customer;
                                    phone_number.setText(customer.getTelephone());
                                    order_name.setText(customer.getName());
                                    Log.e("customer", "" + customer.getReceivable());
                                    qiankuan_money_num.setText(String.valueOf(customer.getReceivable()));
                                }
                            }
                        }
                    }
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //处理返回的数据
                            close_text.setText("已关闭");
                            order.setIs_close(1);
                            orderEasyPresenter.getOrderInfo(id);
                            showToast("关闭成功");
                        }
                    }
                    Log.e("收银信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    Log.e("OrderNoDetails", result.toString());
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            isResult = true;
                            orderEasyPresenter.getOrderInfo(id);
                        }
                    }
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //处理返回的数据
                            Log.e("OrderNoDetails", "" + result.toString());
                            order = (Order) GsonUtils.getEntity(result.get("result").getAsJsonObject().toString(), Order.class);
                            if (order != null) {
                                if (order.getOrder_id() != -1) {
                                    order.setOriginal_order_id(order.getOrder_id());
                                }
                                initData();
                            } else {
                                showToast("数据异常");
                            }
                        }
                    }
                    Log.e("订单信息", result.toString());
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


    private void initData() {
        setTelephone();
        //更换右上角图标
        int owe_num = 0, oper_num = 0;
        for (Goods good : order.getGoods_list()) {

            for (Product product : good.getProduct_list()) {
                owe_num += product.getOwe_num();
                oper_num += product.getOperate_num();
            }
        }
        double price = order.getOrder_sum() - order.getPayable();
        price = (double) Math.round(price * 100) / 100;
        if (price == 0) {
            youhui_layout.setVisibility(View.GONE);
        } else {
            youhui_layout.setVisibility(View.VISIBLE);
            youhui_money_num.setText(price + "");
        }
        if (order.getIs_wechat() == 1) {
            if (order.getOrder_status() == 1) {
                view_switcher.setDisplayedChild(1);
            } else {
                view_switcher.setDisplayedChild(0);
            }
        } else {
            view_switcher.setDisplayedChild(0);
        }
        switch (order.getOrder_type()) {
            case 1:

                if (order.getIs_wechat() == 1) {
                    type_image.setImageResource(R.drawable.img_weixin);
                } else {
                    type_image.setImageResource(R.drawable.img_dingdan);
                }
                view_switcher.setVisibility(View.VISIBLE);
                jine_text.setText("订单金额：¥");
                huopin_leixing.setText("总要货数：");
                qianhuo_layout.setVisibility(View.VISIBLE);
                if (owe_num <= 0) {
                    qianhuo_num.setText("(已全部发货)");
                } else {
                    qianhuo_num.setText("(总欠货" + owe_num + ")");
                }
                break;
            case 2:
                type_image.setImageResource(R.drawable.img_tuidan_sign);
                jine_text.setText("本次应退：¥");
                huopin_leixing.setText("总退货数：");
                qianhuo_layout.setVisibility(View.GONE);
                view_switcher.setVisibility(View.GONE);
                youhui_layout.setVisibility(View.GONE);
                break;
            case 3:
                youhui_layout.setVisibility(View.GONE);
                type_image.setImageResource(R.drawable.img_change_sign);
                jine_text.setText("本次应退：¥");
                huopin_leixing.setText("总退货数：");
                qianhuo_layout.setVisibility(View.GONE);
                view_switcher.setVisibility(View.GONE);
                break;
        }
        name_shou.setText(String.valueOf(PinyinUtil.getFirstStr(order.getCustomer_name())));
        order_num.setText(order.getOrder_no());
        order_remarks.setText(order.getRemark());
        //时间
        data_time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(order.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
        if (order.getAddress().equals("")) {
            addres.setText("添加物流地址");
        } else {
            addres.setText(order.getAddress());
        }
        kaidanren_name.setText(order.getUser_name());
        order_money_num.setText(String.valueOf(order.getPayable()));

        yaohuo_num.setText(String.valueOf(oper_num));
        int status = order.getIs_close();
        if (status != 1) {
            adapter = new OrderDetailAdapter(order.getGoods_list(), this, order.getOrder_type(), false);
        } else {
            qianhuo_layout.setVisibility(View.GONE);
            yiguanbi.setVisibility(View.VISIBLE);
            goto_shoukuan.setVisibility(View.GONE);
            adapter = new OrderDetailAdapter(order.getGoods_list(), this, order.getOrder_type(), true);
        }
        orderno_list_view.setAdapter(adapter);
        final int groupCount = orderno_list_view.getCount();
        isExpand = false;
        for (int i = 0; i < groupCount; i++) {
            orderno_list_view.collapseGroup(i);
        }

        guanbi_image.setImageResource(R.drawable.icon_guanbi);
        if (status == 1 || owe_num != oper_num) {

            guanbi_image.setImageResource(R.drawable.icon_zailaiyidan);
            close_text.setText("再来一单");
            close_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(OrderNoDetailsActivity.this, BillingActivity.class);
                    Bundle bundle = new Bundle();
                    order.setTelephone(detailsCustomer.getTelephone());
                    bundle.putDouble("receivable", detailsCustomer.getReceivable());
                    bundle.putSerializable("Order", order);
                    bundle.putString("flag", "details");
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1001);
                }
            });

        } else {
            guanbi_image.setImageResource(R.drawable.icon_guanbi);
            close_text.setText("关闭订单");
            close_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialogs();
                }
            });

        }

        adapter.notifyDataSetChanged();
    }

    private void setTelephone() {
        if (DataStorageUtils.getInstance().getCustomerLists().size() > 0) {
            list = DataStorageUtils.getInstance().getCustomerLists();
            for (Customer customer : list) {
                if (order.getCustomer_id() == customer.getCustomer_id()) {
                    detailsCustomer = customer;
                    order.setTelephone(customer.getTelephone());
                    phone_number.setText(customer.getTelephone());
                    order_name.setText(customer.getName());
                    qiankuan_money_num.setText(String.valueOf(customer.getReceivable()));
                }
            }
        } else {
            orderEasyPresenter.getCustomerList();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            Log.e("JJF", "id" + id);
            isResult = true;
            orderEasyPresenter.getOrderInfo(id);
        }
        if (resultCode == 1004) {
            orderEasyPresenter.getCustomerList();
        }
        if (resultCode == 1003) {
            //获取选择的商品list
            Bundle bundle = data.getExtras();
            String addr = bundle.getString("data");
            order.setAddress(addr);
            addres.setText(addr);
            orderEasyPresenter.updateOrderAddr(order.getOrder_id(), addr, 2);
        }
    }

    @Override
    public void onRefresh() {
        orderEasyPresenter.getOrderInfo(id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("isResult", isResult);
            setResult(1001, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
