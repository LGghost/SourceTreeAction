package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.Window;
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
import cn.order.ordereasy.adapter.OrderDetailAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CustomExpandableListView;

public class ProcurementDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OrderEasyView {
    private OrderEasyPresenter orderEasyPresenter;
    private Order order;
    private OrderDetailAdapter adapter;
    private String order_no;
    private SupplierBean supplierBean;
    private AlertDialog alertDialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procurement_details_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            id = bundle.getInt("id");
            order_no = bundle.getString("order_no");
            Log.e("JJF", "id" + id);
            orderEasyPresenter.supplierOrderInfo(id, order_no);
        }
        orderno_list_view.setGroupIndicator(null);
        orderno_list_view.setFocusable(false);
    }


    private void initRefreshLayout() {
        store_refresh.setOnRefreshListener(this);
        int owe_num = 0, oper_num = 0;
        for (Goods good : order.getGoods_list()) {
            for (Product product : good.getProduct_list()) {
                owe_num += product.getOwe_num();
                oper_num += product.getOperate_num();
            }
        }
        order_num.setText(order.getOrder_no());
        order_name.setText(supplierBean.getName());
        money_number.setText(supplierBean.getDebt() + "");
        kaidanren_name.setText(order.getUser_name());
        data_time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(order.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
        if (order.getOrder_type() == 2) {
            bottom_layout.setVisibility(View.GONE);
            youhui_layout.setVisibility(View.GONE);
            jine_text.setText("本次应退：¥");
            jine_text.setTextColor(getResources().getColor(R.color.touzi_huise));
            order_money_num.setTextColor(getResources().getColor(R.color.touzi_huise));
            huopin_leixing.setText("总采购退货数：");
            type_image.setImageResource(R.drawable.img_tuidan_sign);
            yaohuo_num.setText(oper_num + "");
        } else {
            bottom_layout.setVisibility(View.VISIBLE);
            youhui_layout.setVisibility(View.VISIBLE);
            youhui_money_num.setText("10.00");
            jine_text.setTextColor(getResources().getColor(R.color.heise));
            order_money_num.setTextColor(getResources().getColor(R.color.heise));
            type_image.setImageResource(R.drawable.img_dingdan);
            yaohuo_num.setText(oper_num + "");
        }
        order_money_num.setText(order.getPayable() + "");

        int status = order.getIs_close();
        if (status == 1) {
            qianhuo_layout.setVisibility(View.GONE);
            yiguanbi.setVisibility(View.VISIBLE);
            goto_shoukuan.setVisibility(View.GONE);
        }
        if (order.getIs_close() == 1 || owe_num != oper_num) {
            guanbi_image.setImageResource(R.drawable.icon_zailaiyidan);
            close_text.setText("再来一单");
            close_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProcurementDetailsActivity.this, BillingPurchaseActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putDouble("receivable", supplierBean.getDebt());
                    bundle.putSerializable("Order", order);
                    bundle.putString("flag", "details");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            guanbi_image.setImageResource(R.drawable.icon_guanbi);
            close_text.setText("关闭订单");
            close_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialogs();
                }
            });

        }
    }


    //下拉刷新控件
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.order_num)
    TextView order_num;
    @InjectView(R.id.order_name)
    TextView order_name;
    @InjectView(R.id.money_number)
    TextView money_number;
    @InjectView(R.id.yiguanbi)
    ImageView yiguanbi;
    //订单状态图片
    @InjectView(R.id.type_image)
    ImageView type_image;
    @InjectView(R.id.kaidanren_name)
    TextView kaidanren_name;
    @InjectView(R.id.data_time)
    TextView data_time;
    @InjectView(R.id.jine_text)
    TextView jine_text;
    @InjectView(R.id.order_money_num)
    TextView order_money_num;
    @InjectView(R.id.huopin_leixing)
    TextView huopin_leixing;
    @InjectView(R.id.yaohuo_num)
    TextView yaohuo_num;
    @InjectView(R.id.tv_zhankai)
    TextView tv_zhankai;
    @InjectView(R.id.order_remarks)
    TextView order_remarks;
    @InjectView(R.id.close_text)
    TextView close_text;
    @InjectView(R.id.goto_shoukuan)
    TextView goto_shoukuan;
    @InjectView(R.id.guanbi_image)
    ImageView guanbi_image;
    @InjectView(R.id.qianhuo_layout)
    LinearLayout qianhuo_layout;
    @InjectView(R.id.youhui_money_num)
    TextView youhui_money_num;

    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;
    @InjectView(R.id.youhui_layout)
    LinearLayout youhui_layout;
    @InjectView(R.id.orderno_list_view)
    CustomExpandableListView orderno_list_view;
    boolean isExpand = false;

    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    //点击去收款
    @OnClick(R.id.goto_shoukuan)
    void goto_shoukuan() {
        Intent intent = new Intent(this, SupplierPaymentActivity.class);
        intent.putExtra("data", supplierBean);
        startActivity(intent);
    }

    //去发货
    @OnClick(R.id.fahuo)
    void fahuo_click() {
        if (order == null) {
            return;
        }
        if (order.getIs_close() == 1) {
            ToastUtil.show("已关闭订单不能进行此操作");
            return;
        }
        int owe = 0;
        if (order != null && order.getGoods_list() != null) {
            for (Goods goods : order.getGoods_list()) {
                for (Product product : goods.getProduct_list()) {
                    owe += product.getOwe_num();
                }
            }
        }
        if (owe <= 0) {
            showToast("该客户的货已全部入库");
            return;
        }
        Intent intent = new Intent(ProcurementDetailsActivity.this, DeliverGoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "procurement");
        bundle.putSerializable("data", order);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    //退货
    @OnClick(R.id.tuiqianhuo)
    void tuiqianhuo() {
        Intent intent = new Intent(this, ReturnGoodsActivity.class);
        Bundle bundle = new Bundle();
        Customer customer = new Customer();
        customer.setName(supplierBean.getName());
        customer.setCustomer_id(supplierBean.getSupplier_id());
        List<String> addre = new ArrayList<>();
        addre.add(supplierBean.getAddress());
        customer.setAddress(addre);
        bundle.putSerializable("data", customer);
        bundle.putString("flag", "procurement");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @OnClick(R.id.order_remarks_layout)
//备注
    void order_remarks_layout() {
    }

    @OnClick(R.id.zhankai_view)
    void zhankai() {
        if (!isExpand) {
            int groupCount = orderno_list_view.getCount();
            if (groupCount < 1) return;
            for (int i = 0; i < groupCount; i++) {
                isExpand = true;
                orderno_list_view.expandGroup(i);
                tv_zhankai.setText("统一收起");
            }
        } else {
            int groupCount = orderno_list_view.getCount();
            if (groupCount < 1) return;
            for (int i = 0; i < groupCount; i++) {
                isExpand = false;
                orderno_list_view.collapseGroup(i);
                tv_zhankai.setText("统一展开");
            }
        }


    }


    @Override
    public void onRefresh() {
        orderEasyPresenter.supplierOrderInfo(id, order_no);
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            DataStorageUtils.getInstance().setPurchaseBilling(true);
            orderEasyPresenter.supplierOrderInfo(id, order_no);
        }

    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        if (type == 0) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    JsonArray jsonArray = data.getAsJsonObject("result").getAsJsonArray("list");
                    List<SupplierBean> lsit = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        SupplierBean supplierBean = (SupplierBean) GsonUtils.getEntity(jsonArray.get(i).toString(), SupplierBean.class);
                        lsit.add(supplierBean);
                    }
                    for (SupplierBean bean : lsit) {
                        if (bean.getSupplier_id() == order.getSupplier_id()) {
                            supplierBean = bean;
                            break;
                        }
                    }
                    initRefreshLayout();
                }
            }

        } else if (type == 1) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //处理返回的数据
                    Log.e("ProcurementDetail", "" + data.toString());
                    order = (Order) GsonUtils.getEntity(data.get("result").getAsJsonObject().toString(), Order.class);
                    if (order != null) {
                        if (order.getOrder_id() != -1) {
                            order.setOriginal_order_id(order.getOrder_id());
                        }
                        adapter = new OrderDetailAdapter(order.getGoods_list(), this, order.getOrder_type(), true);
                        orderno_list_view.setAdapter(adapter);
                        orderEasyPresenter.supplierIndex();
                    } else {
                        showToast("数据异常");
                    }
                }
            }
        } else if (type == 2) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                Log.e("ProcurementDetail", "" + data.toString());
                qianhuo_layout.setVisibility(View.GONE);
                yiguanbi.setVisibility(View.VISIBLE);
                goto_shoukuan.setVisibility(View.GONE);
            }
        }
    }

    private void showdialogs() {//跟据type判断是否是微信订单
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);

        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        title_name.setText("温馨提示");
        text_conten.setText("您确认要关闭此订单吗？");
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
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
                orderEasyPresenter.supplierOrderClose(order.getOrder_id());
                alertDialog.dismiss();
            }
        });
    }
}