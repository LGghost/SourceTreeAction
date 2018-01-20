package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.InventoryResultsAdapter;
import cn.order.ordereasy.adapter.InventorySheetPeopleAdapter;
import cn.order.ordereasy.bean.Employee;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.Inventory;
import cn.order.ordereasy.bean.MyEmployee;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.UserInfo;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * 盘点单Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class InventorySheetActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {

    Inventory inventory;
    OrderEasyPresenter orderEasyPresenter;
    InventorySheetPeopleAdapter adapter;
    List<Map<String, Object>> datas = new ArrayList<>();
    private int user_id;
    private boolean isEnd = false;
    private int is_adjust;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_sheet);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            inventory = (Inventory) bundle.getSerializable("data");
            is_adjust = inventory.getIs_adjust();
            if (inventory.getCreate_time().equals("")) {
                pandian_data.setText("");
            } else {
                pandian_data.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(inventory.getCreate_time()), "yyyy-MM-dd"));
            }
            huopin_num.setText(String.valueOf(inventory.getStore_num()));
            pandian_faqiren.setText(inventory.getUser_name());
            if (inventory.getIs_complete() == 1) {
                pandian_jixu.setVisibility(View.GONE);
                colos_pandian.setVisibility(View.GONE);
                pandian_state_image.setImageResource(R.drawable.img_jieshu);
                pandian_state.setText("已结束");
            }
            if (is_adjust == 1) {
                see_pandian.setVisibility(View.VISIBLE);
            } else {
                see_pandian.setVisibility(View.GONE);
            }
            orderEasyPresenter.getUserInfo();
            refreshData(true);
        }

        adapter = new InventorySheetPeopleAdapter(this, datas);
        pandiandan_listview.setAdapter(adapter);
        pandiandan_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventorySheetActivity.this, InventoryRecordActivity.class);
                Bundle bundle = new Bundle();
                Inventory i = inventory;
                i.setUser_id(Integer.parseInt(datas.get(position).get("user_id").toString()));
                i.setUser_name(datas.get(position).get("user_name").toString());
                bundle.putSerializable("data", inventory);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //结束盘点
    @InjectView(R.id.colos_pandian)
    TextView colos_pandian;

    //盘点日期
    @InjectView(R.id.pandian_data)
    TextView pandian_data;

    //盘点状态图片
    @InjectView(R.id.pandian_state_image)
    ImageView pandian_state_image;

    //盘点状态
    @InjectView(R.id.pandian_state)
    TextView pandian_state;

    //盘点货品数
    @InjectView(R.id.huopin_num)
    TextView huopin_num;

    //盘点数
    @InjectView(R.id.pandian_num)
    TextView pandian_num;

    //点击查看盘点详情
    @InjectView(R.id.pandian_click)
    LinearLayout pandian_click;

    //盘点人数
    @InjectView(R.id.see_pandian)
    TextView see_pandian;

    //盘点人数
    @InjectView(R.id.pandian_renshu)
    TextView pandian_renshu;

    //盘点发起人
    @InjectView(R.id.pandian_faqiren)
    TextView pandian_faqiren;

    //继续盘点按钮
    @InjectView(R.id.pandian_jixu)
    Button pandian_jixu;

    //ListView
    @InjectView(R.id.pandiandan_listview)
    ListView pandiandan_listview;

    //点击查看盘点记录
    @InjectView(R.id.lay_click)
    LinearLayout lay_click;

    //点击查看盘点记录
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        Intent intent = new Intent();
        intent.putExtra("isEnd", isEnd);
        setResult(1001, intent);
        finish();
    }

    //查看按钮
    @OnClick(R.id.pandian_click)
    void pandian_click() {
        Intent intent = new Intent(InventorySheetActivity.this, InventoryResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", inventory);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }

    //查看盘点记录
    @OnClick(R.id.lay_click)
    void lay_click() {

    }

    @OnClick(R.id.pandian_jixu)
    void jixu() {
        Intent intent = new Intent(InventorySheetActivity.this, InventorySearchActivity.class);
        Bundle bundle = new Bundle();
        inventory.setUser_id(user_id);
        bundle.putSerializable("data", inventory);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1002);
    }

    //结束盘点按钮
    @OnClick(R.id.colos_pandian)
    void colos_pandian() {
        Intent intent = new Intent(InventorySheetActivity.this, InventoryResultsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", inventory);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }

    @OnClick(R.id.see_pandian)
    void see_pandian() {
        Intent intent = new Intent(InventorySheetActivity.this, AdjustingRecordActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("id", inventory.getOperate_id());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1002) {
            refreshData(false);
        }
        if (resultCode == 1003) {
            isEnd = true;
            inventory.setIs_complete(1);
            pandian_jixu.setVisibility(View.GONE);
            colos_pandian.setVisibility(View.GONE);
            pandian_state_image.setImageResource(R.drawable.img_jieshu);
            pandian_state.setText("已结束");
//            clearData();
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
        store_refresh.setRefreshing(false);
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
                            List<Map<String, Object>> data = new ArrayList<>();
                            JsonArray array = result.getAsJsonObject("result").get("list").getAsJsonArray();
                            int num = 0;
                            for (int i = 0; i < array.size(); i++) {
                                Map<String, Object> map = new HashMap<>();
                                map.put("avatar", array.get(i).getAsJsonObject().get("avatar").getAsString());
                                map.put("goods_num", array.get(i).getAsJsonObject().get("goods_num").getAsString());
                                map.put("is_boss", array.get(i).getAsJsonObject().get("is_boss").getAsString());
                                map.put("user_id", array.get(i).getAsJsonObject().get("user_id").getAsString());
                                map.put("user_name", array.get(i).getAsJsonObject().get("user_name").getAsString());
                                map.put("operate_num", array.get(i).getAsJsonObject().get("operate_num").getAsString());
                                num += array.get(i).getAsJsonObject().get("operate_num").getAsInt();
                                data.add(map);
                            }
                            datas = data;
                            adapter.setData(datas);
                            adapter.notifyDataSetChanged();
                            pandian_renshu.setText(String.valueOf(data.size()));
                            pandian_num.setText(String.valueOf(num));
                            JsonObject object = result.getAsJsonObject("result").get("info").getAsJsonObject();
                            pandian_data.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(object.get("create_time").getAsString()), "yyyy-MM-dd"));
                            huopin_num.setText(String.valueOf(object.get("goods_num").getAsString()));
                            pandian_faqiren.setText(object.get("user_name").getAsString());
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("结束信息", result.toString());
                    break;
                case 1002:
                    JsonObject result1 = (JsonObject) msg.obj;
                    if (result1 != null) {
                        if (result1.get("code").getAsInt() == 1) {
                            //获取用户登录信息成功
                            JsonObject jo = result1.getAsJsonObject("result").getAsJsonObject("user_info");
                            Gson gson = new Gson();
                            UserInfo userinfo = gson.fromJson(jo.toString(), UserInfo.class);
                            user_id = userinfo.user_id;
                        }
                    }
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

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getInventory(inventory.getInventory_id());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.putExtra("isEnd", isEnd);
            setResult(1001, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void clearData() {
        List<Goods> saveGoods = DataStorageUtils.getInstance().getShelvesGoods();
        for (Goods good : saveGoods) {
            List<Product> productData = good.getProduct_list();
            for (Product product : productData) {
                if (product.getOperate_num() > 0) {
                    product.setOperate_num(0);
                }
            }
        }
    }
}
