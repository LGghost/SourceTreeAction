package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.UserInfo;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;

/**
 * Created by Administrator on 2017/9/4.
 * <p>
 * 金融
 */

public class FinanceActivity extends BaseActivity implements OrderEasyView {
    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;
    private SharedPreferences spPreferences;
    private String name;
    private String phone;
    private String sex;
    private String identity;
    private String purpose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        spPreferences = getSharedPreferences("user", 0);
        String user = spPreferences.getString("userinfo", "");
        if (!TextUtils.isEmpty(user)) {
            Gson gson = new Gson();
            UserInfo userinfo = gson.fromJson(user, UserInfo.class);
            finance_name.setText(userinfo.name);
            finance_name.setSelection(userinfo.name.length());
            finance_phone.setText(userinfo.telephone);
            finance_phone.setSelection(userinfo.telephone.length());
        } else {
            orderEasyPresenter.getUserInfo();
        }
    }


    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    //名字
    @InjectView(R.id.finance_name)
    EditText finance_name;
    //电话
    @InjectView(R.id.finance_phone)
    EditText finance_phone;
    //名别
    @InjectView(R.id.finance_sex)
    TextView finance_sex;
    //身份
    @InjectView(R.id.finance_identity)
    TextView finance_identity;
    //用途
    @InjectView(R.id.finance_purpose)
    EditText finance_purpose;
    @InjectView(R.id.finance_purpose_text)
    TextView finance_purpose_text;
    @InjectView(R.id.purpose_frame_layout)
    FrameLayout purpose_frame_layout;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        FinanceActivity.this.finish();
    }

    //提交
    @OnClick(R.id.submit)
    void submit() {
        name = finance_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请填写姓名");
            return;
        }
        phone = finance_phone.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show("请填写电话");
            return;
        }
        sex = finance_sex.getText().toString();
        if (TextUtils.isEmpty(sex)) {
            ToastUtil.show("请选择姓别");
            return;
        }
        identity = finance_identity.getText().toString();
        if (TextUtils.isEmpty(identity)) {
            ToastUtil.show("请选择身份");
            return;
        }
        purpose = finance_purpose_text.getText().toString();
        if (TextUtils.isEmpty(purpose)) {
            ToastUtil.show("请填写用途");
            return;
        }
        orderEasyPresenter.loanAsk(phone, name, purpose, identity, sex);
    }

    @OnClick(R.id.sex_layout)
    void sex_layout() {//性别
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setTitle("请选择性别")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("男", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_sex.setText("男");
                    }
                })
                .addSheetItem("女", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_sex.setText("女");
                    }
                });

        actionSheet.show();
    }

    @OnClick(R.id.identity_layout)
    void identity_layout() {//身份
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setTitle("请选择身份")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem("企业老板", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_identity.setText("企业老板");
                    }
                })
                .addSheetItem("企业管理人员", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_identity.setText("企业管理人员");
                    }
                })
                .addSheetItem("个体工商户", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_identity.setText("个体工商户");
                    }
                })
                .addSheetItem("普通上班族", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_identity.setText("普通上班族");
                    }
                })
                .addSheetItem("其他", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        finance_identity.setText("其他");
                    }
                });

        actionSheet.show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        if (isTouchPointInView(purpose_frame_layout, x, y)) {
            finance_purpose.setVisibility(View.VISIBLE);
            finance_purpose_text.setVisibility(View.GONE);
            finance_purpose.setSelection(finance_purpose.getText().toString().length());
        } else {
            finance_purpose.setVisibility(View.GONE);
            finance_purpose_text.setVisibility(View.VISIBLE);
            String content = finance_purpose.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                finance_purpose_text.setText(content);
            } else {
                finance_purpose_text.setText("");
                finance_purpose.setVisibility(View.VISIBLE);
                finance_purpose_text.setVisibility(View.GONE);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    //判断触点是否在指定的控件上
    private boolean isTouchPointInView(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int top = location[1];
        int right = left + view.getMeasuredWidth();
        int bottom = top + view.getMeasuredHeight();
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        if (type == 0) {
            if (data.get("code").getAsInt() == 1) {
                showDialog();
            }
        }
        if (type == 1) {
            if (data.get("code").getAsInt() == 1) {
                //获取用户登录信息成功
                JsonObject jo = data.getAsJsonObject("result").getAsJsonObject("user_info");
                JsonObject jo3 = jo.getAsJsonObject("shop_info");
                Gson gson = new Gson();
                UserInfo userinfo = gson.fromJson(jo.toString(), UserInfo.class);
                finance_name.setText(userinfo.name);
                finance_name.setSelection(userinfo.name.length());
                finance_phone.setText(userinfo.telephone);
                finance_phone.setSelection(userinfo.telephone.length());
            }
        }
    }

    private void showDialog() {
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
        text_conten.setText("您的授信申请已提交成功，客服人员将会及时跟您取得联系!");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        View view1 = window.findViewById(R.id.view1);
        view1.setVisibility(View.GONE);
        quxiao.setVisibility(View.GONE);
        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setText("确定");
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                alertDialog.dismiss();
            }
        });

    }
}
