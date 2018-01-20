package cn.order.ordereasy.view.activity;

/**
 * 
 * ��设置店铺activity
 * 
 * **/

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.MainActivity;

public class SetupShopActivity extends BaseActivity implements OrderEasyView{

	OrderEasyPresenter orderEasyPresenter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.setup_shop);
		setColor(this,this.getResources().getColor(R.color.lanse));
		ButterKnife.inject(this);
		orderEasyPresenter=new OrderEasyPresenterImp(this);
		SharedPreferences spPreferences=getSharedPreferences("user", 0);
		String shopinfo=spPreferences.getString("shopinfo","");
		if(!TextUtils.isEmpty(shopinfo)){
			JsonObject shop= (JsonObject) GsonUtils.getObj(shopinfo,JsonObject.class);
			String name=shop.get("name").getAsString();
			String bossName=shop.get("boss_name").getAsString();
			shop_name.setText(name);
			boss_name.setText(bossName);
		}
	}

	//找到控件ID
	//输入店铺名称
	@InjectView(R.id.shop_name)
	ClearEditText shop_name;

	//输入老板姓名
	@InjectView(R.id.boss_name)
	ClearEditText boss_name;

	//保存
	@InjectView(R.id.baocun)
	Button baocun;

	@OnClick(R.id.baocun)
	void save(){
		String name=shop_name.getText().toString();
		if(TextUtils.isEmpty(name)){
			showToast("请输入店铺名称！");
			return;
		}
		String bossName=boss_name.getText().toString();
		if(TextUtils.isEmpty(bossName)){
			showToast("请输入老板姓名！");
			return;
		}
		orderEasyPresenter.initStoreInfo(name,bossName);
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
			if(data!=null){
				int status=data.get("code").getAsInt();
				if(status==1){
					showToast("设置成功");
					finish();
				}else{
					String msg =data.get("message").getAsString();
					showToast(msg);
				}
			}
		}
		MyLog.e("初始化店铺信息",data.toString());
	}

}
