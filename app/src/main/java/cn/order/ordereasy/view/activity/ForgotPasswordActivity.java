package cn.order.ordereasy.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 *
 * 忘记密码activity
 *
 * Created by Administrator on 2017/9/3.
 */

public class ForgotPasswordActivity extends BaseActivity implements OrderEasyView{
    private int userid;
    private String tel;
    private String password;
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        setColor(this,this.getResources().getColor(R.color.lanse));
        orderEasyPresenter=new OrderEasyPresenterImp(this);
        ButterKnife.inject(this);

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;


    //手机号
    @InjectView(R.id.phone_number)
    EditText phone_number;

    //验证码
    @InjectView(R.id.yzm_number)
    ClearEditText yzm_number;

    //验证码
    @InjectView(R.id.down_time)
    TextView down_time;

    //密码输入框
    @InjectView(R.id.password_number)
    ClearEditText password_number;

    //确认按钮
    @InjectView(R.id.zhuce_but)
    Button zhuce_but;

    //验证码获取按钮
    @InjectView(R.id.hq_yzm)
    LinearLayout hq_yzm;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        ForgotPasswordActivity.this.finish();
    }


    //验证码获取
    @OnClick(R.id.down_time)
    void  down_time() {
        String tel=phone_number.getText().toString();
        if(TextUtils.isEmpty(tel)){
            //为空的话
            showToast("请输入手机号码！！！");
            return;
        }
        orderEasyPresenter.getCode();
        hq_yzm.setClickable(false);
        hq_yzm.setFocusable(false);
        time = 60;
        Countdown();
    }

    //确认按钮
    @OnClick(R.id.zhuce_but)
    void  zhuce_but()
    {
        String pwd = password_number.getText().toString();
        String tel=phone_number.getText().toString();
        String smscode=yzm_number.getText().toString();
        if(TextUtils.isEmpty(smscode)){
            //为空的话
            showToast("请输入验证码！！！");
            return;
        }
        orderEasyPresenter.forgot(tel,pwd,smscode,code);
        hq_yzm.setClickable(false);
        time = 60;
        Countdown();
    }


    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data,int type) {
        Message message=new Message();
        switch (type){
            case 1:
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1001;

                }
                break;
            case 2:
                message=new Message();
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1002;
                }
                break;
            case 3:
                message=new Message();
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1003;

                }

                break;
            default:
                break;
        }
        message.obj=data;
        handler.sendMessage(message);
    }
    int time = 10;
    private String code;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1001:
                    JsonObject result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        if(status==1){
                            //成功
                            code = result.get("result").getAsString();
                            String tel=phone_number.getText().toString();
                            orderEasyPresenter.getSmsCode(tel,"forgot",code);
                        }
                    }
                    Log.e("密钥信息",result.toString());
                    break;
                case 1002:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){
                            ToastUtil.show("验证码发送成功！");
                        }else{
                            ToastUtil.show("验证码发送失败！");
                        }
                    }
                    Log.e("验证码信息",result.toString());
                    break;
                case 1003:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        if(status==1){
                            ToastUtil.show("修改密码成功！");
                            finish();
                        }
                    }
                    Log.e("注册信息",result.toString());
                    break;
                case 1008:
                    down_time.setText("" + time + "s后重试");
                    down_time.setTextColor(getResources().getColor(R.color.white));
                    down_time.setBackground(getResources().getDrawable(R.drawable.yuanjiao_but_huise));
                    hq_yzm.setClickable(false);
                    hq_yzm.setFocusable(false);
                    if (time == 0) {
                        down_time.setText("获取验证码");
                        down_time.setTextColor(getResources().getColor(R.color.white));
                        down_time.setBackground(getResources().getDrawable(R.drawable.yuanjiao_but));
                        hq_yzm.setClickable(true);
                        hq_yzm.setFocusable(true);
                    }
                    break;
                case 1007:
                    ToastUtil.show("出错了哟~");
                    break;
                case 9999:
                    ToastUtil.show("网络有问题哟~");
                    break;
            }
        }
    };
    /** 60秒倒计时 */
    private void Countdown() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 60; i > 0; i--) {
                    if(time<=0) break;
                    time--;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.sendMessage(handler.obtainMessage(1008));
                }
            }
        }.start();
    }
}
