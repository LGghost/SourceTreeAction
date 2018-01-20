package cn.order.ordereasy.bean;

/**
 * Created by Administrator on 2015/5/28.
 * <p>
 * 标签实体类
 */
public class TopicLabelObject {

    public int id;
    public int count;
    public String name;
    public int isSelect;

    public TopicLabelObject(int id, int count, String name, int isSelect) {
        this.id = id;
        this.count = count;
        this.name = name;
        this.isSelect = isSelect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }
}
