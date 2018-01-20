package cn.order.ordereasy.bean;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ProductListBean extends BaseEntity {


    private String product_id;
    private String operate_num;


    public String getProduct_id() {
        return product_id;
    }

    public String getOperate_num() {
        return operate_num;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public void setOperate_num(String operate_num) {
        this.operate_num = operate_num;
    }
}
