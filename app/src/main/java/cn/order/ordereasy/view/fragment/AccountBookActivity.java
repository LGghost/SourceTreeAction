package cn.order.ordereasy.view.fragment;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import cn.order.ordereasy.R;
import cn.order.ordereasy.widget.CustomBar;

public class AccountBookActivity extends FragmentActivity implements CustomBar.SelectListener, View.OnClickListener {
    private CustomBar customBar;
    private ImageView return_text;
    private FragmentManager manager;
    private FragmentTrade tradeFragment;
    private FragmentOweNumber owenumberFragment;
    private FragmentReceivables receivablesFragment;
    private FragmentStock stockFragment;
    private Fragment mCurrentFragment = null; // 记录当前显示的Fragment

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_book_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        customBar = (CustomBar) findViewById(R.id.custom_bar);
        return_text = (ImageView) findViewById(R.id.return_click);
        // 设置选中监听
        customBar.setSelectListener(this);
        return_text.setOnClickListener(this);
        initData();
    }

    private void initData() {
        tradeFragment = new FragmentTrade();
        owenumberFragment = new FragmentOweNumber();
        receivablesFragment = new FragmentReceivables();
        stockFragment = new FragmentStock();
        mCurrentFragment = tradeFragment;
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.account_book_frame_layout, mCurrentFragment, mCurrentFragment.getClass().getName());
        transaction.commitAllowingStateLoss();

    }

    @Override
    public void getOnclick(int i) {
        switch (i) {
            case 0://交易
                switchFragment(tradeFragment).commit();
                break;
            case 2://欠数
                switchFragment(owenumberFragment).commit();
                break;
            case 4://收支
                switchFragment(receivablesFragment).commit();
                break;
            case 6://库存
                switchFragment(stockFragment).commit();
                break;
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
            transaction.add(R.id.account_book_frame_layout, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment);
        }
        mCurrentFragment = targetFragment;
        return transaction;
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

    @Override
    public void onClick(View v) {
        finish();
    }
}