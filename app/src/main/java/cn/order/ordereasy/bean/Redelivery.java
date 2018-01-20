package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mrpan on 2017/9/24.
 */

public class Redelivery {
        private int customer_id = 0;
        private String address = "";
        private int is_deliver = 0;
        private int original_order_id = 0;
        private int order_type = 0;
        private double payable = 0;
        private List<Map<String,Object>> product_list=new ArrayList<>();
        private String remark = "";

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getIs_deliver() {
        return is_deliver;
    }

    public void setIs_deliver(int is_deliver) {
        this.is_deliver = is_deliver;
    }

    public int getOriginal_order_id() {
        return original_order_id;
    }

    public void setOriginal_order_id(int original_order_id) {
        this.original_order_id = original_order_id;
    }

    public int getOrder_type() {
        return order_type;
    }

    public void setOrder_type(int order_type) {
        this.order_type = order_type;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public List<Map<String, Object>> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<Map<String, Object>> product_list) {
        this.product_list = product_list;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
