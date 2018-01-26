package cn.order.ordereasy.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import cn.order.ordereasy.R;
import cn.order.ordereasy.receiver.MessageReceiver;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UmengUtils;
import cn.order.ordereasy.utils.XGPushUtils;
import cn.order.ordereasy.view.activity.BillingActivity;
import cn.order.ordereasy.view.activity.ExperienceInterfaceActivity;
import cn.order.ordereasy.view.activity.LoginActity;
import cn.order.ordereasy.view.activity.SetupShopActivity;
import cn.order.ordereasy.view.activity.ShangHuoActivity;

public class MainActivity extends FragmentActivity implements OnClickListener, FragmentStore.setFragmentPageListen {
    // 定义Fragment页面
    private FragmentShelves fragmentThings;
    private FragmentStore fragmentStore;
    private FragmentOrder fragmentOrder;
    private FragmentCust fragmentCust;
    private FragmentManager manager;
    private boolean isFrist = false;
    private Fragment mCurrentFragment = null; // 记录当前显示的Fragment
    // 定义布局对象
    private LinearLayout StoreFl, ThingsFl, OrderFl, CustFl;

    // 定义图片组件对象
    private ImageView StoreIv, ThingsIv, OrderIv, CustIv;

    // 定义按钮图片组件
    private ImageView toggleImageView, plusImageView;

    // 定义PopupWindow
    private PopupWindow popWindow;
    // 获取手机屏幕分辨率的类
    private DisplayMetrics dm;

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setColor(this, this.getResources().getColor(R.color.lanse));

        initView();
        initData();

        // 初始化默认为选中点击了“店铺”按钮
        clickStoreBtn();
        isGrantExternalRW(this);
        //判断是否设置了店铺名称和老板名字
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        MyLog.e("店铺信息", shopinfo);
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String name = shop.get("name").getAsString();

            long remain_time = shop.get("remain_time").getAsLong();
            long expire = shop.get("expire").getAsLong();

            if (remain_time <= 0) {
                Intent intent = new Intent(MainActivity.this, ExperienceInterfaceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("remain_time", remain_time);
                bundle.putLong("expire", expire);
                bundle.putString("flag", "guoqi");
                intent.putExtras(bundle);
                startActivity(intent);
            }


            String bossName = shop.get("boss_name").getAsString();
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(bossName)) {
                Intent intent = new Intent(MainActivity.this, SetupShopActivity.class);
                startActivity(intent);
            }
        } else {
            Intent intent = new Intent(MainActivity.this, SetupShopActivity.class);
            startActivity(intent);
        }
    }


    /**
     * 初始化组件
     */
    private void initView() {
        // 实例化布局对象
        ThingsFl = (LinearLayout) findViewById(R.id.layout_things);
        StoreFl = (LinearLayout) findViewById(R.id.layout_store);
        OrderFl = (LinearLayout) findViewById(R.id.layout_order);
        CustFl = (LinearLayout) findViewById(R.id.layout_cust);

        // 实例化图片组件对象
        ThingsIv = (ImageView) findViewById(R.id.image_things);
        StoreIv = (ImageView) findViewById(R.id.image_store);
        OrderIv = (ImageView) findViewById(R.id.image_order);
        CustIv = (ImageView) findViewById(R.id.image_cust);

        // 实例化按钮图片组件
        toggleImageView = (ImageView) findViewById(R.id.toggle_btn);
        plusImageView = (ImageView) findViewById(R.id.plus_btn);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //信鸽注册
        XGPushUtils.getInstance().register(this);

        fragmentStore = new FragmentStore();
        fragmentThings = new FragmentShelves();
        fragmentOrder = new FragmentOrder();
        fragmentCust = new FragmentCust();
        mCurrentFragment = fragmentStore;
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frame_content, mCurrentFragment, mCurrentFragment.getClass().getName());
        transaction.commitAllowingStateLoss();
