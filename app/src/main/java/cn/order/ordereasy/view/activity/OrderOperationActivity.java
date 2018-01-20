package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderOperationAdapter;
import cn.order.ordereasy.bean.OrderOperation;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class OrderOperationActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private OrderEasyPresenter orderEasyPresenter;
    private OrderOperationAdapter adapter;
    private List<OrderOperation> datas;
    private int order_id;
    private int ordre_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_operation_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        listview.setOnItemClickListener(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            order_id = bundle.getInt("order_id");
            ordre_type= bundle.getInt("order_type");
            refreshData(true);
        }
        adapter = new OrderOperationAdapter(this,ordre_type);
        listview.setAdapter(adapter);
    }

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    ListView listview;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        if (type == 0) {
            Log.e("OrderOperation", "data:" + data.toString());
            int status = data.get("code").getAsInt();
            if (status == 1) {
                datas = new ArrayList<>();
                JsonArray jsonArray = data.get("result").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    OrderOperation orderOperation = (OrderOperation) GsonUtils.getEntity(jsonArray.get(i).toString(), OrderOperation.class);
                    datas.add(orderOperation);
                }
                if (datas.size() > 0) {
                    store_refresh.setVisibility(View.VISIBLE);
                    no_data_view.setVisibility(View.GONE);
                    adapter.setData(datas);
                } else {
                    store_refresh.setVisibility(View.GONE);
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getOrderLogs(order_id);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getItem(position).getLog_type() == 4) {
            Intent intent = new Intent(this, OperationRecordActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("id", adapter.getItem(position).getLog_data().getOperate_id());
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (adapter.getItem(position).getLog_type() == 5) {
            Intent intent = new Intent(this, OrderNoDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", adapter.getItem(position).getLog_data().getOrder_id());
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }
}