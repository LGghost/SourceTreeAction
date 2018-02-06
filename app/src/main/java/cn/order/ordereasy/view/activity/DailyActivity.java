package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.DetailCustomersAdapter;
import cn.order.ordereasy.adapter.DetailOrdersAdapter;
import cn.order.ordereasy.adapter.DetailSpecsAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.PopWinShare;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.AccountBookActivity;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;

/**
 * Created by Administrator on 2017/9/5.
 * <p>
 * 日报Activity
 */

public class DailyActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {

    View view;
    private PopWinShare popwindows;
    OrderEasyPresenter orderEasyPresenter;
    private String dateStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Log.e("DailyActivity", "onCreate");
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        dateStr = simple.format(date).toString();
        orderEasyPresenter.getDaily(dateStr);
        ProgressUtil.showDialog(DailyActivity.this);
        initRefreshLayout();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("DailyActivity", "onNewIntent");
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            dateStr = bundle.getString("date");
            title.setText(dateStr + "日报");
            orderEasyPresenter.getDaily(dateStr);
        }
    }

    private void initRefreshLayout() {
        store_refresh.setOnRefreshListener(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //更多按钮
    @InjectView(R.id.more)
    TextView more;
    @InjectView(R.id.title)
    TextView title;

    //订单量
    @InjectView(R.id.diangdan_num)
    TextView diangdan_num;

    //交易额
    @InjectView(R.id.jiaoyie)
    TextView jiaoyie;

    //销售量
    @InjectView(R.id.xiaoshou_num)
    TextView xiaoshou_num;

    //今日欠款
    @InjectView(R.id.qiankuan_num)
    TextView qiankuan_num;

    //总欠款
    @InjectView(R.id.zong_qiankuan)
    TextView zong_qiankuan;

    //总欠货
    @InjectView(R.id.zong_qianhuo)
    TextView zong_qianhuo;

    //昨日库存
    @InjectView(R.id.zuori_kucun)
    TextView zuori_kucun;

    //今日库存
    @InjectView(R.id.jinri_kucun)
    TextView jinri_kucun;

    //发货数
    @InjectView(R.id.fahuoshu)
    TextView fahuoshu;

    //退货数
    @InjectView(R.id.tuihuoshu)
    TextView tuihuoshu;

    //入账
    @InjectView(R.id.ruzhang)
    TextView ruzhang;

    //出账
    @InjectView(R.id.chuzhang)
    TextView chuzhang;

    //销售量
    @InjectView(R.id.xiaoshou_liang)
    TextView xiaoshou_liang;

    //入库
    @InjectView(R.id.ruku)
    TextView ruku;

    //出库
    @InjectView(R.id.chuku)
    TextView chuku;

    //发货退款
    @InjectView(R.id.fahuo_tuihuo)
    TextView fahuo_tuihuo;

    //库存调整
    @InjectView(R.id.kucun_tiaozheng_jia)
    TextView kucun_tiaozheng_jia;
    //库存调整
    @InjectView(R.id.kucun_tiaozheng_jian)
    TextView kucun_tiaozheng_jian;
    //下拉刷新控件
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        DailyActivity.this.finish();
    }

    //更多按钮点击事件
    @OnClick(R.id.more)
    void more() {
        popwindows = new PopWinShare(DailyActivity.this, new View.OnClickListener() {

            public void onClick(View v) {
                if (v.getId() == R.id.add_task_layout) {
                    //跳转到账本详情
                    Intent intent = new Intent(DailyActivity.this, AccountBookActivity.class);
                    startActivity(intent);
                } else if (v.getId() == R.id.team_member_layout) {
                    //跳转到往期日报
                    Intent intent = new Intent(DailyActivity.this, PastDailyActivity.class);
                    startActivity(intent);
                }
                popwindows.dismiss();
            }
        });
        popwindows.setBtnStatusText("账本详情");
        popwindows.setBtnBottomText("前往日报");
        popwindows.setFristImageView(R.drawable.img_zhangben);
        popwindows.setSecondImageView(R.drawable.img_ribao);
        popwindows.showPopupWindow(more);

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1001) {
                JsonObject jo1 = (JsonObject) msg.obj;
                JsonObject jo2 = jo1.getAsJsonObject("result");
                Log.e("DailyActivity", "" + jo2.toString());
                //绑定日报界面数据
                diangdan_num.setText(jo2.get("bill_num").getAsInt() + "");
                jiaoyie.setText(jo2.get("trade_sum").getAsDouble() + "");
                xiaoshou_num.setText(jo2.get("sale_num").getAsInt() + "");

                qiankuan_num.setText(jo2.get("owe_money").getAsDouble() + "");
                zong_qiankuan.setText(jo2.get("total_owe_money").getAsDouble() + "");
                zong_qianhuo.setText(jo2.get("total_owe_goods").getAsDouble() + "");

                zuori_kucun.setText(jo2.get("store_num_of_yesterday").getAsInt() + "");
                jinri_kucun.setText(jo2.get("store_num").getAsInt() + "");
                fahuoshu.setText(jo2.get("deliver_num").getAsInt() + "");
                tuihuoshu.setText(jo2.get("return_num").getAsInt() + "");
                ruzhang.setText("¥ " + jo2.get("income").getAsDouble());
                chuzhang.setText("¥ " + jo2.get("expenditure").getAsDouble());

                xiaoshou_liang.setText(jo2.get("sale_num").getAsInt() + "");
                ruku.setText(jo2.get("in_store_num").getAsInt() + "");
                chuku.setText(jo2.get("out_store_num").getAsInt() + "");
                fahuo_tuihuo.setText(jo2.get("deliver_num").getAsInt() + "/" + jo2.get("return_num").getAsInt());
                kucun_tiaozheng_jia.setText("+" + jo2.get("adjust_in_num").getAsInt());
                kucun_tiaozheng_jian.setText("-" + jo2.get("adjust_out_num").getAsInt());
            }
        }
    };

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        if (data.get("code").getAsInt() == 1) {
            ProgressUtil.dissDialog();
            Message mess = new Message();
            mess.what = 1001;
            mess.obj = data;

            handler.sendMessage(mess);
        }
    }

    @Override
    public void onRefresh() {
        orderEasyPresenter.getDaily(dateStr);
    }
}
