package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.InventoryResultsAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Inventory;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CustomExpandableListView;

/**
 * Created by Administrator on 2017/9/3.
 * <p>
 * 盘点结果
 */

public class InventoryResultsActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {

    InventoryResultsAdapter adapter;
    AlertDialog alertDialog;
    int id = 0;
    OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inventory_results);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Inventory inventory = (Inventory) bundle.getSerializable("data");
            id = inventory.getInventory_id();
            if (inventory.getIs_complete() == 1) {
                bottom_layout.setVisibility(View.GONE);
            }
            refreshData(true);
        }
        refresh_layout.setOnRefreshListener(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;

    //本次实盘 货品数
    @InjectView(R.id.shipan_goods_num)
    TextView shipan_goods_num;

    //本次实盘 数量
    @InjectView(R.id.shipan_num)
    TextView shipan_num;

    //账面库存 货品数
    @InjectView(R.id.kucun_goods_num)
    TextView kucun_goods_num;

    //账面库存 数量
    @InjectView(R.id.kucun_num)
    TextView kucun_num;


    //盘亏 货品数
    @InjectView(R.id.pankui_goods_num)
    TextView pankui_goods_num;

    //盘亏 数量
    @InjectView(R.id.pankui_num)
    TextView pankui_num;

    //盘盈 货品数
    @InjectView(R.id.panying_goods_num)
    TextView panying_goods_num;

    //盘盈 数量
    @InjectView(R.id.panying_num)
    TextView panying_num;

    //未盘 数量
    @InjectView(R.id.weipan_num)
    TextView weipan_num;

    @InjectView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;

    //底部按钮1
    @InjectView(R.id.button_one)
    Button button_one;
    //底部按钮2
    @InjectView(R.id.button_two)
    Button button_two;

    @InjectView(R.id.pandian_jieguo_listview)
    CustomExpandableListView pandian_jieguo_listview;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        InventoryResultsActivity.this.finish();
    }


    //返回按钮
    @OnClick(R.id.button_one)
    void button_one() {
        showdialogs();
    }

    //返回按钮
    @OnClick(R.id.button_two)
    void button_two() {
        showdialogs2();
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
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
        refresh_layout.setRefreshing(false);
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


                            //获取总计
                            JsonObject total = result.getAsJsonObject("result").get("total").getAsJsonObject();

                            pankui_num.setText(total.get("dec_num").getAsString());//盘亏数
                            pankui_goods_num.setText(total.get("dec_num_goods").getAsString());//盘亏货品数

                            panying_num.setText(total.get("inc_num").getAsString());//盘盈数
                            panying_goods_num.setText(total.get("inc_num_goods").getAsString());//盘盈货品数

                            shipan_goods_num.setText(total.get("inventory_num").getAsString());//本次实盘 盘点货品数
                            shipan_num.setText(total.get("operate_num").getAsString());//本次实盘数

                            weipan_num.setText(total.get("remain_inventory_goods").getAsString());//未盘货品数

                            kucun_goods_num.setText(total.get("inventory_num").getAsString());//账面库存货品数
                            kucun_num.setText(total.get("store_num").getAsString());//账面库存数量
                            //获取商品列表
                            JsonArray array = result.getAsJsonObject("result").get("page_list").getAsJsonArray();
                            List<Goods> goods = new ArrayList<>();
                            for (int i = 0; i < array.size(); i++) {
                                Goods good = (Goods) GsonUtils.getEntity(array.get(i).toString(), Goods.class);
                                goods.add(good);
                            }
                            adapter = new InventoryResultsAdapter(goods, InventoryResultsActivity.this);
                            pandian_jieguo_listview.setAdapter(adapter);
                        }
                    }
                    Log.e("结束信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            ToastUtil.show("盘点结束");
                            setResult(1003);
                            finish();
                        }
                    }
                    Log.e("新建信息", result.toString());
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

    //弹出框
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
        text_conten.setText("直接结束盘点不会对库存做调整，您确定要结束盘点吗？");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
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
                if (adapter.getGoods().size() > 0) {
                    orderEasyPresenter.commitInventoryInfo(id, "提交盘点", 0);
                } else {
                    ToastUtil.show("请选择盘点货品");
                }
                alertDialog.dismiss();
            }
        });
    }

    //弹出框
    private void showdialogs2() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入变动备注");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
        //hint内容
        ed_type_name.setHint("最多输入8个字符");
        //限制输入长度
        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});

        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
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
                if (adapter.getGoods().size() > 0) {
                    orderEasyPresenter.commitInventoryInfo(id, "提交盘点", 1);
                } else {
                    ToastUtil.show("请选择盘点货品");
                }
                alertDialog.dismiss();
            }
        });

        //监听edittext
        ed_type_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    queren.setTextColor(getResources().getColor(R.color.lanse));
                } else {
                    queren.setTextColor(getResources().getColor(R.color.touzi_huise));
                    queren.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getInventoryInfo(id, -1, 1);
    }
}
