package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnRVItemClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SelectAddrAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/17.\
 * <p>
 * 管理收货地址
 */

public class HarvestAddressActivity extends BaseActivity implements OrderEasyView, AdapterView.OnItemClickListener, BGAOnItemChildClickListener, BGAOnRVItemClickListener {

    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;
    SelectAddrAdapter selectAddrAdapter;
    Customer customer = new Customer();
    String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.harvest_address);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        selectAddrAdapter = new SelectAddrAdapter(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Customer customer = (Customer) bundle.getSerializable("data");
            if (customer != null) {
                this.customer.setCustomer_id(customer.getCustomer_id());
                orderEasyPresenter.getCustomerInfo(customer.getCustomer_id());
            }
            String f = bundle.getString("flag");
            if (!TextUtils.isEmpty(f)) flag = f;
        }
        harvest_addr_listview.setAdapter(selectAddrAdapter);
        harvest_addr_listview.setOnItemClickListener(this);
        selectAddrAdapter.setOnItemChildClickListener(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //添加地址按钮
    @InjectView(R.id.add_address)
    TextView add_address;

    //不选择地址按钮
    @InjectView(R.id.no_addr)
    TextView no_addr;

    //ListView
    @InjectView(R.id.harvest_addr_listview)
    ListView harvest_addr_listview;

    @OnClick(R.id.no_addr)
    void no_addr() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", "无");
        intent.putExtras(bundle);
        setResult(1003, intent);
        finish();
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        HarvestAddressActivity.this.finish();
    }

    //返回按钮
    @OnClick(R.id.add_address)
    void add_address() {
        showdialogs("", -1);
    }


    //弹出框
    private void showdialogs(String addr, final int pos) {
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
        if (!TextUtils.isEmpty(addr)) {
            ed_type_name.setHint(addr);
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
                List<String> addrs = customer.getAddress();
                if (addrs == null) addrs = new ArrayList<String>();

                String addr = ed_type_name.getText().toString();
                if (pos != -1) {
                    addrs.set(pos, addr);
                } else {
                    if (addrs.size() >= 10) {
                        ToastUtil.show("最多添加10个地址");
                        alertDialog.dismiss();
                        return;
                    }
                    addrs.add(addr);
                }
                customer.setAddress(addrs);
                orderEasyPresenter.updateCustomer(customer);
                alertDialog.dismiss();
            }
        });

        //监听edittext
        ed_type_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    queren.setTextColor(getResources().getColor(R.color.lanse));
                    queren.setEnabled(true);
                } else {
                    queren.setTextColor(getResources().getColor(R.color.touzi_huise));
                    queren.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
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
        Log.e("HarvestAddress", "type:" + type);
        Message message = new Message();
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
            JsonObject result;
            switch (msg.what) {
                case 1001:

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
                        Log.e("HarvestAddress", result.get("result").toString());
                        if (status == 1) {
                            Customer cust = (Customer) GsonUtils.getEntity(result.get("result").toString(), Customer.class);
                            List<String> addrs = cust.getAddress();
                            if (addrs == null) addrs = new ArrayList<>();
                            selectAddrAdapter.setData(addrs);
                            selectAddrAdapter.notifyDataSetChanged();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            Log.e("HarvestAddress", result.get("result").toString());
                            Customer cust = (Customer) GsonUtils.getEntity(result.get("result").toString(), Customer.class);
                            List<String> addrs = cust.getAddress();
                            if (addrs == null) addrs = new ArrayList<>();
                            selectAddrAdapter.setData(addrs);
                            customer = cust;
                            selectAddrAdapter.notifyDataSetChanged();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("信息", result.toString());
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String addr = selectAddrAdapter.getData().get(position);
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", addr);
        intent.putExtras(bundle);
        setResult(1003, intent);
        finish();
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        Log.e("HarvestAddress", "onItemChildClick");
        String addr = selectAddrAdapter.getData().get(position);
        if (childView.getId() == R.id.addr_edit) {
            showdialogs(addr, position);
        } else {
            selectAddrAdapter.getData().remove(position);
            selectAddrAdapter.closeOpenedSwipeItemLayout();
            customer.setAddress(selectAddrAdapter.getData());
            selectAddrAdapter.notifyDataSetChanged();
            orderEasyPresenter.updateCustomer(customer);

        }
    }

    @Override
    public void onRVItemClick(ViewGroup parent, View itemView, int position) {

    }
}
