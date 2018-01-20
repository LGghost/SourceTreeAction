package cn.order.ordereasy.bean;

import java.util.List;

public class TradeChild extends BaseEntity {
    private int goods_id;
    private int operate_num;
    private int owe_num;
    private int product_id;
    private int store_num;
    private List<String> spec_data;

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

    public int getOwe_num() {
        return owe_num;
    }

    public void setOwe_num(int owe_num) {
        this.owe_num = owe_num;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getStore_num() {
        return store_num;
    }

    public void setStore_num(int store_num) {
        this.store_num = store_num;
    }

    public List<String> getSpec_data() {
        return spec_data;
    }

    public void setSpec_data(List<String> spec_data) {
        this.spec_data = spec_data;
    }
}