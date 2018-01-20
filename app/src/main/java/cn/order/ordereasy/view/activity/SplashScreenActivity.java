package cn.order.ordereasy.view.activity;

/**
 * App初始加载图
 * 
 * */
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import cn.order.ordereasy.R;
import cn.order.ordereasy.view.fragment.MainActivity;

public class SplashScreenActivity extends BaseActivity {
	/**
	 * Called when the activity is first created.
	 */
	
	Handler handler;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		getWindow().setFormat(PixelFormat.RGBA_8888);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DITHER);

		setContentView(R.layout.splashscreen);
		setTranslucent(this);
		// Display the current version number
		PackageManager pm = getPackageManager();
		// 显示版本号
		// try {
		// PackageInfo pi = pm.getPackageInfo("org.wordpress.android", 0);
		// TextView versionNumber = (TextView) findViewById(R.id.versionNumber);
		// versionNumber.setText("Version " + pi.versionName);
		// } catch (NameNotFoundException e) {
		// e.printStackTrace();
		// }

		new Handler().postDelayed(new Runnable() {
			public void run() {
				/* Create an Intent that will start the Main WordPress Activity. */
				Intent mainIntent = new Intent(SplashScreenActivity.this,LoginActity.class);
				Bundle bundle=new Bundle();
				bundle.putBoolean("flag",true);
				mainIntent.putExtras(bundle);
				SplashScreenActivity.this.startActivity(mainIntent);
				SplashScreenActivity.this.finish();
			}
		}, 2900); // 2900 for release

	}
	
	

	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	// @Override
	// public void onResume() {
	// super.onResume();
	// Utils.ProgressDialogs(SplashScreenActivity.this, " ");
	// }
	//
	// @Override
	// public void onPause() {
	// super.onPause();
	// Utils.CloseProgressDialog();
	// }
}
