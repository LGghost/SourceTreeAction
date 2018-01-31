package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

public class SupplierBean extends BaseEntity {
    private int order_id;
    private String name;
    private String user;
    private String phone;
    private String call;
    private double arrears;
    private String address;
    private String remarks;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public double getArrears() {
        return arrears;
    }

    public void setArrears(double arrears) {
        this.arrears = arrears;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public static List likeString1(List<SupplierBean> data, String likename) {
        List<SupplierBean> list = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().contains(likename) || data.get(i).getPhone().contains(likename) ) {
                list.add(data.get(i));
            }
        }
        return list;
    }
}
