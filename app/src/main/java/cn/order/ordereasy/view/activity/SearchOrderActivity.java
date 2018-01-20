package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SearchOrderListAdapter;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Mr.Pan on 2017/10/16.
 */

public class SearchOrderActivity extends BaseActivity implements OrderEasyView {
    OrderEasyPresenter orderEasyPresenter;
    SearchOrderListAdapter adapter;
    List<OrderList> orders = new ArrayList<>();

    //当前页数
    private int currentPage = 1, totalSize = 0, pageSize = 0, pageTotal = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_goods_two);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        adapter = new SearchOrderListAdapter(this);
        sousuo_listview.setAdapter(adapter);
        sousuo_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchOrderActivity.this, OrderNoDetailsActivity.class);
                int order_id = adapter.getData().get(position).getOrder_id();
                Bundle bundle = new Bundle();
                bundle.putInt("id", order_id);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
        initData();

    }

    private void initData() {
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    orderEasyPresenter.searchRecordList(1, s.toString());
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


    @InjectView(R.id.sousuo_listview)
    ListView sousuo_listview;
    @InjectView(R.id.et_search)
    EditText et_search;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        SearchOrderActivity.this.finish();
    }


    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
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
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //分页处理
                            JsonObject page = result.getAsJsonObject("result").getAsJsonObject("page");
                            MyLog.e("page", page.toString());
                            totalSize = page.get("total").getAsInt();
                            currentPage = page.get("cur_page").getAsInt();
                            pageSize = page.get("page_size").getAsInt();
                            pageTotal = page.get("page_total").getAsInt();
                            if (currentPage == 1) orders = new ArrayList<>();
                            //列表显示
                            JsonArray jsonArray = result.getAsJsonObject("result").getAsJsonArray("page_list");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                OrderList good = (OrderList) GsonUtils.getEntity(jsonArray.get(i).toString(), OrderList.class);
                                orders.add(good);
                            }
                            adapter.setData(orders);
                            adapter.notifyDataSetChanged();
                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(SearchOrderActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1002:

                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

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


}