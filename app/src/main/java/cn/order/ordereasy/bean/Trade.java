package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

public class Trade extends BaseEntity {
    private String cover = "";
    private int goods_id;
    private String goods_no;
    private int goods_owe_num;
    private int operate_num;
    private int owe_num;
    private int sale_num;
    private List<TradeChild> product_list = new ArrayList<>();
    private int store_num;
    private String title;

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public String getGoods_no() {
        return goods_no;
    }

    public void setGoods_no(String goods_no) {
        this.goods_no = goods_no;
    }

    public int getGoods_owe_num() {
        return goods_owe_num;
    }

    public void setGoods_owe_num(int goods_owe_num) {
        this.goods_owe_num = goods_owe_num;
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

    public List<TradeChild> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<TradeChild> product_list) {
        this.product_list = product_list;
    }

    public int getStore_num() {
        return store_num;
    }

    public void setStore_num(int store_num) {
        this.store_num = store_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }
}