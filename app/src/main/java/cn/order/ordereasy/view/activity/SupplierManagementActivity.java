package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SupplierAdapter;
import cn.order.ordereasy.adapter.billingPurchaseAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.bean.SupplierIndex;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.widget.CharacterParser;
import cn.order.ordereasy.widget.IndexView;
import cn.order.ordereasy.widget.PinnedSectionListView;
import cn.order.ordereasy.widget.PinyinComparator;

public class SupplierManagementActivity extends BaseActivity implements AbsListView.OnScrollListener, IndexView.Delegate, SwipeRefreshLayout.OnRefreshListener, SupplierAdapter.OnItemClickListener {
    private SupplierAdapter adapter;
    private List<SupplierIndex> phoneBookIndexs = new ArrayList<>();
    private String type = "supplier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.supplier_management_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
        }

        getList();
        adapter = new SupplierAdapter(this, type);
        supplier_listview.setAdapter(adapter);
        adapter.addDataToList(getSortData());
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
        SupplierIndex phoneBookIndex = new SupplierIndex();
        SupplierBean phoneBook = new SupplierBean();
        phoneBook.setOrder_id(1);
        phoneBook.setName("北京xxx供应商");
        phoneBook.setUser("张三");
        phoneBook.setArrears(125.00);
        phoneBook.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook.setPhone("18822222222");
        phoneBook.setCall("0731-88888888");
        phoneBookIndex.setSupplierBean(phoneBook);

        SupplierIndex phoneBookIndex1 = new SupplierIndex();
        SupplierBean phoneBook1 = new SupplierBean();
        phoneBook1.setOrder_id(2);
        phoneBook1.setName("成都xxx供应商");
        phoneBook1.setUser("王五");
        phoneBook1.setArrears(10.00);
        phoneBook1.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook1.setPhone("18822222222");
        phoneBook1.setCall("0731-88888888");
        phoneBookIndex1.setSupplierBean(phoneBook1);


        SupplierIndex phoneBookIndex2 = new SupplierIndex();
        SupplierBean phoneBook2 = new SupplierBean();
        phoneBook2.setOrder_id(3);
        phoneBook2.setName("零散xxx供应商");
        phoneBook2.setUser("无");
        phoneBook2.setArrears(1.00);
        phoneBook2.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook2.setPhone("18822222222");
        phoneBook2.setCall("0731-88888888");
        phoneBookIndex2.setSupplierBean(phoneBook2);

        SupplierIndex phoneBookIndex3 = new SupplierIndex();
        SupplierBean phoneBook3 = new SupplierBean();
        phoneBook3.setOrder_id(4);
        phoneBook3.setName("上海xxx供应商");
        phoneBook3.setUser("李四");
        phoneBook3.setArrears(120.00);
        phoneBook3.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook3.setPhone("18822222222");
        phoneBook3.setCall("0731-88888888");
        phoneBookIndex3.setSupplierBean(phoneBook3);

        SupplierIndex phoneBookIndex4 = new SupplierIndex();
        SupplierBean phoneBook4 = new SupplierBean();
        phoneBook4.setOrder_id(5);
        phoneBook4.setName("深圳xxx供应商");
        phoneBook4.setUser("刘德华");
        phoneBook4.setArrears(520.00);
        phoneBook4.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook4.setPhone("18822222222");
        phoneBook4.setCall("0731-88888888");
        phoneBookIndex4.setSupplierBean(phoneBook4);

        SupplierIndex phoneBookIndex5 = new SupplierIndex();
        SupplierBean phoneBook5 = new SupplierBean();
        phoneBook5.setOrder_id(6);
        phoneBook5.setName("长沙xxx供应商");
        phoneBook5.setUser("赵六");
        phoneBook5.setArrears(105.00);
        phoneBook5.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook5.setPhone("18822222222");
        phoneBook5.setCall("0731-88888888");
        phoneBookIndex5.setSupplierBean(phoneBook5);

        SupplierIndex phoneBookIndex6 = new SupplierIndex();
        SupplierBean phoneBook6 = new SupplierBean();
        phoneBook6.setOrder_id(7);
        phoneBook6.setName("武汉xxx供应商");
        phoneBook6.setUser("钱四");
        phoneBook6.setArrears(55.00);
        phoneBook6.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook6.setPhone("18822222222");
        phoneBook6.setCall("0731-88888888");
        phoneBookIndex6.setSupplierBean(phoneBook6);

        SupplierIndex phoneBookIndex7 = new SupplierIndex();
        SupplierBean phoneBook7 = new SupplierBean();
        phoneBook7.setOrder_id(8);
        phoneBook7.setName("南京xxx供应商");
        phoneBook7.setUser("小四");
        phoneBook7.setArrears(111.00);
        phoneBook7.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook7.setPhone("18822222222");
        phoneBook7.setCall("0731-88888888");
        phoneBookIndex7.setSupplierBean(phoneBook7);

        SupplierIndex phoneBookIndex8 = new SupplierIndex();
        SupplierBean phoneBook8 = new SupplierBean();
        phoneBook8.setOrder_id(9);
        phoneBook8.setName("贵阳xxx供应商");
        phoneBook8.setUser("小吴");
        phoneBook8.setArrears(125.00);
        phoneBook8.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook8.setPhone("18822222222");
        phoneBook8.setCall("0731-88888888");
        phoneBookIndex8.setSupplierBean(phoneBook8);

        SupplierIndex phoneBookIndex9 = new SupplierIndex();
        SupplierBean phoneBook9 = new SupplierBean();
        phoneBook9.setOrder_id(10);
        phoneBook9.setName("云南xxx供应商");
        phoneBook9.setUser("寻弋");
        phoneBook9.setArrears(120.00);
        phoneBook9.setAddress("湖南省长沙市岳麓区罗马广场");
        phoneBook9.setPhone("18822222222");
        phoneBook9.setCall("0731-88888888");
        phoneBookIndex9.setSupplierBean(phoneBook9);

        phoneBookIndexs.add(phoneBookIndex);
        phoneBookIndexs.add(phoneBookIndex1);
        phoneBookIndexs.add(phoneBookIndex2);
        phoneBookIndexs.add(phoneBookIndex3);
        phoneBookIndexs.add(phoneBookIndex4);
        phoneBookIndexs.add(phoneBookIndex5);
        phoneBookIndexs.add(phoneBookIndex6);
        phoneBookIndexs.add(phoneBookIndex7);
        phoneBookIndexs.add(phoneBookIndex8);
        phoneBookIndexs.add(phoneBookIndex9);
        supplier_num.setText("(" + phoneBookIndexs.size() + ")");

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
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        supplier_import.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//通讯录导入
                String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE};
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 0);
                popupWindow.dismiss();
            }
        });

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

    @Override
    public void choose(SupplierBean phoneBook) {
        Intent intent = new Intent();
        intent.putExtra("data", phoneBook);
        setResult(1006, intent);
        finish();
    }
}
