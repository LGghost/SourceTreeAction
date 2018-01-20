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

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.DetailOrdersAdapter;
import cn.order.ordereasy.adapter.DetailSpecsAdapter;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.view.activity.OrderNoDetailsActivity;
import cn.order.ordereasy.widget.NoScrollListView;

public class FragmentChildStcok extends Fragment implements AbsListView.OnScrollListener, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {
    private ListView lsitview;
    //悬浮部分layout
    private LinearLayout invisLayout;
    private NoScrollListView specs_list;
    private TextView kucun_num;
    private TextView qianhuo_num;
    private DetailSpecsAdapter detailSpecsAdapter;
    private DetailOrdersAdapter detailOrdersAdapter;
    private SwipeRefreshLayout refresh_layout;
    private boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.child_stcok_fragment_layout, container, false);
        lsitview = (ListView) view.findViewById(R.id.listview);
        refresh_layout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(this);
        invisLayout = (LinearLayout) view.findViewById(R.id.invis_layout);
        detailSpecsAdapter = new DetailSpecsAdapter(getActivity(), new ArrayList<Product>());
        detailOrdersAdapter = new DetailOrdersAdapter(getActivity(), new ArrayList<Order>());
        //取得页面可滚动的其他部分
        View header = View.inflate(getActivity(), R.layout.header_layout, null);
        specs_list = (NoScrollListView) header.findViewById(R.id.specs_list);
        qianhuo_num = (TextView) header.findViewById(R.id.qianhuo_num);
        specs_list.setAdapter(detailSpecsAdapter);
        //添加到listView头部
        lsitview.addHeaderView(header);
        lsitview.setOnScrollListener(this);
        lsitview.setAdapter(detailOrdersAdapter);
        lsitview.setOnItemClickListener(this);
        return view;
    }

    public void setData(List<Product> products, int store_num, int owe_num) {
        //这两个参数怎么又没有
        qianhuo_num.setText("总库存:" + store_num + ", 总欠货:" + owe_num);
        detailSpecsAdapter.setData(products);
        detailSpecsAdapter.notifyDataSetChanged();
    }

    public void setOrdersList(List<Order> orders) {
        Log.e("FragmentChildStcok", "orders" + orders.size());
        if (orders.size() > 0) {
            //取得ListView条目中的悬浮部分(itemLayout)，并将其添加到头部
            if (isFirst) {
                isFirst = false;
                lsitview.addHeaderView(View.inflate(getActivity(), R.layout.invis_layout, null));
            }
            detailOrdersAdapter.setData(orders);
            detailOrdersAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

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
    public void onRefresh() {
        ((DetailsGoodsActivity) getActivity()).refreshData(false);
    }

    public void endRefreshing() {
        if (refresh_layout != null && refresh_layout.isRefreshing()) {
            refresh_layout.setRefreshing(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position > 1) {
            Intent intent = new Intent(getActivity(), OrderNoDetailsActivity.class);
            int order_id = detailOrdersAdapter.getData().get(position - 2).getOrder_id();
            Bundle bundle = new Bundle();
            bundle.putInt("id", order_id);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}