//        clickStoreBtn();
        // 给布局对象设置监听
        StoreFl.setOnClickListener(this);
        ThingsFl.setOnClickListener(this);
        OrderFl.setOnClickListener(this);
        CustFl.setOnClickListener(this);

        // 给按钮图片设置监听
        toggleImageView.setOnClickListener(this);
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        int isLogin = spPreferences.getInt("isLogin", 0);
        if (isLogin == 0) {
            Intent intent = new Intent(this, LoginActity.class);
            startActivity(intent);
        }
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.frame_content, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment);
        }
        mCurrentFragment = targetFragment;
        return transaction;
    }

    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // 点击店铺按钮
            case R.id.layout_store:
                clickStoreBtn();
                break;
            // 点击货架按钮
            case R.id.layout_things:
                clickThingsBtn();
                break;
            // 点击我的订单按钮
            case R.id.layout_order:
                clickOrderBtn();
                break;
            // 点击客户按钮
            case R.id.layout_cust:
                clickCustBtn();
                break;
            // 点击中间按钮
            case R.id.toggle_btn:
                clickToggleBtn();
                break;
        }
    }


    /**
     * 点击了“店铺”按钮
     */
    private void clickStoreBtn() {
        if (isFrist) {
            switchFragment(fragmentStore).commit();
            fragmentStore.onResume();
        }
        isFrist = true;
        // 改变选中状态
        StoreFl.setSelected(true);
        StoreIv.setSelected(true);

        ThingsFl.setSelected(false);
        ThingsIv.setSelected(false);

        OrderFl.setSelected(false);
        OrderIv.setSelected(false);

        CustFl.setSelected(false);
        CustIv.setSelected(false);
    }

    /**
     * 点击了“货架”按钮
     */
    private void clickThingsBtn() {
        switchFragment(fragmentThings).commit();
        StoreFl.setSelected(false);
        StoreIv.setSelected(false);

        ThingsFl.setSelected(true);
        ThingsIv.setSelected(true);

        OrderFl.setSelected(false);
        OrderIv.setSelected(false);

        CustFl.setSelected(false);
        CustIv.setSelected(false);
    }

    /**
     * 点击了“订单”按钮
     */
    private void clickOrderBtn() {

        switchFragment(fragmentOrder).commit();
        StoreFl.setSelected(false);
        StoreIv.setSelected(false);

        ThingsFl.setSelected(false);
        ThingsIv.setSelected(false);

        OrderFl.setSelected(true);
        OrderIv.setSelected(true);

        CustFl.setSelected(false);
        CustIv.setSelected(false);
    }

    /**
     * 点击了“客户”按钮
     */
    private void clickCustBtn() {
        switchFragment(fragmentCust).commit();
        StoreFl.setSelected(false);
        StoreIv.setSelected(false);

        ThingsFl.setSelected(false);
        ThingsIv.setSelected(false);

        OrderFl.setSelected(false);
        OrderIv.setSelected(false);

        CustFl.setSelected(true);
        CustIv.setSelected(true);

    }

    /**
     * 点击了中间按钮
     */
    private void clickToggleBtn() {
        showdialogs();
    }

    /**
     * 改变显示的按钮图片为正常状态
     */
    private void changeButtonImage() {
        plusImageView.setSelected(false);
    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.popwindow_layout, null);
        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.popwindow_layout);
        window.setGravity(Gravity.BOTTOM);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        alertDialog.getWindow().setAttributes(lp);

        //上货点击事件
        LinearLayout shanghuo = (LinearLayout) window.findViewById(R.id.shanghuo);
        shanghuo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShangHuoActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        //开单点击事件
        LinearLayout kaidan = (LinearLayout) window.findViewById(R.id.kaidan);
        kaidan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BillingActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        //邀请买家点击事件
        LinearLayout yaoqing = (LinearLayout) window.findViewById(R.id.yaoqing);
        yaoqing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengUtils.getInstance().share(MainActivity.this, "欢迎光临本店", shareListener);
                alertDialog.dismiss();
            }
        });

        //邀请买家点击事件
        ImageView chacha_1 = (ImageView) window.findViewById(R.id.chacha_1);
        chacha_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ToastUtil.show("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ToastUtil.show("失败" + t.getMessage());
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ToastUtil.show("取消了");
        }
    };


    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();

            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }

    @Override
    public void setFragmentPage(int type) {
        switch (type) {
            case 0:
                clickStoreBtn();//店铺
                break;
            case 1:
                clickThingsBtn();//货架
                break;
            case 2:
                clickOrderBtn();//订单
                break;
            case 3:
                clickCustBtn();//客户
                break;
        }
    }

    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View StatusView = createStatusView(activity, color);
            // 添加statusView到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(StatusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content))
                    .getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏的高度
        int resourceId = activity.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources()
                .getDimensionPixelSize(resourceId);

        // 绘制一个和状态栏一样高度的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);

        return statusView;
    }
}
