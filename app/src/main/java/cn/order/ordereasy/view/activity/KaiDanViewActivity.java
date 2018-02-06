package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import butterknife.OnTextChanged;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.AddOrderCustomerAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ClearEditText;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/17.
 */

public class KaiDanViewActivity extends BaseActivity implements OrderEasyView{

    OrderEasyPresenter orderEasyPresenter;
    List<Customer> data=new ArrayList<>();
    Customer customer=new Customer();
    AddOrderCustomerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kaidan_view);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter=new OrderEasyPresenterImp(this);
        orderEasyPresenter.getCustomerList();
        adapter=new AddOrderCustomerAdapter(this,data);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null){
            customer= (Customer) bundle.getSerializable("data");
            if(customer!=null){
                if(customer.getCustomer_id()!=0){
                    cust_name.setText(customer.getCustomer_name());
                    cust_tel.setText(customer.getTelephone());
                }
            }
        }
        view_view.setVisibility(View.GONE);
        kehu_listview.setAdapter(adapter);
        kehu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent();
                Bundle bundle=new Bundle();
                Customer customer=new Customer();
                customer.setCustomer_id(adapter.getData().get(position).getCustomer_id());
                customer.setCustomer_name(adapter.getData().get(position).getName());
                customer.setTelephone(adapter.getData().get(position).getTelephone());
                customer.setAddress(adapter.getData().get(position).getAddress());
                customer.setReceivable(adapter.getData().get(position).getReceivable());
                bundle.putSerializable("data",customer);
                intent.putExtras(bundle);
                setResult(1001,intent);
                finish();
            }
        });
        cust_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    List<Customer> list=Customer.likeString(data,s.toString());
                    if(list.size()>0){
                        view_view.setVisibility(View.VISIBLE);
                        adapter.setData(list);
                        adapter.notifyDataSetChanged();
                    }else{
                        //list=new ArrayList<Customer>();
                        adapter.setData(data);
                        adapter.notifyDataSetChanged();
                        view_view.setVisibility(View.GONE);
                    }

                }else{
                   // List<Customer> list=new ArrayList<Customer>();
                    adapter.setData(data);
                    adapter.notifyDataSetChanged();
                    view_view.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //输入客户名称
    @InjectView(R.id.cust_name)
    ClearEditText cust_name;

    //输入客户名称
    @InjectView(R.id.lingshouke)
    TextView lingshouke;


    //输入客户手机号码
    @InjectView(R.id.cust_tel)
    ClearEditText cust_tel;

    //通讯录按钮
    @InjectView(R.id.customers_list)
    ImageView customers_list;

    //listview 视图
    @InjectView(R.id.view_view)
    LinearLayout view_view;

    //ListView
    @InjectView(R.id.kehu_listview)
    ListView kehu_listview;

    @InjectView(R.id.queding)
    TextView queding;



    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        KaiDanViewActivity.this.finish();
    }


    @OnClick(R.id.customers_list)
    void selectCusts(){
        Intent intent =new Intent(KaiDanViewActivity.this,SelectCustomersActivity.class);
        //利用bundle来存取数据
        Bundle bundle=new Bundle();
        bundle.putString("flag","order");
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent,1004);
    }

    @OnClick(R.id.lingshouke)
    void lingshouke(){
        Customer customer=new Customer();
        SharedPreferences sp=getSharedPreferences("customers",0);
        String data=sp.getString("customers","");
        if(!TextUtils.isEmpty(data)){
            JsonArray array = (JsonArray) GsonUtils.getObj(data,JsonArray.class);
            for(int i=0;i<array.size();i++){
                Customer c= (Customer) GsonUtils.getEntity(array.get(i).toString(),Customer.class);
                if(c.getName().equals("零售客")){
                    customer.setCustomer_name(c.getCustomer_name());
                    customer.setName(c.getName());
                    customer.setTelephone(c.getTelephone());
                    customer.setAddress(c.getAddress());
                    customer.setCustomer_id(c.getCustomer_id());

                }
            }
        }
        Intent intent =new Intent();
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",customer);
        intent.putExtras(bundle);
        setResult(1001,intent);
        finish();
    }

    @OnClick(R.id.queding)
    void queding(){
        String name= cust_name.getText().toString();
        String tel=cust_tel.getText().toString();
        if(TextUtils.isEmpty(name)){
            showToast("姓名不能为空！");
            return;
        }
        if(TextUtils.isEmpty(tel)){
            showToast("电话号码不能为空！");
            return;
        }
        if(customer.getCustomer_id()!=0){
            customer.setName(name);
            orderEasyPresenter.updateCustomer(customer);
        }else{
            Customer customer=new Customer();
            customer.setCustomer_name(name);
            customer.setName(name);
            customer.setTelephone(tel);
            orderEasyPresenter.addCustomer(customer);
        }
        ProgressUtil.showDialog(this);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        Message message=new Message();
        ProgressUtil.dissDialog();
        switch (type){
            case 0:
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1001;

                }
                break;
            case 1:
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1002;

                }
                break;
            case 2:
                message=new Message();
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1003;
                }
                break;
            case 3:
                message=new Message();
                if(data==null){
                    message.what=1007;
                }else{
                    message.what=1004;

                }

                break;
            default:
                break;
        }
        message.obj=data;
        handler.sendMessage(message);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1001:
                    JsonObject result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        if(status==1){
							JsonArray jsonArray =result.get("result").getAsJsonArray();
                            if(jsonArray.size()>0){
                                SharedPreferences sp=getSharedPreferences("customers",0);
                                sp.edit().putString("customers",result.get("result").toString()).commit();
                            }
							for(int i=0;i<jsonArray.size();i++){
								Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(),Customer.class);
								data.add(customer);
							}

							/*adapter.setData(data);
							adapter.notifyDataSetChanged();*/
                        }
                    }
                    Log.e("信息",result.toString());
                    break;
                case 1002:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){
                            Intent intent =new Intent();
                            Bundle bundle=new Bundle();
                            Customer customer=new Customer();
                            customer.setCustomer_id(result.get("result").getAsJsonObject().get("customer_id").getAsInt());
                            customer.setCustomer_name(result.get("result").getAsJsonObject().get("name").getAsString());
                            customer.setTelephone(result.get("result").getAsJsonObject().get("telephone").getAsString());
                            customer.setReceivable(result.get("result").getAsJsonObject().get("receivable").getAsDouble());
                            bundle.putSerializable("data",customer);
                            intent.putExtras(bundle);
                            setResult(1001,intent);
                            finish();
                        }
                    }
                    Log.e("商品信息",result.toString());
                    break;
                case 1003:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){
                            Intent intent =new Intent();
                            Bundle bundle=new Bundle();
                            Customer customer=new Customer();
                            customer.setCustomer_id(result.get("result").getAsJsonObject().get("customer_id").getAsInt());
                            customer.setCustomer_name(result.get("result").getAsJsonObject().get("name").getAsString());
                            bundle.putSerializable("data",customer);
                            intent.putExtras(bundle);
                            setResult(1001,intent);
                            finish();
                        }
                    }
                    Log.e("保存信息",result.toString());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 1004){
            Bundle bundle=data.getExtras();
            Customer customer= (Customer) bundle.getSerializable("data");
            Intent intent =new Intent();
            bundle.putSerializable("data",customer);
            intent.putExtras(bundle);
            setResult(1004,intent);
            finish();

        }
    }
}
