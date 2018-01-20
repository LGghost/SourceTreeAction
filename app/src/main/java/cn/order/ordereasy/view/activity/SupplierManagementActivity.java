package cn.order.ordereasy.view.activity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SupplierAdapter;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.bean.SupplierIndex;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.widget.IndexView;
import cn.order.ordereasy.widget.PinnedSectionListView;

public class SupplierManagementActivity extends BaseActivity implements AbsListView.OnScrollListener, IndexView.Delegate {
    private SupplierAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_management_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new SupplierAdapter(this);
        supplier_listview.setAdapter(adapter);
        adapter.addDataToList(initData());
        indexview.bringToFront();
        tv_listindexview_tip.bringToFront();
        supplier_listview.setOnScrollListener(this);
        indexview.setDelegate(this);
    }

    private List<SupplierIndex> initData() {
        char index = 'A';
        List<SupplierIndex> phoneBookIndexs = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            SupplierIndex phoneBookIndex = new SupplierIndex();
            phoneBookIndex.setIndex(String.valueOf(index));
            if (i % 8 == 0) {
                index = (char) (index + 1);
                phoneBookIndex.setType(adapter.SECTION);
            } else {
                phoneBookIndex.setType(adapter.ITEM);
                SupplierBean phoneBook = new SupplierBean();
                phoneBook.setName("啊文" + i);
                phoneBook.setArrears(125.00);
                phoneBook.setUser("张三");
                phoneBookIndex.setSupplierBean(phoneBook);
            }
            phoneBookIndexs.add(phoneBookIndex);

        }
        return phoneBookIndexs;
    }

    @InjectView(R.id.supplier_num)
    TextView supplier_num;
    @InjectView(R.id.indexview)
    IndexView indexview;
    @InjectView(R.id.tv_listindexview_tip)
    TextView tv_listindexview_tip;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.supplier_listview)
    PinnedSectionListView supplier_listview;
    @InjectView(R.id.add_supplier)
    ImageView addSupplier;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    //添加供应商
    @OnClick(R.id.add_supplier)
    void add_supplier() {
        showPopWindow();
    }

    //搜索
    @OnClick(R.id.et_search)
    void et_search() {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    tv_listindexview_tip.setVisibility(View.GONE);
                    break;

            }
        }
    };

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (adapter.getCount() > 0) {
            tv_listindexview_tip.setText(adapter.getItem(firstVisibleItem).getIndex());
        }
    }

    @Override
    public void onIndexViewSelectedChanged(IndexView indexView, String text) {
        int position = adapter.getPositionForCategory(text.charAt(0)) - 1;
        if (position != -1) {
            // position的item滑动到ListView的第一个可见条目
            supplier_listview.setSelection(position);
            tv_listindexview_tip.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(100, 1000);
        }
    }

    private void showPopWindow() {
        final PopupWindow popupWindow;
        View contentView = LayoutInflater.from(this).inflate(R.layout.supplier_add_popuwindow, null, false);
        LinearLayout add_supplier = (LinearLayout) contentView.findViewById(R.id.add_supplier);
        LinearLayout supplier_import = (LinearLayout) contentView.findViewById(R.id.supplier_import);
        popupWindow = new PopupWindow(contentView, ScreenUtil.getWindowsW(this) / 3 + 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(addSupplier);
//        SystemfieldUtils.setBackgroundAlpha(this, 0.8f);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                SystemfieldUtils.setBackgroundAlpha(SupplierManagementActivity.this, 1.0f);
//            }
//        });
        add_supplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//新增供应商
            }
        });
        supplier_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//通讯录导入
            }
        });

    }

}
