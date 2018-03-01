package cn.order.ordereasy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.DiscountCustomer;
import cn.order.ordereasy.bean.Fahuo;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.bean.TopicLabelObject;

public class DataStorageUtils {
    //上货列表
    private List<Goods> shelvesGoods = new ArrayList<>();
    //上货分类列表
    private List<TopicLabelObject> genreGoods = new ArrayList<>();
    //订单列表
    private List<OrderList> orderLists = new ArrayList<>();
    //员工信息列表
    private List<TopicLabelObject> yuangongLists = new ArrayList<>();
    //订单的总页数
    private int pageTotal = 1;
    //客户列表
    private List<Customer> customerLists = new ArrayList<>();
    private int orderPageTotal = 1;
    //客户主页订单
    private List<OrderList> chOrderLists = new ArrayList<>();
    private int moneyPageTotal = 1;
    //客户主页收银
    private List<Money> chMoneyLists = new ArrayList<>();
    private int fahuoPageTotal = 1;
    //客户主页发货记录
    private List<Fahuo> chFahuoLists = new ArrayList<>();
    //规格列表
    private List<Map<String, Object>> guigeLists = new ArrayList<>();

    //选择客户列表
    private List<Customer> selectCustomer = new ArrayList<>();

    //客户分类列表
    private List<DiscountCustomer> discountCustomers = new ArrayList<>();
    private Customer retailCustomer;
    private boolean isShanghuo = false;
    private boolean isBilling = false;
    private boolean isCustomer = false;
    private boolean isAddCustomer = false;
    private boolean isPurchaseBilling = false;//采购开单
    private DataStorageUtils() {
    }

    private static DataStorageUtils dataStorage = null;

    public static DataStorageUtils getInstance() {
        if (dataStorage == null)
            dataStorage = new DataStorageUtils();
        return dataStorage;
    }

    public List<Goods> getShelvesGoods() {
        return shelvesGoods;
    }

    public void setShelvesGoods(List<Goods> shelvesGoods) {
        this.shelvesGoods = shelvesGoods;
    }

    public List<TopicLabelObject> getGenreGoods() {
        return genreGoods;
    }

    public void setGenreGoods(List<TopicLabelObject> genreGoods) {
        this.genreGoods = genreGoods;
    }

    public List<OrderList> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(List<OrderList> orderLists) {
        this.orderLists = orderLists;
    }

    public List<TopicLabelObject> getYuangongLists() {
        return yuangongLists;
    }

    public void setYuangongLists(List<TopicLabelObject> yuangongLists) {
        this.yuangongLists = yuangongLists;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public List<Customer> getCustomerLists() {
        return customerLists;
    }

    public void setCustomerLists(List<Customer> customerLists) {
        this.customerLists = customerLists;
    }

    public boolean isShanghuo() {
        return isShanghuo;
    }

    public void setShanghuo(boolean shanghuo) {
        isShanghuo = shanghuo;
    }

    public boolean isBilling() {
        return isBilling;
    }

    public void setBilling(boolean billing) {
        isBilling = billing;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public int getOrderPageTotal() {
        return orderPageTotal;
    }

    public void setOrderPageTotal(int orderPageTotal) {
        this.orderPageTotal = orderPageTotal;
    }

    public List<OrderList> getChOrderLists() {
        return chOrderLists;
    }

    public void setChOrderLists(List<OrderList> chOrderLists) {
        this.chOrderLists = chOrderLists;
    }

    public int getMoneyPageTotal() {
        return moneyPageTotal;
    }

    public void setMoneyPageTotal(int moneyPageTotal) {
        this.moneyPageTotal = moneyPageTotal;
    }

    public List<Money> getChMoneyLists() {
        return chMoneyLists;
    }

    public void setChMoneyLists(List<Money> chMoneyLists) {
        this.chMoneyLists = chMoneyLists;
    }

    public int getFahuoPageTotal() {
        return fahuoPageTotal;
    }

    public void setFahuoPageTotal(int fahuoPageTotal) {
        this.fahuoPageTotal = fahuoPageTotal;
    }

    public List<Fahuo> getChFahuoLists() {
        return chFahuoLists;
    }

    public void setChFahuoLists(List<Fahuo> chFahuoLists) {
        this.chFahuoLists = chFahuoLists;
    }

    public List<Map<String, Object>> getGuigeLists() {
        return guigeLists;
    }

    public void setGuigeLists(List<Map<String, Object>> guigeLists) {
        this.guigeLists = guigeLists;
    }

    public List<Customer> getSelectCustomer() {
        return selectCustomer;
    }

    public void setSelectCustomer(List<Customer> selectCustomer) {
        this.selectCustomer = selectCustomer;
    }

    public List<DiscountCustomer> getDiscountCustomers() {
        return discountCustomers;
    }

    public void setDiscountCustomers(List<DiscountCustomer> discountCustomers) {
        this.discountCustomers = discountCustomers;
    }

    public boolean isAddCustomer() {
        return isAddCustomer;
    }

    public void setAddCustomer(boolean addCustomer) {
        isAddCustomer = addCustomer;
    }

    public Customer getRetailCustomer() {
        return retailCustomer;
    }

    public void setRetailCustomer(Customer retailCustomer) {
        this.retailCustomer = retailCustomer;
    }

    public boolean isPurchaseBilling() {
        return isPurchaseBilling;
    }

    public void setPurchaseBilling(boolean purchaseBilling) {
        isPurchaseBilling = purchaseBilling;
    }

    public void cleanCustomerHomePage() {
        chOrderLists.clear();
        chMoneyLists.clear();
        chFahuoLists.clear();
    }

    public void cleanSelectCustomer() {
        selectCustomer.clear();
    }

    public void cleanData() {
        pageTotal = 1;
        orderPageTotal = 1;
        moneyPageTotal = 1;
        fahuoPageTotal = 1;
        shelvesGoods.clear();
        genreGoods.clear();
        orderLists.clear();
        yuangongLists.clear();
        customerLists.clear();
        chOrderLists.clear();
        chMoneyLists.clear();
        chFahuoLists.clear();
        guigeLists.clear();
    }
}