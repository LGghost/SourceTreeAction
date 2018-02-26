package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

public class SupplierBean extends BaseEntity {
    private int supplier_id;//供应商ID
    private String name;//供应商名称
    private String is_retail;//是否是零售商
    private String contact;//负责人
    private String mobile;//移动电话
    private String tel;//电话
    private double debt;//欠供应商款
    private String province;//省份
    private String city;//城市
    private String district;//地区
    private String address;//地址
    private String remark;
    public int getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(int supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIs_retail() {
        return is_retail;
    }

    public void setIs_retail(String is_retail) {
        this.is_retail = is_retail;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public double getDebt() {
        return debt;
    }

    public void setDebt(double debt) {
        this.debt = debt;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRemarks() {
        return remark;
    }

    public void setRemarks(String remarks) {
        this.remark = remarks;
    }

    public static List likeString1(List<SupplierBean> data, String likename) {
        List<SupplierBean> list = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getName().contains(likename) || data.get(i).getTel().contains(likename) ) {
                list.add(data.get(i));
            }
        }
        return list;
    }
}
