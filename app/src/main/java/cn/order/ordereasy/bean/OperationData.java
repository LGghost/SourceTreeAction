package cn.order.ordereasy.bean;

public class OperationData {
    private int operate_num;            // 退欠货数量
    private int order_id; // 订单id
    private long log_id;// 操作id
    private double money; //收款金额
    private double payment_way;
    private double payable;//改价金额
    private int order_num;//开单数量
    private double order_sum;//开单金额
    private int operate_id;

    public int getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(int operate_id) {
        this.operate_id = operate_id;
    }

    public int getOperate_num() {
        return operate_num;
    }

    public void setOperate_num(int operate_num) {
        this.operate_num = operate_num;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public long getLog_id() {
        return log_id;
    }

    public void setLog_id(long log_id) {
        this.log_id = log_id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getPayment_way() {
        return payment_way;
    }

    public void setPayment_way(double payment_way) {
        this.payment_way = payment_way;
    }

    public double getPayable() {
        return payable;
    }

    public void setPayable(double payable) {
        this.payable = payable;
    }

    public int getOrder_num() {
        return order_num;
    }

    public void setOrder_num(int order_num) {
        this.order_num = order_num;
    }

    public double getOrder_sum() {
        return order_sum;
    }

    public void setOrder_sum(double order_sum) {
        this.order_sum = order_sum;
    }
}