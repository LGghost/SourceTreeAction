package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.billingPurchaseAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class BillingPurchaseActivity extends BaseActivity implements OrderEasyView {

    private final static int REQUEST_CODE_CUST = 1001;
    private Order order = new Order();
    private billingPurchaseAdapter adapter;
    // Goods
    private ToggleButton mTogBtn;
    private ToggleButton rTogBtn;
    private int xianTag = 0;
    private int shoukuanTag = 0;
    private int number = 0;
    private List<Goods> goods = new ArrayList<>();
    private SupplierBean bean;
    private OrderEasyPresenter orderEasyPresenter;
    private String flag = "bill";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billing_purchase_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);//网络请求接口（MVP模式）
        mTogBtn = (ToggleButton) findViewById(R.id.mTogBtn);
        rTogBtn = (ToggleButton) findViewById(R.id.receivables_togbtn);
        mTogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                if (rTogBtn.isChecked()) {
                    shoukuanTag = 1;
                } else {
                    shoukuanTag = 0;
                }
            }
        });


        adapter = new billingPurchaseAdapter(this);
        goods_listview.setFocusable(false);
        adapter.setOnMoneyItemClickListener(new billingPurchaseAdapter.MoneyClickLister() {
            @Override
            public void changeData(double price, int num) {
                number = num;
                Log.e("JJF", "price:" + FileUtils.getMathNumber(price));
                kaidan_money.setText(FileUtils.getMathNumber(price));
                kaidan_order_num.setText("共" + adapter.getData().size() + "种货品 (总数量：" + number + ")");
            }

            @Override
            public void delete(int postion) {
                goods.remove(postion);
            }
        });
        goods_listview.setAdapter(adapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order = (Order) bundle.getSerializable("Order");
            flag = bundle.getString("flag");
            double receivable = bundle.getDouble("receivable");
            order_name.setText(order.getSupplier_name() + " (总欠款：¥" + receivable + ")");
            SupplierBean bean1 = new SupplierBean();
            bean1.setName(order.getSupplier_name());
            bean1.setDebt(receivable);
            bean1.setSupplier_id(order.getSupplier_id());
            bean = bean1;
            if (TextUtils.isEmpty(order.getTelephone())) {
                order_tel.setText("");
                order.setTelephone("");
            } else {
                order_tel.setText(order.getTelephone());
            }
            remarks_textview.setText(order.getRemark());
        }
        searchGoods();
    }

    private void searchGoods() {//获取商品列表用来比较
        ProgressUtil.showDialog(this);
        orderEasyPresenter.getGoodsList();
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.btn_commit)
    Button btn_commit;
    @InjectView(R.id.order_name)
    TextView order_name;
    @InjectView(R.id.order_tel)
    TextView order_tel;

    //扫一扫
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;

    @InjectView(R.id.et_search)
    EditText et_search;

    @InjectView(R.id.kaidan_order_num)
    TextView kaidan_order_num;
    @InjectView(R.id.kaidan_money)
    TextView kaidan_money;

    @InjectView(R.id.remarks_textview)
    TextView remarks_textview;

    @InjectView(R.id.remark_layout)
    LinearLayout remark_layout;
    @InjectView(R.id.list_view)
    ExpandableListView goods_listview;


    @OnClick(R.id.remark_layout)
    void remark_layout() {//备注
        Intent intent = new Intent(BillingPurchaseActivity.this, RemarksActivity.class);
        intent.putExtra("content", order.getRemark());
        intent.putExtra("type", 0);
        startActivityForResult(intent, 1005);
    }

    @OnClick(R.id.name_layout)
    void name_layout() {//选择供应商
        Intent intent = new Intent(BillingPurchaseActivity.this, SupplierManagementActivity.class);
        intent.putExtra("type", "bill");
        startActivityForResult(intent, 1006);
    }


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
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


    @OnClick(R.id.et_search)
    void et_search() {

        Intent intent = new Intent(BillingPurchaseActivity.this, SearchGoodsActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1002);
    }


    @OnClick(R.id.btn_commit)
    void commit() {
        double price = 0;
        int num = 0;
        List<Goods> gList = new ArrayList<>();
        for (Goods good : adapter.getData()) {
            price += good.getPrice();
            num += good.getNum();
            if (good.getNum() != 0) {
                gList.add(good);
                Log.e("Billing", "good:" + good.getProduct_list().get(0).getSell_price());
            }
        }
        if (bean == null) {
            showToast("请选择供应商！");
            return;
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
        order.setCustomer_name(bean.getName());
        if (num < 1) {
            showToast("请选择商品数量！");
            return;
        }
        //orderEasyPresenter.Add_Odder(order);
        Intent intent = new Intent(BillingPurchaseActivity.this, PurchaseConfirmActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "order");
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1002) {
            //获取选择的商品list
            Bundle bundle = data.getExtras();
            Goods good = (Goods) bundle.getSerializable("data");
            goods = order.getGoods_list();
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
            adapter.setData(goods);
            adapter.notifyDataSetChanged();
            kaidan_order_num.setText("共" + adapter.getData().size() + "种货品 (总数量：" + number + ")");
            //initAdapterLister();
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
                    goods = order.getGoods_list();
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
                    adapter.setData(goods);
                    adapter.notifyDataSetChanged();
                    kaidan_order_num.setText("共" + adapter.getData().size() + "种货品 (总数量：" + number + ")");
                } else {
                    Intent intent = new Intent(this, SearchGoodsActivity.class);
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
            if (bundle != null) {
                bean = (SupplierBean) bundle.getSerializable("data");
                this.order.setCustomer_id(bean.getSupplier_id());
                this.order.setCustomer_name(bean.getName());
                order_name.setText(bean.getName());
                order_tel.setVisibility(View.VISIBLE);
                order_tel.setText("欠供应商款：" + bean.getDebt());
            }
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
        if (data != null) {
            int status = data.get("code").getAsInt();
            //String message=result.get("message").getAsString();
            if (status == 1) {
                List<Goods> list = new ArrayList<>();
                JsonArray jsonArray = data.get("result").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    Goods good = (Goods) GsonUtils.getEntity(jsonArray.get(i).toString(), Goods.class);
                    list.add(good);
                }
                DataStorageUtils.getInstance().setShelvesGoods(list);
                if (flag.equals("details")) {
                    screenData(list);
                }
            }
        }
    }

    private void screenData(List<Goods> data) {//订单详情界面跳转过来需要记录以前选中的货品个数价格这里用来对比添加
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
        kaidan_money.setText(String.valueOf(tPrice));
        kaidan_order_num.setText("共" + data2.size() + "种货品 (总数量：" + tNumber + ")");
        order.setGoods_list(data2);
        adapter.setData(order.getGoods_list());
        goods_listview.expandGroup(0);
    }
}
