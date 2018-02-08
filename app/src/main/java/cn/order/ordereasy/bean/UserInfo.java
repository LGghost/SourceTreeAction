package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-9-27.
 */
public class UserInfo extends BaseEntity {
    public int user_id;
    public String name;
    public int is_boss;
    public int shop_id;
    public String telephone;
    public int status;
    public String token;
    public int expire;
    public String login_ip;
    public int last_login;
    public String avatar;
    public int last_time;
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

    public int getIs_boss() {
        return is_boss;
    }

    public void setIs_boss(int is_boss) {
        this.is_boss = is_boss;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    public String getLogin_ip() {
        return login_ip;
    }

    public void setLogin_ip(String login_ip) {
        this.login_ip = login_ip;
    }

    public int getLast_login() {
        return last_login;
    }

    public void setLast_login(int last_login) {
        this.last_login = last_login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLast_time() {
        return last_time;
    }

    public void setLast_time(int last_time) {
        this.last_time = last_time;
    }

    public List<String> getAuth_group_ids() {
        return auth_group_ids;
    }

    public void setAuth_group_ids(List<String> auth_group_ids) {
        this.auth_group_ids = auth_group_ids;
    }
}
