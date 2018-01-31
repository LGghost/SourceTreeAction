package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderDetailAdapter;
import cn.order.ordereasy.bean.ArrearsBean;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CustomExpandableListView;

public class ProcurementDetailsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OrderEasyView {
    private ArrearsBean bean;
    private SupplierBean bean1;
    private OrderEasyPresenter orderEasyPresenter;
    private Order order;
    private OrderDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.procurement_details_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (ArrearsBean) bundle.getSerializable("data");
            bean1 = (SupplierBean) bundle.getSerializable("bean");
        }
        initRefreshLayout();
        orderno_list_view.setGroupIndicator(null);
        orderno_list_view.setFocusable(false);
    }


    private void initRefreshLayout() {
        store_refresh.setOnRefreshListener(this);
        order_num.setText(bean.getDelete_time());
        order_name.setText(bean.getCustomer_name());
        money_number.setText(bean1.getArrears() + "");
        kaidanren_name.setText(bean.getUser_name());
        data_time.setText(bean.getCreate_time());
        if (bean.getType() == 2) {
            bottom_layout.setVisibility(View.GONE);
            youhui_layout.setVisibility(View.GONE);
            jine_text.setText("本次应退：¥");
            jine_text.setTextColor(getResources().getColor(R.color.touzi_huise));
            order_money_num.setTextColor(getResources().getColor(R.color.touzi_huise));
            huopin_leixing.setText("总采购退货输出：");
            type_image.setImageResource(R.drawable.img_tuidan_sign);
            orderEasyPresenter.getOrderInfo(1331);
            yaohuo_num.setText("11");
        } else {
            bottom_layout.setVisibility(View.VISIBLE);
            youhui_layout.setVisibility(View.VISIBLE);
            youhui_money_num.setText("10.00");
            jine_text.setTextColor(getResources().getColor(R.color.heise));
            order_money_num.setTextColor(getResources().getColor(R.color.heise));
            type_image.setImageResource(R.drawable.img_dingdan);
            orderEasyPresenter.getOrderInfo(1321);
            yaohuo_num.setText("23");
        }
        order_money_num.setText(bean.getMoney() + "");

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
        intent.putExtra("data", bean1);
        startActivity(intent);
    }

    //去发货
    @OnClick(R.id.fahuo)
    void fahuo_click() {
        ToastUtil.show("货品已全部入库");
    }

    //退欠货
    @OnClick(R.id.tuiqianhuo)
    void tuiqianhuo() {
        Intent intent = new Intent(this, ReturnGoodsActivity.class);
        Bundle bundle = new Bundle();
        Customer customer = new Customer();
        customer.setName(bean1.getName());
        customer.setCustomer_id(-1);
        List<String> addre = new ArrayList<>();
        addre.add(bean1.getAddress());
        customer.setAddress(addre);
        bundle.putSerializable("data", customer);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //再来一单
    @OnClick(R.id.close_orderno)
    void close_orderno() {

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
        store_refresh.setRefreshing(false);
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
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        if (data != null) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //处理返回的数据
                Log.e("OrderNoDetails", "" + data.toString());
                order = (Order) GsonUtils.getEntity(data.get("result").getAsJsonObject().toString(), Order.class);
                if (order != null) {
                    if (order.getOrder_id() != -1) {
                        order.setOriginal_order_id(order.getOrder_id());
                    }
                    adapter = new OrderDetailAdapter(order.getGoods_list(), this, order.getOrder_type(), true);
                    orderno_list_view.setAdapter(adapter);
                } else {
                    showToast("数据异常");
                }
            } else {
                String message = data.get("message").getAsString();
                showToast(message);
                if (status == -7) {

                }
            }
        }
    }
}