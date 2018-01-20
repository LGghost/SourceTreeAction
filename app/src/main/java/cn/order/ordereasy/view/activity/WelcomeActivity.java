package cn.order.ordereasy.view.activity;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ViewFlipper;

import cn.order.ordereasy.R;

public class WelcomeActivity extends BaseActivity implements OnGestureListener {
	private int[] imgID = { R.drawable.welcome_one, R.drawable.welcome_two,R.drawable.welcome_img_three};
	private ImageView im_1;
	private ImageView im_2;
	private ImageView im_3;
	// private ImageView im_4;
	private List<ImageView> ivs = new ArrayList<ImageView>();
	private ViewFlipper flipper;
	private GestureDetector detector;
	private Button button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTranslucent(this);
		SharedPreferences sPreferences = this.getSharedPreferences("first", 0);
		if (sPreferences.getBoolean("yes", false)) {
			Intent intent = new Intent();
			// 设置每次加载进来时的activity
			intent.setClass(WelcomeActivity.this, SplashScreenActivity.class);
			WelcomeActivity.this.startActivity(intent);
			WelcomeActivity.this.finish();
		}
		Editor editor = sPreferences.edit();
		editor.putBoolean("yes", true);
		editor.commit();
		button = (Button) this.findViewById(R.id.welcome);
		im_1 = (ImageView) findViewById(R.id.iv_1);
		im_2 = (ImageView) findViewById(R.id.iv_2);
		im_3 = (ImageView) findViewById(R.id.iv_3);
		// im_4 = (ImageView) findViewById(R.id.iv_4);

		ivs.add(im_1);
		ivs.add(im_2);
		ivs.add(im_3);
		// ivs.add(im_4);

		detector = new GestureDetector(this);
		flipper = (ViewFlipper) findViewById(R.id.viewflipper);
		for (int i = 0; i < imgID.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setImageResource(imgID[i]);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			flipper.addView(imageView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
		ivs.get(0).setEnabled(false);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent();
				// 设置欢迎界面结束后的activity
				intent.setClass(WelcomeActivity.this,SplashScreenActivity.class);
				WelcomeActivity.this.startActivity(intent);

				WelcomeActivity.this.finish();
			}
		});
	}

	private void dotChange(int index) {
		for (int i = 0; i < ivs.size(); i++) {
			if (i == index) {
				ivs.get(i).setEnabled(false);
			} else {
				ivs.get(i).setEnabled(true);
			}
		}
	}

	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {

	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {

		if (e1.getX() - e2.getX() > 120) {
			// 添加动画
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_left_out));
			View view = flipper.getChildAt(imgID.length - 1);
			View view1 = flipper.getCurrentView();
			// flipper.getDisplayedChild();

			if (view == view1) {
				button.setVisibility(View.VISIBLE);
			} else {
				button.setVisibility(View.GONE);
				this.flipper.showNext();
				dotChange(flipper.getDisplayedChild());

				if (flipper.getDisplayedChild() == imgID.length - 1) {
					button.setVisibility(View.VISIBLE);
				}
			}
			return true;
		}// 从右向左滑动
		else if (e1.getX() - e2.getX() < -120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_right_out));
			if (flipper.getChildAt(0) == flipper.getCurrentView()) {

			} else {
				button.setVisibility(View.GONE);
				this.flipper.showPrevious();
				dotChange(flipper.getDisplayedChild());
			}

			return true;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return this.detector.onTouchEvent(event);
	}

	@Override
	public void onBackPressed() {

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
}
