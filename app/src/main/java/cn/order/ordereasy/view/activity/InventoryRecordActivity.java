package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.InventoryRecordAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Inventory;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * 盘点记录Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class InventoryRecordActivity extends BaseActivity implements OrderEasyView {


    private InventoryRecordAdapter adapter;

    private OrderEasyPresenter orderEasyPresenter;
    private List<Goods> goodsList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inventory_record);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        adapter = new InventoryRecordAdapter(goodsList, this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Inventory id = (Inventory) bundle.getSerializable("data");
            pandian_name.setText(id.getUser_name());
            orderEasyPresenter.getInventoryInfo(id.getInventory_id(), id.getUser_id());
        }

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    //盘点人姓名
    @InjectView(R.id.pandian_name)
    TextView pandian_name;
    //货品数
    @InjectView(R.id.huopin_num)
    TextView huopin_num;
    //盘点数
    @InjectView(R.id.pandian_num)
    TextView pandian_num;

    //ListView
    @InjectView(R.id.pandian_jilu_listview)
    ExpandableListView pandian_jilu_listview;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        InventoryRecordActivity.this.finish();
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

                            //获取商品列表
                            JsonArray array = result.getAsJsonObject("result").get("list").getAsJsonArray();
                            List<Goods> goods = new ArrayList<>();
                            for (int i = 0; i < array.size(); i++) {
                                Goods good = (Goods) GsonUtils.getEntity(array.get(i).toString(), Goods.class);
                                goods.add(good);
                            }
                            goodsList = goods;
                            //获取信息
                            int num = 0;
                            for (Goods good : goodsList) {
                                for (Product product : good.getProduct_list()) {
                                    num += product.getOperate_num();
                                }
                            }
                            pandian_num.setText(String.valueOf(num));
                            huopin_num.setText(String.valueOf(goods.size()));
                            adapter.setGoods(goods);
                            pandian_jilu_listview.setAdapter(adapter);
                            pandian_jilu_listview.setGroupIndicator(null);
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("盘点信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

                        }
                    }
                    Log.e("新建信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

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
}
