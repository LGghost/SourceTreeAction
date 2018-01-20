package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;

public class SetupDiscountActivity extends BaseActivity {
    private int discount;
    private String flag = "add";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_up_discount_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            flag = bundle.getString("flag");
        }

        discount_edit_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    discount_edit_two.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        discount_edit_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    discount_edit_one.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @InjectView(R.id.discount_edit_one)
    EditText discount_edit_one;
    @InjectView(R.id.discount_edit_two)
    EditText discount_edit_two;


    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.discount_confirm)
    void discount_confirm() {
        int one, two;
        if (TextUtils.isEmpty(discount_edit_one.getText().toString())) {
            one = 0;
        } else {
            one = Integer.parseInt(discount_edit_one.getText().toString());
        }
        if (TextUtils.isEmpty(discount_edit_two.getText().toString())) {
            two = 0;
        } else {
            two = Integer.parseInt(discount_edit_two.getText().toString());
        }
        discount = one * 10 + two;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("discount", discount);
        intent.putExtras(bundle);
        if (flag.equals("billing")) {
            setResult(1006, intent);
        } else {
            setResult(1001, intent);
        }
        finish();

    }

    @OnClick(R.id.layout)
    void layout() {
        discount = 100;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putInt("discount", discount);
        intent.putExtras(bundle);
        if (flag.equals("billing")) {
            setResult(1006, intent);
        } else {
            setResult(1001, intent);
        }
        finish();
    }

}
