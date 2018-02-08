package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.TransitionRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.InventorySearchAdapter;
import cn.order.ordereasy.adapter.StockAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Inventory;
import cn.order.ordereasy.bean.InventoryInfo;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CustomExpandableListView;

/**
 * 盘点搜索Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class InventorySearchActivity extends BaseActivity implements OrderEasyView {

    List<Goods> goods = new ArrayList<>();
    StockAdapter adapter;
    OrderEasyPresenter orderEasyPresenter;
    List<Goods> saveGoods = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inventory_search);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new StockAdapter(this, 3);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        pandian_listview.setAdapter(adapter);
        Bundle bundle = getIntent().getExtras();
        saveGoods = DataStorageUtils.getInstance().getShelvesGoods();
        if (bundle != null) {
            Inventory inventory = (Inventory) bundle.getSerializable("data");
            Log.e("InventorySearchActivity", "inventory_id:" + inventory.getInventory_id() + "used_id:" + inventory.getUser_id());
            orderEasyPresenter.getInventoryInfo(inventory.getInventory_id(), inventory.getUser_id());
        }
        adapter.setOnItemClickListener(new StockAdapter.MyItemClickListener() {
            @Override
            public void changeData(int number, int typeBumber) {
                kaidan_order_num.setText(String.valueOf(number));
                kaidan_order_money.setText(String.valueOf(typeBumber));
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.et_search)
    EditText et_search;

    @InjectView(R.id.btn_commit)
    Button btn_commit;

    @InjectView(R.id.kaidan_order_num)
    TextView kaidan_order_num;

    @InjectView(R.id.kaidan_order_money)
    TextView kaidan_order_money;

    @InjectView(R.id.pandian_listview)
    ListView pandian_listview;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        InventorySearchActivity.this.finish();
    }

    @OnClick(R.id.inventory_saomiao)
    void inventory_saomiao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        }else{
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    @OnClick(R.id.et_search)
    void search() {
        Intent intent = new Intent(InventorySearchActivity.this, SearchGoodsActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "inventory");
        Order order = new Order();
        order.setGoods_list(goods);
        bundle.putSerializable("data", order);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, 1002);
    }

    @OnClick(R.id.btn_commit)
    void save() {
        InventoryInfo inventoryInfo = new InventoryInfo();
        orderEasyPresenter.saveInventoryInfo(inventoryInfo.goodToMap(goods));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫一扫结果
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            if (TextUtils.isEmpty(res)) {
                showToast("未扫描到任何结果");
            } else {
                if (DataStorageUtils.getInstance().getShelvesGoods().size() > 0) {
                    boolean isExist = false;
                    List<Goods> goodsList = DataStorageUtils.getInstance().getShelvesGoods();
                    Goods good = new Goods();
                    for (Goods good1 : goodsList) {
                        if (good1.getGoods_no().equals(res)) {
                            good = good1;
                            isExist = true;
                            break;
                        }
                    }
                    if (!isExist) {
                        ToastUtil.show("没有找到该货品");
                        return;
                    }
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
                    adapter.setData(goods);
                    adapter.notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(this, SearchGoodsActivity.class);
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("flag", "inventory");
                    Order order = new Order();
                    order.setGoods_list(goods);
                    bundle1.putSerializable("data", order);
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 1002);
                }
            }
            MyLog.e("扫一扫返回数据", res);
        }
        if (resultCode == 1002) {
            //获取选择的商品list
            Bundle bundle = data.getExtras();
            Goods good = (Goods) bundle.getSerializable("data");
            if (goods == null) goods = new ArrayList<>();
            boolean flag = false;
            for (int i = 0; i < goods.size(); i++) {
                if (good.getGoods_id() == goods.get(i).getGoods_id()) {
                    flag = true;
                }
            }
            if (!flag) {
                goods.add(0,good);
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
            adapter.setData(goods);
            adapter.notifyDataSetChanged();
        }
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
        Log.e("InventorySearchActivity", "loadData3:" + type);
        if (type == 0) {

            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //获取商品列表
                    JsonArray array = data.getAsJsonObject("result").get("list").getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {
                        Goods good = (Goods) GsonUtils.getEntity(array.get(i).toString(), Goods.class);
//                        for (Goods goodData : saveGoods) {
//                            if (good.getGoods_no().equals(goodData.getGoods_no())) {
//                                List<Product> productData = goodData.getProduct_list();
//                                List<Product> goodProduct = good.getProduct_list();
//                                for (int j = 0; j < goodProduct.size(); j++) {
//                                    for (int m = 0; m < productData.size(); m++) {
//                                        if (goodProduct.get(j).getProduct_id() == productData.get(m).getProduct_id()) {
//                                            productData.get(m).setOperate_num(goodProduct.get(j).getOperate_num());
//                                        }
//                                    }
//                                }
//                                goods.add(goodData);
//                            }
//                        }
                        goods.add(good);
                    }
                    adapter.setData(goods);
                }
            }
            Log.e("盘点信息", data.toString());
        }
        if (type == 1) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    ToastUtil.show("盘点成功");
//                    clearData();
                    setResult(1002);
                    finish();
                }
            }
            Log.e("保存信息", data.toString());
        }
    }

//    private void clearData() {
//        for (Goods good : saveGoods) {
//            List<Product> productData = good.getProduct_list();
//            for (Product product : productData) {
//                if (product.getOperate_num() > 0) {
//                    product.setOperate_num(0);
//                }
//            }
//        }
//    }

}
