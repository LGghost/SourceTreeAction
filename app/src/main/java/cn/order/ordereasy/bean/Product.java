package cn.order.ordereasy.bean;

import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/11.
 */

public class Product extends BaseEntity{
    private String product_no ="";
    private List<String> spec_data;
    private double sell_price =0;// 销售价格
    private double market_price=0;// 市场价，暂时没有用
    private double cost_price=0; // 成本价
    private double weight =0;
    private double discount_price =0;
    private int store_num=0;// 库存数，数字
    private int sale_num=0;// 销售数量
    private int owe_num=0;// 欠货数
    private int product_id=0;// Product Id，数字
    private double trade_sum=0;// 交易额，浮点型，保留两位小数
    private int num=0;
    private int pos=0;
    private double default_price = -1;
    private int operate_num=0;

    public int getOperate_num() {
        return operate_num;
    }

    public void setOperate_num(int operate_num) {
        this.operate_num = operate_num;
    }

    public double getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(double discount_price) {
        this.discount_price = discount_price;
    }

    private double price=0;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }



    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
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

    public double getTrade_sum() {
        return trade_sum;
    }

    public void setTrade_sum(double trade_sum) {
        this.trade_sum = trade_sum;
    }

    public String getProduct_no() {
        return product_no;
    }

    public void setProduct_no(String product_no) {
        this.product_no = product_no;
    }

    public List<String> getSpec_data() {
        return spec_data;
    }

    public void setSpec_data(List<String> spec_data) {
        this.spec_data = spec_data;
    }

    public double getSell_price() {
        return sell_price;
    }

    public void setSell_price(double sell_price) {
        this.sell_price = sell_price;
    }

    public double getMarket_price() {
        return market_price;
    }

    public void setMarket_price(double market_price) {
        this.market_price = market_price;
    }

    public double getCost_price() {
        return cost_price;
    }

    public void setCost_price(double cost_price) {
        this.cost_price = cost_price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getStore_num() {
        return store_num;
    }

    public void setStore_num(int store_num) {
        this.store_num = store_num;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public double getDefault_price() {
        return default_price;
    }

    public void setDefault_price(double default_price) {
        this.default_price = default_price;
    }
}
