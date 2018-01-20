package cn.order.ordereasy.view.activity;


/**
 *
 * 注册activity
 *
 *
 * **/


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
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
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class RegisterActivity extends BaseActivity implements OrderEasyView{

	private OrderEasyPresenter orderEasyPresenter;
	private String code;
	int time = 10;


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		setColor(this,this.getResources().getColor(R.color.lanse));
		//控件查找
		ButterKnife.inject(this);
		this.orderEasyPresenter = new OrderEasyPresenterImp(this);
	}

	//找到控件ID
	//返回
	@InjectView(R.id.return_click)
	ImageView return_click;
	//手机号码输入框
	@InjectView(R.id.phone_number)
	ClearEditText phone_number;

	//验证码输入框
	@InjectView(R.id.yzm_number)
	ClearEditText yzm_number;

	//验证码获取按钮
	@InjectView(R.id.hq_yzm)
	LinearLayout hq_yzm;


	//验证码文字状态
	@InjectView(R.id.down_time)
	TextView down_time;

	//密码输入框
	@InjectView(R.id.password_number)
	ClearEditText password_number;
	//邀请码输入框
	@InjectView(R.id.yaoqing_number)
	ClearEditText yaoqing_number;

	//确认注册按钮
	@InjectView(R.id.zhuce_but)
	Button zhuce_but;

	//注册协议
	@InjectView(R.id.xieyi)
	TextView xieyi;

	//需要的点击事件
	//返回证码按钮
	@OnClick(R.id.return_click)
	void  return_click() {
		RegisterActivity.this.finish();
	}

	@OnClick(R.id.hq_yzm)
	void  hq_yzm() {
		String tel=phone_number.getText().toString();
		if(TextUtils.isEmpty(tel)){
			//为空的话
			showToast("请输入手机号码！！！");
			return;
		}
		orderEasyPresenter.getCode();
		hq_yzm.setClickable(false);
		time = 60;
		Countdown();
	}
	//确认注册按钮
	@OnClick(R.id.zhuce_but)
	void  zhuce_but() {
		/**点击事件**/
		String pwd = password_number.getText().toString();
		String tel=phone_number.getText().toString();
		String smscode=yzm_number.getText().toString();
		String yaoqing = yaoqing_number.getText().toString();
		if(TextUtils.isEmpty(smscode)){
			//为空的话
			showToast("请输入验证码！！！");
			return;
		}
		orderEasyPresenter.register(tel,pwd,smscode,code,yaoqing);


	}
	//注册协议按钮
	@OnClick(R.id.xieyi)
	void  xieyi() {
		Intent intent =new Intent(RegisterActivity.this,AgreementActivity.class);
		startActivity(intent);
	}

	@Override
	public void showProgress(int type) {
		ProgressUtil.showDialog(this);
	}

	@Override
	public void hideProgress(int type) {
		ProgressUtil.dissDialog();
		if (type == 2){
			ToastUtil.show("网络连接失败");
		}
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

	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what){
				case 1001:
					JsonObject result= (JsonObject) msg.obj;
					if(result!=null){
						int status=result.get("code").getAsInt();
						String message=result.get("message").getAsString();
						if(status==1){
							//成功
							code = result.get("result").getAsString();
							String tel=phone_number.getText().toString();
							orderEasyPresenter.getSmsCode(tel,"register",code);
						}else{
							ToastUtil.show(message);
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
						String message=result.get("message").getAsString();
						if(status==1){
							ToastUtil.show("注册成功！");
							finish();
						}else{
							ToastUtil.show(message);
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
