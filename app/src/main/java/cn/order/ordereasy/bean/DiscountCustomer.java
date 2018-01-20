package cn.order.ordereasy.bean;

public class DiscountCustomer extends BaseEntity {
    private int rank_id;
    private String rank_name;
    private int rank_discount;
    private boolean isSelect = false;
    public int getRank_id() {
        return rank_id;
    }

    public void setRank_id(int rank_id) {
        this.rank_id = rank_id;
    }

    public String getRank_name() {
        return rank_name;
    }

    public void setRank_name(String rank_name) {
        this.rank_name = rank_name;
    }

    public int getRank_discount() {
        return rank_discount;
    }

    public void setRank_discount(int rank_discount) {
        this.rank_discount = rank_discount;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
