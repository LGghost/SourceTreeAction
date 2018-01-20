package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.StockWarningAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.utils.DataCompareUtils;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.widget.DownListView;

public class StockWarningActivity extends BaseActivity {
    private StockWarningAdapter adapter;
    private int sort = -1;
    private int state = -1;
    private int array = -1;
    private List<Goods> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_warning_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initData();
    }

    private void initData() {
        adapter = new StockWarningAdapter(this);
        list_view.setAdapter(adapter);
        stock_state.setItemsData(getList(getResources().getStringArray(R.array.warning_state)), 0);
        stock_array.setItemsData(getList(getResources().getStringArray(R.array.shelves_array)), 0);
        stock_sort.setHigh(true);
        stock_sort.setItemsData(DataStorageUtils.getInstance().getGenreGoods(), 0);
        getData();
        initSetOnListener();
    }

    private void getData() {
        List<Goods> goods = DataStorageUtils.getInstance().getShelvesGoods();
        for (Goods good : goods) {
            Log.e("StockWarning", "是否：" + good.getIs_enable_stock_warn());
            if (good.getIs_enable_stock_warn() == 1) {
                list.add(good);
            }
        }
        screenData(sort, state, array);
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
            public void selected(TopicLabelObject topic) {//
                state = topic.getId();
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

    @InjectView(R.id.et_search)
    EditText et_search;
    @InjectView(R.id.stock_sort)
    DownListView stock_sort;
    @InjectView(R.id.stock_state)
    DownListView stock_state;
    @InjectView(R.id.stock_array)
    DownListView stock_array;
    @InjectView(R.id.list_view)
    ListView list_view;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
    @InjectView(R.id.mask)
    View mask;

    @OnClick(R.id.return_click)
    void return_click() {//返回
        finish();
    }

    @OnClick(R.id.et_search)
    void et_search() {//搜索
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    List<Goods> goodsList = Goods.likeString2(list, s.toString());
                    if (goodsList.size() > 0) {
                        adapter.setData(goodsList);
                    } else {
                        adapter.setData(new ArrayList<Goods>());
                    }
                } else {
                    adapter.setData(new ArrayList<Goods>());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @OnClick(R.id.saoyisao)
    void saoyisao() {//扫一扫
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(this, ScanActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9001) {
            List<Goods> goodsList = new ArrayList<>();
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            boolean isExist = false;
            if (TextUtils.isEmpty(res)) {
                ToastUtil.show("没有识别到二维码信息");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    String str = String.valueOf(list.get(i).getGoods_no());
                    if (str.equals(res)) {
                        goodsList.add(list.get(i));
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    if (goodsList.size() > 0) {
                        adapter.setData(goodsList);
                    } else {
                        ToastUtil.show("没有找到相关货品");
                    }
                } else {
                    ToastUtil.show("没有找到相关货品");
                }
            }
            MyLog.e("扫一扫返回数据", res);
        }
    }

    private void screenData(int sort, int state, int array) {
        List<Goods> goodsList = DataCompareUtils.screenStockData(list, sort, state, array);
        adapter.setData(goodsList);
        if (adapter.getData().size() > 0) {
            no_data_view.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.VISIBLE);
        }
    }
}