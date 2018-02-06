package cn.order.ordereasy.view.fragment.gooddetailsfragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.PopWinShare;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UmengUtils;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.LoginActity;
import cn.order.ordereasy.view.activity.QRCodePreviewActivity;
import cn.order.ordereasy.view.activity.ShangHuoActivity;
import cn.order.ordereasy.view.activity.StockManageActivity;
import cn.order.ordereasy.widget.GuideDialog;

public class DetailsGoodsActivity extends FragmentActivity implements OrderEasyView {

    private boolean isEdit = false;
    private PopWinShare popwindows;
    private FragmentManager manager;
    private FragmentStcokSales stcoksalesFragment;
    private FragmentPreview previewFragment;
    private FragmentTransaction transaction;
    private Fragment mCurrentFragment = null; // 记录当前显示的Fragment
    private OrderEasyPresenter orderEasyPresenter;
    private int goodId = 0;
    private Goods goods = null;
    private int type = -1;
    private AlertDialog alertDialog;
    private boolean isSales = true;

    private boolean isEditUpdata = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_goods_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        isSales = bundle.getBoolean("isSales");
        if (isSales) {
            more.setVisibility(View.VISIBLE);
            bottom_layout.setVisibility(View.VISIBLE);
        } else {
            more.setVisibility(View.GONE);
            bottom_layout.setVisibility(View.GONE);
        }
        goodId = bundle.getInt("goodId");
        initData();
        //新手引导
        new GuideDialog(3, this);
    }

    private void initData() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        refreshData(true);
        stcoksalesFragment = new FragmentStcokSales();
        previewFragment = new FragmentPreview();
        manager = getSupportFragmentManager();
        stcoksalesFragment.setGoodId(goodId);
        mCurrentFragment = stcoksalesFragment;
        transaction = manager.beginTransaction();
        transaction.add(R.id.details_goods_frame_layout, mCurrentFragment, mCurrentFragment.getClass().getName());
        transaction.commitAllowingStateLoss();

    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getGoodsInfo(goodId);
        orderEasyPresenter.getOweGoodsList(goodId);
    }

    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.details_goods_frame_layout, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment);
        }
        mCurrentFragment = targetFragment;
        return transaction;
    }

    @InjectView(R.id.bbtn1)
    TextView bbtn1;
    @InjectView(R.id.view1)
    View view1;
    //右上角更多按钮
    @InjectView(R.id.more)
    ImageView more;
    @InjectView(R.id.bbtn2)
    TextView bbtn2;
    @InjectView(R.id.view2)
    View view2;
    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;

    //库存及销量
    @OnClick(R.id.bbtn1)
    void bbtn1() {
        // 文字颜色
        bbtn1.setTextColor(getResources().getColor(R.color.lanse));
        bbtn2.setTextColor(getResources().getColor(R.color.heise));
        // view颜色
        view1.setBackgroundColor(getResources().getColor(R.color.lanse));
        view2.setBackgroundColor(getResources().getColor(R.color.white));
        switchFragment(stcoksalesFragment).commit();
    }

    //货品预览
    @OnClick(R.id.bbtn2)
    void bbtn2() {
        // 文字颜色
        bbtn1.setTextColor(getResources().getColor(R.color.heise));
        bbtn2.setTextColor(getResources().getColor(R.color.lanse));
        // view颜色
        view1.setBackgroundColor(getResources().getColor(R.color.white));
        view2.setBackgroundColor(getResources().getColor(R.color.lanse));
        switchFragment(previewFragment).commit();
    }

    @OnClick(R.id.return_click)
    void return_click() {
        Intent intent = new Intent();
        intent.putExtra("isEdit", isEdit);
        setResult(1001, intent);
        finish();
    }

    //推广
    @OnClick(R.id.tuiguang)
    void tuiguang() {
        UmengUtils.getInstance().share(this, goods.getGoods_id(), goods.getCover(), goods.getTitle(), shareListener);
    }

    //库存管理
    @OnClick(R.id.kcgl)
    void kcgl() {
        Intent intent = new Intent(this, StockManageActivity.class);
        startActivity(intent);
    }

    //二维码
    @OnClick(R.id.erweima)
    void erweima() {
        Intent intent = new Intent(this, QRCodePreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("no", goods.getGoods_no());
        bundle.putString("name", goods.getTitle());
        bundle.putString("img", Config.URL_HTTP + "/" + goods.getCover());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //更多
    @OnClick(R.id.more)
    void more() {
        if (goods == null) {
            return;
        }
        final int status = goods.getStatus();
        popwindows = new PopWinShare(DetailsGoodsActivity.this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add_task_layout) {

                    if (status == 1) {
                        type = 1;
                        showdialogs();
                    } else {
                        type = 2;
                        showdialogs();
                    }
                } else if (v.getId() == R.id.team_member_layout) {
                    isEditUpdata = true;
                    orderEasyPresenter.getGoodsInfo(goodId);
                }
                popwindows.dismiss();
            }
        });
        if (status == 1) {
            popwindows.setBtnStatusText("下架");
            type = 1;
        } else {
            popwindows.setBtnStatusText("上架");
            type = 2;
        }
        popwindows.showPopupWindow(more);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            isEdit = true;
            if (data != null) {
                String flag = data.getExtras().getString("flag");
                if (flag.equals("delete")) {
                    Intent intent = new Intent();
                    intent.putExtra("isEdit", isEdit);
                    setResult(1001, intent);
                    finish();
                    return;
                }
            }
            refreshData(false);
        }
    }

    @Override
    public void showProgress(int type) {
        Log.e("DetailsGoodsActivity", "hideProgress:" + type);
    }

    @Override
    public void hideProgress(int type) {
        Log.e("DetailsGoodsActivity", "hideProgress:" + type);
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        stcoksalesFragment.endRefreshing();
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        if (type == 0) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    goods = (Goods) GsonUtils.getEntity(data.get("result").toString(), Goods.class);
                    stcoksalesFragment.setData(goods);
                    previewFragment.setData(goods);
                    if (isEditUpdata) {
                        Intent intent = new Intent(DetailsGoodsActivity.this, ShangHuoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", goods);
                        bundle.putString("flag", "detail");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1001);
                        isEditUpdata = false;
                    }
                }
            }
        } else if (type == 1) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    JsonArray orders = data.getAsJsonObject("result").getAsJsonArray("page_list");
                    Log.e("GoodsDetail", "欠货信息：" + orders);
                    List<Order> os = new ArrayList<>();
                    if (orders.size() != 0) {
                        for (int i = 0; i < orders.size(); i++) {
                            Order order = (Order) GsonUtils.getEntity(orders.get(i).toString(), Order.class);
                            os.add(order);
                        }
                    }
                    stcoksalesFragment.setOrderList(os);
                }
            }
            Log.e("欠货信息", data.toString());

        } else if (type == 4) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    if (type == 1) {
                        //下架图片
                        ToastUtil.show("下架成功");
                    } else if (type == 2) {
                        //上架图片
                        ToastUtil.show("下架成功");
                    }
                    orderEasyPresenter.getGoodsInfo(goodId);
                }
            }
        }
    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        if (type == 1) {
            text_conten.setText("确认要下架该货品吗？货品数据不会被删除，您可以再次选择上架");
        } else if (type == 2) {
            text_conten.setText("确认要上架该货品吗？");
        }


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = true;
                if (type == 1) {
                    popwindows.setBtnStatusText("上架");
                    orderEasyPresenter.makeGoodsStatus(goodId, 0);
                } else if (type == 2) {
                    orderEasyPresenter.makeGoodsStatus(goodId, 1);
                    popwindows.setBtnStatusText("下架");
                }
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
            Toast.makeText(DetailsGoodsActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(DetailsGoodsActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(DetailsGoodsActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            intent.putExtra("isEdit", isEdit);
            setResult(1001, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
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