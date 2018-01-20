package cn.order.ordereasy.bean;

public class OrderOperation extends BaseEntity {
    private String create_time;        // 操作时间
    private OperationData log_data;
    private int log_id;                // 操作id
    private int log_type;                // 操作类型（0-开单，1-改价，2-收款，3-退款，4-发货，5-退欠货，6-关闭订单）
    private long order_id;            // 订单id
    private int shop_id;
    private int user_id;
    private String user_name;        // 操作员工姓名


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public OperationData getLog_data() {
        return log_data;
    }

    public void setLog_data(OperationData log_data) {
        this.log_data = log_data;
    }

    public int getLog_id() {
        return log_id;
    }

    public void setLog_id(int log_id) {
        this.log_id = log_id;
    }

    public int getLog_type() {
        return log_type;
    }

    public void setLog_type(int log_type) {
        this.log_type = log_type;
    }

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
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