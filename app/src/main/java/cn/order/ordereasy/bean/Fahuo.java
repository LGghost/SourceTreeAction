package cn.order.ordereasy.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrpan on 2017/9/22.
 */

public class Fahuo extends BaseEntity {
        private String address = "";
        private String create_time= "";				// 时间
        private int customer_id=0;				// 客户id
        private String customer_name = "";		// 客户姓名
        private String delivery_name = "";			// 物流公司
        private String delivery_no = "";					// 物流单号
        private List<Goods> goods_list=new ArrayList<>();
        private int in_number=0;			// 如果为入库类操作，则用这个数量
        private int operate_id=0;			// 库存操作id
        private int operate_type=0;				// 库存操作类型（0-所有操作，1-入库，2-出库，3-调整，4-发货，5-退货，6-盘点调整，7-退欠货）
        private int order_id=0;				// 订单id
        private int out_number=0;				// 如果为出库类操作，则用这个数量
        private String receive_name ="";		// 收货人姓名
        private String receive_tel= "";		// 收货人电话
        private String remark = "";		// 备注
        private int shop_id=0;				// 店铺id
        private int user_id=0;				// 开单人id
        private String user_name ="";		// 开单人姓名

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

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

    public String getDelivery_name() {
        return delivery_name;
    }

    public void setDelivery_name(String delivery_name) {
        this.delivery_name = delivery_name;
    }

    public String getDelivery_no() {
        return delivery_no;
    }

    public void setDelivery_no(String delivery_no) {
        this.delivery_no = delivery_no;
    }

    public List<Goods> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<Goods> goods_list) {
        this.goods_list = goods_list;
    }

    public int getIn_number() {
        return in_number;
    }

    public void setIn_number(int in_number) {
        this.in_number = in_number;
    }

    public int getOperate_id() {
        return operate_id;
    }

    public void setOperate_id(int operate_id) {
        this.operate_id = operate_id;
    }

    public int getOperate_type() {
        return operate_type;
    }

    public void setOperate_type(int operate_type) {
        this.operate_type = operate_type;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getOut_number() {
        return out_number;
    }

    public void setOut_number(int out_number) {
        this.out_number = out_number;
    }

    public String getReceive_name() {
        return receive_name;
    }

    public void setReceive_name(String receive_name) {
        this.receive_name = receive_name;
    }

    public String getReceive_tel() {
        return receive_tel;
    }

    public void setReceive_tel(String receive_tel) {
        this.receive_tel = receive_tel;
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
