package cn.order.ordereasy.view.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.ActionSheetDialog;

public class SupplierPaymentActivity extends BaseActivity implements OrderEasyView {
    private SupplierBean bean;
    private double cash = 0, alipay = 0, wechat = 0, card = 0, other = 0;
    private int method;
    private int payment_type = 1;
    private OrderEasyPresenter orderEasyPresenter;
    private String remark;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_payment_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (SupplierBean) bundle.getSerializable("data");
        }
        initData();
    }

    private void initData() {
        supplier_name.setText(bean.getName());
        supplier_fund.setText(bean.getDebt() + "");
        business_time_text.setText(TimeUtil.getTimeStamp2Str(new Date(), "yyyy-MM-dd"));
        if (!TextUtils.isEmpty(bean.getRemarks())) {
            supplier_remarks_text.setVisibility(View.VISIBLE);
            supplier_remarks.setVisibility(View.GONE);
            supplier_remarks.setText(bean.getRemarks());
            supplier_remarks_text.setText(bean.getRemarks());
        }
    }

    @InjectView(R.id.supplier_name)
    TextView supplier_name;
    @InjectView(R.id.supplier_fund)
    TextView supplier_fund;
    @InjectView(R.id.payment_method_text)
    TextView payment_method_text;
    @InjectView(R.id.business_time_text)
    TextView business_time_text;
    @InjectView(R.id.this_payment_text)
    EditText this_payment_text;
    @InjectView(R.id.remarks_frame_layout)
    FrameLayout remarks_frame_layout;
    @InjectView(R.id.supplier_remarks)
    EditText supplier_remarks;
    @InjectView(R.id.supplier_remarks_text)
    TextView supplier_remarks_text;
    @InjectView(R.id.confirm)
    TextView confirm;
    @InjectView(R.id.this_payment)
    TextView this_payment;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.payment_method_layout)
    void payment_method_layout() {
        ActionSheetDialog actionSheet = new ActionSheetDialog(this)
                .builder()
                .setTitle("请选择支付方式")
                .setCancelable(false)
                .setCanceledOnTouchOutside(true)
                .addSheetItem(getResources().getString(R.string.cash), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        method = 0;
                        payment_method_text.setText(getResources().getString(R.string.cash));
                    }
                })
                .addSheetItem(getResources().getString(R.string.alipay), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        method = 1;
                        payment_method_text.setText(getResources().getString(R.string.alipay));
                    }
                }).addSheetItem(getResources().getString(R.string.wechat), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        method = 2;
                        payment_method_text.setText(getResources().getString(R.string.wechat));
                    }
                }).addSheetItem(getResources().getString(R.string.bank_card), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        method = 3;
                        payment_method_text.setText(getResources().getString(R.string.bank_card));
                    }
                }).addSheetItem(getResources().getString(R.string.other), ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        method = 4;
                        payment_method_text.setText(getResources().getString(R.string.other));
                    }
                });

        actionSheet.show();
    }

    @OnClick(R.id.business_time_layout)
    void business_time_layout() {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                int month = monthOfYear + 1;
                String monthDate;
                String day;
                if (month < 10) {
                    monthDate = "0" + month;
                } else {
                    monthDate = month + "";
                }
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                business_time_text.setText(year + "-" + monthDate + "-" + day);
            }
        }, TimeUtil.getCurrentYear(), TimeUtil.getCurrentMonth(), TimeUtil.getCurrentDay()).show();
    }

    @OnClick(R.id.this_payment)
    void this_payment() {
        if (this_payment.getText().equals(getResources().getString(R.string.this_payment))) {
            payment_type = 2;
            this_payment.setText(getResources().getString(R.string.supplier_refund));
        } else {
            payment_type = 1;
            this_payment.setText(getResources().getString(R.string.this_payment));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();

        if (isTouchPointInView(remarks_frame_layout, x, y)) {
            supplier_remarks.setVisibility(View.VISIBLE);
            supplier_remarks_text.setVisibility(View.GONE);
            supplier_remarks.setSelection(supplier_remarks.getText().toString().length());
        } else {
            supplier_remarks.setVisibility(View.GONE);
            supplier_remarks_text.setVisibility(View.VISIBLE);
            String content = supplier_remarks.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                supplier_remarks_text.setText(content);
            } else {
                supplier_remarks_text.setText("");
                supplier_remarks.setVisibility(View.VISIBLE);
                supplier_remarks_text.setVisibility(View.GONE);
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
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    @OnClick(R.id.confirm)
    void confirm() {
        remark = supplier_remarks.getText().toString();
        date = business_time_text.getText().toString();
        switch (method) {
            case 0:
                cash = Double.parseDouble(this_payment_text.getText().toString());
                break;
            case 1:
                alipay = Double.parseDouble(this_payment_text.getText().toString());
                break;
            case 2:
                wechat = Double.parseDouble(this_payment_text.getText().toString());
                break;
            case 3:
                card = Double.parseDouble(this_payment_text.getText().toString());
                break;
            case 4:
                other = Double.parseDouble(this_payment_text.getText().toString());
                break;
        }
        orderEasyPresenter.supplierPay(bean.getSupplier_id(), 0, payment_type, cash, wechat, alipay, card, other, remark, date);
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

        if (data != null) {
            Log.e("SupplierPaymentActivity", "data:" + data.toString());
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //处理返回的数据
                if (type == 1) {
                    showToast("付款成功");
                } else {
                    showToast("退款成功");
                }
                setResult(1001);
                SupplierPaymentActivity.this.finish();
            }
        }

    }
}