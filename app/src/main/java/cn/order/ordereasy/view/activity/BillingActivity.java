package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderSelectGoodsListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/4.
 * <p>
 * 开单Activity
 */

public class BillingActivity extends BaseActivity implements OrderEasyView {

    private final static int REQUEST_CODE_CUST = 1001;
    private Order order = new Order();
    private OrderEasyPresenter orderEasyPresenter;
    OrderSelectGoodsListAdapter orderSelectGoodsListAdapter;
    // Goods
    AlertDialog alertDialog;
    private ToggleButton mTogBtn;
    private ToggleButton rTogBtn;
    private int xianTag = 0;
    private int shoukuanTag = 0;
    private int number = 0;
    private String flag = "bill";
    private int discount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.kaidan_view_2);
        setColor(this, this.getResources().getColor(R.color.lanse));
        mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn);
        rTogBtn = (ToggleButton) findViewById(R.id.receivables_togbtn);
        mTogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getIs_wechat() == 1) {
                    if (order.getOrder_status() == 1) {
                        ToastUtil.show("修改订单不能更改状态");
                        if (xianTag == 1) {
                            mTogBtn.setChecked(true);
                        } else {
                            mTogBtn.setChecked(false);
                        }
                        return;
                    }
                }
                if (mTogBtn.isChecked()) {
                    xianTag = 1;
                } else {
                    xianTag = 0;
                }
            }
        });
        rTogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.getIs_wechat() == 1) {
                    if (order.getOrder_status() == 1) {
                        ToastUtil.show("修改订单不能更改状态");
                        if (shoukuanTag == 1) {
                            rTogBtn.setChecked(true);
                        } else {
                            rTogBtn.setChecked(false);
                        }
                        return;
                    }
                }
                if (rTogBtn.isChecked()) {
                    shoukuanTag = 1;
                } else {
                    shoukuanTag = 0;
                }
            }
        });
        ButterKnife.inject(this);

        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderSelectGoodsListAdapter = new OrderSelectGoodsListAdapter(this);
        goods_listview.setFocusable(false);
        orderSelectGoodsListAdapter.setOnMoneyItemClickListener(new OrderSelectGoodsListAdapter.MoneyClickLister() {
            @Override
            public void changeData(double price, int num) {
                number = num;
                kaidan_order_money.setText(FileUtils.getMathNumber(price));
                kaidan_order_num.setText("共" + orderSelectGoodsListAdapter.getData().size() + "种货品 (总数量：" + number + ")");
            }
        });
        goods_listview.setAdapter(orderSelectGoodsListAdapter);
        orderSelectGoodsListAdapter.setOnItemChildClickListener(new BGAOnItemChildClickListener() {
            @Override
            public void onItemChildClick(ViewGroup parent, View childView, int position) {
                if (childView.getId() == R.id.kaidan_shanchu) {

                    orderSelectGoodsListAdapter.removeItem(position);
                    order.setGoods_list(orderSelectGoodsListAdapter.getData());
                    orderSelectGoodsListAdapter.notifyDataSetChanged();
                    orderSelectGoodsListAdapter.changeData();
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
            if (flag.equals("custhome")) {
                Customer customer = (Customer) bundle.getSerializable("data");
                this.order.setCustomer_id(customer.getCustomer_id());
                this.order.setCustomer_name(customer.getCustomer_name());
                order_name.setText(customer.getCustomer_name() + " (总欠款：¥" + customer.getReceivable() + ")");
                if (TextUtils.isEmpty(customer.getTelephone())) {
                    order_tel.setText("");
                    order.setTelephone("");
                } else {
                    order_tel.setText(customer.getTelephone());
                    order.setTelephone(customer.getTelephone());
                }
                layout_order_view1.setVisibility(View.GONE);
                layout_order_view2.setVisibility(View.VISIBLE);
            }
            if (flag.equals("details")) {
                order = (Order) bundle.getSerializable("Order");
                if (order.getIs_wechat() == 1) {
                    if (order.getOrder_status() == 1) {
                        title_text.setText("修改订单");
                        xiugai.setVisibility(View.GONE);
                        xianTag = order.getIs_deliver();
                        shoukuanTag = order.getIs_payment();
                        orderSelectGoodsListAdapter.setFlag("details");
                    }
                }
                double receivable = bundle.getDouble("receivable");
                order_name.setText(order.getCustomer_name() + " (总欠款：¥" + receivable + ")");
                addres.setText(order.getAddress());
                if (TextUtils.isEmpty(order.getTelephone())) {
                    order_tel.setText("");
                    order.setTelephone("");
                } else {
                    order_tel.setText(order.getTelephone());
                }
                remarks_textview.setText(order.getRemark());
                layout_order_view1.setVisibility(View.GONE);
                layout_order_view2.setVisibility(View.VISIBLE);
            }
        }
        searchGoods();
    }

    private void searchGoods() {
        ProgressUtil.showDialog(this);
        orderEasyPresenter.getGoodsList();
    }

    private void screenData(List<Goods> data) {
        List<Goods> data1 = order.getGoods_list();
        List<Goods> data2 = new ArrayList<>();
        double tPrice = 0;
        int tNumber = 0;
        for (int i = 0; i < data1.size(); i++) {
            for (int j = 0; j < data.size(); j++) {
                if (data.get(j).getStatus() != 1) {
                    // 已下架的货品不能用于下单
                    continue;
                }
                if (data1.get(i).getGoods_id() == data.get(j).getGoods_id()) {
                    List<Product> product = data.get(j).getProduct_list();
                    List<Product> product1 = data1.get(i).getProduct_list();
                    int gNumber = 0;
                    double gPrice = 0;
                    for (int n = 0; n < product1.size(); n++) {
                        for (int m = 0; m < product.size(); m++) {
                            if (product1.get(n).getProduct_id() == product.get(m).getProduct_id()) {
                                if (order.getIs_wechat() == 1) {
                                    if (order.getOrder_status() == 1) {
                                        product.get(m).setDefault_price(product1.get(n).getSell_price());
                                    }
                                }
                                product.get(m).setNum(product1.get(n).getOperate_num());
                                product.get(m).setPrice(product1.get(n).getOperate_num() * product1.get(n).getSell_price());
                                tPrice += product1.get(n).getOperate_num() * product1.get(n).getSell_price();
                                tNumber += product1.get(n).getOperate_num();
                                gNumber += product1.get(n).getOperate_num();
                                gPrice += product1.get(n).getOperate_num() * product1.get(n).getSell_price();
                            }
                        }
                    }
                    data.get(j).setNum(gNumber);
                    data.get(j).setPrice(gPrice);
                    data2.add(data.get(j));
                }
            }
        }
        kaidan_order_money.setText(String.valueOf(tPrice));
        kaidan_order_num.setText("共" + data2.size() + "种货品 (总数量：" + tNumber + ")");
        order.setGoods_list(data2);
        orderSelectGoodsListAdapter.setData(order.getGoods_list());
        orderSelectGoodsListAdapter.notifyDataSetChanged();
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.layout_order_view2)
    LinearLayout layout_order_view2;
    @InjectView(R.id.layout_order_view1)
    LinearLayout layout_order_view1;

    @InjectView(R.id.ed_tel)
    ClearEditText ed_tel;
    @InjectView(R.id.ed_name)
    ClearEditText ed_name;
    @InjectView(R.id.btn_commit)
    Button btn_commit;
    @InjectView(R.id.order_name)
    TextView order_name;
    @InjectView(R.id.addres)
    TextView addres;
    @InjectView(R.id.order_tel)
    TextView order_tel;
    @InjectView(R.id.mail_list)
    ImageView mail_list;

    //扫一扫
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;

    @InjectView(R.id.et_search)
    EditText et_search;

    @InjectView(R.id.xiugai)
    ImageView xiugai;
    @InjectView(R.id.kaidan_order_num)
    TextView kaidan_order_num;
    @InjectView(R.id.kaidan_order_money)
    TextView kaidan_order_money;

    @InjectView(R.id.remarks_textview)
    TextView remarks_textview;

    @InjectView(R.id.kaidan_lingshoushang)
    TextView kaidan_lingshoushang;
    @InjectView(R.id.xuanze_shouhuo_addr)
    LinearLayout xuanze_shouhuo_addr;
    @InjectView(R.id.remark_layout)
    LinearLayout remark_layout;
    @InjectView(R.id.goods_listview)
    ListView goods_listview;

    @InjectView(R.id.discount_number)
    TextView discount_number;
    @InjectView(R.id.title_text)
    TextView title_text;

    private void showdialogs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(this, R.layout.tanchuang_view_textview, null);
        //标题
        final TextView title_name = (TextView) view.findViewById(R.id.title_name);
        final TextView text_conten = (TextView) view.findViewById(R.id.text_conten);
        final TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
        final TextView queren = (TextView) view.findViewById(R.id.queren);
        builder.setView(view);
        alertDialog = builder.create();

        title_name.setText("温馨提示");
        text_conten.setText("您确认要取消开单吗？");
        //按钮1点击事件
        quxiao.setText("继续开单");
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
                BillingActivity.this.finish();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @OnClick(R.id.remark_layout)
    void remark_layout() {//备注
        Intent intent = new Intent(BillingActivity.this, RemarksActivity.class);
        intent.putExtra("content", order.getRemark());
        intent.putExtra("type", 0);
        startActivityForResult(intent, 1005);
    }

    @OnClick(R.id.discount_layout)
    void discount_layout() {//整单优惠
        Intent intent = new Intent(BillingActivity.this, SetupDiscountActivity.class);
        intent.putExtra("flag", "billing");
        startActivityForResult(intent, 1006);
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        showdialogs();
    }

    //扫一扫点击事件
    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }

    }


    @OnClick(R.id.xiugai)
    void xiugai() {
        Intent intent = new Intent(BillingActivity.this, KaiDanViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        Customer customer = new Customer();
        customer.setCustomer_id(order.getCustomer_id());
        customer.setCustomer_name(order.getCustomer_name());
        String tel = order_tel.getText().toString();
        customer.setTelephone(tel);
        bundle.putSerializable("data", customer);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    @OnClick(R.id.xuanze_shouhuo_addr)
    void shouhuodizhi() {
        Intent intent = new Intent(BillingActivity.this, HarvestAddressActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        Customer customer = new Customer();
        customer.setCustomer_id(order.getCustomer_id());
        customer.setCustomer_name(order.getCustomer_name());
        customer.setName(order.getCustomer_name());
        customer.setAddress(order.getAddres1());
        String tel = order_tel.getText().toString();
        customer.setTelephone(tel);
        bundle.putSerializable("data", customer);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);

    }

    @OnClick(R.id.kaidan_lingshoushang)
    void lingshou() {
        Customer customer = DataStorageUtils.getInstance().getRetailCustomer();
        order.setCustomer_id(customer.getCustomer_id());
        order.setCustomer_name(customer.getName());
        order_name.setText(customer.getName());
        order_tel.setText(customer.getTelephone());
        order.setTelephone(customer.getTelephone());
        layout_order_view1.setVisibility(View.GONE);
        layout_order_view2.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.et_search)
    void et_search() {

        Intent intent = new Intent(BillingActivity.this, SearchGoodsActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1002);
    }

    @OnClick(R.id.mail_list)
    void selectCusts() {
        Intent intent = new Intent(BillingActivity.this, SelectCustomersActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1004);
    }

    @OnClick(R.id.btn_commit)
    void commit() {
        double price = 0;
        int num = 0;
        List<Goods> gList = new ArrayList<>();
        for (Goods good : orderSelectGoodsListAdapter.getData()) {
            price += good.getPrice();
            num += good.getNum();
            if (good.getNum() != 0) {
                gList.add(good);
                Log.e("Billing", "good:" + good.getProduct_list().get(0).getSell_price());
            }
        }
        order.setGoods_list(gList);
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String userStr = spPreferences.getString("userinfo", "");
        JsonObject user = (JsonObject) GsonUtils.getObj(userStr, JsonObject.class);
        order.setUser_id(user.get("user_id").getAsInt());
        order.setUser_name(user.get("name").getAsString());
        order.setDiscount_price(price);
        order.setIs_deliver(xianTag);
        order.setIs_payment(shoukuanTag);
        order.setPayable(price);
        order.setSubtotal(price);
        order.setOperate_num(num);
        if (num < 1) {
            showToast("请选择商品数量！");
            return;
        }
        if (order.getCustomer_id() == 0) {
            showToast("请选择客户！");
            return;
        }
        //orderEasyPresenter.Add_Odder(order);
        Intent intent = new Intent(BillingActivity.this, OrderNoConfirmActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        if (order.getIs_wechat() == 1) {
            if (order.getOrder_status() == 1) {
                startActivityForResult(intent, 1007);
                return;
            }
        }
        startActivity(intent);

    }

    @OnClick(R.id.ed_tel)
    void tel_click() {
        Intent intent = new Intent(BillingActivity.this, KaiDanViewActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        Customer customer = new Customer();
        customer.setCustomer_id(order.getCustomer_id());
        customer.setCustomer_name(order.getCustomer_name());
        String tel = order_tel.getText().toString();
        customer.setTelephone(tel);
        bundle.putSerializable("data", customer);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_CUST);
    }

    @OnClick(R.id.ed_name)
    void name_click() {
        Intent intent = new Intent(BillingActivity.this, KaiDanViewActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        Customer customer = new Customer();
        customer.setCustomer_id(order.getCustomer_id());
        customer.setCustomer_name(order.getCustomer_name());
        String tel = order_tel.getText().toString();
        customer.setTelephone(tel);
        bundle.putSerializable("data", customer);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_CUST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == REQUEST_CODE_CUST || resultCode == 1004) {
            Bundle bundle = data.getExtras();
            Customer customer = (Customer) bundle.getSerializable("data");
            this.order.setCustomer_id(customer.getCustomer_id());
            this.order.setCustomer_name(customer.getCustomer_name());
            this.order.setAddres1(customer.getAddress());
            order_name.setText(customer.getCustomer_name() + " (总欠款：¥" + customer.getReceivable() + ")");
            if (TextUtils.isEmpty(customer.getTelephone())) {
                order_tel.setText("");
                order.setTelephone("");
            } else {
                order_tel.setText(customer.getTelephone());
                order.setTelephone(customer.getTelephone());
            }
            discount = customer.getRank_discount();
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format((double) discount / 10);
            if (Double.parseDouble(result) == 10) {
                discount_number.setText("无折扣");
            } else {
                discount_number.setText(result + "折");
            }
            layout_order_view1.setVisibility(View.GONE);
            layout_order_view2.setVisibility(View.VISIBLE);
            orderSelectGoodsListAdapter.setDiscount(discount);
            orderSelectGoodsListAdapter.notifyDataSetChanged();
        } else if (resultCode == 1002) {
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
            orderSelectGoodsListAdapter.setDiscount(discount);
            orderSelectGoodsListAdapter.setData(goods);
            orderSelectGoodsListAdapter.notifyDataSetChanged();
            kaidan_order_num.setText("共" + orderSelectGoodsListAdapter.getData().size() + "种货品 (总数量：" + number + ")");
            //initAdapterLister();
        } else if (resultCode == 1003) {
            //获取选择的商品list
            Bundle bundle = data.getExtras();
            String addr = bundle.getString("data");
            if (addr.equals("无")) {
                addres.setText("不需要发货地址");
                order.setAddress("不需要发货地址");
            } else {
                addres.setText(addr);
                order.setAddress(addr);
            }
        } else if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            Log.e("BillingActivity", "" + res);
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
                    kaidan_order_num.setText("共" + orderSelectGoodsListAdapter.getData().size() + "种货品 (总数量：" + number + ")");
                } else {
                    Intent intent = new Intent(BillingActivity.this, SearchGoodsActivity.class);
                    //利用bundle来存取数据
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flag", "order");
                    bundle1.putSerializable("data", order);
                    //再把bundle中的数据传给intent，以传输过去
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 1002);
                }
            }
        } else if (resultCode == 1005) {
            Bundle bundle = data.getExtras();
            String content = bundle.getString("desc");
            Log.e("BillingActivity", "content:" + content);
            if (content != null && !content.equals("")) {
                remarks_textview.setText("已设置");
                order.setRemark(content);
            } else {
                remarks_textview.setText("");
                order.setRemark("");
            }
        } else if (resultCode == 1006) {
            Bundle bundle = data.getExtras();
            discount = bundle.getInt("discount");
            Log.e("BillingActivity", "content:" + discount);
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format((double) discount / 10);
            if (Double.parseDouble(result) == 10) {
                discount_number.setText("无折扣");
            } else {
                discount_number.setText(result + "折");
            }
            orderSelectGoodsListAdapter.setDiscount(discount);
            orderSelectGoodsListAdapter.notifyDataSetChanged();
        } else if (resultCode == 1007) {
            setResult(1001);
            finish();
        }

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

                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            List<Goods> data = new ArrayList<>();
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Goods good = (Goods) GsonUtils.getEntity(jsonArray.get(i).toString(), Goods.class);
                                data.add(good);
                            }
                            DataStorageUtils.getInstance().setShelvesGoods(data);
                            if (flag.equals("details")) {
                                screenData(data);
                            }
                        }
                    }
                    Log.e("商品信息", result.toString());
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            showdialogs();
        }
        return super.onKeyDown(keyCode, event);

    }
}
