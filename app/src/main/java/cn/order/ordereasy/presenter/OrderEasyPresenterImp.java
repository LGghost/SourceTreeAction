package cn.order.ordereasy.presenter;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import cn.order.ordereasy.R;
import cn.order.ordereasy.app.MyApplication;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.InventoryInfo;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Redelivery;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.model.OrderEasyApiModel;
import cn.order.ordereasy.model.OrderEasyApiModelImp;
import cn.order.ordereasy.utils.HttpsUtil;
import cn.order.ordereasy.utils.NetWorkUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.XGPushUtils;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.ExperienceInterfaceActivity;
import cn.order.ordereasy.view.activity.LoginActity;
import cn.order.ordereasy.view.activity.ModifyCustomerActivity;
import cn.order.ordereasy.view.activity.SetupAvtivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by mrpan on 2017/9/4.
 */

public class OrderEasyPresenterImp extends OrderEasyPresenter implements OrderEasyApiModelImp.OrderEasyOnListener {

    private OrderEasyApiModel orderEasyApiModel;
    private OrderEasyView orderEasyView;

    public OrderEasyPresenterImp(OrderEasyView orderEasyView) {

        this.orderEasyApiModel = new OrderEasyApiModelImp(this);
        this.orderEasyView = orderEasyView;
    }

