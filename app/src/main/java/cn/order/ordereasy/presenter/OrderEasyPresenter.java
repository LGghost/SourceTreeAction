package cn.order.ordereasy.presenter;

import java.util.List;
import java.util.Map;

import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.InventoryInfo;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Redelivery;
import rx.Subscription;

/**
 * Created by mrpan on 2017/9/4.
 */

public abstract class OrderEasyPresenter extends BasePresenter {

    /**
     * 登录
     *
     * @param username
     * @param password
     */
    public abstract void login(String username, String password);

    /**
     * 注册
     *
     * @param telephone
     * @param password
     */
    public abstract void register(String telephone, String password, String smscode, String code, String yaoqcode);

    /**
     * 忘记密码
     *
     * @param telephone
     * @param password
     * @param smscode
     */
    public abstract void forgot(String telephone, String password, String smscode, String code);

    /**
     * 获取短信验证码
     *
     * @param telephone
     * @param type
     * @param code
     */
    public abstract void getSmsCode(String telephone, String type, String code);

    public abstract void operateRecordDetail(int operate_id);

    /**
     * 获取密钥
     */
    public abstract void getCode();

    /**
     * 店铺设置
     *
     * @param mobile
     * @param name
     * @param province
     * @param city
     * @param district
     * @param address
     * @param telephone
     * @param wechat
     */
    public abstract void setupStore(String mobile, String name, String province,
                                    String city, String district, String address, String telephone, String wechat, String notice);

    /**
     * 获取店铺信息
     */
    public abstract void getStoreInfo();

    /**
     * 上传图片logo,wx_qrcode
     *
     * @param type
     * @param file
     */
    public abstract void uploadStoreImg(String type, String file);

    /**
     * 上传货物图片
     *
     * @param file
     */
    public abstract void uploadGoodImg(String file);

    /**
     * 获取分类信息
     */
    public abstract void getCategoryInfo();

    /**
     * 新增分类
     */
    public abstract void addCategoryInfo(String name);

    /**
     * 修改分类
     */
    public abstract void updateCategoryInfo(int cateId, String name);

    /**
     * 删除分类
     */
    public abstract void delCategoryInfo(int cateId);

    /**
     * 获取规格信息
     */
    public abstract void getSpecInfo();

    /**
     * 新增规格
     */
    public abstract void addSpecCategoryInfo(String name);

    /**
     * 新增规格属性
     */
    public abstract void addSpecValueInfo(int specId, String value);

    /**
     * 删除规格
     */
    public abstract void delSpecInfo(int specId);

    /**
     * 删除规格属性
     *
     * @param specId
     * @param value
     */
    public abstract void delSpecValueInfo(int specId, String value);

    /**
     * 添加商品
     *
     * @param good
     */
    public abstract void addGoods(Goods good);

    /**
     * 修改商品
     *
     * @param good
     */
    public abstract void updateGood(Goods good);

    /**
     * 获取商品列表
     */
    public abstract void getGoodsList();

    /**
     * 获取商品详情
     *
     * @param goodsId
     */
    public abstract void getGoodsInfo(int goodsId);

    /**
     * 获取商品欠货订单
     *
     * @param goodsId
     */
    public abstract void getOweGoodsList(int goodsId);

    /**
     * 获取购买客户
     *
     * @param goodsId
     */
    public abstract void getGoodsCustomers(int goodsId);

    /**
     * 商品上下架
     *
     * @param goodId
     * @param status
     */
    public abstract void makeGoodsStatus(int goodId, int status);

    /**
     * 商品购买趋势
     *
     * @param goodId
     * @param days
     * @param date
     */
    public abstract void getGoodsBuyHistories(int goodId, int days, String date);

    /**
     * 开单
     *
     * @param order
     */
    public abstract void Add_Odder(Order order);

    /**
     * 关闭订单
     *
     * @param orderId
     */
    public abstract void closeOrder(int orderId);

    /**
     * 获取订单列表
     *
     * @return
     */
    public abstract void getOrdersList(int page, String filter_type, String user_id, String start_time, String end_time);

