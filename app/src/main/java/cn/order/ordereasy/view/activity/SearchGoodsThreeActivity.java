package cn.order.ordereasy.view.activity;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderSelectGoodsAdapter;
import cn.order.ordereasy.adapter.SearchCustomerAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.fragment.FragmentCust;

/**
 * Created by Administrator on 2017/9/17.
 * 搜索货品
 *
 */

public class SearchGoodsThreeActivity extends BaseActivity implements OrderEasyView{

    OrderEasyPresenter orderEasyPresenter;
    SearchCustomerAdapter adapter;
    List<Customer> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_goods_three);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter=new OrderEasyPresenterImp(this);
        adapter=new SearchCustomerAdapter(this);
        sousuo_listview.setAdapter(adapter);
        orderEasyPresenter.getCustomerList();
        sousuo_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer= (Customer) sousuo_listview.getAdapter().getItem(position);
                Intent intent=new Intent(SearchGoodsThreeActivity.this, CustomerHomepageActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("data",customer);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){
                    List<Customer> list=Customer.likeString2(datas,s.toString());
                    if(list.size()>0){
                         adapter.setData(list);
                        adapter.notifyDataSetChanged();
                    }else{
                        adapter.setData(new ArrayList<Customer>());
                        adapter.notifyDataSetChanged();
                    }

                }else{
                    adapter.setData(new ArrayList<Customer>());
                    adapter.notifyDataSetChanged();
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
    TextView return_click;
//
//    //扫一扫
//    @InjectView(R.id.saoyisao)
//    ImageView saoyisao;
    //ListView
    @InjectView(R.id.sousuo_listview)
    ListView sousuo_listview;
    @InjectView(R.id.et_search)
    EditText et_search;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        SearchGoodsThreeActivity.this.finish();
    }


//
//    //扫一扫点击事件
//    @OnClick(R.id.saoyisao)
//    void saoyisao(){
//        Intent intent =new Intent(SearchGoodsTwoActivity.this, ScanActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        Bundle bundle=new Bundle();
//        bundle.putInt("code",9001);
//        intent.putExtras(bundle);
//        startActivityForResult(intent,9001);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //扫一扫结果
//        if(resultCode==9001){
//            Bundle bundle=data.getExtras();
//            String res = bundle.getString("data");
//            if(TextUtils.isEmpty(res)){
//                showToast("未扫描到任何结果");
//            }else{
//
////				shanghuo_no.setText(res);
//            }
//            MyLog.e("扫一扫返回数据",res);
//        }
//    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        Message message=new Message();
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
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        if(status==1){
                            //成功
                            datas=new ArrayList<>();
                            JsonArray jsonArray =result.get("result").getAsJsonArray();
                            for(int i=0;i<jsonArray.size();i++){
                                Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(),Customer.class);
                                String name="";
                                if(TextUtils.isEmpty(customer.getName())){
                                    name="-";
                                }else{
                                    name=customer.getName();
                                }
                                customer.setName(name);
                                datas.add(customer);
                            }
                            initData();
                            //adapter.setData(datas);

                        }
                    }
                    Log.e("信息",result.toString());

                case 1007:
                    ToastUtil.show("出错了哟~");
                    break;
                case 9999:
                    ToastUtil.show("网络有问题哟~");
                    break;
            }
        }
    };



}
