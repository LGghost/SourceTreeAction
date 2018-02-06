package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.presenter.BasePresenter;

/**
 * Created by Mr.Pan on 2017/9/11.
 */

public class Goods extends BaseEntity {
    private int category_id = 0;
    private String category_name = "";
    private String cover = "";
    private int is_enable_stock_warn;
    private int max_stock_warn_num;
    private int min_stock_warn_num;

    private String create_time = "";
    private int goods_id = 0;
    private int goods_num = 0;
    private int is_hidden_price = 0;// 是否隐藏价格，0-不隐藏，1-隐藏
    private int is_hidden_store = 0;
    private int is_hidden_sales_num = 0;
    private Object images;

    private double min_price = 0;// 该货品的所有规格的最低价格，浮点型
    private double max_price = 0;// 该货品的所有规格的最高价格，浮点型
    private int sale_num = 0;
    private int owe_num = 0;
    private List<Spec> spec = new ArrayList<>();
    private int status = 1;       // 上下架状态（0-下架状态，1-上架状态）
    private String title = "";   // 货品名称
    private String update_time = "";   // 货品最新更新时间
    private int isSelected = 0;

    private List<Product> product_list = new ArrayList<>();

    private String description = "";
    private String goods_no = "";
    private int num = 0;
    private int store_num = 0;
    private double price = 0;
    private double discount_price = 0;

    public int getIs_enable_stock_warn() {
        return is_enable_stock_warn;
    }

    public void setIs_enable_stock_warn(int is_enable_stock_warn) {
        this.is_enable_stock_warn = is_enable_stock_warn;
    }

    public int getMax_stock_warn_num() {
        return max_stock_warn_num;
    }

    public void setMax_stock_warn_num(int max_stock_warn_num) {
        this.max_stock_warn_num = max_stock_warn_num;
    }

    public int getMin_stock_warn_num() {
        return min_stock_warn_num;
    }

    public void setMin_stock_warn_num(int min_stock_warn_num) {
        this.min_stock_warn_num = min_stock_warn_num;
    }

    public int getStore_num() {
        return store_num;
    }

    public void setStore_num(int store_num) {
        this.store_num = store_num;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    /*public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }*/

    public Object getImages() {
        return images;
    }

    public void setImages(Object images) {
        this.images = images;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public double getMin_price() {
        return min_price;
    }

    public void setMin_price(double min_price) {
        this.min_price = min_price;
    }

    public double getMax_price() {
        return max_price;
    }

    public void setMax_price(double max_price) {
        this.max_price = max_price;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public int getOwe_num() {
        return owe_num;
    }

    public void setOwe_num(int owe_num) {
        this.owe_num = owe_num;
    }

    public List<Spec> getSpec() {
        return spec;
    }

    public void setSpec(List<Spec> spec) {
        this.spec = spec;
    }

    public List<Product> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<Product> product_list) {
        this.product_list = product_list;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGoods_no() {
        return goods_no;
    }

    public void setGoods_no(String goods_no) {
        this.goods_no = goods_no;
    }

    public int getIs_hidden_price() {
        return is_hidden_price;
    }

    public void setIs_hidden_price(int is_hidden_price) {
        this.is_hidden_price = is_hidden_price;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    public int getIs_hidden_store() {
        return is_hidden_store;
    }

    public void setIs_hidden_store(int is_hidden_store) {
        this.is_hidden_store = is_hidden_store;
    }

    public int getIs_hidden_sales_num() {
        return is_hidden_sales_num;
    }

    public void setIs_hidden_sales_num(int is_hidden_sales_num) {
        this.is_hidden_sales_num = is_hidden_sales_num;
    }

    //模糊查询
    public static List likeString(List<Goods> data, String likename) {
        List<Goods> list = new ArrayList<>();
        list.addAll(data);
        for (int i = 0; i < list.size(); i++) {
            if (!((Goods) (list.get(i))).getTitle().contains(likename) || !((Goods) (list.get(i))).getGoods_no().contains(likename))
                list.remove(i);
        }
        return list;
    }

    public static List likeString2(List<Goods> data, String likename) {
        List<Goods> list = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getTitle().contains(likename) || data.get(i).getGoods_no().contains(likename)) {
                list.add(data.get(i));
            }
        }
        return list;
    }
}
