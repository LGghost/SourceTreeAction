package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 * 更多界面
 * <p>
 * Created by Administrator on 2017/9/4.
 */

public class MoreActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }


    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //规格管理
    @InjectView(R.id.guige_guanli)
    LinearLayout guige_guanli;

    //市场
    @InjectView(R.id.shichang)
    LinearLayout shichang;

    //金融
    @InjectView(R.id.jinrong)
    LinearLayout jinrong;

    //客户分类
    @InjectView(R.id.customer_fenlei)
    LinearLayout customer_fenlei;

    //数据管理
    @InjectView(R.id.shuju_gunli)
    LinearLayout shuju_gunli;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        MoreActivity.this.finish();
    }

    //规格管理
    @OnClick(R.id.guige_guanli)
    void guige_guanli() {
        Intent intent = new Intent(MoreActivity.this, SpecificationsGuanliActivity.class);
        startActivity(intent);
    }

    //市场
    @OnClick(R.id.shichang)
    void shichang() {
        Intent intent = new Intent(MoreActivity.this, MarketActivity.class);
        startActivity(intent);
    }

    //金融
    @OnClick(R.id.jinrong)
    void jinrong() {
        Intent intent = new Intent(MoreActivity.this, FinanceActivity.class);
        startActivity(intent);
    }


    //数据管理
    @OnClick(R.id.shuju_gunli)
    void shuju_gunli() {
        Intent intent = new Intent(MoreActivity.this, DataManagementActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.customer_fenlei)
    void customer_fenlei() {//客户分类
        Intent intent = new Intent(MoreActivity.this, CustomerManageActivity.class);
        startActivity(intent);
    }

//    @OnClick(R.id.purchase)
//    void purchase() {//采购
//        Intent intent = new Intent(MoreActivity.this, PurchaseActivity.class);
//        startActivity(intent);
//    }

    @OnClick(R.id.huopin_feilei)
    void huopin_feilei() {//货品分类
        Intent intent = new Intent(MoreActivity.this, FenleiGuanliActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.dayinji)
    void dayinji() {//打印机
        Intent intent = new Intent(MoreActivity.this, PrintActivity.class);
        startActivity(intent);
    }

//    @OnClick(R.id.dianpu_setup)
//    void dianpu_setup() {//店铺设置
//        Intent intent = new Intent(MoreActivity.this, StoreSettingsActivity.class);
//        startActivity(intent);
//    }
}
