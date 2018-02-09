package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SpecViewAdapter;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;

/**
 * Created by Administrator on 2017/9/10.
 *
 * 规格管理
 *
 */

public class SpecificationsGuanliActivity extends BaseActivity implements OrderEasyView,AdapterView.OnItemClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;
    private SpecViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guige_guanli);
        setColor(this,this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        initRefreshLayout();

        guige_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        guige_listview.setOnItemClickListener(this);
        mAdapter = new SpecViewAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
        guige_listview.setAdapter(mAdapter);
    }

    private void initRefreshLayout() {
        store_refresh.setOnRefreshListener(this);
        orderEasyPresenter.getSpecInfo();
    }

    //弹出框
    private void showdialogs() {
        alertDialog= new AlertDialog.Builder(this).create();
        View view = View.inflate(this,R.layout.tanchuang_view,null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入规格名称");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);

        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
        //hint内容
        ed_type_name.setHint("最多输入10个字符");
        //限制输入长度
        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
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
                orderEasyPresenter.addSpecCategoryInfo(ed_type_name.getText().toString());
                alertDialog.dismiss();
            }
        });
    }



    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //listview
    @InjectView(R.id.guige_listview)
    ListView guige_listview;

    //添加新规格
    @InjectView(R.id.button_click)
    LinearLayout button_click;

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void  return_click() {
        SpecificationsGuanliActivity.this.finish();
    }

    @OnClick(R.id.button_click)
    void add(){
        showdialogs();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
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
                    message.what=1003;

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
                            //成功
                            JsonArray jsonArray =result.get("result").getAsJsonArray();
                            SharedPreferences sp = getSharedPreferences("specs",0);
                            SharedPreferences.Editor edit=sp.edit();
                            List<Map<String,Object>> mapList=new ArrayList<>();
                            for(int i=0;i<jsonArray.size();i++){
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                Map<String,Object> map =new HashMap<>();
                                map.put("title",jsonObject.get("name").getAsString());
                                map.put("id",jsonObject.get("spec_id").getAsInt());
                                edit.putString(jsonObject.get("spec_id").getAsString(),jsonObject.get("values").getAsJsonArray().toString());
                                mapList.add(map);
                            }
                            edit.commit();
                            mAdapter.setData(mapList);
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
                            showToast("新增成功！");
                            orderEasyPresenter.getSpecInfo();
                        }
                    }
                    Log.e("新增信息",result.toString());
                    break;
                case 1003:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if(status==1){
                            showToast("修改成功！");
                            orderEasyPresenter.getSpecInfo();
                        }
                    }
                    Log.e("保存信息",result.toString());
                    break;
                case 1004:
                    result= (JsonObject) msg.obj;
                    if(result!=null){
                        int status=result.get("code").getAsInt();
                        if(status==1){
                            showToast("删除成功！");
                            orderEasyPresenter.getSpecInfo();
                        }
                    }
                    Log.e("保存信息",result.toString());
                    break;
            }
        }
    };

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        final int id= (int) mAdapter.getData().get(position).get("id");
        if (childView.getId() == R.id.tv_item_swipe_delete) {

            orderEasyPresenter.delSpecInfo(id);
            // 作为ListView的item使用时，如果删除了某一个item，请先关闭已经打开的item，否则其他item会显示不正常（RecyclerView不会有这个问题）
            mAdapter.closeOpenedSwipeItemLayout();
            mAdapter.removeItem(position);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String spec_id = mAdapter.getItem(position).get("id").toString();
        Intent intent=new Intent(this,GuigeShuxingListActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("name", mAdapter.getItem(position).get("title").toString());
        bundle.putString("id",spec_id);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onRefresh() {
        orderEasyPresenter.getSpecInfo();
    }
}
