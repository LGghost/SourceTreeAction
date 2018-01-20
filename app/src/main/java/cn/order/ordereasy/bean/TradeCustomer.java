package cn.order.ordereasy.bean;

public class TradeCustomer {
    private int customer_id;
    private Double trade_sum;
    private int sale_num;
    private String customer_name;
    private int level;

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public Double getTrade_sum() {
        return trade_sum;
    }

    public void setTrade_sum(Double trade_sum) {
        this.trade_sum = trade_sum;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSale_num(int sale_num) {
        this.sale_num = sale_num;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}