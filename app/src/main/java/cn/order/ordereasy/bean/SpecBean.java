package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

public class SpecBean extends BaseEntity {
    private List<Spec> spec = new ArrayList<>();

    public List<Spec> getSpec() {
        return spec;
    }

    public void setSpec(List<Spec> spec) {
        this.spec = spec;
    }
}