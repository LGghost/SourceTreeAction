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
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;

public class BillingPurchaseActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.billing_purchase_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
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
        ButterKnife.inject(this);

        adapter = new billingPurchaseAdapter(this);
        goods_listview.setFocusable(false);
        adapter.setOnMoneyItemClickListener(new billingPurchaseAdapter.MoneyClickLister() {
            @Override
            public void changeData(double price, int num) {
                number = num;
                Log.e("JJF", "price:" + FileUtils.getMathNumber(price));
                kaidan_order_money.setText(FileUtils.getMathNumber(price));
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
        }
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
    @InjectView(R.id.kaidan_order_money)
    TextView kaidan_order_money;

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
        } else if (resultCode == 1002) {
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

                order_name.setText(bean.getName());
                order_tel.setVisibility(View.VISIBLE);
                order_tel.setText("欠供应商款：" + bean.getDebt());
            }
        }

    }
}
