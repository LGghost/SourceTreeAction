package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.DeliverGoodsAdapter;
import cn.order.ordereasy.adapter.DeliverListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Fahuo;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UpdataApp;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/20.
 * <p>
 * 发货Activity
 */

public class DeliverGoodsActivity extends BaseActivity implements OrderEasyView {

    DeliverListAdapter adapter;
    Customer customer;
    List<Order> data = new ArrayList<>();
    OrderEasyPresenter orderEasyPresenter;
    String flag = "";
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.deliver_goods);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        //sss
        adapter = new DeliverListAdapter(data, this);
        kehu_orderno_listview.setAdapter(adapter);
        kehu_orderno_listview.setGroupIndicator(null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            if (flag.equals("cust")) {
                customer = (Customer) bundle.getSerializable("data");
                kehu_name.setText(customer.getName());
                orderEasyPresenter.getOweOrdersList(customer.getCustomer_id(), -1);
            } else if (flag.equals("order")) {
                Order order = (Order) bundle.getSerializable("data");
                customer = new Customer();
                customer.setCustomer_id(order.getCustomer_id());
                customer.setCustomer_name(order.getCustomer_name());
                customer.setTelephone(order.getTelephone());
                customer.setAddress(Arrays.asList(new String[]{order.getAddress()}));
                kehu_name.setText(order.getCustomer_name());
                orderEasyPresenter.getOweOrdersList(-1, order.getOrder_id());
            }
        }

        adapter.setOnOrderItemClickListener(new DeliverListAdapter.OrderClickLister() {
            @Override
            public void changeData(int num) {
                fahuo_zong_num.setText(String.valueOf(num));
            }
        });
        checkbox_click.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int num = 0;
                    for (Order order : data) {
                        List<Goods> goods = order.getGoods_list();
                        int pnum = 0;
                        for (Goods good : goods) {
                            List<Product> products = good.getProduct_list();
                            for (Product product : products) {
                                product.setNum(product.getOwe_num());
                                num += product.getOwe_num();
                                pnum += product.getOwe_num();
                            }
                            good.setNum(pnum);
                            good.setProduct_list(products);
                        }
                        order.setGoods_list(goods);
                    }
                    fahuo_zong_num.setText(String.valueOf(num));
                    adapter.notifyDataSetChanged();
                } else {
                    for (Order order : data) {
                        List<Goods> goods = order.getGoods_list();
                        for (Goods good : goods) {
                            List<Product> products = good.getProduct_list();
                            for (Product product : products) {
                                product.setNum(0);
                            }
                            good.setNum(0);
                            good.setProduct_list(products);
                        }
                        order.setGoods_list(goods);
                    }
                    fahuo_zong_num.setText(String.valueOf(0));
                    adapter.notifyDataSetChanged();
                }

            }
        });
        //设置父节点(章目录)不可点击
        kehu_orderno_listview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//返回true,表示不可点击
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //顶部客户名称
    @InjectView(R.id.kehu_name)
    TextView kehu_name;
    //反选按钮
    @InjectView(R.id.checkbox_click)
    CheckBox checkbox_click;

    //发货总数
    @InjectView(R.id.fahuo_zong_num)
    TextView fahuo_zong_num;

    //去发货按钮
    @InjectView(R.id.goto_fahuo)
    Button goto_fahuo;

    //ListView
    @InjectView(R.id.kehu_orderno_listview)
    ExpandableListView kehu_orderno_listview;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        DeliverGoodsActivity.this.finish();
    }

    //去发货按钮
    @OnClick(R.id.goto_fahuo)
    void goto_fahuo() {
        Intent intent = new Intent(DeliverGoodsActivity.this, LogisticsInformationAvtivity.class);
        startActivityForResult(intent, 1001);
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
                ProgressUtil.dissDialog();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:
                ProgressUtil.dissDialog();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;

                }
                break;
            case 2:
                ProgressUtil.dissDialog();
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;
                }
                break;
            case 3:
                ProgressUtil.dissDialog();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == 1001) {
            String delivery_name = intent.getExtras().getString("addrs");
            String delivery_no = intent.getExtras().getString("code");

            List<Delivery> deliveries = new ArrayList<>();
            SharedPreferences spPreferences = getSharedPreferences("user", 0);
            String userStr = spPreferences.getString("userinfo", "");
            JsonObject user = (JsonObject) GsonUtils.getObj(userStr, JsonObject.class);
            for (Order order : data) {
                Delivery delivery = new Delivery();
                delivery.setRemark("发货");
                delivery.setCustomer_id(customer.getCustomer_id());
                delivery.setOperate_id(0);
                delivery.setOperate_type(Config.Operate_TYPE_DELIVER);
                List<Map<String, Object>> maps = new ArrayList<>();
                for (Goods good : order.getGoods_list()) {
                    for (Product product : good.getProduct_list()) {
                        Map<String, Object> map = new HashMap<>();
                        if (product.getNum() > 0) {
                            map.put("operate_num", product.getNum());
                            map.put("product_id", product.getProduct_id());
                            maps.add(map);
                        }
                    }
                }
                delivery.setCustomer_id(customer.getCustomer_id());
                delivery.setOrder_id(order.getOrder_id());
                delivery.setProduct_list(maps);
                delivery.setDelivery_name(delivery_name);
                delivery.setDelivery_no(delivery_no);
                if (maps.size() > 0) {
                    deliveries.add(delivery);
                }
            }
            orderEasyPresenter.delivers(deliveries);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JsonObject result;
            switch (msg.what) {
                case 1001:

                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            showToast("发货成功！");
                            if (flag.equals("cust")) {
                                setResult(1003);
                                finish();
                            } else {
                                setResult(1001);
                                finish();
                            }
                        }
                    }
                    Log.e("发货信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //处理返回的数据
                            JsonArray stocks = result.getAsJsonObject("result").getAsJsonArray("page_list");
                            List<Order> datas = new ArrayList<>();//适配器数据
                            for (int i = 0; i < stocks.size(); i++) {
                                //循环遍历获取的数据，并转成实体
                                Order order = (Order) GsonUtils.getEntity(stocks.get(i).toString(), Order.class);
                                List<Goods> goods = order.getGoods_list();
                                if (goods == null) goods = new ArrayList<>();
                                for (Goods good : goods) {
                                    List<Product> products = good.getProduct_list();
                                    if (products == null) products = new ArrayList<>();
                                    Iterator<Product> iterator = products.iterator();
                                    while (iterator.hasNext()) {
                                        Product product = iterator.next();
                                        if (product.getOwe_num() == 0)
                                            iterator.remove();   //注意这个地方
                                    }
                                    good.setProduct_list(products);
                                }
                                datas.add(order);
                            }
                            data = datas;
                            adapter.setOrders(data);
                            adapter.notifyDataSetChanged();
                            if (data.size() > 0) {
                                no_data_view.setVisibility(View.GONE);
                            } else {
                                showDialog();
                            }
                            int groupCount = kehu_orderno_listview.getCount();
                            for (int i = 0; i < groupCount; i++) {
                                kehu_orderno_listview.expandGroup(i);
                            }
                        }
                    }
                    Log.e("发货订单信息", result.toString());
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

    private void showDialog() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        //设置点击屏幕不让消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("该客户的货已全部发出");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        View view1 = window.findViewById(R.id.view1);
        view1.setVisibility(View.GONE);
        quxiao.setVisibility(View.GONE);
        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setText("确定");
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                alertDialog.dismiss();
            }
        });
    }

}
