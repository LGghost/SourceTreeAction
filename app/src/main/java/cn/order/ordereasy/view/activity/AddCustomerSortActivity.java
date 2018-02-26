package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.DiscountCustomer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class AddCustomerSortActivity extends BaseActivity implements OrderEasyView {
    private DiscountCustomer customer;
    private String flag = "add";
    private OrderEasyPresenter orderEasyPresenter;
    private List<String> customerIds = new ArrayList<>();
    private String Rank_name = "";
    private int discount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_customer_sort_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        if (bundle != null) {
            flag = bundle.getString("flag");
            customer = (DiscountCustomer) bundle.getSerializable("data");
            Rank_name = customer.getRank_name();
            discount = customer.getRank_discount();
            customer_name.setText(Rank_name);
            customer_name.setSelection(Rank_name.length());
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format((double) discount / 10);
            if (Double.parseDouble(result) == 10) {
                customer_discount.setText("无折扣");
            } else {
                customer_discount.setText(result + "折");
            }

            title_name.setText(getString(R.string.edit_customer_sort));
        }
    }

    @InjectView(R.id.customer_name)
    EditText customer_name;
    @InjectView(R.id.customer_discount)
    TextView customer_discount;
    @InjectView(R.id.title_name)
    TextView title_name;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        DataStorageUtils.getInstance().cleanSelectCustomer();
        finish();
    }

    @OnClick(R.id.customer_save)
    void customer_save() {
        Rank_name = customer_name.getText().toString();
        if (flag.equals("edit")) {
            if (!TextUtils.isEmpty(Rank_name)) {
                orderEasyPresenter.customerditRank(customer.getRank_id(), Rank_name, discount);
            } else {
                ToastUtil.show(getString(R.string.add_customer_hint));
            }
        } else {
            if (!TextUtils.isEmpty(Rank_name)) {
                orderEasyPresenter.customeraddRank(Rank_name, discount, customerIds);
            } else {
                ToastUtil.show(getString(R.string.add_customer_hint));
            }
        }
    }

    @OnClick(R.id.discount_layout)
    void discount_layout() {
        Intent intent = new Intent(this, SetupDiscountActivity.class);
        startActivityForResult(intent, 1001);
    }

    @OnClick(R.id.add_customer_layout)
    void add_customer_layout() {
        Intent intent = new Intent(this, SelcetSortCustomerActivity.class);
        intent.putExtra("flag", flag);
        if (flag.equals("edit")) {
            intent.putExtra("rank_id", customer.getRank_id());
            startActivity(intent);
        } else {
            startActivityForResult(intent, 1002);
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            discount = data.getExtras().getInt("discount");
            DecimalFormat df = new DecimalFormat("0.0");
            String result = df.format((double) discount / 10);
            if (Double.parseDouble(result) == 10) {
                customer_discount.setText("无折扣");
            } else {
                customer_discount.setText(result + "折");
            }
        }
        if (resultCode == 1002) {
            List<Customer> cusData = DataStorageUtils.getInstance().getSelectCustomer();
            if (flag.equals("add")) {
                for (Customer customer : cusData) {
                    customerIds.add(customer.getCustomer_id() + "");
                }
            }
        }
    }


    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        ProgressUtil.dissDialog();
        DataStorageUtils.getInstance().setAddCustomer(true);
        if (type == 0) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                DataStorageUtils.getInstance().cleanSelectCustomer();

                ToastUtil.show("添加成功");
                setResult(1001);
                finish();
            }
        }
        if (type == 1) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                ToastUtil.show("编辑成功");
                setResult(1001);
                finish();
            }
        }
    }
}

