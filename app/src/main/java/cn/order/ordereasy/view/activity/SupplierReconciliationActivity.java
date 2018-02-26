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
        phoneBook.setSupplier_id(1);
        phoneBook.setName("北京xxx供应商");
        phoneBook.setContact("张三");
        phoneBook.setDebt(125.00);
        phoneBook.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook.setMobile("18822222222");
        phoneBook.setTel("0731-88888888");

        SupplierBean phoneBook1 = new SupplierBean();
        phoneBook1.setSupplier_id(2);
        phoneBook1.setName("成都xxx供应商");
        phoneBook1.setContact("王五");
        phoneBook1.setDebt(10.00);
        phoneBook1.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook1.setMobile("18822222222");
        phoneBook1.setTel("0731-88888888");


        SupplierBean phoneBook2 = new SupplierBean();
        phoneBook2.setSupplier_id(3);
        phoneBook2.setName("零散xxx供应商");
        phoneBook2.setContact("无");
        phoneBook2.setDebt(1.00);
        phoneBook2.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook2.setMobile("18822222222");
        phoneBook2.setTel("0731-88888888");

        SupplierBean phoneBook3 = new SupplierBean();
        phoneBook3.setSupplier_id(4);
        phoneBook3.setName("上海xxx供应商");
        phoneBook3.setContact("李四");
        phoneBook3.setDebt(120.00);
        phoneBook3.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook3.setMobile("18822222222");
        phoneBook3.setTel("0731-88888888");

        SupplierBean phoneBook4 = new SupplierBean();
        phoneBook4.setSupplier_id(5);
        phoneBook4.setName("深圳xxx供应商");
        phoneBook4.setContact("刘德华");
        phoneBook4.setDebt(520.00);
        phoneBook4.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook4.setMobile("18822222222");
        phoneBook4.setTel("0731-88888888");

        SupplierBean phoneBook5 = new SupplierBean();
        phoneBook5.setSupplier_id(6);
        phoneBook5.setName("长沙xxx供应商");
        phoneBook5.setContact("赵六");
        phoneBook5.setDebt(105.00);
        phoneBook5.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook5.setMobile("18822222222");
        phoneBook5.setTel("0731-88888888");

        SupplierBean phoneBook6 = new SupplierBean();
        phoneBook6.setSupplier_id(7);
        phoneBook6.setName("武汉xxx供应商");
        phoneBook6.setContact("钱四");
        phoneBook6.setDebt(55.00);
        phoneBook6.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook6.setMobile("18822222222");
        phoneBook6.setTel("0731-88888888");

        SupplierBean phoneBook7 = new SupplierBean();
        phoneBook7.setSupplier_id(8);
        phoneBook7.setName("南京xxx供应商");
        phoneBook7.setContact("小四");
        phoneBook7.setDebt(111.00);
        phoneBook7.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook7.setMobile("18822222222");
        phoneBook7.setTel("0731-88888888");

        SupplierBean phoneBook8 = new SupplierBean();
        phoneBook8.setSupplier_id(9);
        phoneBook8.setName("贵阳xxx供应商");
        phoneBook8.setContact("小吴");
        phoneBook8.setDebt(125.00);
        phoneBook8.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook8.setMobile("18822222222");
        phoneBook8.setTel("0731-88888888");

        SupplierBean phoneBook9 = new SupplierBean();
        phoneBook9.setSupplier_id(10);
        phoneBook9.setName("云南xxx供应商");
        phoneBook9.setContact("寻弋");
        phoneBook9.setDebt(120.00);
        phoneBook9.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook9.setMobile("18822222222");
        phoneBook9.setTel("0731-88888888");

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
            num += Bean.getDebt();
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
