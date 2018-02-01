package cn.order.ordereasy.view.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.GoodListAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataCompareUtils;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UmengUtils;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.LoginActity;
import cn.order.ordereasy.view.activity.ScanActivity;
import cn.order.ordereasy.view.activity.SearchGoodsTwoActivity;
import cn.order.ordereasy.view.fragment.gooddetailsfragment.DetailsGoodsActivity;
import cn.order.ordereasy.widget.DownListView;
import cn.order.ordereasy.widget.GuideDialog;

//货架
public class FragmentShelves extends Fragment implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, BGAOnItemChildClickListener {

    private List<Goods> datas = new ArrayList<>();
    private OrderEasyPresenter orderEasyPresenter;
    private GoodListAdapter mAdapter;
    private List<TopicLabelObject> shelves_sort;
    private List<TopicLabelObject> shelves_state = new ArrayList<>();
    private List<TopicLabelObject> shelves_array = new ArrayList<>();
    private int sort = -1;
    private int state = 1;
    private int array = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.shelves_fragment_layout, container, false);
        ButterKnife.inject(this, view);
        initData();
        //新手引导
        new GuideDialog(2, getActivity());
        return view;
    }

    private void initData() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        mAdapter = new GoodListAdapter(getActivity());
        listview.setAdapter(mAdapter);
        store_refresh.setOnRefreshListener(this);
        shelves_state = getList(getActivity().getResources().getStringArray(R.array.shelves_state));
        shelves_array = getList(getActivity().getResources().getStringArray(R.array.shelves_array));
        shelves_fragment_state.setItemsData(shelves_state, 1);
        shelves_fragment_array.setItemsData(shelves_array, 0);
        shelves_fragment_sort.setHigh(true);
        if (DataStorageUtils.getInstance().getShelvesGoods().size() > 0) {
            datas = DataStorageUtils.getInstance().getShelvesGoods();
            screenData(sort, state, array);
        } else {
            refreshData(true);
        }
        if (DataStorageUtils.getInstance().getGenreGoods().size() > 0) {
            shelves_sort = DataStorageUtils.getInstance().getGenreGoods();
            shelves_fragment_sort.setItemsData(shelves_sort, 0);
        } else {
            orderEasyPresenter.getCategoryInfo();
        }
        initSetOnListener();
    }

    private void initSetOnListener() {
        listview.setOnItemClickListener(this);
        mAdapter.setOnItemChildClickListener(this);
        shelves_fragment_sort.setDownItemClickListener(new DownListView.DownItemClickListener() {
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
        shelves_fragment_state.setDownItemClickListener(new DownListView.DownItemClickListener() {
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
        shelves_fragment_array.setDownItemClickListener(new DownListView.DownItemClickListener() {
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

    @InjectView(R.id.shelves_fragment_sort)
    DownListView shelves_fragment_sort;
    @InjectView(R.id.shelves_fragment_state)
    DownListView shelves_fragment_state;
    @InjectView(R.id.shelves_fragment_array)
    DownListView shelves_fragment_array;
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    ListView listview;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    @InjectView(R.id.mask)
    View mask;

    @OnClick(R.id.saoyisao)
    void saoyisao() {//扫描
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(getActivity(), ScanActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    @OnClick(R.id.sousuo)
    void sousuo() {//搜索
        Intent intent = new Intent(getActivity(), SearchGoodsTwoActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            boolean isExist = false;
            if (TextUtils.isEmpty(res)) {
                ToastUtil.show("没有识别到二维码信息");
            } else {
                for (int i = 0; i < datas.size(); i++) {
                    String str = String.valueOf(datas.get(i).getGoods_no());
                    if (str.equals(res)) {
                        res = String.valueOf(datas.get(i).getGoods_id());
                        isExist = true;
                        break;
                    }
                }
                if (isExist) {
                    Intent intent = new Intent(getActivity(), DetailsGoodsActivity.class);
                    Bundle bundle1 = new Bundle();
                    int goodId = Integer.parseInt(res);
                    bundle.putBoolean("isSales", true);
                    bundle1.putInt("goodId", goodId);
                    intent.putExtras(bundle1);
                    startActivityForResult(intent, 1001);
                } else {
                    ToastUtil.show("没有找到该货品");
                }
            }
            MyLog.e("扫一扫返回数据", res);
        } else if (resultCode == 1001) {
            boolean isEdit = data.getExtras().getBoolean("isEdit");
            if (isEdit) {
                refreshData(false);
                shelves_sort = DataStorageUtils.getInstance().getGenreGoods();
                shelves_fragment_sort.setEditData(shelves_sort);
            }
        }
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FragmentShelves", "onResume");
        if (DataStorageUtils.getInstance().isShanghuo()) {
            DataStorageUtils.getInstance().setShanghuo(false);
            refreshData(false);
        }
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
                shelves_fragment_sort.setItemsData(mapList, 0);
            } else {
                if (data.get("code").getAsInt() == -7) {
                    ToastUtil.show(getString(R.string.landfall_overdue));
                    Intent intent = new Intent(getActivity(), LoginActity.class);
                    startActivity(intent);
                }
            }
        } else if (type == 1) {
            if (data != null) {
                int status = data.get("code").getAsInt();
                if (status == 1) {
                    datas = new ArrayList<>();
                    JsonArray jsonArray = data.get("result").getAsJsonArray();
                    if (jsonArray == null) return;
                    SharedPreferences sp = getActivity().getSharedPreferences("goods", 0);
                    if (sp != null) {
                        sp.edit().putString("goods", data.get("result").toString()).commit();
                    }
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
                    Log.e("FragmentThings", "进入网络");
                    DataStorageUtils.getInstance().setShelvesGoods(datas);
                    screenData(sort, state, array);
                }
            }
        }

    }

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(getActivity());
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
        List<Goods> changData = DataCompareUtils.screenData(datas, sort, state, array);
        shelves_fragment_sort.setEditText(changData.size());
        mAdapter.setData(changData);
        mAdapter.notifyDataSetChanged();
        if (changData.size() > 0) {
            no_data_view.setVisibility(View.GONE);
        } else {
            no_data_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), DetailsGoodsActivity.class);
        Bundle bundle = new Bundle();
        int goodId = mAdapter.getData().get(position).getGoods_id();
        bundle.putBoolean("isSales", true);
        bundle.putInt("goodId", goodId);
        intent.putExtras(bundle);
        startActivityForResult(intent, 1001);
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        Goods goods = mAdapter.getData().get(position);
        UmengUtils.getInstance().share(getActivity(), goods.getGoods_id(), goods.getCover(), goods.getTitle(), shareListener);
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), "取消了", Toast.LENGTH_LONG).show();
        }
    };

}
