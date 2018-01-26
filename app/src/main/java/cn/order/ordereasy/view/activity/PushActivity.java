package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class PushActivity extends BaseActivity implements OrderEasyView {
    private String title;
    private String body;
    private String key;
    private int type;
    private int order_id;
    private String shop_key = "";
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            title = bundle.getString("title");
            body = bundle.getString("body");
            key = bundle.getString("key");
            type = bundle.getInt("type");
            order_id = bundle.getInt("order_id");
        }
        SharedPreferences spPreferences = getSharedPreferences("user", 0);
        String shopInfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopInfo)) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(shopInfo);
                shop_key = jsonObject.getString("shop_key");
                showDialog();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            orderEasyPresenter.getStoreInfo();
        }
    }

    @InjectView(R.id.text_conten)
    TextView text_conten;
    @InjectView(R.id.title_text)
    TextView title_text;
    @InjectView(R.id.know)
    TextView know;
    @InjectView(R.id.content_layout)
    LinearLayout content_layout;
    @InjectView(R.id.bottom_layout)
    LinearLayout bottom_layout;

    @OnClick(R.id.cancel)
    void cancel() {
        finish();
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
    }

    @OnClick(R.id.confirm)
    void confirm() {
        Intent intent = new Intent(this, OrderNoDetailsActivity.class);
        intent.putExtra("id", order_id);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
    }

    @OnClick(R.id.know)
    void know() {
        finish();
        overridePendingTransition(R.anim.push_up_down, R.anim.push_up_out);
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
        if (type != 1) {
            finish();
            ToastUtil.show("网络连接失败");
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        if (data != null) {
            Log.e("PushActivity", "result:" + data.toString());
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //成功
                JsonObject shop = data.getAsJsonObject("result").getAsJsonObject("shop_info");
                shop_key = shop.get("shop_key").toString();
                showDialog();
            }

        }
    }

    private void showDialog() {
        content_layout.setVisibility(View.VISIBLE);
        if (shop_key.equals(key)) {
            if (type == 1) {
                bottom_layout.setVisibility(View.VISIBLE);
                know.setVisibility(View.GONE);
                text_conten.setText(body);
                title_text.setText(title);
            }
        } else {
            bottom_layout.setVisibility(View.GONE);
            know.setVisibility(View.VISIBLE);
            text_conten.setText("请登录订单对应的店铺查看");
            title_text.setText("您有新的微信订单");
        }
    }
}