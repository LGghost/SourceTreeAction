package cn.order.ordereasy.bean;

/**
 * Created by Mr.Pan on 2017/9/27.
 */

public class InventoryEmployee extends BaseEntity {
    private int goods_num=0;
    private int is_boss=0;
    private int operate_num=0;
    private int user_id=0;
    private String avatar="";
    private String avauser_nametar="";

    public int getGoods_num() {
        return goods_num;
    }

    public void setGoods_num(int goods_num) {
        this.goods_num = goods_num;
    }

    public int getIs_boss() {
        return is_boss;
    }

    public void setIs_boss(int is_boss) {
        this.is_boss = is_boss;
    }

    public int getOperate_num() {
        return operate_num;
    }

    public void setOperate_num(int operate_num) {
        this.operate_num = operate_num;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvauser_nametar() {
        return avauser_nametar;
    }

    public void setAvauser_nametar(String avauser_nametar) {
        this.avauser_nametar = avauser_nametar;
    }
}
