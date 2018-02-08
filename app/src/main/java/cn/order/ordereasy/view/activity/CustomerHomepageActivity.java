package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.bingoogolapple.refreshlayout.BGARefreshViewHolder;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.CustomerMoneyListAdapter;
import cn.order.ordereasy.adapter.CustomerOrderListAdapter;
import cn.order.ordereasy.adapter.CustomerThingsListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Fahuo;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.PinyinUtil;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.GuideDialog;
import cn.order.ordereasy.widget.LoadMoreListView;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/9/18.
 * <p>
 * 客户主页
 */

public class CustomerHomepageActivity extends BaseActivity implements OrderEasyView, EasyPermissions.PermissionCallbacks, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    Customer customer;
    CustomerOrderListAdapter customerOrderListAdapter;
    CustomerMoneyListAdapter customerMoneyListAdapter;
    CustomerThingsListAdapter customerThingsListAdapter;
    List<OrderList> orders = new ArrayList<>();
    List<Money> moneys = new ArrayList<>();
    List<Fahuo> fahuos = new ArrayList<>();

    OrderEasyPresenter orderEasyPresenter;
    private int customer_id;
    //当前页数
    private int orderCurrentPage = 1, orderPageTotal = 1;
    private int moneyCurrentPage = 1, moneyPageTotal = 1;
    private int fahuoCurrentPage = 1, fahuoPageTotal = 1;
    private boolean isFrist = true;
    private boolean isChange = false;
    private JsonArray arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.customer_homepage);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initRefreshLayout();
        DataStorageUtils.getInstance().cleanCustomerHomePage();
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Customer c = (Customer) bundle.getSerializable("data");
            if (c != null) {
                customer_id = c.getCustomer_id();
                orderEasyPresenter.getCustomerList1();
            }
        }
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String userinfo = spPreferences.getString("userinfo", "");
        if (!TextUtils.isEmpty(userinfo)) {
            JsonObject user = (JsonObject) GsonUtils.getObj(userinfo, JsonObject.class);
            arr = user.getAsJsonArray("auth_group_ids");
        }
        customerMoneyListAdapter = new CustomerMoneyListAdapter(this);
        customerOrderListAdapter = new CustomerOrderListAdapter(this);
        customerThingsListAdapter = new CustomerThingsListAdapter(this);
        khzy_lsitview.setAdapter(customerOrderListAdapter);
        khzy_lsitview.setEmptyView(no_data_image);

        khzy_lsitview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> khzy_lsitview, View view, int position, long id) {
                switch (dataType) {
                    case 0:
                        isChange = true;
                        Intent intent2 = new Intent(CustomerHomepageActivity.this, OrderNoDetailsActivity.class);
                        int order_id = customerOrderListAdapter.getData().get(position).getOrder_id();
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", order_id);
                        intent2.putExtras(bundle);
                        startActivity(intent2);
                        break;
                    case 1:
                        Intent intent3 = new Intent(CustomerHomepageActivity.this, CashierDetailsActivity.class);
                        bundle = new Bundle();
                        bundle.putSerializable("data", customerMoneyListAdapter.getData().get(position));
                        bundle.putString("tel", customer.getTelephone());
                        intent3.putExtras(bundle);
                        startActivity(intent3);
                        break;
                    case 2:
                        Intent intent4 = new Intent(CustomerHomepageActivity.this, OperationRecordActivity.class);
                        bundle = new Bundle();
                        bundle.putInt("id", customerThingsListAdapter.getData().get(position).getOperate_id());
                        intent4.putExtras(bundle);
                        startActivity(intent4);
                        break;
                }
            }
        });
        //新手引导
        new GuideDialog(7, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFrist) {
            isFrist = false;
            ProgressUtil.showDialog(this);
        }
        if (DataStorageUtils.getInstance().getChOrderLists().size() > 0) {
            orders = DataStorageUtils.getInstance().getChOrderLists();
            orderPageTotal = DataStorageUtils.getInstance().getOrderPageTotal();
            customerOrderListAdapter.setData(orders);
            customerOrderListAdapter.notifyDataSetChanged();
            if (dataType == 0) {
                if (customerOrderListAdapter.getData().size() > 0) {
                    no_data_image.setVisibility(View.GONE);
                } else {
                    no_data_image.setVisibility(View.VISIBLE);
                }
            }
        } else {
            orderEasyPresenter.getRecordList(customer_id, "1");
        }
        if (DataStorageUtils.getInstance().getChMoneyLists().size() > 0) {
            moneys = DataStorageUtils.getInstance().getChMoneyLists();
            moneyPageTotal = DataStorageUtils.getInstance().getMoneyPageTotal();
            customerMoneyListAdapter.setData(moneys);
            customerMoneyListAdapter.notifyDataSetChanged();
            if (dataType == 1) {
                if (customerMoneyListAdapter.getData().size() > 0) {
                    no_data_image.setVisibility(View.GONE);
                } else {
                    no_data_image.setVisibility(View.VISIBLE);
                }
            }
        } else {
            orderEasyPresenter.getOrderRecordLlist(customer_id, "1");

        }
        if (DataStorageUtils.getInstance().getChFahuoLists().size() > 0) {
            fahuos = DataStorageUtils.getInstance().getChFahuoLists();
            fahuoPageTotal = DataStorageUtils.getInstance().getFahuoPageTotal();
            customerThingsListAdapter.setData(fahuos);
            customerThingsListAdapter.notifyDataSetChanged();

            if (dataType == 2) {
                if (customerThingsListAdapter.getData().size() > 0) {
                    no_data_image.setVisibility(View.GONE);
                } else {
                    no_data_image.setVisibility(View.VISIBLE);
                }
            }
        } else {
            if (!isSalesperson()) {
                orderEasyPresenter.getOperationRecordList(customer_id, "1");
            }
        }
        Log.e("CustomerHomepage", "isBilling():" + DataStorageUtils.getInstance().isBilling());
        if (DataStorageUtils.getInstance().isBilling()) {
            if (isChange) {
                isChange = false;
                orderEasyPresenter.getCustomerList1();
                orderEasyPresenter.getRecordList(customer_id, "1");
            }
        }
    }

    private void initRefreshLayout() {
        khzy_lsitview.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
    }

    //下拉刷新控件
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //修改客户按钮
    @InjectView(R.id.xiugai_kehu)
    ImageView xiugai_kehu;

    //客户名字首字母
    @InjectView(R.id.kehu_shou_zimu)
    TextView kehu_shou_zimu;

    //电话
    @InjectView(R.id.kehu_phone)
    TextView kehu_phone;

    //客户姓名
    @InjectView(R.id.kehu_name)
    TextView kehu_name;

    /**
     * 三个显示可点击标签
     **/
    //1
    @OnClick(R.id.lay_1)
    void lay_1() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, AllTradeActivity.class);
        intent.putExtra("customer_name", customer.getName());
        intent.putExtra("customer_id", customer.getCustomer_id());
        this.startActivity(intent);
    }

    @OnClick(R.id.lay_2)
    void lay_2() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, ArrearsHistoryActivity.class);
        intent.putExtra("customer_name", customer.getName());
        intent.putExtra("customer_id", customer.getCustomer_id());
        intent.putExtra("customer_tel", customer.getTelephone());
        Log.e("JJF", "customer_name:" + customer.getName() + "customer_id:" + customer.getCustomer_id() + "customer_tel:" + customer.getTelephone());
        this.startActivityForResult(intent, 1002);
    }

    @OnClick(R.id.lay_3)
    void lay_3() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(this, OweActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", customer);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }


    //点击打电话
    @InjectView(R.id.kehu_call_phone)
    ImageView kehu_call_phone;

    //总交易额
    @InjectView(R.id.kehu_zongjiaoyie)
    TextView kehu_zongjiaoyie;
    //欠款
    @InjectView(R.id.kehu_qiankuan)
    TextView kehu_qiankuan;
    //欠货
    @InjectView(R.id.kehu_qianhuo)
    TextView kehu_qianhuo;

    /***
     * 中间标签点击控件
     * */

    //订单记录按钮
    @InjectView(R.id.bbtn1)
    LinearLayout bbtn1;

    //收银记录按钮
    @InjectView(R.id.bbtn2)
    LinearLayout bbtn2;

    //发货记录按钮
    @InjectView(R.id.bbtn3)
    LinearLayout bbtn3;

    //订单记录文字
    @InjectView(R.id.text_1)
    TextView text_1;

    //收银记录文字
    @InjectView(R.id.text_2)
    TextView text_2;

    //发货记录文字
    @InjectView(R.id.text_3)
    TextView text_3;

    //ListView
    @InjectView(R.id.khzy_lsitview)
    LoadMoreListView khzy_lsitview;


    /**
     * 底部三个按钮控件
     **/
    //去收款
    @InjectView(R.id.kehu_shoukuan)
    LinearLayout kehu_shoukuan;

    //去发货
    @InjectView(R.id.kehu_fahuo)
    LinearLayout kehu_fahuo;

    //去退货
    @InjectView(R.id.kehu_tuihuo)
    LinearLayout kehu_tuihuo;

    @InjectView(R.id.no_data_image)
    ImageView no_data_image;

    //需要的点击事件


    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        setResult(1001);
        CustomerHomepageActivity.this.finish();
    }

    @OnClick(R.id.kehu_call_phone)
    void call() {
        choiceContact();
    }

    private void callPhone() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + customer.getTelephone()));
        startActivity(intent);
    }

    @OnClick(R.id.xiugai_kehu)
    void xiugai() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent(CustomerHomepageActivity.this, ModifyCustomerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("id", customer.getCustomer_id());
        intent.putExtras(bundle);
        startActivityForResult(intent, 1004);
    }


    /**
     * 三个选项卡点击事件
     **/
    @OnClick(R.id.bbtn1)
    void bbtn1() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        dataType = 0;
        text_1.setTextColor(getResources().getColor(R.color.white));
        text_2.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_3.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn1.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn2.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn3.setBackgroundColor(getResources().getColor(R.color.white));
        customerOrderListAdapter.setData(orders);
        khzy_lsitview.setAdapter(customerOrderListAdapter);


    }


    @OnClick(R.id.bbtn2)
    void bbtn2() {
        dataType = 1;
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_2.setTextColor(getResources().getColor(R.color.white));
        text_1.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_3.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn2.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn1.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn3.setBackgroundColor(getResources().getColor(R.color.white));
        customerMoneyListAdapter.setData(moneys);
        khzy_lsitview.setAdapter(customerMoneyListAdapter);
//        content_view.removeAllViews();
//        view = LayoutInflater.from(this).inflate(R.layout.the_books_view_two, null);
//        content_view.addView(view);
    }

    @OnClick(R.id.bbtn3)
    void bbtn3() {
        dataType = 2;
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_3.setTextColor(getResources().getColor(R.color.white));
        text_1.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_2.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn3.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn1.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn2.setBackgroundColor(getResources().getColor(R.color.white));
        customerThingsListAdapter.setData(fahuos);
        khzy_lsitview.setAdapter(customerThingsListAdapter);
    }

    /***
     * 底部4个标签点击事件
     *
     * **/
    //取收款
    @OnClick(R.id.kehu_shoukuan)
    void kehu_shoukuan() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent(CustomerHomepageActivity.this, ReceivablesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", customer);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    //去发货
    @OnClick(R.id.kehu_fahuo)
    void kehu_fahuo() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent(CustomerHomepageActivity.this, DeliverGoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "cust");
        bundle.putSerializable("data", customer);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1003);
    }

    //去退货
    @OnClick(R.id.kehu_tuihuo)
    void kehu_tuihuo() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent(CustomerHomepageActivity.this, ReturnGoodsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", customer);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //去开单
    @OnClick(R.id.kehu_kaidan)
    void kehu_kaidan() {
        if (customer == null) {
            return;
        }
        Intent intent = new Intent(CustomerHomepageActivity.this, BillingActivity.class);
        Bundle bundle = new Bundle();
        customer.setCustomer_name(customer.getName());
        bundle.putSerializable("data", customer);
        bundle.putString("flag", "custhome");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void showProgress(int type) {
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    private static final int REQUEST_CODE_PERMISSION_TEL = 1;

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TEL)
    private void choiceContact() {
        String[] perms = {Manifest.permission.CALL_PHONE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            callPhone();
        } else {
            EasyPermissions.requestPermissions(this, "订货无忧需要以下权限:\n\n1.拨打电话", REQUEST_CODE_PERMISSION_TEL, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_TEL) {
            Toast.makeText(this, "您拒绝了「订货无忧」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        refreshData();
        orderEasyPresenter.getCustomerList1();
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
            case 4:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1005;

                }
                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
        Log.e("CustomerHomepageActi", "结束");
    }

    int dataType = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            ProgressUtil.dissDialog();
            khzy_lsitview.setLoadCompleted();
            store_refresh.setRefreshing(false);
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //分页处理
                            JsonObject page = result.getAsJsonObject("result").getAsJsonObject("page");
                            MyLog.e("page", page.toString());
                            orderCurrentPage = page.get("cur_page").getAsInt();
                            orderPageTotal = page.get("page_total").getAsInt();
                            if (orderCurrentPage == 1) orders = new ArrayList<>();
                            //处理返回的数据
                            JsonArray stocks = result.getAsJsonObject("result").getAsJsonArray("page_list");
                            for (int i = 0; i < stocks.size(); i++) {
                                //循环遍历获取的数据，并转成实体
                                OrderList order = (OrderList) GsonUtils.getEntity(stocks.get(i).toString(), OrderList.class);
                                orders.add(order);
                            }
                            DataStorageUtils.getInstance().setOrderPageTotal(orderPageTotal);
                            DataStorageUtils.getInstance().setChOrderLists(orders);
                            customerOrderListAdapter.setData(orders);
                            customerOrderListAdapter.notifyDataSetChanged();

                            if (dataType == 0) {
                                if (customerOrderListAdapter.getData().size() > 0) {
                                    no_data_image.setVisibility(View.GONE);
                                } else {
                                    no_data_image.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    }

                    Log.e("订单信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //分页处理
                            JsonObject page = result.getAsJsonObject("result").getAsJsonObject("page");
                            MyLog.e("page", page.toString());
                            moneyCurrentPage = page.get("cur_page").getAsInt();
                            moneyPageTotal = page.get("page_total").getAsInt();
                            if (moneyCurrentPage == 1) moneys = new ArrayList<>();
                            //处理返回的数据
                            JsonArray stocks = result.getAsJsonObject("result").getAsJsonArray("page_list");
                            for (int i = 0; i < stocks.size(); i++) {
                                //循环遍历获取的数据，并转成实体
                                Money money = (Money) GsonUtils.getEntity(stocks.get(i).toString(), Money.class);
                                moneys.add(money);
                            }
                            DataStorageUtils.getInstance().setMoneyPageTotal(moneyPageTotal);
                            DataStorageUtils.getInstance().setChMoneyLists(moneys);
                            customerMoneyListAdapter.setData(moneys);
                            customerMoneyListAdapter.notifyDataSetChanged();

                            if (dataType == 1) {
                                if (customerMoneyListAdapter.getData().size() > 0) {
                                    no_data_image.setVisibility(View.GONE);
                                } else {
                                    no_data_image.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    Log.e("收银信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //分页处理
                            JsonObject page = result.getAsJsonObject("result").getAsJsonObject("page");
                            MyLog.e("page", page.toString());
                            fahuoCurrentPage = page.get("cur_page").getAsInt();
                            fahuoPageTotal = page.get("page_total").getAsInt();
                            if (fahuoCurrentPage == 1) fahuos = new ArrayList<>();
                            //处理返回的数据
                            JsonArray stocks = result.getAsJsonObject("result").getAsJsonArray("page_list");

                            for (int i = 0; i < stocks.size(); i++) {
                                //循环遍历获取的数据，并转成实体
                                Fahuo fahuo = (Fahuo) GsonUtils.getEntity(stocks.get(i).toString(), Fahuo.class);
                                fahuos.add(fahuo);
                            }
                            DataStorageUtils.getInstance().setFahuoPageTotal(fahuoPageTotal);
                            DataStorageUtils.getInstance().setChFahuoLists(fahuos);
                            customerThingsListAdapter.setData(fahuos);
                            customerThingsListAdapter.notifyDataSetChanged();

                            if (dataType == 2) {
                                if (customerThingsListAdapter.getData().size() > 0) {
                                    no_data_image.setVisibility(View.GONE);
                                } else {
                                    no_data_image.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }

                    Log.e("发货信息", result.toString());
                    break;
                case 1005:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            List<Customer> datas = new ArrayList<>();
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(), Customer.class);
                                String name = "";
                                if (TextUtils.isEmpty(customer.getName())) {
                                    name = "-";
                                } else {
                                    name = customer.getName();
                                }
                                customer.setName(name);
                                datas.add(customer);
                            }
                            DataStorageUtils.getInstance().setCustomerLists(datas);
                            for (Customer hCustomer : datas) {
                                if (hCustomer.getCustomer_id() == customer_id) {
                                    customer = hCustomer;
                                    break;
                                }
                            }
                            kehu_name.setText(customer.getName());
                            kehu_phone.setText(customer.getTelephone());
                            kehu_shou_zimu.setText(String.valueOf(PinyinUtil.getFirstStr(customer.getName())));
                            kehu_qianhuo.setText(String.valueOf(customer.getOwe_sum()));
                            kehu_qiankuan.setText("¥" + String.valueOf(customer.getReceivable()));
                            kehu_zongjiaoyie.setText("¥" + String.valueOf(customer.getTrade_money()));
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


    private void refreshData() {
        if (customer == null) {
            return;
        }
        if (dataType == 0) {
            orderEasyPresenter.getRecordList(customer.getCustomer_id(), "1");
        } else if (dataType == 1) {
            orderEasyPresenter.getOrderRecordLlist(customer.getCustomer_id(), "1");
        } else if (dataType == 2) {
            orderEasyPresenter.getOperationRecordList(customer.getCustomer_id(), "1");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            setResult(1001);
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    @Override
    public void onloadMore() {
        if (dataType == 0) {
            if (orderPageTotal == orderCurrentPage) {
                ToastUtil.show("没有更多数据了");
                khzy_lsitview.setIsLoading(true);
            } else {
                orderCurrentPage++;
                orderEasyPresenter.getRecordList(customer.getCustomer_id(), String.valueOf(orderCurrentPage));
            }

        } else if (dataType == 1) {
            Log.e("CustomerHomepageActi", "下拉");
            if (moneyPageTotal == moneyCurrentPage) {
                ToastUtil.show("没有更多数据了");
                khzy_lsitview.setIsLoading(true);
            } else {
                moneyCurrentPage++;
                orderEasyPresenter.getOrderRecordLlist(customer.getCustomer_id(), String.valueOf(moneyCurrentPage));
            }
        } else if (dataType == 2) {
            if (fahuoPageTotal == fahuoCurrentPage) {
                ToastUtil.show("没有更多数据了");
                khzy_lsitview.setIsLoading(true);
            } else {
                fahuoCurrentPage++;
                orderEasyPresenter.getOperationRecordList(customer.getCustomer_id(), String.valueOf(fahuoCurrentPage));
            }

        }
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    private boolean isSalesperson() {
        if (arr.size() == 1) {
            for (int i = 0; i < arr.size(); i++) {
                if (!arr.get(i).getAsString().equals("")) {
                    if (arr.get(i).getAsInt() == 2) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
