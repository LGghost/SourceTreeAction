package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.AllTransactionsAdapter;
import cn.order.ordereasy.adapter.StockListAdapter;
import cn.order.ordereasy.bean.StockListBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ToastUtil;

/**
 * Created by Administrator on 2017/9/21.
 *
 * 交易额
 *
 */

public class ATurnoverActivity extends BaseActivity {

    //数据获取
    OrderEasyPresenter orderEasyPresenter;
    //适配器
    AllTransactionsAdapter allTransactionsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //数据获取初始化
//        orderEasyPresenter=new OrderEasyPresenterImp(this);
        setContentView(R.layout.a_turnover);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        //初始化adapter
        allTransactionsAdapter=new AllTransactionsAdapter(this);
        //listview设置adapter
        kehu_jiaoyi_listview.setAdapter(allTransactionsAdapter);
        //获取列表信息
//        orderEasyPresenter.getStockList(1);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //顶部标题名称
    @InjectView(R.id.title_name)
    TextView title_name;

    //顶部三角图标
    @InjectView(R.id.sanjiao_tubiao)
    ImageView sanjiao_tubiao;

    //首字母
    @InjectView(R.id.kehu_shou_zimu)
    TextView kehu_shou_zimu;

    //客户名字
    @InjectView(R.id.kehu_name)
    TextView kehu_name;

    //客户手机号码
    @InjectView(R.id.kehu_phone_number)
    TextView kehu_phone_number;

    //总交易额
    @InjectView(R.id.zong_jiaoyie)
    TextView zong_jiaoyie;

    //总销售量
    @InjectView(R.id.zong_xiaoshouliang)
    TextView zong_xiaoshouliang;

    //ListView
    @InjectView(R.id.kehu_jiaoyi_listview)
    ListView kehu_jiaoyi_listview;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        ATurnoverActivity.this.finish();
    }

//    @Override
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
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        if(status==1){
                            //处理返回的数据
                            JsonArray stocks=result.getAsJsonObject("result").getAsJsonArray("page_list");
                            List<StockListBean> datas=new ArrayList<>();//适配器数据
                            for(int i=0;i<stocks.size();i++){
                                //循环遍历获取的数据，并转成实体
                                StockListBean stock=(StockListBean) GsonUtils.getEntity(stocks.get(i).toString(),StockListBean.class);
                                datas.add(stock);
                            }

                            // 为适配器设置数据
                            allTransactionsAdapter.setData(datas);
                            allTransactionsAdapter.notifyDataSetChanged();
                        }else{
                            if(status==-7){
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(ATurnoverActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("交易信息",result.toString());
                    break;
                case 1002:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){

                        }else{
                            if(status==-7){
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(ATurnoverActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("信息",result.toString());
                    break;
                case 1003:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){

                        }else{
                            if(status==-7){
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(ATurnoverActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("保存信息",result.toString());
                    break;
                case 1004:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){

                        }else{

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
}
