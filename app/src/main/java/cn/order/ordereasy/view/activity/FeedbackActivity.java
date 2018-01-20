package cn.order.ordereasy.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/13.
 *
 * 意见反馈
 *
 */

public class FeedbackActivity extends BaseActivity implements OrderEasyView{

    OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed_back);
        setColor(this,this.getResources().getColor(R.color.lanse));
        orderEasyPresenter=new OrderEasyPresenterImp(this);
        ButterKnife.inject(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //确定按钮
    @InjectView(R.id.queding)
    TextView queding;

    //输入框内容
    @InjectView(R.id.ed_content)
    EditText ed_content;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        FeedbackActivity.this.finish();
    }

    @OnClick(R.id.queding)
    void queding(){
        String text=ed_content.getText().toString();
        if(TextUtils.isEmpty(text)){
            showToast("请输入反馈内容");
            return;
        }
        orderEasyPresenter.getUserFeedback(text);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        if(type==0){
            if(data.get("code").getAsInt()==1)
            {
                showToast("提交成功");
                finish();
            }
            else
            {
                ToastUtil.show("提交异常!");
            }
        }
    }
}