    /**
     * 搜索订单
     *
     * @param page
     * @param keyword
     */
    public abstract void searchRecordList(int page, String keyword);

    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    public abstract void getOrderInfo(int orderId);

    /**
     * 获取订单操作日志
     *
     * @param orderId
     * @return
     */
    public abstract void getOrderLogs(int orderId);


    /**
     * 获取欠货订单
     *
     * @return
     */
    public abstract void getOweOrdersList(int custId, int order_id);

    /**
     * 更改订单收货地址
     *
     * @param orderId
     * @param addr
     * @return
     */
    public abstract void updateOrderAddr(int orderId, String addr, int type);

    /**
     * 客户列表
     *
     * @return
     */
    public abstract void getCustomerList();

    /**
     * 客户列表
     *
     * @return
     */
    public abstract void getCustomerList1();

    /**
     * 添加客户
     *
     * @param customer
     * @return
     */
    public abstract void addCustomer(Customer customer);

    /**
     * 修改客户
     *
     * @param customer
     * @return
     */
    public abstract void updateCustomer(Customer customer);

    /**
     * 删除客户
     *
     * @param customerId
     * @return
     */
    public abstract void delCustomer(int customerId);

    /**
     * 批量导入
     *
     * @param customerList
     * @return
     */
    public abstract void importCustomers(List<Customer> customerList);

    /**
     * 获取客户信息
     *
     * @param customerId
     * @return
     */
    public abstract void getCustomerInfo(int customerId);

    /**
     * 客户销售商品
     *
     * @param customer_id
     * @param is_owe
     * @return
     */
    public abstract void getCustSalesGoods(int customer_id, int is_owe);

    /**
     * 客户欠款记录
     *
     * @param customer_id
     * @return
     */
    public abstract void getCustomerOweLog(int customer_id);

    /**
     * 修改客户欠款
     *
     * @param customer_id
     * @param money
     * @param remark
     * @return
     */
    public abstract void adjustmentOweInfo(int customer_id, double money, String remark);

    /**
     * 获取公开账单
     *
     * @param sign
     * @param customer_id
     * @param shop_id
     * @return
     */
    public abstract void getCustomerAccountInfo(String sign, int customer_id, int shop_id);

    //库存列表
    public abstract void getStockList(int operate_type);

    //客户主页--订单记录
    public abstract void getRecordList(int customer_id, String page);


    //客户主页--收银记录
    public abstract void getOrderRecordLlist(int customer_id, String page);

    // 客户主页--操作（发货）记录
    public abstract void getOperationRecordList(int customer_id, String page);

    //库存调整记录
    public abstract void getOperationRecordList(int page, int operate_type, int user_id, String begindate, String enddate);

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
     */
    public abstract void addOrderPay(int customer_id, int payment_type, double cash, double wechat, double alipay, double bank_card, double other);

    // 客户主页--操作（发货）记录
    public abstract void getDaily(String date);

    /**
     * 发货
     *
     * @param deliveries
     */
    public abstract void delivers(List<Delivery> deliveries);

    /**
     * 出库入库
     *
     * @param delivery
     */
    public abstract void deliver(Delivery delivery);

    /**
     * 退货
     *
     * @param redelivery
     */
    public abstract void redeliver(Redelivery redelivery);

    /**
     * 首页今日交易额，今日开单数，当前欠款数，当前欠货数
     *
     * @param
     */
    public abstract void getNumToday(int type);

    /**
     * 首页今日交易额，今日开单数，当前欠款数，当前欠货数
     *
     * @param
     */
    public abstract void getNumToday2(int type);

    /**
     * 店铺基本数据
     */
    public abstract void getStoreData();

    /**
     * 获取员工数
     *
     * @param
     */
    public abstract void getEmployee(int type);

    /**
     * 添加员工
     *
     * @param
     */
    public abstract void addEmployee(String name, String tel, String pass, List<Integer> list);

    /**
     * 获取盘点列表
     */
    public abstract void getInventoryList(int page);

    /**
     * 获取盘点详情
     *
     * @param inventory_id
     */
    public abstract void getInventoryInfo(int inventory_id, int user_id);

    /**
     * 获取盘点详情
     *
     * @param inventory_id
     */
    public abstract void getInventory(int inventory_id);

