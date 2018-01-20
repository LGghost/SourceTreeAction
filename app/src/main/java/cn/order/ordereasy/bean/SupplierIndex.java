package cn.order.ordereasy.bean;

public class SupplierIndex {
    private int type;
    private int sectionPosition;
    private int position;
    private String index;
    private SupplierBean supplierBean;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSectionPosition() {
        return sectionPosition;
    }

    public void setSectionPosition(int sectionPosition) {
        this.sectionPosition = sectionPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public SupplierBean getSupplierBean() {
        return supplierBean;
    }

    public void setSupplierBean(SupplierBean supplierBean) {
        this.supplierBean = supplierBean;
    }
}
