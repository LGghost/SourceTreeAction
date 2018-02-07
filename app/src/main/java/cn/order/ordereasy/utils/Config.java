package cn.order.ordereasy.utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Config {
    public static String DIR_CACHE_PATH = getExtSDCardPath()
            .toString()  +"/order/Cache/";
    public static String DIR_IMAGE_PATH = getExtSDCardPath()
            .toString() + "/order/Image/";
    public static String DIR_IMAGE_PATH1 = getExtSDCardPath()
            .toString() + "/ordereasy/Images/";
    public static boolean IS_DEBUG = true;

    public static String stamp="123456789";
    public static String key="wr_shop.app,.*-api";

    public static String URL="https://www.dinghuo5u.com/";

    public static String TEST_URL="https://test.dinghuo5u.com/";

    public static String URL_HTTP ="http://www.dinghuo5u.com";
    public static String SHARE_URL ="https://www.dinghuo5u.com/share/owe.html?";
    /**
     * 入库
     */
    public static int Operate_TYPE_RUKU=1;
    /**
     * 出库
     */
    public static int Operate_TYPE_CHUKU=2;
    /**
     * 调整
     */
    public static int Operate_TYPE_TIAOZHENG=3;

    /**
     * 发货
     */
    public static int Operate_TYPE_DELIVER=4;
    /**
     * 退货
     */
    public static int Operate_TYPE_REDELIVER=5;
    /**
     * 盘点调整
     */
    public static int Operate_TYPE_Stocktaking=6;
    /**
     * 退欠货
     */
    public static int Operate_TYPE_ReturnOweGoods=7;

    /**
     * 普通订单[在app下单和微信订单]
     */
    public static int ORDER_TYPE_RUKU=1;
    /**
     * 退货订单
     */
    public static int ORDER_TYPE_TUIHUO=2;
    /**
     *退欠货订单
     */
    public static int ORDER_TYPE_QIANHUO=3;


    /**
     *提交用户反馈
     */
    public final static String user_feedback="api/user/User/feedback";

    /**
     *清空店铺数据
     */
    public final static String empty_store_data="api/user/shop/recovery";


    /**
     * 登录
     */
    public final static String login_url="api/user/login/index";

    /**
     * 注册
     */
    public final static String register_url="api/user/login/androidRegister";
    /**
     * 忘记密码
     */
    public final static String forgot_url="api/user/login/androidForgot";


    /**
     * 获取短信验证码
     */
    public final static String smsCode_url="api/user/login/androidCode";

    /**
     * 获取验证码
     */
    public final static String code_url="api/user/login/android";

    /**
     * 店铺设置
     */
    public final static String store_setup_url = "api/user/shop/setting";

    /**
     * 店铺信息
     */
    public final static String store_info_url = "api/user/shop/index";

    /**
     * 上传店铺图片
     */
    public final static String store_upload_img_url ="api/user/upload/shop";

    /**
     * 分类信息
     */
    public final static String category_info ="api/goods/category/index";

    /**
     * 修改分类
     */
    public final static String category_update_info="api/goods/category/edit";

    /**
     * 删除分类
     */
    public final static String category_del_info="api/goods/category/del";

    /**
     * 新增分类
     */
    public final static String category_add_info="api/goods/category/add";

    /**
     * 规格信息
     */
    public final static String spec_get_info="api/goods/spec/index";

    /**
     * 添加规格
     */
    public final static String spec_add_info="api/goods/spec/add";

    /**
     * 删除规格
     */
    public final static String spec_del_info="api/goods/spec/del";

    /**
     * 删除规格属性
     */
    public final static String spec_del_value_info="api/goods/spec/value_del";

    /**
     * 添加规格属性
     */
    public final static String spec_value_add_info="api/goods/spec/value_add";

    /**
     * 添加商品
     */
    public final static String goods_add_url="api/goods/goods/add";
    /**
     * 货品操作记录
     */
    public final static String goods_operate_info_url="api/goods/operate/info";

    /**
     * 商品列表
     */
    public final static String goods_list_url="api/goods/goods/index";

    /**
     * 商品编辑
     */
    public final static String goods_edit_url=" api/goods/goods/edit";

    /**
     * 商品详情
     */
    public final static String goods_info_url=" api/goods/goods/info";

    /**
     * 开单
     */
    public final static String Add_Odder="api/order/order/add";

    /**
     * 商品图片上传
     */
    public final static String good_upload_url="api/user/upload/index";

    /**
     * 欠货订单
     */
    public final static String good_oweorder_url="api/goods/goods/oweGoodsOrder";

    /**
     * 商品购买客户
     */
    public final static String good_customer_url="api/goods/goods/purchaseCustomer";

    /**
     * 商品购买趋势
     */
    public final static String good_buy_line_url = "api/goods/goods/goodsSalesTrend";


    /**
     * 商品上下架
     */
    public final static String good_make_status_url="api/goods/goods/status";


    /**
     * 库存列表
     */
    public final static String stock_list_url="api/goods/operate/index";


    /**
     * 订单列表
     */
    public final static String order_list_url="api/order/order/index";
    /**
     * 订单详情
     */
    public final static String order_info_url="api/order/order/info";
    /**
     * 订单操作历史
     */
    public final static String order_history_url="api/order/log/index";
    /**
     * 欠货订单
     */
    public final static String order_oweorder_url="api/order/order/oweOrder";
    /**
     * 修改订单收货地址
     */
    public final static String order_update_addr_url="api/order/order/addressOrder";
    /**
     * 关闭订单
     */
    public final static String order_close_url="api/order/order/closeOrder";
    /**
     * 客户主页--订单记录
     */
    public final static String cashier_record_list="/api/order/order/index";

    /**
     * 客户主页--收银记录
     */
    public final static String order_record_list="/api/order/pay/index";

    /**
     * 客户主页--发货记录
     */
    public final static String operation_record_list="/api/goods/operate/index";

    /**
     *库存操作
     */
    public final static String operation_add_url="/api/goods/operate/add";

    /**
     * 客户列表
     */
    public final static String cust_list_url="customer/index";

    /**
     *添加客户
     */
    public final static String cust_add_url="customer/add";
    /**
     * 客户修改
     */
    public final static String cust_edit_url="customer/edit";
    /**
     * 删除客户
     */
    public final static String cust_del_url="customer/del";
    /**
     * 批量倒入
     */
    public final static String cust_import_url="customer/batchAdd";
    /**
     * 客户详情
     */
    public final static String cust_info_url="customer/info";
    /**
     * 客户销售商品
     */
    public final static String cust_goods_url="api/customer/customer/Goods";
    /**
     * 客户欠款记录
     */
    public final static String cust_owelogs_url="api/customer/accountLog/Index";
    /**
     * 调整客户欠款
     */
    public final static String cust_adjustment_url="api/customer/AccountLog/adjustment";
    /**
     * 账单公开接口
     */
    public final static String cust_account_url="api/main/default/account";

    /**
     * 收款
     */
    public final static String cust_order_pay_add_url="api/order/pay/add";

    /**
     * 日报
     */
    public final static String daily_url="api/statistics/daily/index";
    /**
     * 发货
     */
    public final static String good_fahuo_url="api/statistics/daily/index";

    /**
     * 首页获取今日交易额，今日开单数，当前欠款数，当前欠货数的数据
     */
    public final static String num_today_url="/api/statistics/account/index";
    /**
     * 今日数据
     */
    public final static String amount_today_url="api/user/shop/daily";

    /**
     * 首页获取今日交易额，今日开单数，当前欠款数，当前欠货数的数据
     */
    public final static String num_today_url2="api/statistics/Account/owe";

    /**
     * 获取员工列表
     * @return
     */
    public final static String get_employee_url="api/user/user/index";

    /**
     * 添加员工
     * @return
     */
    public final static String add_employee_url="api/user/user/add";

    /**
     * 盘点单列表
     */
    public final static String list_inventory_url="api/goods/inventory/index";

    /**
     * 盘点单详情
     */
    public final static String get_inventory_url="api/goods/inventory/info";

    /**
     * 新增盘点单
     */
    public final static String add_inventory_url="api/goods/inventory/add";

    /**
     * 删除盘点单
     */
    public final static String del_inventory_url="api/goods/inventory/del";

    /**
     * 新增修改盘点记录
     */
    public final static String save_user_inventory_url="api/goods/inventory/userInventorySave";
    /**
     * 提交盘点单
     */
    public final static String commit_inventory_url="api/goods/inventory/inventorySubmit";
    /**
     * 个人盘点详情
     */
    public final static String into_inventory_url="api/goods/inventory/inventoryInfo";

    /**
     * 盘点详情
     */
    public final static String list_all_inventory_url="api/goods/inventory/inventoryList";

    /**
     * 删除员工接口
     */
    public final static String delete_employee_url="api/user/user/del";


    /**
     * 获取用户信息接口
     */
    public final static String get_userinfo_url="api/user/shop/index";

    public final static String storage_all_inventory_url="api/goods/inventory/inventoryList";


    /**
     * 修改用户信息接口
     */
    public final static String update_userinfo_url="api/user/user/edit";


    /**
     * 修改用户密码接口
     */
    public final static String update_userpass_url="api/user/user/password";

    /**
     * 账本页面，次数接口
     */
    public final static String booking2_url="/api/statistics/account/owe";

    /**
     * 账本页面，账本接口
     */
    public final static String booking4_url="/api/statistics/account/delivery";

    /**
     * 账本页面，收支接口
     */
    public final static String booking3_url="/api/statistics/account/payments";

    /**
     * 账本页面，交易接口
     */
    public final static String booking1_url="/api/statistics/account/index";

    /**
     * 店铺初始化
     */
    public final static String init_shop_url="api/user/shop/initialization";

    /**
     * 修改头像
     */
    public final static String upload_user_avatar="api/user/upload/avatar";
    /**
     * 是否更新
     */
    public final static String isupdataapp_url="check/version";

    /**
     * 客户等级列表
     */
    public final static String customer_rankList_url="customer/rankList";
    /**
     * 删除客户等级列表
     */
    public final static String customer_delRank_url="customer/delRank";

    /**
     * 编辑客户等级
     */
    public final static String customer_editRank_url="customer/editRank";

    /**
     * 增加客户等级
     */
    public final static String customer_addRank_url="customer/addRank";

    /**
     * 增加等级所属客户
     */
    public final static String customer_toRank_url="customer/toRank";
    /**
     * 支付记录(财务详情)导出
     */
    public final static String export_payList_url="export/payList";

    /**
     * 产品销售详情记录
     */
    public final static String export_saleProductList_url="export/saleProductList";
    /**
     * 产品销售统计
     */
    public final static String export_saleProductCount_url="export/saleProductCount";

    /**
     * 导出记录
     */
    public final static String export_log_url="export/log";
    /**
     * 导出数据下载
     */
    public final static String export_download_url="export/download";
    /**
     * 导出状态查询
     */
    public final static String export_status_url="export/status";

    /**
     * 贷款申请
     */
    public final static String loan_ask_url="Loan/ask";
    /**
     * 开单
     */
    public final static String order_confirm_url="order/confirm";
    /**
     * 删除订单
     */
    public final static String goods_del_url="goods/del";

    /**
     * 删除盘点
     */
    public final static String inventory_info_url="/api/goods/inventory/del";

    public static String getExtSDCardPath()
    {
        String state = Environment.getExternalStorageState();
        //如果状态不是mounted，无法读写
        if (!state.equals(Environment.MEDIA_MOUNTED))
        {
            return null;
        }
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
}
