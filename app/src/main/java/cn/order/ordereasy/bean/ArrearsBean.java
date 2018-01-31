package cn.order.ordereasy.bean;

public class ArrearsBean extends BaseEntity {

    private String create_time;// 创建时间
    private int customer_id;        // 客户id
    private String customer_name;   // 客户姓名
    private String delete_time;                // 删除时间，暂时没有用
    private int is_adjustment;           // 是否是调整欠款
    private int log_id;                // 记录id
    private double money;                // 收款，退款，调整金额等
    private String note;    // 服务器给的备注
    private int order_id = -1;                // 如果是订单产生的记录，则有订单id
    private String remark;                // 调整备注
    private int shop_id;                // 店铺id，暂时没有用
    private double total_debt;            // 累计欠款
    private int type;                    // 类型（1-应收[开单]，2-应退[开退货单或退欠货单]，3-实收[对客户收款]，4-实退[对客户退款]）
    private int user_id;                // 员工id
    private String user_name;        // 员工姓名

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDelete_time() {
        return delete_time;
    }

    public void setDelete_time(String delete_time) {
        this.delete_time = delete_time;
    }

    public int getIs_adjustment() {
        return is_adjustment;
    }

    public void setIs_adjustment(int is_adjustment) {
        this.is_adjustment = is_adjustment;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public double getTotal_debt() {
        return total_debt;
    }

    public void setTotal_debt(double total_debt) {
        this.total_debt = total_debt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
