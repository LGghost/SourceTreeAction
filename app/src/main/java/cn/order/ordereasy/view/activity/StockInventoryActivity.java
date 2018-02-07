package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
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
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.InventoryListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.Inventory;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.LoadMoreListView;

/**
 * Created by Administrator on 2017/9/7.
 * <p>
 * 库存盘点Activity
 */

public class StockInventoryActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, LoadMoreListView.OnLoadMoreListener, BGAOnItemChildClickListener {

    OrderEasyPresenter orderEasyPresenter;
    InventoryListAdapter adapter;
    AlertDialog alertDialog;
    private int pageCurrent = 1, pageTotal = 1;
    List<Inventory> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        orderEasyPresenter = new OrderEasyPresenterImp(this);
        setContentView(R.layout.stock_inventory);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new InventoryListAdapter(this);
        listView.setAdapter(adapter);
        refreshData(true);
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Inventory data = adapter.getData().get(position);
                Intent intent = new Intent(StockInventoryActivity.this, InventorySheetActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", data);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1003);
            }
        });
        adapter.setOnItemChildClickListener(this);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //
    @InjectView(R.id.new_pandian_onclick)
    LinearLayout new_pandian_onclick;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;

    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText("上次盘点未结束，无法新建盘点单");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("我知道了");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setVisibility(View.GONE);
        View view1 = (View) window.findViewById(R.id.view1);
        view1.setVisibility(View.GONE);
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        StockInventoryActivity.this.finish();
    }

    //返回按钮
    @OnClick(R.id.new_pandian_onclick)
    void new_pandian_onclick() {
        for (Inventory inventory : adapter.getData()) {
            if (inventory.getIs_complete() != 1) {
                showdialogs();
                return;
            }
        }
        orderEasyPresenter.addInventory();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            boolean isEnd = data.getBooleanExtra("isEnd", false);
            if (isEnd) {
                pageCurrent = 1;
                refreshData(false);
            }
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        listView.setLoadCompleted();
        store_refresh.setRefreshing(false);
        Message message = new Message();
        switch (type) {
            case 0:
                ProgressUtil.dissDialog();
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
                            if (pageCurrent == 1) {
                                if (data.size() > 0) {
                                    data.clear();
                                }
                            }
                            pageTotal = page.get("page_total").getAsInt();
                            JsonArray array = result.get("result").getAsJsonObject().get("page_list").getAsJsonArray();
                            for (int i = 0; i < array.size(); i++) {
                                Inventory inventory = (Inventory) GsonUtils.getEntity(array.get(i).toString(), Inventory.class);
                                data.add(inventory);
                            }
                            adapter.setData(data);
                            adapter.notifyDataSetChanged();
                            if (adapter.getData().size() > 0) {
                                no_data_view.setVisibility(View.GONE);
                            } else {
                                no_data_view.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    Log.e("盘点信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            Log.e("StockInventoryActivity", "" + result.get("result").getAsJsonObject().get("info").toString());
                            Inventory inventory = (Inventory) GsonUtils.getEntity(result.get("result").getAsJsonObject().get("info").toString(), Inventory.class);
                            data.add(0, inventory);
                            adapter.setData(data);
                            adapter.notifyDataSetChanged();
                            Intent intent = new Intent(StockInventoryActivity.this, InventorySheetActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("data", inventory);
                            intent.putExtras(bundle);
                            startActivityForResult(intent, 1001);
                        }
                    }
                    Log.e("新建信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            ToastUtil.show("删除成功");
                        }
                    }
                    Log.e("新建信息", result.toString());
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
    public void onloadMore() {
        //上拉加载更多 完成时关闭
        if (pageTotal == pageCurrent) {
            ToastUtil.show("没有更多数据了");
            listView.setIsLoading(true);
        } else {
            pageCurrent++;
            refreshData(false);
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新 完成时关闭
        pageCurrent = 1;
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getInventoryList(pageCurrent);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        final int id= adapter.getData().get(position).getInventory_id();
        if(adapter.getData().get(position).getIs_complete() != 1){
            if (childView.getId() == R.id.tv_item_swipe_delete) {
                orderEasyPresenter.inventoryInfo(id);
                adapter.closeOpenedSwipeItemLayout();
                adapter.removeItem(position);
                adapter.notifyDataSetChanged();
            }
        }else {
            ToastUtil.show("已完成的盘点不能删除");
        }

    }
}
