package cn.order.ordereasy.service;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.order.ordereasy.app.MyApplication;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.InventoryInfo;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Redelivery;
import cn.order.ordereasy.utils.BitmapUtils;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.Md5Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

/**
 * Created by Mr.Pan on 2017/9/4.
 */

public class OrderEasyApiService {


    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    public static Observable<JsonObject> login(String username, String password) {
        HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put("username", username);
        paramsMap.put("password", password);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("OrderEasyApiModel", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.login_url, body);
    }

    /**
     * 注册
     *
     * @param telephone
     * @param password
     * @return
     */
    public static Observable<JsonObject> register(String telephone, String password, String smscode, String code, String yaoqcode) {
        HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put("telephone", telephone);
        paramsMap.put("password", password);
        paramsMap.put("sms_code", smscode);
        paramsMap.put("code", code);
        paramsMap.put("invite_code", yaoqcode);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("OrderEasyApiModel", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.register_url, body);
    }

    /**
     * 忘记密码
     *
     * @param telephone
     * @param password
     * @param smscode
     * @return
     */
    public static Observable<JsonObject> forgot(String telephone, String password, String smscode, String code) {
        HashMap<String, String> paramsMap = new HashMap<>();

        paramsMap.put("telephone", telephone);
        paramsMap.put("password", password);
        paramsMap.put("sms_code", smscode);
        paramsMap.put("code", code);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("OrderEasyApiModel", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.forgot_url, body);
    }

