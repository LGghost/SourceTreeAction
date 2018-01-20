package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/24.
 * <p>
 * 清空店铺数据
 */

public class EmptyStoreDataActivity extends BaseActivity implements OrderEasyView {

    AlertDialog alertDialog;
    OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_store_data);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //ToggleButton1
    @InjectView(R.id.tg_but_one)
    ToggleButton tg_but_one;

    //ToggleButton2
    @InjectView(R.id.tg_but_two)
    ToggleButton tg_but_two;

    //ToggleButton3
    @InjectView(R.id.tg_but_three)
    ToggleButton tg_but_three;

    //EditText输入密码
    @InjectView(R.id.login_password)
    EditText login_password;

    //eyes查看隐藏密码
    @InjectView(R.id.eyes)
    RelativeLayout eyes;

    //Button点击确定
    @InjectView(R.id.queding_onclick)
    Button queding_onclick;
    @InjectView(R.id.clear_edit)
    EditText clear_edit;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        EmptyStoreDataActivity.this.finish();
    }

    //返回按钮
    @OnClick(R.id.queding_onclick)
    void queding_onclick() {
        showdialogs();
    }

    @OnClick(R.id.tg_but_one)
    void tg_but_one() {
        if (tg_but_one.isChecked()) {
            dialogs(0, true);
        } else {
            dialogs(0, false);
        }

    }

    @OnClick(R.id.tg_but_two)
    void tg_but_two() {
        if (tg_but_two.isChecked()) {
            dialogs(1, true);
        } else {
            dialogs(1, false);
        }

    }

    @OnClick(R.id.tg_but_three)
    void tg_but_three() {
        if (tg_but_three.isChecked()) {
            dialogs(2, true);
        } else {
            dialogs(2, false);
        }
    }

    private void dialogs(int type, boolean isSelcet) {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.prompt_dialog_layout);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);

        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        String title;
        String retain;
        if (isSelcet) {
            retain = "清空";
        } else {
            retain = "保留";
        }
        if (type == 0) {
            title = "货品";
        } else if (type == 1) {
            title = "客户";
        } else {
            title = "员工";
        }
        title_name.setText("您选择了" + retain + "“" + title + "”");
        text_conten.setText("执行清空操作时,将" + retain + "全部" + "“" + title + "”" + "数据");
        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("警告");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText(getTishi());


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
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
                alertDialog.dismiss();
                String str1 = "1", str2 = "1", str3 = "1";
                if (tg_but_one.isChecked()) {
                    str1 = "0";
                }
                if (tg_but_two.isChecked()) {
                    str2 = "0";
                }
                if (tg_but_three.isChecked()) {
                    str3 = "0";
                }
                String clear = clear_edit.getText().toString();
                if (TextUtils.isEmpty(clear) || !clear.equals("确认清空")) {
                    showToast("请输入确认清空");
                    return;
                }
                if (TextUtils.isEmpty(login_password.getText().toString())) {
                    showToast("请输入登录密码！");
                    return;
                }

                orderEasyPresenter.getEmptyStoreData(str1, str2, str3, login_password.getText().toString());
            }
        });
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        if (type == 0) {
            if (data != null) {
                if (data.get("code").getAsInt() == 1) {
                    showToast("清空数据成功");
                    DataStorageUtils.getInstance().cleanData();
                    finish();
                } else {
                    String message = data.get("message").getAsString();
                    if (!TextUtils.isEmpty(message)) {
                        ToastUtil.show(message);
                    } else {
                        ToastUtil.show("清空数据失败，您没有权限操作");
                    }
                }
            }
        }
    }


    private String getTishi() {
        List<String> list = new ArrayList<>();
        String one, two, three;
        StringBuffer str = new StringBuffer();
        if (tg_but_one.isChecked()) {
            one = "0";
        } else {
            one = "1";
        }
        if (tg_but_two.isChecked()) {
            two = "0";
        } else {
            two = "1";
        }
        if (tg_but_three.isChecked()) {
            three = "0";
        } else {
            three = "1";
        }
        list.add(one);
        list.add(two);
        list.add(three);
        if (one.equals("0") && two.equals("0") && three.equals("0")) {

        } else {
            str.append("您已选择保留");
        }
        int sign1 = -1;
        int sign = -1;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("1")) {
                String str1 = "";
                if (i == 0) {
                    str1 = "货品";
                    sign = 0;
                } else if (i == 1) {
                    if (sign != -1) {
                        str.append("，");
                    }
                    str1 = "客户";
                    sign = 1;
                } else {
                    if (sign != -1) {
                        str.append("，");
                    }
                    str1 = "员工";
                    sign = 2;
                }
                str.append(str1);
            }
        }
        if (sign != -1) {
            str.append("数据，");
        }
        str.append("将清空");
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals("0")) {
                String str1 = "";
                if (i == 0) {
                    str1 = "货品";
                    sign1 = 0;
                } else if (i == 1) {
                    if (sign1 != -1) {
                        str.append("，");
                    }
                    str1 = "客户";
                    sign1 = 0;
                } else {
                    if (sign1 != -1) {
                        str.append("，");
                    }
                    str1 = "员工";
                }
                str.append(str1);
            }
        }
        str.append("，订单，库存，账本，日报等其它店铺所有数据，清空后不能再还原");
        return new String(str);
    }


}
