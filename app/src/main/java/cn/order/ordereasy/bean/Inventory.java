package cn.order.ordereasy.bean;

/**
 * Created by mrpan on 2017/9/26.
 */

public class Inventory extends BaseEntity{
    private int inventory_id=0;
    private int shop_id=0;
    private int user_id=0;
    private int is_complete=0;
    private int operate_id=0;
    private int is_adjust=0;
    private int is_boss=0;
    private int store_num=0;
    private String remark="";
    private String create_time="";
    private String avatar="";
    private String user_name="";

    public int getInventory_id() {
        return inventory_id;
    }

    public void setInventory_id(int inventory_id) {
        this.inventory_id = inventory_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getIs_complete() {
        return is_complete;
    }

    public void setIs_complete(int is_complete) {
        this.is_complete = is_complete;
    }

    public int getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(int operate_id) {
        this.operate_id = operate_id;
    }

    public int getIs_adjust() {
        return is_adjust;
    }

    public void setIs_adjust(int is_adjust) {
        this.is_adjust = is_adjust;
    }

    public int getIs_boss() {
        return is_boss;
    }

    public void setIs_boss(int is_boss) {
        this.is_boss = is_boss;
    }

    public int getStore_num() {
        return store_num;
    }

    public void setStore_num(int store_num) {
        this.store_num = store_num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
