package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.StockStatisticsAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.widget.NoScrollListView;

public class StockStatisticsActivity extends BaseActivity {
    private StockStatisticsAdapter adapter;
    private List<Goods> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_statistics_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        list = DataStorageUtils.getInstance().getShelvesGoods();
        adapter = new StockStatisticsAdapter(this, list);
        listview.setAdapter(adapter);
        scroll_view.smoothScrollTo(0,20);
        initData();
    }

    private void initData() {
        stock_type.setText(list.size() + "");
        stock_owe.setText(getOweNumber() + "");
        total_stock.setText(getStockNumber() + "");
    }

    private int getOweNumber() {
        int Owe_num = 0;
        for (Goods goods : list) {
            for (Product product : goods.getProduct_list()) {
                Owe_num += product.getOwe_num();
            }
        }
        return Owe_num;
    }

    private int getStockNumber() {
        int Stock_num = 0;
        for (Goods goods : list) {
            Stock_num += goods.getStore_num();
        }
        return Stock_num;
    }

    @InjectView(R.id.stock_type)
    TextView stock_type;
    @InjectView(R.id.stock_owe)
    TextView stock_owe;
    @InjectView(R.id.total_stock)
    TextView total_stock;
    @InjectView(R.id.scroll_view)
    ScrollView scroll_view;
    @InjectView(R.id.listview)
    NoScrollListView listview;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

}
