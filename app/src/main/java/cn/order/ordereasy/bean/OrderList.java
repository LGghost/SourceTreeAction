package cn.order.ordereasy.bean;

import com.umeng.socialize.media.Base;

/**
 * Created by Mr.Pan on 2017/9/28.
 */

public class OrderList extends BaseEntity{

    private int order_id;
    private String customer_name;
    private int customer_id;
    private String order_no;
    private int order_type;
    private int is_close;
    private int order_num;
    private double order_sum;
    private int owe_num;
    private int return_num;
    private String create_time;
    private String remark;
    private String receive_name;
    private String receive_tel;
    private String address;
    private double payable;
    private int is_deliver;
    private int is_wechat;
    private int order_status;

    //采购订单
    private String supplier_name;
    private int supplier_id;
    private int user_id;
    private String user_name;

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getOrder_no() {
        return order_no;
    }

    public void setOrder_no(String order_no) {
        this.order_no = order_no;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public int getIs_close() {
        return is_close;
    }

    public void setIs_close(int is_close) {
        this.is_close = is_close;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public double getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(double order_sum) {
        this.order_sum = order_sum;
    }

    public int getOwe_num() {
        return owe_num;
    }

    public void setOwe_num(int owe_num) {
        this.owe_num = owe_num;
    }

    public int getReturn_num() {
        return return_num;
    }

    public void setReturn_num(int return_num) {
        this.return_num = return_num;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getReceive_tel() {
        return receive_tel;
    }

    public void setReceive_tel(String receive_tel) {
        this.receive_tel = receive_tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public int getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(int is_deliver) {
        this.is_deliver = is_deliver;
    }

    public int getIs_wechat() {
        return is_wechat;
    }

    public void setIs_wechat(int is_wechat) {
        this.is_wechat = is_wechat;
    }

    public int getOrder_status() {
        return order_status;
    }

    public void setOrder_status(int order_status) {
        this.order_status = order_status;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
