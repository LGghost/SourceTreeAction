package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by mrpan on 2017/9/24.
 */

public class Delivery {
        private int customer_id = 0;
        private String delivery_name = "";
        private String delivery_no = "";
        private int operate_id = 0;
        private int operate_type = 0;
        private int order_id = 0;
        private List<Map<String,Object>> product_list=new ArrayList<>();
        private String remark = "";

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public int getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(int operate_id) {
        this.operate_id = operate_id;
    }

    public int getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(int operate_type) {
        this.operate_type = operate_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
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
