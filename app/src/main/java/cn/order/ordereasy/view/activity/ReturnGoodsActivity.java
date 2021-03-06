package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderSelectGoodsListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.Redelivery;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * 退货Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class ReturnGoodsActivity extends BaseActivity implements OrderEasyView {
    private Order order = new Order();
    OrderSelectGoodsListAdapter orderSelectGoodsListAdapter;
    private OrderEasyPresenter orderEasyPresenter;
    private int tuihuoNumber;
    private AlertDialog alertDialog;
    private String flag;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.return_goods);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Customer customer = (Customer) bundle.getSerializable("data");
            flag = bundle.getString("flag");//区分采购退货和客户退货
            order.setCustomer_id(customer.getCustomer_id());
            order.setCustomer_name(customer.getName());
            kehu_name.setText(customer.getName());
            if (customer.getAddress().size() > 0) {
                order.setAddress(customer.getAddress().get(0));
            }
        }
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderSelectGoodsListAdapter = new OrderSelectGoodsListAdapter(this);
        orderSelectGoodsListAdapter.setOnMoneyItemClickListener(new OrderSelectGoodsListAdapter.MoneyClickLister() {
            @Override
            public void changeData(double price, int num) {
                //kaidan_order_money.setText(String.valueOf(price));
                //kaidan_order_num.setText(String.valueOf(num));
                //计算三个值
                tuihuo_num.setText(String.valueOf(num));
                money_num.setText(String.valueOf(price));
                int n = 0;
                for (Goods good : order.getGoods_list()) {
                    if (good.getNum() > 0) {
                        n++;
                    }
                }
                tuihuoNumber = n;
                huopin_num.setText(String.valueOf(n));

            }
        });
        tuihuo_listview.setAdapter(orderSelectGoodsListAdapter);
        orderSelectGoodsListAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                if (childView.getId() == R.id.kaidan_shanchu) {
                    showdialogs(position);
                }
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //扫一扫
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;

    //输入框
    @InjectView(R.id.et_search)
    EditText et_search;

    //listview
    @InjectView(R.id.tuihuo_listview)
    ListView tuihuo_listview;

    //退货数量
    @InjectView(R.id.tuihuo_num)
    TextView tuihuo_num;
    @InjectView(R.id.kehu_name)
    TextView kehu_name;

    //总计金额
    @InjectView(R.id.money_num)
    TextView money_num;
    //货品数量
    @InjectView(R.id.huopin_num)
    TextView huopin_num;
    //退款按钮
    @InjectView(R.id.btn_commit)
    TextView btn_commit;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        ReturnGoodsActivity.this.finish();
    }

    //输入框点击
    @OnClick(R.id.et_search)
    void et_search() {
        et_search.setInputType(InputType.TYPE_NULL);
        Intent intent = new Intent(ReturnGoodsActivity.this, SearchGoodsActivity.class);
        //Intent intent =new Intent(BillingActivity.this,SearchGoodsActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "tuihuo");
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }

    //扫一扫点击事件
    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(ReturnGoodsActivity.this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    //去退
    @OnClick(R.id.btn_commit)
    void btn_commit() {
        if (tuihuoNumber == 0) {
            ToastUtil.show("操作数量不能为0");
            return;
        }
        Intent intent;
        if(flag.equals("procurement")){
            intent = new Intent(ReturnGoodsActivity.this, PurchaseConfirmActivity.class);
        }else {
            intent = new Intent(ReturnGoodsActivity.this, OrderNoConfirmActivity.class);
        }
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "tuihuo");
        //退货必须为现场发货
        order.setIs_deliver(1);
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫一扫结果
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            if (TextUtils.isEmpty(res)) {
                showToast("没有识别到二维码信息");
            } else {
                if (DataStorageUtils.getInstance().getShelvesGoods().size() > 0) {
                    boolean isExist = false;
                    List<Goods> goodsList = DataStorageUtils.getInstance().getShelvesGoods();
                    Goods good = new Goods();
                    for (Goods good1 : goodsList) {
                        if (String.valueOf(good1.getGoods_no()).equals(res)) {
                            good = good1;
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        ToastUtil.show("没有找到该货品");
                        return;
                    }
                    List<Goods> goods = order.getGoods_list();
                    if (goods == null) goods = new ArrayList<>();
                    boolean flag = false;
                    for (int i = 0; i < goods.size(); i++) {
                        if (goods.get(i).getGoods_no().equals(res)) {
                            flag = true;
                        }
                    }
                    if (!flag) {
                        goods.add(0, good);
                    } else {
                        showToast("部分商品相同，已作出过滤");
                    }
                    for (int i = 0; i < goods.size(); i++) {
                        List<Product> products = goods.get(i).getProduct_list();
                        if (products == null) products = new ArrayList<>();
                        for (Product p : products) {
                            p.setPos(i);
                        }
                        goods.get(i).setProduct_list(products);
                    }
                    order.setGoods_list(goods);
                    orderSelectGoodsListAdapter.setData(goods);
                    orderSelectGoodsListAdapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(ReturnGoodsActivity.this, SearchGoodsActivity.class);
                    //利用bundle来存取数据
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flag", "order");
                    bundle1.putSerializable("data", order);
                    //再把bundle中的数据传给intent，以传输过去
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 1002);
                }
            }
        }

        if (resultCode == 1002) {
            //获取选择的商品list
            Bundle bundle = data.getExtras();
            Goods good = (Goods) bundle.getSerializable("data");
            List<Goods> goods = order.getGoods_list();
            if (goods == null) goods = new ArrayList<>();
            boolean flag = false;
            for (int i = 0; i < goods.size(); i++) {
                if (good.getGoods_id() == goods.get(i).getGoods_id()) {
                    flag = true;
                }
            }
            if (!flag) {
                goods.add(good);
            } else {
                showToast("部分商品相同，已作出过滤");
            }
            for (int i = 0; i < goods.size(); i++) {
                List<Product> products = goods.get(i).getProduct_list();
                if (products == null) products = new ArrayList<>();
                for (Product p : products) {
                    p.setPos(i);
                }
                goods.get(i).setProduct_list(products);
            }
            order.setGoods_list(goods);
            orderSelectGoodsListAdapter.setData(goods);
            orderSelectGoodsListAdapter.notifyDataSetChanged();
            //initAdapterLister();
        }
        if (resultCode == 1003) {
            finish();
        }
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

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
                            showToast("退货成功");
                            finish();
                        }
                    }
                    Log.e("退货信息", result.toString());
                    break;
            }
        }
    };

    private void showdialogs(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.tanchuang_view_textview, null);
        //标题
        final TextView title_name = (TextView) view.findViewById(R.id.title_name);
        final TextView text_conten = (TextView) view.findViewById(R.id.text_conten);
        final TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
        final TextView queren = (TextView) view.findViewById(R.id.queren);
        builder.setView(view);
        alertDialog = builder.create();
        title_name.setVisibility(View.GONE);
        text_conten.setText("确认要删除货品吗？");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //按钮2确认点击事件
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderSelectGoodsListAdapter.removeItem(position);
                order.setGoods_list(orderSelectGoodsListAdapter.getData());
                orderSelectGoodsListAdapter.notifyDataSetChanged();
                orderSelectGoodsListAdapter.changeData();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
