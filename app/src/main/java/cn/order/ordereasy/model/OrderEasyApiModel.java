package cn.order.ordereasy.model;

import java.util.List;
import java.util.Map;

import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Redelivery;
import rx.Subscription;

public interface OrderEasyApiModel {


    /**
     * 登录
     *
     * @param username
     * @param password
     * @return
     */
    Subscription login(String username, String password);


    //注册
    Subscription register(String telephone, String password, String smscode, String code, String yaoqcode);

    //忘记密码
    Subscription forgot(String telephone, String password, String smscode, String code);

    //获取短信验证码
    Subscription getSmsCode(String telephone, String type, String code);

    //获取验证码
    Subscription getCode();

    Subscription operateRecordDetail(int operate_id);

    //设置店铺
    Subscription setupStore(String mobile, String name, String province, String city, String district, String address, String telephone, String wechat, String notice);

    //店铺信息
    Subscription getStoreInfo();

    Subscription uploadStoreImg(String type, String file);

    //上传商品图片
    Subscription uploadShoopImg(String file);

    //获取分类信息
    Subscription getCategoryInfo();

    //新增分类
    Subscription addCategoryInfo(String name);

    //修改分类
    Subscription updateCategoryInfo(int cateId, String name);

    //删除分类
    Subscription delCategoryInfo(int cateId);

    //获取规格信息
    Subscription getSpecInfo();

    //新增规格
    Subscription addSpecInfo(String name);

    //新增规格属性
    Subscription addSpecValueInfo(int sepcId, String value);

    //删除规格
    Subscription delSpecInfo(int cateId);

    //删除规格属性信息
    Subscription delSpecValueInfo(int cateId, String value);

    //获取商品列表
    Subscription getGoodsList();

    //新增商品
    Subscription addGoods(Goods good);

    //修改商品
    Subscription updateGoods(Goods good);

    //获取商品详情
    Subscription getGoodsInfo(int goodId);

    //商品欠货订单
    Subscription getOweGoodsList(int goodId);

    //购买客户
    Subscription getGoodsCustomers(int goodId);

    //商品上下架
    Subscription makeGoodsStatus(int goodId, int status);

    //商品购买趋势
    Subscription getGoodsBuyHistories(int goodId, int days, String date);

    //开单
    Subscription Add_Odder(Order order);

    /**
     * 关闭订单
     *
     * @param orderId
     */
    Subscription closeOrder(int orderId);

    /**
     * 获取订单列表
     *
     * @return
     */
    Subscription getOrdersList(int page, String filter_type, String user_id, String start_time, String end_time);

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    Subscription getOrderInfo(int orderId);

    /**
     * 获取订单操作日志
     *
     * @param orderId
     * @return
     */
    Subscription getOrderLogs(int orderId);


    /**
     * 获取欠货订单
     *
     * @return
     */
    Subscription getOweOrdersList(int custId, int order_id);

    /**
     * 更改订单收货地址
     *
     * @param orderId
     * @param addr
     * @return
     */
    Subscription updateOrderAddr(int orderId, String addr, int type);

    /**
     * 客户列表
     *
     * @return
     */
    Subscription getCustomerList();

    /**
     * 客户列表
     *
     * @return
     */
    Subscription getCustomerList1();

    /**
     * 添加客户
     *
     * @param customer
     * @return
     */
    Subscription addCustomer(Customer customer);

    /**
     * 修改客户
     *
     * @param customer
     * @return
     */
    Subscription updateCustomer(Customer customer);

    /**
     * 删除客户
     *
     * @param customerId
     * @return
     */
    Subscription delCustomer(int customerId);

    /**
     * 批量导入
     *
     * @param customerList
     * @return
     */
    Subscription importCustomers(List<Customer> customerList);

    /**
     * 获取客户信息
     *
     * @param customerId
     * @return
     */
    Subscription getCustomerInfo(int customerId);

    /**
     * 客户销售商品
     *
     * @param customer_id
     * @param is_owe
     * @return
     */
    Subscription getCustSalesGoods(int customer_id, int is_owe);

    /**
     * 客户欠款记录
     *
     * @param customer_id
     * @return
     */
    Subscription getCustomerOweLog(int customer_id);

    /**
     * 修改客户欠款
     *
     * @param customer_id
     * @param money
     * @param remark
     * @return
     */
    Subscription adjustmentOweInfo(int customer_id, double money, String remark);

    /**
     * 获取公开账单
     *
     * @param sign
     * @param customer_id
     * @param shop_id
     * @return
     */
    Subscription getCustomerAccountInfo(String sign, int customer_id, int shop_id);

    /**
     * 库存列表
     *
     * @param operate_type
     * @return
     */
    Subscription getStockList(int operate_type);

    /**
     * 客户主页--订单记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    Subscription getRecordList(int customer_id, String page);

    /**
     * 搜索订单
     *
     * @param page
     * @param keyword
     * @return
     */
    Subscription searchRecordList(int page, String keyword);

    /**
     * 客户主页--收银记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    Subscription getOrderRecordLlist(int customer_id, String page);

    /**
     * 客户主页--操作（发货）记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    Subscription getOperationRecordList(int customer_id, String page);

    /**
     * 库存调整记录
     *
     * @param page
     * @return
     */
    Subscription getOperationRecordList(int page, int operate_type, int user_id, String begindate, String enddate);

    /**
     * 收款
     *
     * @param customer_id
     * @param payment_type
     * @param cash
     * @param wechat
     * @param alipay
     * @param bank_card
     * @param other
     * @return
     */
    Subscription addOrderPay(int customer_id, int payment_type, double cash, double wechat, double alipay, double bank_card, double other);


    /**
     * 日报
     *
     * @param date
     * @return
     */
    Subscription getDaily(String date);

