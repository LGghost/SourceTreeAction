package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/16.
 */

public class StockListBean extends BaseEntity{

    private int operate_id=0;
    private int operate_type=0;
    private String user_name;
    private String create_time;
    private int out_number=0;
    private int in_number=0;
    private int order_id=0;
    private int customer_id=0;
    private String customer_name;
    private String receive_name;
    private int receive_tel=0;
    private String address;
    private String delivery_name;
    private String delivery_no;

    public int getOperate_id() {
        return operate_id;
    }

    public int getOperate_type() {
        return operate_type;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public int getOut_number() {
        return out_number;
    }

    public int getIn_number() {
        return in_number;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public int getReceive_tel() {
        return receive_tel;
    }

    public String getAddress() {
        return address;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setOperate_id(int operate_id) {
        this.operate_id = operate_id;
    }

    public void setOperate_type(int operate_type) {
        this.operate_type = operate_type;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public void setOut_number(int out_number) {
        this.out_number = out_number;
    }

    public void setIn_number(int in_number) {
        this.in_number = in_number;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public void setReceive_tel(int receive_tel) {
        this.receive_tel = receive_tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }
}
