package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ReconciliationAdapter;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.bean.SupplierIndex;

public class SupplierReconciliationActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private ReconciliationAdapter adapter;
    private List<SupplierBean> supplierBean = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_reconciliation_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new ReconciliationAdapter(this);
        store_refresh.setOnRefreshListener(this);
        listview.setAdapter(adapter);
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
        SupplierBean phoneBook = new SupplierBean();
        phoneBook.setOrder_id(1);
        phoneBook.setName("北京xxx供应商");
        phoneBook.setUser("张三");
        phoneBook.setArrears(125.00);
        phoneBook.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook.setPhone("18822222222");
        phoneBook.setCall("0731-88888888");

        SupplierBean phoneBook1 = new SupplierBean();
        phoneBook1.setOrder_id(2);
        phoneBook1.setName("成都xxx供应商");
        phoneBook1.setUser("王五");
        phoneBook1.setArrears(10.00);
        phoneBook1.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook1.setPhone("18822222222");
        phoneBook1.setCall("0731-88888888");


        SupplierBean phoneBook2 = new SupplierBean();
        phoneBook2.setOrder_id(3);
        phoneBook2.setName("零散xxx供应商");
        phoneBook2.setUser("无");
        phoneBook2.setArrears(1.00);
        phoneBook2.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook2.setPhone("18822222222");
        phoneBook2.setCall("0731-88888888");

        SupplierBean phoneBook3 = new SupplierBean();
        phoneBook3.setOrder_id(4);
        phoneBook3.setName("上海xxx供应商");
        phoneBook3.setUser("李四");
        phoneBook3.setArrears(120.00);
        phoneBook3.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook3.setPhone("18822222222");
        phoneBook3.setCall("0731-88888888");

        SupplierBean phoneBook4 = new SupplierBean();
        phoneBook4.setOrder_id(5);
        phoneBook4.setName("深圳xxx供应商");
        phoneBook4.setUser("刘德华");
        phoneBook4.setArrears(520.00);
        phoneBook4.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook4.setPhone("18822222222");
        phoneBook4.setCall("0731-88888888");

        SupplierBean phoneBook5 = new SupplierBean();
        phoneBook5.setOrder_id(6);
        phoneBook5.setName("长沙xxx供应商");
        phoneBook5.setUser("赵六");
        phoneBook5.setArrears(105.00);
        phoneBook5.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook5.setPhone("18822222222");
        phoneBook5.setCall("0731-88888888");

        SupplierBean phoneBook6 = new SupplierBean();
        phoneBook6.setOrder_id(7);
        phoneBook6.setName("武汉xxx供应商");
        phoneBook6.setUser("钱四");
        phoneBook6.setArrears(55.00);
        phoneBook6.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook6.setPhone("18822222222");
        phoneBook6.setCall("0731-88888888");

        SupplierBean phoneBook7 = new SupplierBean();
        phoneBook7.setOrder_id(8);
        phoneBook7.setName("南京xxx供应商");
        phoneBook7.setUser("小四");
        phoneBook7.setArrears(111.00);
        phoneBook7.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook7.setPhone("18822222222");
        phoneBook7.setCall("0731-88888888");

        SupplierBean phoneBook8 = new SupplierBean();
        phoneBook8.setOrder_id(9);
        phoneBook8.setName("贵阳xxx供应商");
        phoneBook8.setUser("小吴");
        phoneBook8.setArrears(125.00);
        phoneBook8.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook8.setPhone("18822222222");
        phoneBook8.setCall("0731-88888888");

        SupplierBean phoneBook9 = new SupplierBean();
        phoneBook9.setOrder_id(10);
        phoneBook9.setName("云南xxx供应商");
        phoneBook9.setUser("寻弋");
        phoneBook9.setArrears(120.00);
        phoneBook9.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook9.setPhone("18822222222");
        phoneBook9.setCall("0731-88888888");

        supplierBean.add(phoneBook);
        supplierBean.add(phoneBook1);
        supplierBean.add(phoneBook2);
        supplierBean.add(phoneBook3);
        supplierBean.add(phoneBook4);
        supplierBean.add(phoneBook5);
        supplierBean.add(phoneBook6);
        supplierBean.add(phoneBook7);
        supplierBean.add(phoneBook8);
        supplierBean.add(phoneBook9);
        adapter.setData(supplierBean);
        all_supplier.setText(supplierBean.size() + "");
        yingfu_supplier.setText(supplierBean.size() - 3 + "");
        double num = 0.00;
        for (SupplierBean Bean : supplierBean) {
            num += Bean.getArrears();
        }
        yingfu_money.setText("¥" + num);
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
        store_refresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                store_refresh.setRefreshing(false);
            }
        }, 1000);
    }
}