    /**
     * 退货
     *
     * @param redelivery
     * @return
     */
    Subscription redeliver(Redelivery redelivery);

    /**
     * 出库入库
     *
     * @param delivery
     * @return
     */
    Subscription deliver(Delivery delivery);

    /**
     * 发货
     *
     * @param deliveries
     * @return
     */
    Subscription delivers(List<Delivery> deliveries);

    /**
     * 今日交易额，今日开单数，当前欠款数，当前欠货数的数据显示
     *
     * @param
     * @return
     */
    Subscription getNumToday(int type);

    /**
     * 今日交易额，今日开单数，当前欠款数，当前欠货数的数据显示
     *
     * @param
     * @return
     */
    Subscription getNumToday2(int type);

    /**
     * 获取员工数据
     *
     * @param
     * @return
     */
    Subscription getEmployeeNum(int type);

    /**
     * 添加员工
     */
    Subscription addEmployeeNum(String name, String tel, String pass, List<Integer> list);

    /**
     * 获取盘点单列表
     *
     * @return
     */
    Subscription getInventoryList(int page);

    /**
     * 盘点详情
     *
     * @param inventory_id
     * @return
     */
    Subscription getInventoryInfo(int inventory_id, int user_id);

    /**
     * 盘点详情
     *
     * @param inventory_id
     * @return
     */
    Subscription getInventory(int inventory_id);

    /**
     * 新建盘点
     *
     * @return
     */
    Subscription addInventory();

    /**
     * 提交盘点单
     *
     * @param inventory_id
     * @param remark
     * @param is_adjust
     * @return
     */
    Subscription commitInventoryInfo(int inventory_id, String remark, int is_adjust);

    /**
     * 加入或继续盘点
     *
     * @param inventoryInfo
     * @return
     */
    Subscription saveInventoryInfo(List<Map<String, Object>> inventoryInfo);

    /**
     * 获取盘点结果
     *
     * @param inventory_id
     * @param user_id
     * @param page
     * @return
     */
    Subscription getInventoryInfo(int inventory_id, int user_id, int page);

    /**
     * 删除员工
     */
    Subscription deleteEmployee(String user_id);

    /**
     * 获取员工信息
     */
    Subscription getUserInfo();

    /**
     * 修改员工信息
     */
    Subscription updateUserInfo(int id, String name, List<String> list);

    /**
     * 修改员工密码
     */
    Subscription updateUserPass(int id, String pass, String pass1);

    /**
     * 账本页面 次数方法
     */
    Subscription booking2(int type, int page);

    /**
     * 账本页面 账本方法
     */
    Subscription booking4(int days, String starttime, String endtime);

    /**
     * 账本页面 账本方法
     */
    Subscription booking3(int days, String starttime, String endtime);

    /**
     * 账本页面 交易方法
     */
    Subscription booking1(int days, int page, int type, String user_id, String starttime, String endtime);

    /**
     * 初始化店铺
     *
     * @param shopname
     * @param bossname
     * @return
     */
    Subscription initStoreInfo(String shopname, String bossname);

    /**
     * 反馈信息
     *
     * @param content
     * @return
     */
    Subscription getUserFeedback(String content);

    /**
     * 清空店铺数据
     *
     * @param goods
     * @param customer
     * @param user
     */
    Subscription getEmptyStoreData(String goods, String customer, String user, String password);

    /**
     * 店铺基本数据
     *
     * @return
     */
    Subscription getStoreData();

    Subscription uploadUserAvatar(String file);

    /**
     * 客户交易记录
     *
     * @param customer_id
     * @param days
     * @param is_owe
     * @param page
     * @return
     */
    Subscription customerTransactionRecord(int customer_id, int days, int is_owe, int page);

    /**
     * 欠款历史记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    Subscription arrearsHistory(int customer_id, int page);

    /**
     * 调整欠款
     *
     * @param customer_id
     * @param money
     * @return
     */
    Subscription adjustmentArrears(int customer_id, double money, String remark);

    /**
     * 是否更新app
     *
     * @return
     */
    Subscription isUpdataApp();

    /**
     * 客户等级列表
     *
     * @return
     */
    Subscription customerRankList();

    /**
     * 删除客户等级列表
     *
     * @return
     */
    Subscription customerdelRank(int rank_id);

    /**
     * 编辑客户等级
     *
     * @return
     */
    Subscription customerditRank(int rank_id, String rank_name, int rank_discount);

    /**
     * 增加客户等级
     *
     * @return
     */
    Subscription customeraddRank(int rank_id, String rank_name, List<String> customer_ids);

    /**
     * 增加等级所属客户
     *
     * @return
     */
    Subscription customertoRank(List<String> customer_ids, int rank_id);

    /**
     * 支付记录(财务详情)导出
     *
     * @return
     */
    Subscription exportPayList(String start_time, String end_time);

    /**
     * 产品销售详情记录
     *
     * @return
     */
    Subscription exportSaleProductList(String start_time);

    /**
     * 产品销售统计
     *
     * @return
     */
    Subscription exportSaleProductCount(String start_time, String end_time);

    /**
     * 导出记录
     *
     * @return
     */
    Subscription exportLog();

    /**
     * 导出状态查询
     *
     * @return
     */
    Subscription exportStatus(int log_id);
    /**
     * 贷款申请
     *
     * @return
     */
    Subscription loanAsk(String telephone,String name,String purpose,String identity,String gender);
    /**
     * 订单修改和确认
     *
     * @return
     */
    Subscription orderConfirm(Order order);


    /**
     * 删除订单
     *
     * @return
     */
    Subscription goodsDel(int goodId);

    /**
     * 删除盘点
     *
     * @return
     */
    Subscription inventoryInfo(int inventory_id);

}