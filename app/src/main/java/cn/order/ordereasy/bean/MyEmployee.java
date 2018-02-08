package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-9-26.
 */

public class MyEmployee extends BaseEntity {
    public int user_id;
    public String name;
    public String avatar;
    public String telephone;
    public int is_boss;
    public int status;
    public List<String> auth_group_ids = new ArrayList<>();

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getIs_boss() {
        return is_boss;
    }

    public void setIs_boss(int is_boss) {
        this.is_boss = is_boss;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getAuth_group_ids() {
        return auth_group_ids;
    }

    public void setAuth_group_ids(List<String> auth_group_ids) {
        this.auth_group_ids = auth_group_ids;
    }
}
