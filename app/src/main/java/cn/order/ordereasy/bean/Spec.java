package cn.order.ordereasy.bean;

import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/11.
 */

public class Spec extends BaseEntity {
    private int spec_id=0;
    private String name = "";// 规格名称（如”颜色”）
    private List<String> values;

    public int getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(int spec_id) {
        this.spec_id = spec_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}
