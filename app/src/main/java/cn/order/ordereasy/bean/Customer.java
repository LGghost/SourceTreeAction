package cn.order.ordereasy.bean;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/16.
 */

public class Customer extends BaseEntity {
    private String customer_name = "";
    private int customer_id = 0;
    private int sale_num = 0;
    private int is_retail = 0;
    private String telephone = "";
    private int level = 0;
    private double receivable = 0;
    private double trade_money = 0;
    private String name;
    private int sales_sum;
    private int owe_sum;
    private String topic = "";
    private List<String> address = new ArrayList<>();
    private int order_id;
    private int rank_discount = 100;        // 分类等级折扣
    private int rank_id = 0;            // 分类等级id
    private String rank_name = "";        // 分类等级名称

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getReceivable() {
        return receivable;
    }

    public void setReceivable(double receivable) {
        this.receivable = receivable;
    }

    public double getTrade_money() {
        return trade_money;
    }

    public void setTrade_money(double trade_money) {
        this.trade_money = trade_money;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSales_sum() {
        return sales_sum;
    }

    public void setSales_sum(int sales_sum) {
        this.sales_sum = sales_sum;
    }

    public int getOwe_sum() {
        return owe_sum;
    }

    public void setOwe_sum(int owe_sum) {
        this.owe_sum = owe_sum;
    }

    public List<String> getAddress() {
        return address;
    }

    public void setAddress(List<String> address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getSale_num() {
        return sale_num;
    }

    public void setSales_num(int sales_num) {
        this.sale_num = sales_num;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getRank_discount() {
        return rank_discount;
    }

    public void setRank_discount(int rank_discount) {
        this.rank_discount = rank_discount;
    }

    public int getRank_id() {
        return rank_id;
    }

    public void setRank_id(int rank_id) {
        this.rank_id = rank_id;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public int getIs_retail() {
        return is_retail;
    }

    public void setIs_retail(int is_retail) {
        this.is_retail = is_retail;
    }

    //模糊查询
    public static List likeString(List<Customer> data, String likename) {
        List<Customer> list = data;
        for (int i = 0; i < list.size(); i++) {
            if (((Customer) (list.get(i))).getName().indexOf(likename) <= -1)
                list.remove(i);
        }
        return list;
    }

    public static List likeString2(List<Customer> data, String likename) {
        List<Customer> list = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().contains(likename) || data.get(i).getCustomer_name().contains(likename) || data.get(i).getTelephone().contains(likename)) {
                list.add(data.get(i));
            }
        }
        return list;
    }
}
