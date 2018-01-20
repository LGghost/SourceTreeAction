package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.sql.Time;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SelectAddrAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.DiscountCustomer;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.MainActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/9/21.
 */

public class ModifyCustomerActivity extends BaseActivity implements OrderEasyView, EasyPermissions.PermissionCallbacks, BGAOnItemChildClickListener {

    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;
    Customer customer;
    List<String> addrs = new ArrayList<>();
    SelectAddrAdapter selectAddrAdapter;
    private int rank_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_customer);
        setColor(this, this.getResources().getColor(R.color.lanse));
        Bundle bundle = getIntent().getExtras();
        int id = bundle.getInt("id");
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        ProgressUtil.showDialog(this);
        orderEasyPresenter.getCustomerInfo(id);
        ButterKnife.inject(this);
        selectAddrAdapter = new SelectAddrAdapter(this);
        selectAddrAdapter.setData(addrs);
        selectAddrAdapter.setOnItemChildClickListener(this);
        addr_listview.setAdapter(selectAddrAdapter);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //客户名字输入
    @InjectView(R.id.kehu_name)
    ClearEditText kehu_name;

    //客户手机号码输入
    @InjectView(R.id.kehu_phone_number)
    ClearEditText kehu_phone_number;

    //点击进入通讯录
    @InjectView(R.id.tongxunlu)
    ImageView tongxunlu;

    //点击添加地址
    @InjectView(R.id.add_address)
    LinearLayout add_address;

    //地址ListView
    @InjectView(R.id.addr_listview)
    ListView addr_listview;
    //客户分类
    @InjectView(R.id.customer_fenlei)
    TextView customer_fenlei;
    //折扣
    @InjectView(R.id.add_customer_discount)
    TextView add_customer_discount;

    //删除客户
    @InjectView(R.id.delete_btn)
    Button delete_btn;
    //保存按钮
    @InjectView(R.id.baocun_text)
    TextView baocun_text;

    @OnClick(R.id.tongxunlu)
    void tongxunlu() {
        choiceContact();
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        ModifyCustomerActivity.this.finish();
    }

    @OnClick(R.id.add_address)
    void add_address() {
        showdialogs(-1);
    }

    //添加客户分类
    @OnClick(R.id.add_customer_feilei)
    void add_customer_feilei() {
        Intent intent = new Intent(this, CustomerManageActivity.class);
        intent.putExtra("flag", "newCustomer");
        if (rank_id == 0) {
            intent.putExtra("rank_id", -1);
        } else {
            intent.putExtra("rank_id", rank_id);
        }
        startActivityForResult(intent, 1001);
    }

    @OnClick(R.id.baocun_text)
    void save() {
        String name = kehu_name.getText().toString();
        String tel = kehu_phone_number.getText().toString();
        List<String> address = addrs;
        Customer customer = new Customer();
        customer.setAddress(address);
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show("请输入客户姓名");
            return;
        }
        if (TextUtils.isEmpty(tel)) {
            ToastUtil.show("请输入客户电话");
            return;
        }
        if (!name.equals(this.customer.getName())) {
            customer.setCustomer_name(name);
        }
        if (address.size() < 1) {
            showToast("请至少填写一个地址！");
            return;
        }
        if (!tel.equals(this.customer.getTelephone())) {
            customer.setTelephone(tel);
        }

        customer.setCustomer_id(this.customer.getCustomer_id());
        customer.setRank_id(rank_id);
        orderEasyPresenter.updateCustomer(customer);
    }

    //删除
    @OnClick(R.id.delete_btn)
    void delete_btn() {
        orderEasyPresenter.delCustomer(customer.getCustomer_id());
    }

    //弹出框
    private void showdialogs(final int pos) {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入物流地址");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);

        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
        //hint内容
        ed_type_name.setHint("");
        //限制输入长度
//        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        if (pos != -1) {
            String str = addrs.get(pos);
            ed_type_name.setHint(str);
        }

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
                String addr = ed_type_name.getText().toString();
                if (!TextUtils.isEmpty(addr)) {
                    if (pos != -1) {
                        addrs.set(pos, addr);
                    } else {
                        if (addrs.size() >= 10) {
                            ToastUtil.show("添加地址已达上线，不能添加地址");
                            alertDialog.dismiss();
                            return;
                        }
                        addrs.add(addr);
                    }
                    selectAddrAdapter.setData(addrs);
                    selectAddrAdapter.notifyDataSetChanged();
                    alertDialog.dismiss();
                } else {
                    ToastUtil.show("请输入地址");
                }
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
        Log.e("ModifyCustomer", "type:" + type);
        Message message = new Message();
        ProgressUtil.dissDialog();
        switch (type) {
            case 0:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;

                }
                break;
            case 2:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;
                }
                break;
            case 3:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1004;

                }

                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            showToast("删除成功");
                            Intent intent = new Intent(ModifyCustomerActivity.this, MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            DataStorageUtils.getInstance().setCustomer(true);
                            finish();
                        } else {
                            if (status == -1||status == -9) {
                                String message = result.get("message").getAsString();
                                ToastUtil.show(message);
                            }
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(ModifyCustomerActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            showToast("修改成功");
                            Customer cust = (Customer) GsonUtils.getEntity(result.get("result").toString(), Customer.class);
                            setResult(1004);
                            finish();
                        } else {
                            if (status == -1||status == -9) {
                                String message = result.get("message").getAsString();
                                ToastUtil.show(message);
                            }
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(ModifyCustomerActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //处理返回的数据
                            Customer cust = (Customer) GsonUtils.getEntity(result.get("result").toString(), Customer.class);
                            List<String> adds = cust.getAddress();
                            if (adds == null) adds = new ArrayList<>();
                            selectAddrAdapter.setData(adds);
                            customer = cust;
                            addrs = adds;
                            initData();
                            selectAddrAdapter.notifyDataSetChanged();
                        } else {
                            if (status == -1||status == -9) {
                                String message = result.get("message").getAsString();
                                ToastUtil.show(message);
                            }
                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1007:
                    ToastUtil.show("出错了哟~");
                    break;
                case 9999:
                    ToastUtil.show("网络有问题哟~");
                    break;
            }
        }
    };

    void initData() {
        kehu_name.setText(customer.getName());
        kehu_phone_number.setText(customer.getTelephone());
        rank_id = customer.getRank_id();
        customer_fenlei.setText(customer.getRank_name());
        DecimalFormat df = new DecimalFormat("0.0");
        String result = df.format((double) customer.getRank_discount() / 10);
        if (Double.parseDouble(result) == 10) {
            add_customer_discount.setText("无折扣");
        } else {
            add_customer_discount.setText(result + "折");
        }

    }

    /*跳转联系人列表的回调函数*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                if (data == null) {
                    return;
                }
                //处理返回的data,获取选择的联系人信息
                Uri uri = data.getData();
                String[] contacts = getPhoneContacts(uri);
                kehu_name.setText(contacts[0]);
                kehu_phone_number.setText(contacts[1]);
                break;
            case 1001:
                if (data == null) {
                    return;
                }
                DiscountCustomer customer = (DiscountCustomer) data.getExtras().getSerializable("data");
                rank_id = customer.getRank_id();
                customer_fenlei.setText(customer.getRank_name());
                DecimalFormat df = new DecimalFormat("0.0");
                String result = df.format((double) customer.getRank_discount() / 10);
                if (Double.parseDouble(result) == 10) {
                    add_customer_discount.setText("无折扣");
                } else {
                    add_customer_discount.setText(result + "折");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            }
            phone.close();
            cursor.close();
        } else {
            return null;
        }
        return contact;
    }

    private static final int REQUEST_CODE_PERMISSION_TEL = 1;

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TEL)
    private void choiceContact() {
        String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Uri uri = Uri.parse("content://contacts/people");
            Intent intent = new Intent(Intent.ACTION_PICK, uri);
            startActivityForResult(intent, 0);
        } else {
            EasyPermissions.requestPermissions(this, "订货无忧需要以下权限:\n\n1.获取通讯录信息\n\n2.手机状态信息", REQUEST_CODE_PERMISSION_TEL, perms);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_TEL) {
            Toast.makeText(this, "您拒绝了「订货无忧」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {

        if (childView.getId() == R.id.addr_edit) {
            showdialogs(position);
        } else if (childView.getId() == R.id.tv_item_swipe_delete) {
            addrs.remove(position);
            selectAddrAdapter.closeOpenedSwipeItemLayout();
            selectAddrAdapter.notifyDataSetChanged();
        }
    }
}