    /**
     * 获取短信验证码(注册：register，忘记密码：forgot)
     *
     * @return
     */
    public static Observable<JsonObject> getSmsCode(String telephone, String type, String code) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("telephone", telephone);
        paramsMap.put("type", type);
        paramsMap.put("code", code);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("OrderEasyApiModel", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.smsCode_url, body);
    }

    /**
     * 获取密钥
     *
     * @return
     */
    public static Observable<JsonObject> getCode() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("OrderEasyApiModel", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.code_url, body);
    }

    /**
     * 店铺设置
     *
     * @param mobile
     * @param name
     * @param province
     * @param city
     * @param district
     * @param address
     * @param telephone
     * @param wechat
     * @return
     */
    public static Observable<JsonObject> setupStore(String mobile, String name, String province,
                                                    String city, String district, String address, String telephone, String wechat, String notice) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (!TextUtils.isEmpty(mobile)) {
            paramsMap.put("mobile", mobile);
        }
        if (!TextUtils.isEmpty(name)) {
            paramsMap.put("name", name);
        }
        if (!TextUtils.isEmpty(province)) {
            paramsMap.put("province", province);
        }
        if (!TextUtils.isEmpty(city)) {
            paramsMap.put("city", city);
        }
        if (!TextUtils.isEmpty(district)) {
            paramsMap.put("district", district);
        }
        if (!TextUtils.isEmpty(address)) {
            paramsMap.put("address", address);
        }
        if (!TextUtils.isEmpty(telephone)) {
            paramsMap.put("telephone", telephone);
        }
        if (!TextUtils.isEmpty(wechat)) {
            paramsMap.put("wechat", wechat);
        }
        if (!TextUtils.isEmpty(notice)) {
            paramsMap.put("notice", notice);
        }
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.store_setup_url, body);
    }

    /**
     * 获取店铺信息
     *
     * @return
     */
    public static Observable<JsonObject> getStoreInfo() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.store_info_url, body);
    }

    /**
     * logo,wx_qrcode
     *
     * @param type
     * @param img
     * @return
     */
    public static Observable<JsonObject> uploadStoreImg(String type, String img) {
        final Map<String, RequestBody> map = new HashMap<>();
        map.put("type", RequestBody.create(MediaType.parse("form-data"), type));
        String filePath = BitmapUtils.compressImage(img, 480, 800, 1024);
        Log.e("uploadStoreImg", "file路径：" + filePath);
        File file = new File(filePath);
        Log.e("uploadStoreImg", "file长度：" + file.length());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        return MyApplication.getInstance().getService().uploadImgs(Config.store_upload_img_url, map, body);
    }

    public static Observable<JsonObject> uploadGoodImg(String img) {
        final Map<String, RequestBody> map = new HashMap<>();
        map.put("type", RequestBody.create(MediaType.parse("form-data"), ""));
        String filePath = BitmapUtils.compressImage(img, 480, 800, 1024);
        Log.e("uploadGoodImg", "file路径：" + filePath);
        File file = new File(filePath);
        Log.e("uploadGoodImg", "file长度：" + file.length());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        return MyApplication.getInstance().getService().uploadImgs(Config.good_upload_url, map, body);
    }

    public static Observable<JsonObject> uploadUserAvatar(String img) {
        final Map<String, RequestBody> map = new HashMap<>();
        map.put("type", RequestBody.create(MediaType.parse("form-data"), ""));
        String filePath = BitmapUtils.compressImage(img, 480, 800, 1024);
        Log.e("uploadUserAvatar", "file路径：" + filePath);
        File file = new File(filePath);
        Log.e("uploadUserAvatar", "file长度：" + file.length());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        return MyApplication.getInstance().getService().uploadImgs(Config.upload_user_avatar, map, body);
    }

    /**
     * 获取分类信息
     *
     * @return
     */
    public static Observable<JsonObject> getCategoryInfo() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.category_info, body);
    }

    /**
     * 删除分类信息
     *
     * @param cateId
     * @return
     */
    public static Observable<JsonObject> delCategoryInfo(int cateId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("category_id", String.valueOf(cateId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.category_del_info, body);
    }

    /**
     * 增加分类信息
     *
     * @param name
     * @return
     */
    public static Observable<JsonObject> addCategoryInfo(String name) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.category_add_info, body);
    }

    /**
     * 修改分类信息
     *
     * @param cateId
     * @param name
     * @return
     */
    public static Observable<JsonObject> updateCategoryInfo(int cateId, String name) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("category_id", String.valueOf(cateId));
        paramsMap.put("name", name);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.category_update_info, body);
    }

    /**
     * 获取规格信息
     *
     * @return
     */
    public static Observable<JsonObject> getSpecInfo() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.spec_get_info, body);
    }

    /**
     * 删除规格信息
     *
     * @param spec_id
     * @return
     */
    public static Observable<JsonObject> delSpecInfo(int spec_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("spec_id", String.valueOf(spec_id));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.spec_del_info, body);
    }

    public static Observable<JsonObject> delSpecValueInfo(int spec_id, String value) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("spec_id", String.valueOf(spec_id));
        paramsMap.put("value", value);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.spec_del_value_info, body);
    }

    /**
     * 添加规格信息
     *
     * @param name
     * @return
     */
    public static Observable<JsonObject> addSpecInfo(String name) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        //paramsMap.put("value",value);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.spec_add_info, body);
    }


    /**
     * 添加规格属性
     *
     * @param spec_id
     * @param value
     * @return
     */
    public static Observable<JsonObject> addSpecValueInfo(int spec_id, String value) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("value", value);
        paramsMap.put("spec_id", String.valueOf(spec_id));
        //paramsMap.put("value",value);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.spec_value_add_info, body);
    }

    /**
     * 添加商品信息
     *
     * @param good
     * @return
     */
    public static Observable<JsonObject> addGoods(Goods good) {
        String strEntity = GsonUtils.getJsonStr(good);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.goods_add_url, body);
    }

    /**
     * 修改商品信息
     *
     * @param good
     * @return
     */
    public static Observable<JsonObject> updateGoods(Goods good) {
        String strEntity = GsonUtils.getJsonStr(good);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.goods_edit_url, body);
    }

    /**
     * 获取商品列表
     *
     * @return
     */
    public static Observable<JsonObject> getGoodsList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.goods_list_url, body);
    }

    /**
     * 获取商品详情
     *
     * @param goodsId
     * @return
     */
    public static Observable<JsonObject> getGoodsInfo(int goodsId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goods_id", String.valueOf(goodsId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.goods_info_url, body);
    }

    /**
     * 获取商品欠货订单
     *
     * @param goodsId
     * @return
     */
    public static Observable<JsonObject> getOweGoodsList(int goodsId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goods_id", String.valueOf(goodsId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_oweorder_url, body);
    }

    /**
     * 获取购买客户
     *
     * @param goodsId
     * @return
     */
    public static Observable<JsonObject> getGoodsCustomers(int goodsId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goods_id", String.valueOf(goodsId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_customer_url, body);
    }

    /**
     * 商品上下架
     *
     * @param goodsId
     * @param status
     * @return
     */
    public static Observable<JsonObject> makeGoodsStatus(int goodsId, int status) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goods_id", String.valueOf(goodsId));
        paramsMap.put("status", String.valueOf(status));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_make_status_url, body);
    }

    /**
     * 商品购买趋势
     *
     * @param goodsId
     * @param days
     * @param date
     * @return
     */
    public static Observable<JsonObject> getGoodsBuyHistory(int goodsId, int days, String date) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goods_id", String.valueOf(goodsId));
        paramsMap.put("days", String.valueOf(days));
        paramsMap.put("date", date);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_buy_line_url, body);
    }

    /**
     * 开单
     *
     * @param order
     * @return
     */
    public static Observable<JsonObject> Add_Odder(Order order) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        List<Map<String, Object>> map = new ArrayList<>();
        List<Goods> goods = order.getGoods_list();
        if (goods == null) goods = new ArrayList<>();
        for (Goods good : goods) {
            List<Product> products = good.getProduct_list();
            for (Product p : products) {
                Map<String, Object> data = new HashMap<>();
                data.put("operate_num", p.getNum());
                data.put("product_id", p.getProduct_id());
                if (p.getDefault_price() != -1) {
                    data.put("cost_price", p.getDefault_price());
                    data.put("sell_price", p.getDefault_price());
                }
                map.add(data);
            }
        }
        paramsMap.put("address", order.getAddress());
        paramsMap.put("customer_id", order.getCustomer_id());
        paramsMap.put("is_deliver", order.getIs_deliver());
        paramsMap.put("is_payment", order.getIs_payment());
        paramsMap.put("order_type", order.getOrder_type());
        paramsMap.put("original_order_id", order.getOriginal_order_id());
        paramsMap.put("payable", order.getPayable());
        paramsMap.put("product_list", map);
        paramsMap.put("remark", order.getRemark());

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.Add_Odder, body);
    }

    /**
     * 关闭订单
     *
     * @param orderId
     * @return
     */
    public static Observable<JsonObject> closeOrder(int orderId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("order_id", String.valueOf(orderId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_close_url, body);
    }

    /**
     * 获取订单列表
     *
     * @return
     */
    public static Observable<JsonObject> getOrdersList(int page, String filter_type, String user_id, String start_time, String end_time) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("page", page);
        paramsMap.put("filter_type", filter_type);
        paramsMap.put("user_id", user_id);
        if (!TextUtils.isEmpty(start_time)) {
            paramsMap.put("start_time", start_time);
        }
        if (!TextUtils.isEmpty(end_time)) {
            paramsMap.put("end_time", end_time);
        }

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_list_url, body);
    }

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    public static Observable<JsonObject> getOrderInfo(int orderId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("order_id", String.valueOf(orderId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getOrderInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_info_url, body);
    }

    /**
     * 获取订单操作日志
     *
     * @param orderId
     * @return
     */
    public static Observable<JsonObject> getOrderLogs(int orderId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("order_id", String.valueOf(orderId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_history_url, body);
    }


    /**
     * 获取欠货订单
     *
     * @return
     */
    public static Observable<JsonObject> getOweOrdersList(int customer_id, int order_id) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        if (customer_id != -1) {
            paramsMap.put("customer_id", customer_id);
        }
        if (order_id != -1) {
            paramsMap.put("order_id", order_id);
        }
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_oweorder_url, body);
    }

    /**
     * 更改订单收货地址
     *
     * @param orderId
     * @param addr
     * @return
     */
    public static Observable<JsonObject> updateOrderAddr(int orderId, String addr) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("order_id", String.valueOf(orderId));
        paramsMap.put("address", addr);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_update_addr_url, body);
    }

    /**
     * 客户列表
     *
     * @return
     */
    public static Observable<JsonObject> getCustomerList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_list_url, body);
    }

    /**
     * 客户列表
     *
     * @return
     */
    public static Observable<JsonObject> getCustomerList1() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_list_url, body);
    }

    /**
     * 添加客户
     *
     * @param customer
     * @return
     */
    public static Observable<JsonObject> addCustomer(Customer customer) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", customer.getCustomer_name());
        paramsMap.put("telephone", customer.getTelephone());
        paramsMap.put("rank_id", customer.getRank_id());
        paramsMap.put("address", customer.getAddress());
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_add_url, body);
    }

    /**
     * 修改客户
     *
     * @param customer
     * @return
     */
    public static Observable<JsonObject> updateCustomer(Customer customer) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        if (!TextUtils.isEmpty(customer.getCustomer_name())) {
            paramsMap.put("name", customer.getCustomer_name());
        }
        paramsMap.put("customer_id", String.valueOf(customer.getCustomer_id()));
        if (!TextUtils.isEmpty(customer.getTelephone())) {
            paramsMap.put("telephone", customer.getTelephone());
        }
        if (customer.getAddress().size() > 0) {
            paramsMap.put("address", customer.getAddress());
        }
        paramsMap.put("rank_id", customer.getRank_id());

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_edit_url, body);
    }

    /**
     * 删除客户
     *
     * @param customerId
     * @return
     */
    public static Observable<JsonObject> delCustomer(int customerId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customerId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_del_url, body);
    }

    /**
     * 店铺基本数据
     *
     * @return
     */
    public static Observable<JsonObject> getStoreData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.amount_today_url, body);
    }

    /**
     * 批量导入
     *
     * @param customerList
     * @return
     */
    public static Observable<JsonObject> importCustomers(List<Customer> customerList) {
        String strEntity = GsonUtils.getJsonStr(customerList);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_import_url, body);
    }

    /**
     * 获取客户信息
     *
     * @param customerId
     * @return
     */
    public static Observable<JsonObject> getCustomerInfo(int customerId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customerId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.cust_info_url, body);
    }

    /**
     * 客户销售商品
     *
     * @param customer_id
     * @param is_owe
     * @return
     */
    public static Observable<JsonObject> getCustSalesGoods(int customer_id, int is_owe) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customer_id));
        paramsMap.put("is_owe", String.valueOf(is_owe));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_customer_url, body);
    }

    /**
     * 客户欠款记录
     *
     * @param customer_id
     * @return
     */
    public static Observable<JsonObject> getCustomerOweLog(int customer_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("order_id", String.valueOf(customer_id));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_customer_url, body);
    }

    /**
     * 修改客户欠款
     *
     * @param customer_id
     * @param money
     * @param remark
     * @return
     */
    public static Observable<JsonObject> adjustmentOweInfo(int customer_id, double money, String remark) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customer_id));
        paramsMap.put("money", String.valueOf(money));
        paramsMap.put("remark", remark);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_customer_url, body);
    }

    /**
     * 获取公开账单
     *
     * @param sign
     * @param customer_id
     * @param shop_id
     * @return
     */
    public static Observable<JsonObject> getCustomerAccountInfo(String sign, int customer_id, int shop_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("shop_id", String.valueOf(shop_id));
        paramsMap.put("customer_id", String.valueOf(customer_id));
        paramsMap.put("sign", sign);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.good_customer_url, body);
    }

    /**
     * 获取库存列表
     *
     * @param operate_type
     * @return
     */
    public static Observable<JsonObject> getStockList(int operate_type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("operate_type", String.valueOf(operate_type));

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.stock_list_url, body);
    }

    /**
     * 客户主页--订单记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    public static Observable<JsonObject> getRecordList(int customer_id, String page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customer_id));
        paramsMap.put("page", String.valueOf(page));

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.cashier_record_list, body);
    }

    /**
     * 搜索订单
     *
     * @param page
     * @param keyword
     * @return
     */
    public static Observable<JsonObject> searchRecordList(int page, String keyword) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("keyword", keyword);
        paramsMap.put("page", String.valueOf(page));

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.cashier_record_list, body);
    }

    /**
     * 客户主页--收银记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    public static Observable<JsonObject> getOrderRecordLlist(int customer_id, String page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (customer_id != -1) {
            paramsMap.put("customer_id", String.valueOf(customer_id));
        }
        paramsMap.put("page", String.valueOf(page));

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.order_record_list, body);
    }

    /**
     * 客户主页--操作（发货）记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    public static Observable<JsonObject> getOperationRecordList(int customer_id, String page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customer_id));
        paramsMap.put("page", String.valueOf(page));

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.operation_record_list, body);
    }

    /**
     * 库存调整记录
     *
     * @param page
     * @return
     */
    public static Observable<JsonObject> getOperationRecordList(int page, int operate_type, int user_id, String begindate, String enddate) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", String.valueOf(page));
        paramsMap.put("operate_type", String.valueOf(operate_type));
        if (user_id != -1) {
            paramsMap.put("user_id", String.valueOf(user_id));
        }
        paramsMap.put("start_time", begindate);
        paramsMap.put("end_time", enddate);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.operation_record_list, body);
    }

    /**
     * 收款
     *
     * @param customer_id
     * @param payment_type
     * @param cash
     * @param wechat
     * @param alipay
     * @param bank_card
     * @param other
     * @return
     */
    public static Observable<JsonObject> addOrderPay(int customer_id, int payment_type, double cash, double wechat, double alipay, double bank_card, double other) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", String.valueOf(customer_id));
        paramsMap.put("payment_type", payment_type);
        HashMap<String, Object> pay_data = new HashMap<>();
        pay_data.put("cash", cash);
        pay_data.put("wechat", wechat);
        pay_data.put("alipay", alipay);
        pay_data.put("bank_card", bank_card);
        pay_data.put("other", other);

        paramsMap.put("pay_data", pay_data);

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.cust_order_pay_add_url, body);
    }

    /**
     * 首页-日报
     *
     * @param date
     * @return
     */
    public static Observable<JsonObject> getDaily(String date) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("date", String.valueOf(date));

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.daily_url, body);
    }

    /**
     * 发货
     *
     * @param deliveries
     * @return
     */
    public static Observable<JsonObject> delivers(List<Delivery> deliveries) {
        String strEntity = GsonUtils.getJsonStr(deliveries);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.operation_add_url, body);
    }

    public static Observable<JsonObject> operateRecordDetail(int operate_id) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("operate_id", operate_id);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.goods_operate_info_url, body);
    }

    /**
     * 出库入库
     *
     * @param delivery
     * @return
     */
    public static Observable<JsonObject> deliver(Delivery delivery) {
        String strEntity = GsonUtils.getJsonStr(delivery);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("deliver", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.operation_add_url, body);
    }

    /**
     * 退货
     *
     * @param redelivery
     * @return
     */
    public static Observable<JsonObject> redeliver(Redelivery redelivery) {
        String strEntity = GsonUtils.getJsonStr(redelivery);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.Add_Odder, body);
    }

    /**
     * 首页今日交易额，今日开单数，当前欠款数，当前欠货数的数据获取
     */
    public static Observable<JsonObject> getNumToday(int type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", String.valueOf(type));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getNumToday", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.num_today_url, body);
    }

    /**
     * 首页今日交易额，今日开单数，当前欠款数，当前欠货数的数据获取
     */
    public static Observable<JsonObject> getNumToday2(int type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", String.valueOf(type));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getNumToday2", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.num_today_url2, body);
    }

    /**
     * 获取员工和老板列表
     */
    public static Observable<JsonObject> getEmployee() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.get_employee_url, body);
    }

    /**
     * 添加员工
     */
    public static Observable<JsonObject> addEmployee(String name, String tel, String pass, List<Integer> list) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("name", name);
        paramsMap.put("telephone", tel);
        paramsMap.put("password", pass);
        paramsMap.put("auth_group_ids", list);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.add_employee_url, body);
    }

    /**
     * 获取盘点单列表
     *
     * @return
     */
    public static Observable<JsonObject> inventoryList(int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", String.valueOf(page));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.list_inventory_url, body);
    }

    /**
     * 盘点单详情
     *
     * @param inventory_id
     * @return
     */
    public static Observable<JsonObject> getInventoryInfo(int inventory_id, int user_id) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("inventory_id", inventory_id);
        paramsMap.put("user_id", user_id);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.into_inventory_url, body);
    }

    /**
     * 盘点单详情
     *
     * @param inventory_id
     * @return
     */
    public static Observable<JsonObject> getInventory(int inventory_id) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("inventory_id", inventory_id);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.get_inventory_url, body);
    }

    /**
     * 新建盘点
     *
     * @return
     */
    public static Observable<JsonObject> addInventoryInfo() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        //paramsMap.put("inventory_id",inventory_id);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.add_inventory_url, body);
    }

    /**
     * 提交盘点
     *
     * @param inventory_id
     * @param remark
     * @param is_adjust
     * @return
     */
    public static Observable<JsonObject> commitInventoryInfo(int inventory_id, String remark, int is_adjust) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("inventory_id", inventory_id);
        paramsMap.put("remark", remark);
        paramsMap.put("is_adjust", is_adjust);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.commit_inventory_url, body);
    }

    /**
     * 加入盘点，或继续盘点
     *
     * @param inventoryInfo
     * @return
     */
    public static Observable<JsonObject> saveInventoryInfo(List<Map<String, Object>> inventoryInfo) {
        String strEntity = GsonUtils.getJsonStr(inventoryInfo);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("saveInventoryInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.save_user_inventory_url, body);
    }

    /**
     * 获取盘点结果
     *
     * @param inventory_id
     * @param user_id
     * @param page
     * @return
     */
    public static Observable<JsonObject> getInventoryInfo(int inventory_id, int user_id, int page) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("inventory_id", inventory_id);
        if (user_id != -1) {
            paramsMap.put("user_id", user_id);
        }
        paramsMap.put("page", page);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("saveInventoryInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.list_all_inventory_url, body);
    }

    //删除员工
    public static Observable<JsonObject> deleteEmployee(String user_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_id", user_id);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("addEmployee", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.delete_employee_url, body);
    }

    //获取用户登录信息
    public static Observable<JsonObject> getUserInfo() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.get_userinfo_url, body);
    }

    //修改用户信息
    public static Observable<JsonObject> updateUserInfo(int id, String name, List<String> list) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("user_id", id + "");
        paramsMap.put("name", name);
        paramsMap.put("auth_group_ids", list);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("updateUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.update_userinfo_url, body);
    }

    //修改用户密码
    public static Observable<JsonObject> updateUserPass(int id, String pass, String pass1) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("user_id", id + "");
        if (!TextUtils.isEmpty(pass)) {
            paramsMap.put("origin_password", pass);
        }
        paramsMap.put("password", pass1);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("updateUserPass", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.update_userpass_url, body);
    }

    //首页，点击账本，1：次数页面
    public static Observable<JsonObject> booking1(int days, int page, int type, String user_id, String starttime, String endtime) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (days != -1) {
            paramsMap.put("days", days + "");
        }
        paramsMap.put("page", page + "");
        if (type != -1) {
            paramsMap.put("type", type + "");
        }
        if (!user_id.equals("0")) {
            paramsMap.put("user_id", user_id);
        }
        if (!TextUtils.isEmpty(starttime)) {
            paramsMap.put("start_time", starttime);
        }
        if (!TextUtils.isEmpty(endtime)) {
            paramsMap.put("end_time", endtime);
        }

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("booking1", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.booking1_url, body);
    }

    //首页，点击账本，2：次数页面
    public static Observable<JsonObject> booking2(int type, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("type", type + "");
        paramsMap.put("page", page + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("booking2", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.booking2_url, body);
    }

    //首页，点击账本，3：次数页面
    public static Observable<JsonObject> booking3(int days, String starttime, String endtime) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (days != -1) {
            paramsMap.put("days", days + "");
        }
        if (!TextUtils.isEmpty(starttime)) {
            paramsMap.put("start_time", starttime);
        }
        if (!TextUtils.isEmpty(endtime)) {
            paramsMap.put("end_time", endtime);
        }
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("booking3", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.booking3_url, body);
    }

    //首页，点击账本，4：次数页面
    public static Observable<JsonObject> booking4(int days, String starttime, String endtime) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (days != -1) {
            paramsMap.put("days", days + "");
        }
        if (!TextUtils.isEmpty(starttime)) {
            paramsMap.put("start_time", starttime);
        }
        if (!TextUtils.isEmpty(endtime)) {
            paramsMap.put("end_time", endtime);
        }
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("booking4", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.booking4_url, body);
    }

    /**
     * 客户交易记录
     *
     * @param customer_id
     * @param days
     * @param is_owe
     * @param page
     * @return
     */
    public static Observable<JsonObject> customerTransactionRecord(int customer_id, int days, int is_owe, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", customer_id + "");
        if (days != -1) {
            paramsMap.put("days", days + "");
        }
        paramsMap.put("is_owe", is_owe + "");
        paramsMap.put("page", page + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("transactionRecord", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.cust_goods_url, body);
    }

    /**
     * 初始化店铺
     *
     * @param shopname
     * @param bossname
     * @return
     */
    public static Observable<JsonObject> initStoreInfo(String shopname, String bossname) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("shop_name", shopname);
        paramsMap.put("boss_name", bossname);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("initStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.init_shop_url, body);
    }

    /**
     * 用户反馈
     *
     * @param content
     * @return
     */
    public static Observable<JsonObject> getUserFeedback(String content) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("content", content);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("initStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.user_feedback, body);
    }

    /**
     * 清空店铺数据
     *
     * @param goods
     * @param customer
     * @param user
     * @return
     */
    public static Observable<JsonObject> getEmptyStoreData(String goods, String customer, String user, String password) {
        HashMap<String, String> paramsMap = new HashMap<>();
        if (goods.equals("1")) {
            paramsMap.put("goods", goods);
        }
        if (customer.equals("1")) {
            paramsMap.put("customer", customer);
        }
        if (user.equals("1")) {
            paramsMap.put("user", user);
        }
        paramsMap.put("password", Md5Utils.StrToMd5(password));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("initStoreInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.empty_store_data, body);
    }

    /**
     * 欠款历史记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    public static Observable<JsonObject> arrearsHistory(int customer_id, int page) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", customer_id + "");
        paramsMap.put("page", page + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("arrearsHistory", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.cust_owelogs_url, body);
    }

    /**
     * 调整欠款
     *
     * @param customer_id
     * @param money
     * @return
     */
    public static Observable<JsonObject> adjustmentArrears(int customer_id, double money, String remark) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("customer_id", customer_id + "");
        paramsMap.put("money", money + "");
        paramsMap.put("remark", remark);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("adjustmentArrears", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.cust_adjustment_url, body);
    }

    /**
     * 是否更新app
     */
    public static Observable<JsonObject> isUpdataApp() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.isupdataapp_url, body);
    }

    /**
     * 客户等级列表
     */
    public static Observable<JsonObject> customerRankList() {
        HashMap<String, String> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.customer_rankList_url, body);
    }

    /**
     * 删除客户等级列表
     */
    public static Observable<JsonObject> customerdelRank(int rank_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("rank_id", rank_id + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.customer_delRank_url, body);
    }

    /**
     * 编辑客户等级
     */
    public static Observable<JsonObject> customerditRank(int rank_id, String rank_name, int rank_discount) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("rank_id", rank_id + "");
        paramsMap.put("rank_name", rank_name);
        paramsMap.put("rank_discount", rank_discount + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.customer_editRank_url, body);
    }

    /**
     * 增加客户等级
     */
    public static Observable<JsonObject> customeraddRank(int rank_discount, String rank_name, List<String> customer_ids) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("rank_discount", rank_discount + "");
        paramsMap.put("rank_name", rank_name);
        paramsMap.put("customer_ids", customer_ids);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.customer_addRank_url, body);
    }

    /**
     * 增加等级所属客户
     */
    public static Observable<JsonObject> customertoRank(List<String> customer_ids, int rank_id) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("customer_ids", customer_ids);
        paramsMap.put("rank_id", rank_id + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.customer_toRank_url, body);
    }

    /**
     * 支付记录(财务详情)导出
     */
    public static Observable<JsonObject> exportPayList(String start_time, String end_time) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("start_time", start_time);
        paramsMap.put("end_time", end_time);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.export_payList_url, body);
    }

    /**
     * 产品销售详情记录
     */
    public static Observable<JsonObject> exportSaleProductList(String start_time) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("start_time", start_time);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.export_saleProductList_url, body);
    }

    /**
     * 产品销售统计
     */
    public static Observable<JsonObject> exportSaleProductCount(String start_time, String end_time) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("start_time", start_time);
        paramsMap.put("end_time", end_time);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.export_saleProductCount_url, body);
    }

    /**
     * 导出记录
     */
    public static Observable<JsonObject> exportLog() {
        HashMap<String, Object> paramsMap = new HashMap<>();
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.export_log_url, body);
    }


    /**
     * 导出状态查询
     */
    public static Observable<JsonObject> exportStatus(int log_id) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("log_id", log_id + "");
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.export_status_url, body);
    }

    /**
     * 贷款申请
     */
    public static Observable<JsonObject> loanAsk(String telephone, String name, String purpose, String identity, String gender) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("telephone", telephone);
        paramsMap.put("name", name);
        paramsMap.put("purpose", purpose);
        paramsMap.put("identity", identity);
        paramsMap.put("gender", gender);
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("getUserInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.loan_ask_url, body);
    }

    /**
     * 修改微信订单
     *
     * @param order
     * @return
     */
    public static Observable<JsonObject> orderConfirm(Order order) {
        HashMap<String, Object> paramsMap = new HashMap<>();
        List<Map<String, Object>> map = new ArrayList<>();
        List<Goods> goods = order.getGoods_list();
        if (goods == null) goods = new ArrayList<>();
        for (Goods good : goods) {
            List<Product> products = good.getProduct_list();
            for (Product p : products) {
                Map<String, Object> data = new HashMap<>();
                data.put("operate_num", p.getNum());
                data.put("product_id", p.getProduct_id());
                if (p.getDefault_price() != -1) {
                    data.put("cost_price", p.getDefault_price());
                    data.put("sell_price", p.getDefault_price());
                }
//                else {
//                    data.put("cost_price", p.getCost_price());
//                    data.put("sell_price", p.getSell_price());
//                }
                map.add(data);
            }
        }
        paramsMap.put("address", order.getAddress());
        paramsMap.put("order_id", order.getOrder_id());
        paramsMap.put("act", order.getAct());
        paramsMap.put("payable", order.getPayable());
        paramsMap.put("product_list", map);
        paramsMap.put("remark", order.getRemark());

        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.order_confirm_url, body);
    }

    /**
     * 删除订单
     *
     * @return
     */
    public static Observable<JsonObject> goodsDel(int goodId) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("goods_id", String.valueOf(goodId));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("setupStore", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService(1).request(Config.goods_del_url, body);
    }
    /**
     * 删除盘点
     *
     * @return
     */
    public static Observable<JsonObject> inventoryInfo(int inventory_id) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("inventory_id", String.valueOf(inventory_id));
        String strEntity = GsonUtils.getJsonStr(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), strEntity);
        Log.e("inventoryInfo", "发送参数：" + strEntity);
        return MyApplication.getInstance().getService().request(Config.inventory_info_url, body);
    }
}
