package cn.order.ordereasy.view.fragment.gooddetailsfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.DetailCustomersAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.CustomerHomepageActivity;
import cn.order.ordereasy.view.activity.LoginActity;

public class FragmentCustomer extends Fragment implements AbsListView.OnScrollListener, OrderEasyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    //listView
    private ListView lsitview;
    //悬浮部分layout
    private LinearLayout invisLayout;
    private OrderEasyPresenter orderEasyPresenter;
    private DetailCustomersAdapter detailCustomersAdapter;
    private int goodId = 0;
    private TextView textView;
    private SwipeRefreshLayout refresh_layout;
    private ImageView no_data_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_stcok_fragment_layout, container, false);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        detailCustomersAdapter = new DetailCustomersAdapter(getActivity(), new ArrayList<Customer>());
        refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        no_data_view = (ImageView) view.findViewById(R.id.no_data_view);
        refresh_layout.setOnRefreshListener(this);
        lsitview = (ListView) view.findViewById(R.id.listview);
        invisLayout = (LinearLayout) view.findViewById(R.id.invis_layout);
        View header = View.inflate(getActivity(), R.layout.invis_layout, null);
        textView = (TextView) header.findViewById(R.id.textView10);
        textView.setText("购买了此货品的客户");
        lsitview.addHeaderView(header);
        lsitview.setOnScrollListener(this);
        lsitview.setOnItemClickListener(this);
        lsitview.setAdapter(detailCustomersAdapter);
        refreshData(true);
        return view;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    public void setGoodId(int goodId) {
        this.goodId = goodId;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem >= 1) {
            invisLayout.setVisibility(View.VISIBLE);
        } else {
            invisLayout.setVisibility(View.GONE);
        }
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
    public void loadData(JsonObject data, int type) {
        refresh_layout.setRefreshing(false);
        if (type == 2) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    //成功
                    JsonArray customers = data.getAsJsonObject("result").getAsJsonArray("page_list");
                    List<Customer> custs = new ArrayList<>();
                    for (int i = 0; i < customers.size(); i++) {
                        Customer customer = (Customer) GsonUtils.getEntity(customers.get(i).toString(), Customer.class);
                        custs.add(customer);
                    }
                    detailCustomersAdapter.setData(custs);
                    detailCustomersAdapter.notifyDataSetChanged();
                    if (custs.size() > 0) {
                        no_data_view.setVisibility(View.GONE);
                    } else {
                        no_data_view.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (status == -7) {
                        ToastUtil.show(getString(R.string.landfall_overdue));
                        Intent intent = new Intent(getActivity(), LoginActity.class);
                        startActivity(intent);
                    }
                }
            }
            Log.e("客户信息", data.toString());
        }
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(getActivity());
        }
        orderEasyPresenter.getGoodsCustomers(goodId);
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 0) {
            Customer customer1 = detailCustomersAdapter.getData().get(position - 1);
            Customer customer2 = new Customer();
            List<Customer> customerList = DataStorageUtils.getInstance().getCustomerLists();
            for (Customer customer : customerList) {
                if (customer1.getCustomer_id() == customer.getCustomer_id()) {
                    customer2 = customer;
                }
            }
            Intent intent = new Intent(getActivity(), CustomerHomepageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", customer2);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}