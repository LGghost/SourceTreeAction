package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.StockAdapter;
import cn.order.ordereasy.adapter.StorageInAdapter;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CustomExpandableListView;

/**
 * Created by Administrator on 2017/9/6.
 */

public class TiaoZhengAityity extends BaseActivity implements OrderEasyView {

    StockAdapter adapter;
    OrderEasyPresenter orderEasyPresenter;
    List<Goods> goods = new ArrayList<>();

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.tiao_zheng);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Goods good = (Goods) bundle.getSerializable("data");
            goods.add(good);
        }
        adapter = new StockAdapter(this, 2);
        adapter.setData(goods);
        pandian_listview.setAdapter(adapter);

        adapter.setOnItemClickListener(new StockAdapter.MyItemClickListener() {
            @Override
            public void changeData(int number, int typeBumber) {
                kucun_num.setText(String.valueOf(number));
                kucun_type.setText(String.valueOf(typeBumber));
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //返回按钮
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;

    @InjectView(R.id.pandian_listview)
    ListView pandian_listview;

    @InjectView(R.id.kucun_num)
    TextView kucun_num;
    @InjectView(R.id.kucun_type)
    TextView kucun_type;
    //et_search
    @InjectView(R.id.et_search)
    EditText et_search;

    //et_search
    @InjectView(R.id.queren_chuku)
    Button queren_chuku;


    @OnClick(R.id.et_search)
    void search() {
        Intent intent = new Intent(TiaoZhengAityity.this, SearchGoodsActivity.class);
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

    @OnClick(R.id.queren_chuku)
    void save() {
        showdialogs();
    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("库存变动操作");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
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
                SharedPreferences spPreferences = getSharedPreferences("user", 0);
                String userStr = spPreferences.getString("userinfo", "");
                JsonObject user = (JsonObject) GsonUtils.getObj(userStr, JsonObject.class);
                Delivery delivery = new Delivery();
                delivery.setRemark(ed_type_name.getText().toString());
                delivery.setOperate_id(user.get("user_id").getAsInt());
                delivery.setOperate_type(Config.Operate_TYPE_TIAOZHENG);
                List<Map<String, Object>> maps = new ArrayList<>();
                for (Goods good : goods) {
                    for (Product product : good.getProduct_list()) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("operate_num", product.getNum());
                        map.put("product_id", product.getProduct_id());
                        maps.add(map);
                    }
                }
                delivery.setCustomer_id(0);
                delivery.setOrder_id(0);
                delivery.setProduct_list(maps);
                List<Delivery> deliveries = new ArrayList<>();
                deliveries.add(delivery);
                orderEasyPresenter.delivers(deliveries);
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
                    queren.setEnabled(true);
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

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        TiaoZhengAityity.this.finish();
    }


    //扫一扫点击事件
    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(TiaoZhengAityity.this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
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
                            //成功

                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            showToast("调整成功！");
                            setResult(1001);
                            finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //扫一扫结果
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            Log.e("jjf", "res:" + res);
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
            //initAdapterLister();
        }
    }
}
