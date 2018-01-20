package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;

/**
 * Created by Administrator on 2017/9/4.
 * <p>
 * 市场
 */

public class MarketActivity extends BaseActivity {
    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.market);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
    }


    //    //弹出框
//    private void showdialogs() {
//        alertDialog= new AlertDialog.Builder(this).create();
//        View view = View.inflate(this,R.layout.tanchuang_view,null);
//        alertDialog.setView(view);
//
//        alertDialog.show();
//        Window window = alertDialog.getWindow();
//        //window.setContentView(view);
//        window.setContentView(R.layout.tanchuang_view);
//        //标题
//        TextView title_name = (TextView) window.findViewById(R.id.title_name);
//        //输入框
//        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
//
//        //按钮1点击事件
//        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
//        quxiao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//
//        //按钮2确认点击事件
//        TextView queren = (TextView) window.findViewById(R.id.queren);
//        queren.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                orderEasyPresenter.addCategoryInfo(ed_type_name.getText().toString());
//                alertDialog.dismiss();// ;showdialogs();
//            }
//        });
//    }
    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        MarketActivity.this.finish();
    }
}
