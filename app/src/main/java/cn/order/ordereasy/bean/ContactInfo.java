package cn.order.ordereasy.bean;

/**
 * Created by mrpan on 2017/10/14.
 */

public class ContactInfo {
    private String name;// 姓名
    private String number;// 电话号码
    private String topic;
    private int isCheck = 0;

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
