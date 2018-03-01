package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ReconciliationAdapter;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.bean.SupplierIndex;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.view.OrderEasyView;

public class SupplierReconciliationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, OrderEasyView, AdapterView.OnItemClickListener {
    private ReconciliationAdapter adapter;
    private List<SupplierBean> supplierBean = new ArrayList<>();
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_reconciliation_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);//网络请求
        adapter = new ReconciliationAdapter(this);
        store_refresh.setOnRefreshListener(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(this);
        initData();
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    List<SupplierBean> list = SupplierBean.likeString1(supplierBean, s.toString());
                    if (list.size() > 0) {
                        adapter.setData(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setData(supplierBean);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    adapter.setData(supplierBean);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        refreshData(true);

    }

    @InjectView(R.id.all_supplier)
    TextView all_supplier;
    @InjectView(R.id.yingfu_supplier)
    TextView yingfu_supplier;
    @InjectView(R.id.yingfu_money)
    TextView yingfu_money;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.et_search)
    EditText et_search;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }


    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.supplierIndex();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("SupplierManagement", "data:" + data.toString());
        if (data != null) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //成功
                supplierBean.clear();
                JsonArray jsonArray = data.getAsJsonObject("result").getAsJsonArray("list");
                int number = 0;
                if (jsonArray.size() > 0) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        SupplierBean supplierBean1 = (SupplierBean) GsonUtils.getEntity(jsonArray.get(i).toString(), SupplierBean.class);
                        supplierBean.add(supplierBean1);
                        if (supplierBean1.getIs_retail() == 0) {
                            number++;
                        }
                    }
                    adapter.setData(supplierBean);
                    adapter.notifyDataSetChanged();
                    all_supplier.setText(supplierBean.size() + "");
                    yingfu_supplier.setText(supplierBean.size() - number + "");
                    double num = 0.00;
                    double num1 = 0.00;
                    for (SupplierBean Bean : supplierBean) {
                        if (Bean.getIs_retail() == 1) {
                            num += Bean.getDebt();
                        } else {
                            num1 += Bean.getDebt();
                        }
                    }
                    num = num - num1;
                    yingfu_money.setText("¥" + num);
                }

                if (jsonArray.size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        SupplierBean phoneBook = (SupplierBean) adapter.getItem(position);
        Intent intent = new Intent(this, PurchaseRecordActivity.class);
        intent.putExtra("data", phoneBook);
        startActivity(intent);
    }
}
