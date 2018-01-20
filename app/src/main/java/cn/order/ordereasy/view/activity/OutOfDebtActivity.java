package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.TuiQianGoodsAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/20.
 * <p>
 * 退欠货Activity
 */

public class OutOfDebtActivity extends BaseActivity implements OrderEasyView, TuiQianGoodsAdapter.MyItemClickListener {

    TuiQianGoodsAdapter adapter;
    Customer customer;
    List<Goods> goods = new ArrayList<>();
    OrderEasyPresenter orderEasyPresenter;
    String flag = "";
    Order order;
    Order order1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tuiqian_goods);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        //sss
        adapter = new TuiQianGoodsAdapter(goods, this);
        adapter.setOnItemClickListener(this);
        kehu_orderno_listview.setAdapter(adapter);
        kehu_orderno_listview.setGroupIndicator(null);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            order1 = (Order) bundle.getSerializable("data");
            kehu_name.setText(order1.getCustomer_name());
            orderEasyPresenter.getOweOrdersList(-1, order1.getOrder_id());
        }

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //顶部客户名称
    @InjectView(R.id.kehu_name)
    TextView kehu_name;
//    //反选按钮
//    @InjectView(R.id.checkbox_click)
//    CheckBox checkbox_click;

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
        OutOfDebtActivity.this.finish();
    }


    @OnClick(R.id.goto_fahuo)
    void goto_fahuo() {
        order.setOrder_type(3);
        order.setOriginal_order_id(order.getOrder_id());
        order.setCustomer_id(order1.getCustomer_id());
        order.setIs_deliver(order1.getIs_deliver());
        order.setCustomer_name(order1.getCustomer_name());
        order.setTelephone(order1.getTelephone());
        order.setAddress(order1.getAddress());
        for (int i = 0; i < order.getGoods_list().size(); i++) {
            List<Product> products = order.getGoods_list().get(i).getProduct_list();
            double price = 0;
            for (Product product : products) {
                product.setSell_price(product.getDiscount_price());
                price += product.getNum() * product.getDiscount_price();
            }
            order.getGoods_list().get(i).setPrice(price);
            order.getGoods_list().get(i).setProduct_list(products);
        }
        Intent intent = new Intent(this, OrderNoConfirmActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "tuihuo");
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivity(intent);
        setResult(1001);
        finish();
//        orderEasyPresenter.Add_Odder(order);
    }

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


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JsonObject result;
            switch (msg.what) {
                case 1001:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            showToast("退欠货成功！");
                            setResult(1001);
                            finish();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("退欠货信息", result.toString());
                    break;
                case 1002:

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
                                List<Goods> removeList = new ArrayList<>();
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
                                    if (products.size() == 0) {
                                        removeList.add(good);
                                    }
                                }
                                goods.removeAll(removeList);
                                order.setGoods_list(goods);
                                datas.add(order);
                            }
                            if (datas.size() > 0) {
                                order = datas.get(0);
                                goods = order.getGoods_list();
                                adapter.setGoods(goods);
                                adapter.notifyDataSetChanged();
                                if (goods.size() > 0) {
                                    no_data_view.setVisibility(View.GONE);
                                } else {
                                    no_data_view.setVisibility(View.VISIBLE);
                                }
                                int groupCount = kehu_orderno_listview.getCount();
                                for (int i = 0; i < groupCount; i++) {
                                    kehu_orderno_listview.expandGroup(i);
                                }
                            }
                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(OutOfDebtActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("发货订单信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(OutOfDebtActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
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
    public void onItemClick() {
        int num = 0;
        int payable = 0;
        for (Goods goods1 : adapter.getGoods()) {
            num += goods1.getNum();
            payable += goods1.getDiscount_price();
        }
        order.setPayable(payable);
        fahuo_zong_num.setText("" + num);
    }
}
