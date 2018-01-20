package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;
import cn.order.ordereasy.R;
import cn.order.ordereasy.app.MyApplication;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ToastUtil;
import cn.pedant.SweetAlert.SweetAlertDialog;


public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener {
    protected String TAG;
    protected MyApplication mApp;
    private SweetAlertDialog mLoadingDialog;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        mApp = MyApplication.getInstance();

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

    /**
     * 52.     * 生成一个和状态栏大小相同的矩形条
     * 53.     * @param activity 需要设置的activity
     * 54.     * @param color 状态栏的颜色值
     * 55.     * @return 状态栏矩形条
     * 56.
     */
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

    /**
     * 77.     * 使状态栏透明
     * 78.     * 适用于图片作为背景的界面，此时需要图片填充到状态栏
     * 79.     * @param activity 需要设置的activity
     * 80.
     */
    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
                    .findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

//    /**
//     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
//     */
//    private void initSwipeBackFinish() {
//        mSwipeBackHelper = new BGASwipeBackHelper(this, this);
//
//        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
//        // 下面几项可以不配置，这里只是为了讲述接口用法。
//
//        // 设置滑动返回是否可用。默认值为 true
//        mSwipeBackHelper.setSwipeBackEnable(true);
//        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
//        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
//        // 设置是否是微信滑动返回样式。默认值为 true
//        mSwipeBackHelper.setIsWeChatStyle(true);
//        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
//        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
//        // 设置是否显示滑动返回的阴影效果。默认值为 true
//        mSwipeBackHelper.setIsNeedShowShadow(true);
//        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
//        mSwipeBackHelper.setIsShadowAlphaGradient(true);
//        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
//        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
//    }

    /**
     * 查找View
     *
     * @param id   控件的id
     * @param <VT> View类型
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) findViewById(id);
    }


    /**
     * 需要处理点击事件时，重写该方法
     *
     * @param v
     */
    public void onClick(View v) {
    }

    protected void showToast(String text) {
        ToastUtil.show(text);
    }

    public void showLoadingDialog() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
            mLoadingDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            mLoadingDialog.setCancelable(false);
            mLoadingDialog.setTitleText("数据加载中...");
        }
        mLoadingDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }


}