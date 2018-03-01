package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SupplierAdapter;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.bean.SupplierIndex;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CharacterParser;
import cn.order.ordereasy.widget.IndexView;
import cn.order.ordereasy.widget.PinnedSectionListView;

public class SupplierManagementActivity extends BaseActivity implements AbsListView.OnScrollListener, IndexView.Delegate, SwipeRefreshLayout.OnRefreshListener, SupplierAdapter.OnItemClickListener, OrderEasyView {
    private SupplierAdapter adapter;
    private List<SupplierIndex> phoneBookIndexs = new ArrayList<>();
    private String type = "supplier";
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_management_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");//区分开单的时候选择供应商
        }

        getList();
        adapter = new SupplierAdapter(this, type);
        supplier_listview.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        indexview.bringToFront();
        tv_listindexview_tip.bringToFront();
        supplier_listview.setOnScrollListener(this);
        indexview.setDelegate(this);
        store_refresh.setOnRefreshListener(this);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    List<SupplierIndex> list = SupplierIndex.likeString2(phoneBookIndexs, s.toString());
                    if (list.size() > 0) {
                        adapter.setData(list);
                        adapter.notifyDataSetChanged();
                    } else {
                        adapter.setData(getSortData());
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    adapter.setData(getSortData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void getList() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);//网络请求
        refreshData(true);
    }


    public List<SupplierIndex> getSortData() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        CharacterParser characterParser = CharacterParser.getInstance();
        for (SupplierIndex phoneBookIndex : phoneBookIndexs) {
            String name = "";
            if (TextUtils.isEmpty(phoneBookIndex.getSupplierBean().getName())) {
                name = "#";
            } else {
                name = phoneBookIndex.getSupplierBean().getName();
            }
            phoneBookIndex.setIndex(characterParser.getSelling(name).substring(0, 1).toUpperCase());
        }
        Collections.sort(phoneBookIndexs, pinyinComparator);


        List<SupplierIndex> phoneBookIndexs1 = new ArrayList<>();
        String name = "";
        for (int i = 0; i < phoneBookIndexs.size(); i++) {
            if (i == 0) {
                SupplierIndex phoneBookIndex = new SupplierIndex();
                name = phoneBookIndexs.get(i).getIndex();
                phoneBookIndex.setIndex(name);
                phoneBookIndex.setType(adapter.SECTION);
                phoneBookIndexs1.add(phoneBookIndex);
            }
            Log.e("Supplier", "Index:" + name + "---->Index1:" + phoneBookIndexs.get(i).getIndex());
            if (name.equals(phoneBookIndexs.get(i).getIndex())) {
                phoneBookIndexs.get(i).setType(adapter.ITEM);
                phoneBookIndexs1.add(phoneBookIndexs.get(i));
            } else {
                SupplierIndex phoneBookIndex = new SupplierIndex();
                phoneBookIndex.setIndex(phoneBookIndexs.get(i).getIndex());
                phoneBookIndex.setType(adapter.SECTION);
                phoneBookIndexs1.add(phoneBookIndex);
                phoneBookIndexs.get(i).setType(adapter.ITEM);
                phoneBookIndexs1.add(phoneBookIndexs.get(i));
                name = phoneBookIndexs.get(i).getIndex();
            }
        }
        return phoneBookIndexs1;
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
    public void loadData(JsonObject data, int type) {
        Log.e("SupplierManagement", "data:" + data.toString());
        if (data != null) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                //成功
                phoneBookIndexs.clear();
                JsonArray jsonArray = data.getAsJsonObject("result").getAsJsonArray("list");
                if (jsonArray.size() > 0) {
                    supplier_num.setText("(" + jsonArray.size() + ")");
                }
                for (int i = 0; i < jsonArray.size(); i++) {
                    SupplierBean supplierBean = (SupplierBean) GsonUtils.getEntity(jsonArray.get(i).toString(), SupplierBean.class);
                    SupplierIndex phoneBookIndex = new SupplierIndex();
                    phoneBookIndex.setSupplierBean(supplierBean);
                    phoneBookIndexs.add(phoneBookIndex);
                }
                adapter.setData(getSortData());
                adapter.notifyDataSetChanged();
                if (adapter.getData().size() > 0) {
                    no_data_view.setVisibility(View.GONE);
                } else {
                    no_data_view.setVisibility(View.VISIBLE);
                }
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001 || resultCode == 1002) {
            refreshData(false);
        }
    }

    class PinyinComparator implements Comparator<SupplierIndex> {

        public int compare(SupplierIndex o1, SupplierIndex o2) {
            if (o1.getIndex().equals("@") || o2.getIndex().equals("#")) {
                return -1;
            } else if (o1.getIndex().equals("#") || o2.getIndex().equals("@")) {
                return 1;
            } else {
                return o1.getIndex().compareTo(o2.getIndex());
            }
        }

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
    @InjectView(R.id.et_search)
    EditText et_search;

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
                Intent intent = new Intent(SupplierManagementActivity.this, AddSuppliersActivity.class);
                startActivityForResult(intent, 1001);
                popupWindow.dismiss();
            }
        });
        supplier_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//通讯录导入
                Intent intent = new Intent(SupplierManagementActivity.this, TelListActivity.class);
                intent.putExtra("flag", "Supplier");
                startActivityForResult(intent, 1002);
                popupWindow.dismiss();
            }
        });

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
    public void choose(SupplierBean phoneBook) {
        Intent intent = new Intent();
        intent.putExtra("data", phoneBook);
        setResult(1006, intent);
        finish();
    }

    @Override
    public void startActivity(SupplierBean phoneBook) {
        Intent intent = new Intent(this, SupplierDetailsActivity.class);
        intent.putExtra("data", phoneBook);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void payActivity(SupplierBean phoneBook) {
        Intent intent = new Intent(this, SupplierPaymentActivity.class);
        intent.putExtra("data", phoneBook);
        startActivityForResult(intent, 1001);
    }
}
