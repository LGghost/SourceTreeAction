package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lljjcoder.city_20170724.CityPickerView;
import com.lljjcoder.city_20170724.bean.CityBean;
import com.lljjcoder.city_20170724.bean.DistrictBean;
import com.lljjcoder.city_20170724.bean.ProvinceBean;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.utils.ToastUtil;

public class AddSuppliersActivity extends BaseActivity {
    private String name;
    private String contacts;
    private String phone;
    private String call;
    private String address;
    private String payment;
    private String remarks;
    private String region;
    private SupplierBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_suppliers_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            bean = (SupplierBean) bundle.getSerializable("data");
            supplier_name.setText(bean.getName());
            supplier_contacts.setText(bean.getUser());
            supplier_phone.setText(bean.getPhone());
            supplier_call.setText(bean.getCall());
            supplier_address.setText(bean.getAddress());
            supplier_payment.setText(bean.getArrears() + "");
            supplier_remarks_text.setVisibility(View.VISIBLE);
            supplier_remarks.setVisibility(View.GONE);
            supplier_remarks.setText(bean.getRemarks());
            supplier_remarks_text.setText(bean.getRemarks());
            delete_supplier.setVisibility(View.VISIBLE);
        }

    }

    @InjectView(R.id.supplier_name)
    EditText supplier_name;
    @InjectView(R.id.supplier_contacts)
    EditText supplier_contacts;
    @InjectView(R.id.supplier_phone)
    EditText supplier_phone;
    @InjectView(R.id.supplier_call)
    EditText supplier_call;
    @InjectView(R.id.supplier_address)
    EditText supplier_address;
    @InjectView(R.id.supplier_payment)
    EditText supplier_payment;
    @InjectView(R.id.supplier_remarks)
    EditText supplier_remarks;
    @InjectView(R.id.supplier_address_text)
    TextView supplier_address_text;
    @InjectView(R.id.supplier_remarks_text)
    TextView supplier_remarks_text;
    @InjectView(R.id.region_text)
    TextView region_text;

    @InjectView(R.id.address_frame_layout)
    FrameLayout address_frame_layout;
    @InjectView(R.id.remarks_frame_layout)
    FrameLayout remarks_frame_layout;
    @InjectView(R.id.delete_supplier)
    LinearLayout delete_supplier;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finishActivity();
    }

    @OnClick(R.id.supplier_save)
    void supplier_save() {
        name = supplier_name.getText().toString();
        contacts = supplier_contacts.getText().toString();
        phone = supplier_phone.getText().toString();
        call = supplier_call.getText().toString();
        address = supplier_address_text.getText().toString();
        payment = supplier_payment.getText().toString();
        remarks = supplier_remarks_text.getText().toString();
        region = region_text.toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入供应商名称");
            return;
        }
        finish();
    }

    @OnClick(R.id.supplier_contacts_image)
    void supplier_contacts() {//联系人
        String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};
        Intent intent = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.delete_supplier)
    void delete_supplier() {//删除供应商

    }

    @OnClick(R.id.region_layout)
    void region_layout() {//地区
        CityPickerView cityPicker = new CityPickerView.Builder(AddSuppliersActivity.this)
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#dcdcdc")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPickerView.OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                //返回结果
                //ProvinceBean 省份信息
                //CityBean     城市信息
                //DistrictBean 区县信息
                region_text.setText(city.getName() + district.getName() + province.getName());
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (data == null) {
                return;
            }
            //处理返回的data,获取选择的联系人信息
            Uri uri = data.getData();
            String[] contacts = getPhoneContacts(uri);
            supplier_contacts.setText(contacts[0]);
            supplier_phone.setText(contacts[1]);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getRawX();
        int y = (int) ev.getRawY();
        if (isTouchPointInView(address_frame_layout, x, y)) {
            supplier_address.setVisibility(View.VISIBLE);
            supplier_address_text.setVisibility(View.GONE);
            supplier_address.setSelection(supplier_address.getText().toString().length());
        } else {
            supplier_address.setVisibility(View.GONE);
            supplier_address_text.setVisibility(View.VISIBLE);
            String content = supplier_address.getText().toString();
            if (!TextUtils.isEmpty(content)) {
                supplier_address_text.setText(content);
            } else {
                supplier_address_text.setText("");
                supplier_address.setVisibility(View.VISIBLE);
                supplier_address_text.setVisibility(View.GONE);
            }
        }

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
        //view.isClickable() &&
        if (y >= top && y <= bottom && x >= left
                && x <= right) {
            return true;
        }
        return false;
    }

    private void finishActivity() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        finish();
    }

    private String[] getPhoneContacts(Uri uri) {
        String[] contact = new String[2];
        //得到ContentResolver对象
        ContentResolver cr = getContentResolver();
        //取得电话本中开始一项的光标
        Cursor cursor = cr.query(uri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            //取得联系人姓名
            int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            contact[0] = cursor.getString(nameFieldColumnIndex);
            //取得电话号码
            String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
            if (phone != null) {
                phone.moveToFirst();
                if (phone.getCount() > 0) {
                    contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                } else {
                    contact[1] = "无";
                }
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            finishActivity();
        }
        return super.onKeyDown(keyCode, event);
    }
}
