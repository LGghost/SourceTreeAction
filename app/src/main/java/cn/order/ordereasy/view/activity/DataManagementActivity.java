package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 *
 * 数据管理Activity
 *
 * Created by Administrator on 2017/9/10.
 */

public class DataManagementActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.data_management);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //清空店铺数据按钮
    @InjectView(R.id.clear_shuju)
    LinearLayout clear_shuju;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        DataManagementActivity.this.finish();
    }

    //清空店铺数据按钮
    @OnClick(R.id.clear_shuju)
    void  clear_shuju() {
       Intent intent =new Intent(DataManagementActivity.this,EmptyStoreDataActivity.class);
        startActivity(intent);
    }
}