    /**
     * 添加盘点单
     */
    public abstract void addInventory();

    /**
     * 提交盘点
     *
     * @param inventory_id
     * @param remark
     * @param is_adjust
     */
    public abstract void commitInventoryInfo(int inventory_id, String remark, int is_adjust);

    /**
     * 加入或继续盘点
     *
     * @param inventoryInfo
     */
    public abstract void saveInventoryInfo(List<Map<String, Object>> inventoryInfo);

    /**
     * 获取盘点结果
     *
     * @param inventory_id
     * @param user_id
     * @param page
     */
    public abstract void getInventoryInfo(int inventory_id, int user_id, int page);

    /**
     * 删除员工
     *
     * @param
     */
    public abstract void deleteEmployee(String user_id);

    /**
     * 获取员工信息
     *
     * @param
     */
    public abstract void getUserInfo();

    /**
     * 修改员工信息
     *
     * @param
     */
    public abstract void updateUserInfo(int id, String name, List<Integer> list);

    /**
     * 修改员工密码
     *
     * @param
     */
    public abstract void updateUserPass(int id, String pass, String pass1);

    /**
     * 账本页面 次数方法
     *
     * @param
     */
    public abstract void booking2(int type, int page);

    /**
     * 账本页面 次数方法
     *
     * @param
     */
    public abstract void booking4(int days, String starttime, String endtime);

    /**
     * 账本页面 次数方法
     *
     * @param
     */
    public abstract void booking3(int days, String starttime, String endtime);

    /**
     * 账本页面 次数方法
     *
     * @param
     */
    public abstract void booking1(int days, int page, int type, String user_id, String starttime, String endtime);

    /**
     * 初始化店铺
     *
     * @param shopname
     * @param bossname
     */
    public abstract void initStoreInfo(String shopname, String bossname);

    /**
     * 用户反馈
     *
     * @param content
     */
    public abstract void getUserFeedback(String content);


    /**
     * 清空店铺数据
     *
     * @param goods
     * @param customer
     * @param user
     */
    public abstract void getEmptyStoreData(String goods, String customer, String user, String password);

    /**
     * 上传用户图片
     *
     * @param file
     */
    public abstract void uploadUserAvatar(String file);

    /**
     * 客户交易记录
     *
     * @param customer_id
     * @param days
     * @param is_owe
     * @param page
     */
    public abstract void customerTransactionRecord(int customer_id, int days, int is_owe, int page);


    /**
     * 欠款历史记录
     *
     * @param customer_id
     * @param page
     */
    public abstract void arrearsHistory(int customer_id, int page);

    /**
     * 调整欠款
     *
     * @param customer_id
     * @param money
     * @param remark
     */
    public abstract void adjustmentArrears(int customer_id, double money, String remark);

    /**
     * 是否更新App
     */
    public abstract void isUpdataApp();

    /**
     * 客户等级列表
     */
    public abstract void customerRankList();


    /**
     * 删除客户等级列表
     */
    public abstract void customerdelRank(int rank_id);

    /**
     * 编辑客户等级
     */
    public abstract void customerditRank(int rank_id, String rank_name, int rank_discount);

    ;

    /**
     * 增加客户等级
     */
    public abstract void customeraddRank(String rank_name, int rank_discount, List<String> customer_ids);

    /**
     * 增加等级所属客户
     */
    public abstract void customertoRank(List<String> customer_ids, int rank_id);

    /**
     * 支付记录(财务详情)导出
     */
    public abstract void exportPayList(String start_time, String end_time);

    /**
     * 产品销售详情记录
     */
    public abstract void exportSaleProductList(String start_time);

    /**
     * 产品销售统计
     */
    public abstract void exportSaleProductCount(String start_time,String end_time);

    /**
     * 导出记录
     */
    public abstract void exportLog();


    /**
     * 导出状态查询
     */
    public abstract void exportStatus(int log_id);
    /**
     * 贷款申请
     */
    public abstract void loanAsk(String telephone,String name,String purpose,String identity,String gender);
    /**
     * 订单修改和确认
     *
     * @return
     */
    public abstract void orderConfirm(Order order);
}




