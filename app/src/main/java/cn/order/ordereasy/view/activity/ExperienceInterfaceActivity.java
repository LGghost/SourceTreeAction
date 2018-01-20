package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.view.fragment.MainActivity;

/**
 * Created by Administrator on 2017/9/29.
 */

public class ExperienceInterfaceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.experience_interface);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            String flag=bundle.getString("flag");
            if(flag.equals("guoqi")){
                //过期了跳转
                long remain_time=bundle.getLong("remain_time");
                if(remain_time<=0){
                    //过期了
                }
                long expire=bundle.getLong("expire");
                data_time.setText(TimeUtil.getTimeStamp2Str(expire,"yyyy-MM-dd"));

            }else{
                //其它跳转
                //long expire=bundle.getLong("expire");
                //data_time.setText(TimeUtil.getTimeStamp2Str(expire,"yyyy-MM-dd"));
            }
        }
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

   //用户名
    @InjectView(R.id.user_name)
    TextView user_name;

    //体验时间
    @InjectView(R.id.data_time)
    TextView data_time;

    //客服电话
    @InjectView(R.id.kehu_call_phone)
    TextView kehu_call_phone;

    //官网地址
    @InjectView(R.id.guanwang_net)
    TextView guanwang_net;

    //继续体验
    @InjectView(R.id.continue_to_experience)
    Button continue_to_experience;

    //退出登录
    @InjectView(R.id.out_login)
    Button out_login;



    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        ExperienceInterfaceActivity.this.finish();
    }

    //打电话
    @OnClick(R.id.kehu_call_phone)
    void  kehu_call_phone() {
        Intent intent = new Intent();
        Uri data = Uri.parse("tel:" + "13974977597");
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(data);
        this.startActivity(intent);
    }

    //访问官网
    @OnClick(R.id.guanwang_net)
    void  guanwang_net() {

        Uri uri = Uri.parse("https://dinghuo5u.com");
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
    }

    //继续体验
    @OnClick(R.id.continue_to_experience)
    void  continue_to_experience() {
        Intent intent =new Intent(ExperienceInterfaceActivity.this, MainActivity.class);
        startActivity(intent);

    }
    //退出登录
    @OnClick(R.id.out_login)
    void  out_login() {
        SharedPreferences spPreferences = ExperienceInterfaceActivity.this.getSharedPreferences("userinfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = spPreferences.edit();
        editor.putString("username", "");
        editor.putBoolean("isTagLogin", false);
        editor.commit();
        MobclickAgent.onProfileSignOff();
        Intent intent = new Intent(ExperienceInterfaceActivity.this,LoginActity.class);
        startActivity(intent);
        ExperienceInterfaceActivity.this.finish();
    }
}
