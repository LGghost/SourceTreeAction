package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/16.
 */

public class Order extends BaseEntity {
    private int order_id = -1;
    private String order_no;
    private String create_time;
    private int order_num;
    private int owe_num;
    private double payable;//订单实际金额
    private int goods_id;
    private int operate_num;
    private double discount_price;
    private double subtotal;
    private int order_type;
    private int is_deliver;
    private int is_payment;
    private int shop_id;
    private int user_id;
    private int is_close;
    private int customer_id;
    private double order_sum;//订单原始金额
    private int original_order_id = 0;
    private int is_wechat;
    private double sale_amount;
    private String customer_name;
    private String address;
    private String user_name;
    private String remark;
    private List<Goods> goods_list;
    private String telephone;
    private List<String> addres1;
    private int order_status;
    private String act;
    public double getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(double order_sum) {
        this.order_sum = order_sum;
    }

    public int getIs_wechat() {
        return is_wechat;
    }

    public void setIs_wechat(int is_wechat) {
        this.is_wechat = is_wechat;
    }

    public double getSale_amount() {
        return sale_amount;
    }

    public void setSale_amount(double sale_amount) {
        this.sale_amount = sale_amount;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(int is_deliver) {
        this.is_deliver = is_deliver;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_close() {
        return is_close;
    }

    public void setIs_close(int is_close) {
        this.is_close = is_close;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<Goods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<Goods> goods_list) {
        this.goods_list = goods_list;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public int getOwe_num() {
        return owe_num;
    }

    public void setOwe_num(int owe_num) {
        this.owe_num = owe_num;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getOperate_num() {
        return operate_num;
    }

    public void setOperate_num(int operate_num) {
        this.operate_num = operate_num;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public List<String> getAddres1() {
        return addres1;
    }

    public void setAddres1(List<String> addres1) {
        this.addres1 = addres1;
    }

    public int getIs_payment() {
        return is_payment;
    }

    public int getOriginal_order_id() {
        return original_order_id;
    }

    public void setOriginal_order_id(int original_order_id) {
        this.original_order_id = original_order_id;
    }

    public void setIs_payment(int is_payment) {
        this.is_payment = is_payment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }
}
