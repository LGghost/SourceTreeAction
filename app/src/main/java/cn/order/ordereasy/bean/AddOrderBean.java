package cn.order.ordereasy.bean;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/12.
 */

public class AddOrderBean extends BaseEntity {

    /**
     * 开单。新建订单
     * @param shop_id
     * @param user_id
     * @param customer_id
     * @param order_type
     * @param is_deliver
     * @param payable
     * @param address
     * @param product_list
     * @param product_id
     * @param operate_num
     * @return
     */

    private int shop_id=0;
    private int user_id;
    private int customer_id;
    private String order_type;
    private String is_deliver;
    private String payable;
    private String address;

//    private String product_id;
//    private String operate_num;


    private List<ProductListBean>ProductListBean=new ArrayList<>();

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setOrder_type(String order_type) {
        this.order_type = order_type;
    }

    public void setIs_deliver(String is_deliver) {
        this.is_deliver = is_deliver;
    }

    public void setPayable(String payable) {
        this.payable = payable;
    }

    public void setAddress(String address) {
        this.address = address;
    }


//    public void setProduct_id(String product_id) {
//        this.product_id = product_id;
//    }
//
//    public void setOperate_num(String operate_num) {
//        this.operate_num = operate_num;
//    }

    public int getShop_id() {

        return shop_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public String getOrder_type() {
        return order_type;
    }

    public void setProductListBean(List<cn.order.ordereasy.bean.ProductListBean> productListBean) {
        ProductListBean = productListBean;
    }

    public List<cn.order.ordereasy.bean.ProductListBean> getProductListBean() {

        return ProductListBean;
    }

    public String getIs_deliver() {

        return is_deliver;
    }

    public String getPayable() {
        return payable;
    }

    public String getAddress() {
        return address;
    }
//
//    public String getProduct_id() {
//        return product_id;
//    }
//
//    public String getOperate_num() {
//        return operate_num;
//    }
}
