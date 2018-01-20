package cn.order.ordereasy.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

/**
 * Created by Administrator on 2017/9/21.
 *
 * 欠货列表
 *
 */

public class DefaultListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.default_list);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        DefaultListActivity.this.finish();
    }
}
