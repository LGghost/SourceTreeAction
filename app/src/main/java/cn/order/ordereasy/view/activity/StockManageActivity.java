package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.StockListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataCompareUtils;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.DownListView;

public class StockManageActivity extends BaseActivity implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {
    private OrderEasyPresenter orderEasyPresenter;
    private StockListAdapter stockListAdapter;
    private List<Goods> datas = new ArrayList<>();
    private List<TopicLabelObject> shelves_sort;
    private List<TopicLabelObject> shelves_state = new ArrayList<>();
    private List<TopicLabelObject> shelves_array = new ArrayList<>();
    private int sort = -1;
    private int state = 1;
    private int array = -1;
    private PopupWindow mPopupWindow;
    private Goods good;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_manage_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            good = (Goods) bundle.getSerializable("data");
        }
        initData();
    }

    private void initData() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        stockListAdapter = new StockListAdapter(datas, this);
        list_view.setAdapter(stockListAdapter);
        list_view.setGroupIndicator(null);
        store_refresh.setOnRefreshListener(this);
        shelves_state = getList(getResources().getStringArray(R.array.shelves_state1));
        shelves_array = getList(getResources().getStringArray(R.array.shelves_array));
        stock_state.setItemsData(shelves_state, 1);
        stock_array.setItemsData(shelves_array, 0);
        stock_sort.setHigh(true);
        if (DataStorageUtils.getInstance().getShelvesGoods().size() > 0) {
            datas = DataStorageUtils.getInstance().getShelvesGoods();
            kucun_num.setText("(" + datas.size() + ")");
            screenData(sort, state, array);
        } else {
            refreshData(true);
        }
        if (DataStorageUtils.getInstance().getGenreGoods().size() > 0) {
            shelves_sort = DataStorageUtils.getInstance().getGenreGoods();
            stock_sort.setItemsData(shelves_sort, 0);
        } else {
            orderEasyPresenter.getCategoryInfo();
        }
        initSetOnListener();
    }

    private void initSetOnListener() {
        stock_sort.setDownItemClickListener(new DownListView.DownItemClickListener() {
            @Override
            public void selected(TopicLabelObject topic) {//全部分类
                sort = topic.getId();
                screenData(sort, state, array);
            }

            @Override
            public void onClick(boolean isShow) {
                if (isShow) {
                    mask.setVisibility(View.VISIBLE);
                } else {
                    mask.setVisibility(View.GONE);
                }
            }


        });
        stock_state.setDownItemClickListener(new DownListView.DownItemClickListener() {
            @Override
            public void selected(TopicLabelObject topic) {//上下架
                state = topic.getId();
                if (state == 2) {
                    state = 0;
                }
                screenData(sort, state, array);
            }

            @Override
            public void onClick(boolean isShow) {
                if (isShow) {
                    mask.setVisibility(View.VISIBLE);
                } else {
                    mask.setVisibility(View.GONE);
                }
            }
        });
        stock_array.setDownItemClickListener(new DownListView.DownItemClickListener() {
            @Override
            public void selected(TopicLabelObject topic) {//排序
                array = topic.getId();
                screenData(sort, state, array);
            }

            @Override
            public void onClick(boolean isShow) {
                if (isShow) {
                    mask.setVisibility(View.VISIBLE);
                } else {
                    mask.setVisibility(View.GONE);
                }
            }
        });
        stockListAdapter.setOnItemClickListener(new StockListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int childpostion, int groupposition) {
                initPopupWindow(view, groupposition);
            }
        });
    }

    @InjectView(R.id.kucun_num)
    TextView kucun_num;
    @InjectView(R.id.mask)
    View mask;
    @InjectView(R.id.more)
    TextView more;
    @InjectView(R.id.et_search)
    EditText et_search;
    @InjectView(R.id.stock_sort)
    DownListView stock_sort;
    @InjectView(R.id.stock_state)
    DownListView stock_state;
    @InjectView(R.id.stock_array)
    DownListView stock_array;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.list_view)
    ExpandableListView list_view;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    @OnClick(R.id.return_click)
    void return_click() {//返回
        finish();
    }

    @OnClick(R.id.more)
    void more() {//更多
        showPopWindow();
    }


    @OnClick(R.id.et_search)
    void et_search() {//搜索
        et_search.setInputType(InputType.TYPE_NULL);
        Intent intent = new Intent(this, StockSearchGoodsActivity.class);
        startActivityForResult(intent, 1002);
    }

    @OnClick(R.id.piliang_ruku)
    void piliang_ruku() {//批量入库
        Intent intent = new Intent(this, StorageAvtivity.class);
        startActivityForResult(intent, 1001);
    }

    @OnClick(R.id.piliang_chuku)
    void piliang_chuku() {//批量出库
        Intent intent = new Intent(this, TheLibraryAityity.class);
        startActivityForResult(intent, 1001);
    }

    @OnClick(R.id.kucun_pandian)
    void kucun_pandian() {//库存盘点
        Intent intent = new Intent(this, StockInventoryActivity.class);
        startActivity(intent);
    }


    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        store_refresh.setRefreshing(false);

        if (type == 0) {//全部交易数据
            if (data.get("code").getAsInt() == 1) {//表示请求成功
                //成功
                JsonArray jsonArray = data.get("result").getAsJsonArray();
                List<TopicLabelObject> mapList = new ArrayList<>();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                    TopicLabelObject topicLabelObject1 = new TopicLabelObject(jsonObject.get("category_id").getAsInt(), jsonObject.get("goods_num").getAsInt(), jsonObject.get("name").getAsString(), 0);
                    mapList.add(topicLabelObject1);
                }
                DataStorageUtils.getInstance().setGenreGoods(mapList);
                stock_sort.setItemsData(mapList, 0);
            }
        } else if (type == 1) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    datas = new ArrayList<>();
                    JsonArray jsonArray = data.get("result").getAsJsonArray();
                    if (jsonArray == null) return;
                    kucun_num.setText("(" + jsonArray.size() + ")");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        String object = jsonArray.get(i).getAsJsonObject().toString();

                        Goods good = (Goods) GsonUtils.getEntity(object, Goods.class);
                        int store_num = 0, sale_num = 0;
                        for (Product p : good.getProduct_list()) {
                            store_num += p.getStore_num();
                            sale_num += p.getSale_num();
                        }
                        good.setStore_num(store_num);
                        good.setSale_num(sale_num);
                        datas.add(good);
                    }
                    DataStorageUtils.getInstance().setShelvesGoods(datas);
                    screenData(sort, state, array);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            refreshData(false);
            shelves_sort = DataStorageUtils.getInstance().getGenreGoods();
            stock_sort.setEditData(shelves_sort);
        } else if (resultCode == 1002) {
            Bundle bundle = data.getExtras();
            Goods good = (Goods) bundle.getSerializable("data");
            List<Goods> goodData = stockListAdapter.getGoods();
            for (int i = 0; i < goodData.size(); i++) {
                if (goodData.get(i).getGoods_no().equals(good.getGoods_no())) {
                    list_view.expandGroup(i);
                    list_view.setSelectedGroup(i);
                } else {
                    list_view.collapseGroup(i);
                }
            }
        }
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.getGoodsList();
    }

    private List<TopicLabelObject> getList(String[] list) {
        List<TopicLabelObject> topicList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            TopicLabelObject object;
            if (i == 0) {
                object = new TopicLabelObject(-1, -1, list[i], 1);
            } else {
                object = new TopicLabelObject(i, -1, list[i], 0);
            }
            topicList.add(object);
        }
        return topicList;
    }

    private void screenData(int sort, int state, int array) {

        List<Goods> goodsList = DataCompareUtils.screenData(datas, sort, state, array);
        kucun_num.setText("(" + goodsList.size() + ")");
        stockListAdapter.setGoods(goodsList);
        stockListAdapter.notifyDataSetChanged();
        if (stockListAdapter.getGoods().size() > 0) {
            no_data_view.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.VISIBLE);
        }
        if(good != null){
            List<Goods> goodData = stockListAdapter.getGoods();
            for (int i = 0; i < goodData.size(); i++) {
                if (goodData.get(i).getGoods_no().equals(good.getGoods_no())) {
                    list_view.expandGroup(i);
                    list_view.setSelectedGroup(i);
                } else {
                    list_view.collapseGroup(i);
                }
            }
        }
    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    private void initPopupWindow(View v, int pos) {
        View view = LayoutInflater.from(this).inflate(R.layout.kucun_item_view, null, false);
        final Goods good = stockListAdapter.getGoods().get(pos);
        LinearLayout btn1 = (LinearLayout) view.findViewById(R.id.click_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//入库
                Intent intent = new Intent(StockManageActivity.this, StorageAvtivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
                mPopupWindow.dismiss();
            }
        });
        LinearLayout btn2 = (LinearLayout) view.findViewById(R.id.click_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//出库
                Intent intent = new Intent(StockManageActivity.this, TheLibraryAityity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
                mPopupWindow.dismiss();
            }
        });
        LinearLayout btn3 = (LinearLayout) view.findViewById(R.id.click_3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//调整
                Intent intent = new Intent(StockManageActivity.this, TiaoZhengAityity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(view, 600, LinearLayout.LayoutParams.WRAP_CONTENT);   //生成PopWindow
//        SystemfieldUtils.setBackgroundAlpha(this, 0.8f);
        /** 为其设置背景，使得其内外焦点都可以获得 */
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
//        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                SystemfieldUtils.setBackgroundAlpha(StockManageActivity.this, 1.0f);
//            }
//        });
        //popupwindow相对view位置x轴偏移量
        View viewTemp = mPopupWindow.getContentView();
        viewTemp.measure(0, 0);
        //控件大小
        int width = viewTemp.getMeasuredWidth();
        int xOffset = (v.getWidth() - width) / 2;
        mPopupWindow.showAsDropDown(v, xOffset, 0);
    }

    private void showPopWindow() {
        final PopupWindow popupWindow;
        View contentView = LayoutInflater.from(this).inflate(R.layout.more_popupwind_layout, null, false);
        LinearLayout stock_count = (LinearLayout) contentView.findViewById(R.id.stock_count);
        LinearLayout stock_change = (LinearLayout) contentView.findViewById(R.id.stock_change);
        LinearLayout stock_early_warning = (LinearLayout) contentView.findViewById(R.id.stock_early_warning);
        popupWindow = new PopupWindow(contentView, ScreenUtil.getWindowsW(this) / 3 + 100, LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(more);
//        SystemfieldUtils.setBackgroundAlpha(this, 0.8f);
//        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                SystemfieldUtils.setBackgroundAlpha(StockManageActivity.this, 1.0f);
//            }
//        });
        stock_count.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//库存统计
                Intent intent = new Intent(StockManageActivity.this, StockStatisticsActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        stock_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//库存变动
                Intent intent = new Intent(StockManageActivity.this, StockChangeActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
        stock_early_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//库存预警
                Intent intent = new Intent(StockManageActivity.this, StockWarningActivity.class);
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }

}