    @Override
    public void onSuccess(JsonObject res, int type) {
        orderEasyView.hideProgress(1);
        orderEasyView.loadData(res, type);

        if (res != null) {
            int status = res.get("code").getAsInt();
            if (status == 1) {

            } else if (status == -7) {
                SharedPreferences spPreferences = MyApplication.getInstance().mContext.getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = spPreferences.edit();
                editor.putString("user_pwd", "");
                editor.putBoolean("isTagLogin", false);
                editor.commit();
                XGPushUtils.getInstance().unRegister(MyApplication.getInstance().mContext);
                ToastUtil.show(MyApplication.getInstance().mContext.getString(R.string.landfall_overdue));
                Intent intent = new Intent(MyApplication.getInstance().mContext, LoginActity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getInstance().mContext.startActivity(intent);
            } else if (status == -21 || status == -22) {
                ToastUtil.show(MyApplication.getInstance().mContext.getString(R.string.landfall_overdue2));
                Intent intent = new Intent(MyApplication.getInstance().mContext, ExperienceInterfaceActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApplication.getInstance().mContext.startActivity(intent);
            } else {
                Log.e("OrderEasyPresenterImp", "type:" + type);
                String message = res.get("message").getAsString();
                if (!TextUtils.isEmpty(message)) {
                    ToastUtil.show(message);
                } else {
                    ToastUtil.show(MyApplication.getInstance().mContext.getString(R.string.landfall_overdue1) + "<01>");
                }
            }

        } else {
            ToastUtil.show(MyApplication.getInstance().mContext.getString(R.string.landfall_overdue1) + "<02>");
        }
    }


    @Override
    public void onFailure(Throwable e) {
        orderEasyView.hideProgress(1);
        Log.e("ShangHuo", "失败：" + e);
        if (e.toString().equals("java.net.SocketTimeoutException: connect timed out")) {
            ToastUtil.show("没有获取到数据");
        } else {
            ToastUtil.show(MyApplication.getInstance().mContext.getString(R.string.landfall_overdue1) + "<03>");
        }
    }

    //登录
    @Override
    public void login(String username, String password) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            orderEasyApiModel.login(username, password);
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //注册
    @Override
    public void register(String telephone, String password, String smscode, String code, String yaoqcode) {
        orderEasyView.showProgress(3);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.register(telephone, password, smscode, code, yaoqcode));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //忘记密码
    @Override
    public void forgot(String telephone, String password, String smscode, String code) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            orderEasyApiModel.forgot(telephone, password, smscode, code);
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //获取验证码
    @Override
    public void getSmsCode(String telephone, String type, String code) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getSmsCode(telephone, type, code));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void operateRecordDetail(int operate_id) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.operateRecordDetail(operate_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //获取验证码
    @Override
    public void getCode() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCode());
        } else {
            orderEasyView.hideProgress(2);
        }

    }


    //设置店铺
    @Override
    public void setupStore(String mobile, String name, String province, String city, String district, String address, String telephone, String wechat, String notice) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            orderEasyApiModel.setupStore(mobile, name, province, city, district, address, telephone, wechat, notice);
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //店铺信息
    @Override
    public void getStoreInfo() {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getStoreInfo());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void uploadStoreImg(String type, String file) {

        orderEasyView.showProgress(3);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.uploadStoreImg(type, file));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void uploadGoodImg(String file) {
        orderEasyView.showProgress(3);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.uploadShoopImg(file));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCategoryInfo() {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCategoryInfo());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void addCategoryInfo(String name) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addCategoryInfo(name));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void updateCategoryInfo(int cateId, String name) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.updateCategoryInfo(cateId, name));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void delCategoryInfo(int cateId) {
        orderEasyView.showProgress(3);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.delCategoryInfo(cateId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getSpecInfo() {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getSpecInfo());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void addSpecCategoryInfo(String name) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addSpecInfo(name));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void addSpecValueInfo(int specId, String value) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addSpecValueInfo(specId, value));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void delSpecInfo(int specId) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.delSpecInfo(specId));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void delSpecValueInfo(int specId, String value) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.delSpecValueInfo(specId, value));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void addGoods(Goods good) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addGoods(good));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void updateGood(Goods good) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.updateGoods(good));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getGoodsList() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getGoodsList());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getGoodsInfo(int goodsId) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getGoodsInfo(goodsId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getOweGoodsList(int goodsId) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOweGoodsList(goodsId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getGoodsCustomers(int goodsId) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getGoodsCustomers(goodsId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void makeGoodsStatus(int goodId, int status) {
        orderEasyView.showProgress(4);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.makeGoodsStatus(goodId, status));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getGoodsBuyHistories(int goodId, int days, String date) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getGoodsBuyHistories(goodId, days, date));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //开单
    @Override
    public void Add_Odder(Order order) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.Add_Odder(order));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void closeOrder(int orderId) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.closeOrder(orderId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getOrdersList(int page, String filter_type, String user_id, String start_time, String end_time) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOrdersList(page, filter_type, user_id, start_time, end_time));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void searchRecordList(int page, String keyword) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.searchRecordList(page, keyword));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getOrderInfo(int orderId) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOrderInfo(orderId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getOrderLogs(int orderId) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOrderLogs(orderId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getOweOrdersList(int custId, int order_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOweOrdersList(custId, order_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void updateOrderAddr(int orderId, String addr, int type) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.updateOrderAddr(orderId, addr, type));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCustomerList() {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCustomerList());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCustomerList1() {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCustomerList1());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void addCustomer(Customer customer) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addCustomer(customer));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.updateCustomer(customer));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void delCustomer(int customerId) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.delCustomer(customerId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void importCustomers(List<Customer> customerList) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.importCustomers(customerList));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCustomerInfo(int customerId) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCustomerInfo(customerId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCustSalesGoods(int customer_id, int is_owe) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCustSalesGoods(customer_id, is_owe));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCustomerOweLog(int customer_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCustomerOweLog(customer_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void adjustmentOweInfo(int customer_id, double money, String remark) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.adjustmentOweInfo(customer_id, money, remark));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getCustomerAccountInfo(String sign, int customer_id, int shop_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getCustomerAccountInfo(sign, customer_id, shop_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getStockList(int operate_type) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getStockList(operate_type));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //客户主页--订单记录
    @Override
    public void getRecordList(int customer_id, String page) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getRecordList(customer_id, page));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //客户主页--收银记录
    @Override
    public void getOrderRecordLlist(int customer_id, String page) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOrderRecordLlist(customer_id, page));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    // 客户主页--操作（发货）记录
    @Override
    public void getOperationRecordList(int customer_id, String page) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOperationRecordList(customer_id, page));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //库存调整记录
    @Override
    public void getOperationRecordList(int page, int operate_type, int user_id, String begindate, String enddate) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getOperationRecordList(page, operate_type, user_id, begindate, enddate));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void addOrderPay(int customer_id, int payment_type, double cash, double wechat, double alipay, double bank_card, double other) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addOrderPay(customer_id, payment_type, cash, wechat, alipay, bank_card, other));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //日报
    @Override
    public void getDaily(String date) {
        orderEasyView.showProgress(2);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getDaily(date));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void delivers(List<Delivery> deliveries) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.delivers(deliveries));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void deliver(Delivery delivery) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.deliver(delivery));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void redeliver(Redelivery redelivery) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.redeliver(redelivery));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getNumToday(int type) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getNumToday(type));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getNumToday2(int type) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getNumToday2(type));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getStoreData() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getStoreData());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getEmployee(int type) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getEmployeeNum(type));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void addEmployee(String name, String tel, String pass, List<Integer> list) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addEmployeeNum(name, tel, pass, list));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getInventoryList(int page) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getInventoryList(page));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getInventoryInfo(int inventory_id, int user_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getInventoryInfo(inventory_id, user_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getInventory(int inventory_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getInventory(inventory_id));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void addInventory() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.addInventory());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void commitInventoryInfo(int inventory_id, String remark, int is_adjust) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.commitInventoryInfo(inventory_id, remark, is_adjust));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void saveInventoryInfo(List<Map<String, Object>> inventoryInfo) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.saveInventoryInfo(inventoryInfo));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getInventoryInfo(int inventory_id, int user_id, int page) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getInventoryInfo(inventory_id, user_id, page));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void deleteEmployee(String user_id) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.deleteEmployee(user_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void getUserInfo() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getUserInfo());
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void updateUserInfo(int id, String name, List<String> list) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.updateUserInfo(id, name, list));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void updateUserPass(int id, String pass, String pass1) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.updateUserPass(id, pass, pass1));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void booking2(int type, int page) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.booking2(type, page));
        } else {
            orderEasyView.hideProgress(2);
        }

    }

    @Override
    public void booking4(int days, String starttime, String endtime) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.booking4(days, starttime, endtime));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void booking3(int days, String starttime, String endtime) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.booking3(days, starttime, endtime));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void booking1(int days, int page, int type, String user_id, String starttime, String endtime) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.booking1(days, page, type, user_id, starttime, endtime));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void initStoreInfo(String shopname, String bossname) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.initStoreInfo(shopname, bossname));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //用户反馈
    @Override
    public void getUserFeedback(String content) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getUserFeedback(content));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //清空店铺数据
    @Override
    public void getEmptyStoreData(String goods, String customer, String user, String password) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.getEmptyStoreData(goods, customer, user, password));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void uploadUserAvatar(String file) {
        orderEasyView.showProgress(3);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.uploadUserAvatar(file));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void customerTransactionRecord(int customer_id, int days, int is_owe, int page) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.customerTransactionRecord(customer_id, days, is_owe, page));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //欠款历史记录
    @Override
    public void arrearsHistory(int customer_id, int page) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.arrearsHistory(customer_id, page));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //调整欠款
    @Override
    public void adjustmentArrears(int customer_id, double money, String remark) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.adjustmentArrears(customer_id, money, remark));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void isUpdataApp() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.isUpdataApp());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //客户等级列表
    @Override
    public void customerRankList() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.customerRankList());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //删除客户等级列表
    @Override
    public void customerdelRank(int rank_id) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.customerdelRank(rank_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //编辑客户等级
    @Override
    public void customerditRank(int rank_id, String rank_name, int rank_discount) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.customerditRank(rank_id, rank_name, rank_discount));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //增加客户等级
    @Override
    public void customeraddRank(String rank_name, int rank_discount, List<String> customer_ids) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.customeraddRank(rank_discount, rank_name, customer_ids));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //增加等级所属客户
    @Override
    public void customertoRank(List<String> customer_ids, int rank_id) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.customertoRank(customer_ids, rank_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //支付记录(财务详情)导出
    @Override
    public void exportPayList(String start_time, String end_time) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.exportPayList(start_time, end_time));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //产品销售详情记录
    @Override
    public void exportSaleProductList(String start_time) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.exportSaleProductList(start_time));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //产品销售统计
    @Override
    public void exportSaleProductCount(String start_time, String end_time) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.exportSaleProductCount(start_time, end_time));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //导出记录
    @Override
    public void exportLog() {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.exportLog());
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //导出状态查询
    @Override
    public void exportStatus(int log_id) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.exportStatus(log_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //贷款申请
    @Override
    public void loanAsk(String telephone, String name, String purpose, String identity, String gender) {
        orderEasyView.showProgress(1);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.loanAsk(telephone, name, purpose, identity, gender));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //订单修改和确认
    @Override
    public void orderConfirm(Order order) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.orderConfirm(order));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    // 删除订单
    @Override
    public void goodsDel(int goodId) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.goodsDel(goodId));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    @Override
    public void inventoryInfo(int inventory_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.inventoryInfo(inventory_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }

    //供货商列表
    @Override
    public void supplierIndex() {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.supplierIndex());
        } else {
            orderEasyView.hideProgress(2);
        }
    }
    //增加供货商
    @Override
    public void supplierAdd(SupplierBean supplierBean) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.supplierAdd(supplierBean));
        } else {
            orderEasyView.hideProgress(2);
        }
    }
    //编辑供货商
    @Override
    public void supplierEdit(SupplierBean supplierBean) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.supplierEdit(supplierBean));
        } else {
            orderEasyView.hideProgress(2);
        }
    }
    //导入供货商
    @Override
    public void supplierImport(List<SupplierBean> supplierList) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.supplierImport(supplierList));
        } else {
            orderEasyView.hideProgress(2);
        }
    }
    //导入供货商
    @Override
    public void supplierInfo(int supplier_id) {
        orderEasyView.showProgress(0);
        if (NetWorkUtils.isNetworkConnected(MyApplication.getInstance().mContext)) {
            addSubscription(orderEasyApiModel.supplierInfo(supplier_id));
        } else {
            orderEasyView.hideProgress(2);
        }
    }


}
