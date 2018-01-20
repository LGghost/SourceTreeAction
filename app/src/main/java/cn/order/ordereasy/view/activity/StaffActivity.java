package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.MyEmployeeAdapter;
import cn.order.ordereasy.bean.Employee;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/13.
 * <p>
 * <p>
 * 首页员工管理
 */
public class StaffActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private OrderEasyPresenter orderEasyPresenter;
    private MyEmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.staff);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initRefreshLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    //下拉刷新控件
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    private void initRefreshLayout() {
        //获取老板和员工的数据
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        list_view.setOnItemClickListener(this);
        refreshData(true);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //添加员工按钮
    @InjectView(R.id.add_yuangong)
    ImageView add_yuangong;

    @InjectView(R.id.yuangong_num)
    TextView yuangong_num;

    //ListView
    @InjectView(R.id.list_view)
    ListView list_view;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        StaffActivity.this.finish();
    }

    //添加员工按钮
    @OnClick(R.id.add_yuangong)
    void add_yuangong() {
        Intent intent = new Intent(StaffActivity.this, AddStaffActivity.class);
        startActivityForResult(intent, 1001);
    }


    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("StaffActivity", "resultCode" + resultCode);
        if (resultCode == 1001) {
            Log.e("StaffActivity", "onActivityResult");
            refreshData(false);
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);
        int status = data.get("code").getAsInt();
        if (status == 1 && type == 1) {
            ProgressUtil.dissDialog();
            Log.e("Staff", "data" + data.toString());
            Employee employee = (Employee) GsonUtils.getEntity(data.toString(), Employee.class);
            adapter = new MyEmployeeAdapter(employee.result, this.getApplicationContext(), list_view);
            list_view.setAdapter(adapter);
            yuangong_num.setText("( " + employee.result.size() + " )");
            if (employee.result.size() > 0) {
                no_data_view.setVisibility(View.GONE);
            } else {
                no_data_view.setVisibility(View.VISIBLE);
            }
        } else {
            ProgressUtil.dissDialog();
            if (status == -1 || status == -9) {
                String message = data.get("message").getAsString();
                ToastUtil.show(message);
                no_data_view.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    private void refreshData(boolean isRefresh) {
        if (isRefresh) {
            ProgressUtil.showDialog(StaffActivity.this);
        }
        orderEasyPresenter.getEmployee(1);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getData().get(position).is_boss == 0) {
            //证明他为员工，是可以删除的
            Intent intent = new Intent();
            intent.setClass(this, EmployeeInformationActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", adapter.getData().get(position));
            intent.putExtras(bundle);
            this.startActivityForResult(intent, 1001);
        }
    }
}
