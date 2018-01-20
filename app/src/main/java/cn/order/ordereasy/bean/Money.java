package cn.order.ordereasy.bean;

/**
 * Created by mrpan on 2017/9/22.
 */

public class Money extends BaseEntity {
    private int order_id=0;
    private String customer_name ="";
    private int customer_id=0;
    private String order_no="";
    private int order_type=0;
    private int is_close=0;
    private int order_num=0;
    private int owe_num=0;
    private int return_num=0;
    private String create_time="";
    private String remark="";
    private String receive_name="";
    private String receive_tel="";
    private String address="";
    private String user_name="";
    private int payment_type;
    private int payment_way;
    private int order_sum=0;
    private double payable=0;
    private int is_wechat=0;
    private int is_deliver=0;
    private double money=0;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    public int getPayment_way() {
        return payment_way;
    }

    public void setPayment_way(int payment_way) {
        this.payment_way = payment_way;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

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

    public int getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(int order_sum) {
        this.order_sum = order_sum;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public int getIs_wechat() {
        return is_wechat;
    }

    public void setIs_wechat(int is_wechat) {
        this.is_wechat = is_wechat;
    }

    public int getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(int is_deliver) {
        this.is_deliver = is_deliver;
    }
}
