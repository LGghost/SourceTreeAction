package cn.order.ordereasy.view.activity;

/**
 * 设置Activity
 **/

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.UserInfo;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UpdataApp;
import cn.order.ordereasy.utils.XGPushUtils;
import cn.order.ordereasy.view.OrderEasyView;

public class SetupAvtivity extends BaseActivity implements OrderEasyView {
    private OrderEasyPresenter orderEasyPresenter;
    private AlertDialog alertDialog;
    private UserInfo info;
    private String info1;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up);
        setColor(this, this.getResources().getColor(R.color.lanse));
        //控件查找
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderEasyPresenter.getUserInfo();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        //设置点击屏幕不让消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("您确定要退出登录吗？");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences spPreferences = SetupAvtivity.this.getSharedPreferences("user", MODE_PRIVATE);
                SharedPreferences.Editor editor = spPreferences.edit();
                editor.putString("user_pwd", "");
                editor.putBoolean("isTagLogin", false);
                editor.commit();
                DataStorageUtils.getInstance().cleanData();
                MobclickAgent.onProfileSignOff();
                XGPushUtils.getInstance().unRegister(SetupAvtivity.this);
                Intent intent = new Intent(SetupAvtivity.this, LoginActity.class);
                startActivity(intent);
                SetupAvtivity.this.finish();

                alertDialog.dismiss();
            }
        });
    }

    private void showUpdatadialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        //设置点击屏幕不让消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText(info1 + "您确定要更新版本？");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdataApp(SetupAvtivity.this, url);
                alertDialog.dismiss();
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //顶部个人资料点击修改
    @InjectView(R.id.xiugai)
    LinearLayout xiugai;

    //头像
    @InjectView(R.id.touxiang)
    ImageView touxiang;

    //用户名
    @InjectView(R.id.username)
    TextView username;
    //用户名
    @InjectView(R.id.user_state)
    TextView user_state;
    //电话号码
    @InjectView(R.id.phone_number)
    TextView phone_number;

    //修改密码
    @InjectView(R.id.gai_password)
    LinearLayout gai_password;

    //打电话给客服
    @InjectView(R.id.call_kefu)
    LinearLayout call_kefu;
    //反馈信息
    @InjectView(R.id.fankui)
    LinearLayout fankui;

    //评分
    @InjectView(R.id.score)
    LinearLayout score;

    //使用帮助
    @InjectView(R.id.help)
    LinearLayout help;

    //关于
    @InjectView(R.id.about)
    LinearLayout about;
    //检测更新
    @InjectView(R.id.updata_text)
    LinearLayout updata_text;

    //退出登录
    @InjectView(R.id.log_out)
    TextView log_out;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        SetupAvtivity.this.finish();
    }


    //修改资料按钮
    @OnClick(R.id.xiugai)
    void xiugai() {
        Intent intent = new Intent(SetupAvtivity.this, PersonalInformationActivity.class);
        Bundle bundle = new Bundle();
        if (info != null) {
            bundle.putSerializable("data", info);
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, 1001);
    }

    //修改密码按钮
    @OnClick(R.id.gai_password)
    void gai_password() {
        Intent intent = new Intent(SetupAvtivity.this, ModifyPasswordActivity.class);

        if (info != null) {
            intent.putExtra("user_id", info.user_id);
            intent.putExtra("tel", info.telephone);
        }

        startActivity(intent);
    }

    //打电话给客服
    @OnClick(R.id.call_kefu)
    void call_kefu() {
        Intent intent = new Intent();
        Uri data = Uri.parse("tel:" + "13974977597");
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(data);
        this.startActivity(intent);
    }

    //意见反馈
    @OnClick(R.id.fankui)
    void fankui() {
        Intent intent = new Intent(SetupAvtivity.this, FeedbackActivity.class);
        startActivity(intent);

    }

    //评分按钮
    @OnClick(R.id.score)
    void score() {
        Uri uri = Uri.parse("http://baike.baidu.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    //帮助按钮
    @OnClick(R.id.help)
    void help() {
        Intent intent = new Intent(SetupAvtivity.this, UseHelpActivity.class);
        startActivity(intent);
    }

    //帮助按钮
    @OnClick(R.id.remaining_time)
    void remaining_time() {
        Intent intent = new Intent(SetupAvtivity.this, ExperienceInterfaceActivity.class);
        startActivity(intent);
    }

    //关于按钮
    @OnClick(R.id.about)
    void about() {
        Intent intent = new Intent(SetupAvtivity.this, AboutActivity.class);
        startActivity(intent);
    }

    //检测更新
    @OnClick(R.id.updata_text)
    void updata_text() {
        orderEasyPresenter.isUpdataApp();
    }

    //退出登录
    @OnClick(R.id.log_out)
    void log_out() {
        showdialogs();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            orderEasyPresenter.getUserInfo();
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("返回信息", data.toString());
        if (type == 3) {
            if (data != null) {
                if (data.get("code").getAsInt() == 1) {
                    String version1 = data.getAsJsonObject("result").get("ver").getAsString();
                    info1 = data.getAsJsonObject("result").get("info").getAsString();
                    url = data.getAsJsonObject("result").get("url").getAsString();
                    Log.e("Setup", "" + SystemfieldUtils.getVersionId(version1));
                    if (SystemfieldUtils.getVersion(this) < SystemfieldUtils.getVersionId(version1)) {
                        showUpdatadialogs();
                    } else {
                        ToastUtil.show("已是最新版本");
                    }
                }
            }

        }
        if (type == 1) {
            if (data.get("code").getAsInt() == 1) {
                //获取用户登录信息成功
                JsonObject jo = data.getAsJsonObject("result").getAsJsonObject("user_info");
                JsonObject jo3 = jo.getAsJsonObject("shop_info");
                Gson gson = new Gson();
                UserInfo userinfo = gson.fromJson(jo.toString(), UserInfo.class);
                info = userinfo;
                username.setText(info.getName());
                phone_number.setText(info.getTelephone());
                setUserState(userinfo);
                String avatar = info.getAvatar();
                if (!TextUtils.isEmpty(avatar)) {
                    ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + avatar, touxiang);
                }
            }
        } else if (type == 3) {

        }
    }

    private void setUserState(UserInfo userinfo) {
        String boss = "";
        String shop_keeper = "";
        String salesperson = "";
        String godown_man = "";
        List<String> role = userinfo.getAuth_group_ids();
        if (userinfo.getIs_boss() == 1) {
            boss = getString(R.string.employee_boss);
            user_state.setText("(" + boss + ")");
            user_state.setTextColor(getResources().getColor(R.color.shouye_hongse));
        } else {
            for (int m = 0; m < role.size(); m++) {
                if (Integer.parseInt(role.get(m)) == 0) {
                } else if (Integer.parseInt(role.get(m)) == 1) {
                    shop_keeper = getString(R.string.shop_keeper) + "/";
                } else if (Integer.parseInt(role.get(m)) == 2) {
                    salesperson = getString(R.string.salesperson) + "/";
                } else if (Integer.parseInt(role.get(m)) == 3) {
                    godown_man = getString(R.string.godown_man);
                }
            }
            String power = shop_keeper + salesperson + godown_man;
            if (power.endsWith("/")) {
                power = power.substring(0, power.length() - 1);
            }
            user_state.setText("(" + power + ")");
            user_state.setTextColor(getResources().getColor(R.color.shouye_lanse));
        }
    }
}
