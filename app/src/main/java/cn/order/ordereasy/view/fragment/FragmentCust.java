package cn.order.ordereasy.view.fragment;
/**
 * 第4个fragment
 **/

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import cn.order.ordereasy.adapter.CustomerListAdapter;
import cn.order.ordereasy.adapter.CustomerListTwoAdapter;
import cn.order.ordereasy.adapter.SelectCustomerAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.AddCustomerActivity;
import cn.order.ordereasy.view.activity.CustomerHomepageActivity;
import cn.order.ordereasy.view.activity.LoginActity;
import cn.order.ordereasy.view.activity.SearchGoodsThreeActivity;
import cn.order.ordereasy.widget.CharacterParser;
import cn.order.ordereasy.widget.GuideDialog;
import cn.order.ordereasy.widget.IndexView;
import cn.order.ordereasy.widget.PinyinComparator;


public class FragmentCust extends Fragment implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {
    View view;
    private View rootView;// 缓存Fragment view
    OrderEasyPresenter orderEasyPresenter;
    private CustomerListAdapter customerListAdapter;
    private CustomerListTwoAdapter customerListTwoAdapter;
    private SelectCustomerAdapter selectCustomerAdapter;
    SharedPreferences sp;
    List<Customer> datas = new ArrayList<>();

    @InjectView(R.id.indexview)
    IndexView mIndexView;
    private int type = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_cust, null);
        }
        sp = getActivity().getSharedPreferences("customers", 0);
        ButterKnife.inject(this, rootView);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        customerListAdapter = new CustomerListAdapter(getActivity());
        customerListTwoAdapter = new CustomerListTwoAdapter(getActivity());
        kehu_listview.setAdapter(customerListAdapter);
        kehu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Customer customer = (Customer) kehu_listview.getAdapter().getItem(position);
                Intent intent = new Intent(getActivity(), CustomerHomepageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", customer);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1001);
            }
        });
        selectCustomerAdapter = new SelectCustomerAdapter(getActivity());
        initRefreshLayout();
        //新手引导
        new GuideDialog(6, getActivity());
        return rootView;
    }


    //下拉刷新
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    private void initRefreshLayout() {
        updataCustomer();
        store_refresh.setOnRefreshListener(this);
    }

    private void updataCustomer() {
        if (DataStorageUtils.getInstance().getCustomerLists().size() > 0) {
            datas = DataStorageUtils.getInstance().getCustomerLists();
            kehu_num.setText("(" + datas.size() + ")");
            ReceivableCmp cmp = new ReceivableCmp();
            Collections.sort(datas, cmp);
            if (type == 1) {
                customerListAdapter.setData(datas);
                customerListAdapter.notifyDataSetChanged();
            } else if (type == 2) {
                selectBbtn2();
            } else if (type == 3) {
                selectBbtn3();
            }
            if (customerListAdapter.getData().size() > 0) {
                no_data_view.setVisibility(View.GONE);
            } else {
                no_data_view.setVisibility(View.VISIBLE);
            }
        } else {
            refreshData(true);
        }
    }

    //顶部添加客户按钮
    @InjectView(R.id.add_kehu)
    ImageView add_kehu;

    //搜索按钮
    @InjectView(R.id.sousuo)
    ImageView sousuo;


    /**
     * 三个选项卡控件
     **/
    //第一个按钮
    @InjectView(R.id.bbtn1)
    LinearLayout bbtn1;
    @InjectView(R.id.kehu_num)
    TextView kehu_num;
    //第2个按钮
    @InjectView(R.id.bbtn2)
    LinearLayout bbtn2;
    //第3个按钮
    @InjectView(R.id.bbtn3)
    LinearLayout bbtn3;

    @InjectView(R.id.tv_listindexview_tip)
    TextView mTopcTv;

    //第一个按钮文字
    @InjectView(R.id.text_1)
    TextView text_1;
    //第2个按钮文字
    @InjectView(R.id.text_2)
    TextView text_2;
    //第3个按钮文字
    @InjectView(R.id.text_3)
    TextView text_3;

    //ListView
    @InjectView(R.id.kehu_listview)
    ListView kehu_listview;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;
//
//	//显示视图
//	@InjectView(R.id.detail_view3)
//	View detail_view3;


    //添加客户点击
    @OnClick(R.id.add_kehu)
    void setAdd_kehu() {
        Intent intent = new Intent(getActivity(), AddCustomerActivity.class);
        startActivity(intent);
    }

    //搜索点击事件
    @OnClick(R.id.sousuo)
    void sousuo() {
        Intent intent = new Intent(getActivity(), SearchGoodsThreeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FragmentCust", "onResume");
        if (DataStorageUtils.getInstance().isCustomer()) {
            DataStorageUtils.getInstance().setCustomer(false);
            refreshData(false);
        }
    }

    /**
     * 三个选项卡点击事件
     **/
    @OnClick(R.id.bbtn1)
    void bbtn1() {
        type = 1;
        selectBbtn1();
    }

    private void selectBbtn1() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_1.setTextColor(getResources().getColor(R.color.white));
        text_2.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_3.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn1.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn2.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn3.setBackgroundColor(getResources().getColor(R.color.white));
        ReceivableCmp cmp = new ReceivableCmp();
        Collections.sort(datas, cmp);
        customerListAdapter.setData(datas);
        kehu_listview.setAdapter(customerListAdapter);
        customerListAdapter.notifyDataSetChanged();
        mIndexView.setVisibility(View.GONE);
        mTopcTv.setVisibility(View.GONE);
    }

    @OnClick(R.id.bbtn2)
    void bbtn2() {
        type = 2;
        selectBbtn2();
    }

    private void selectBbtn2() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_2.setTextColor(getResources().getColor(R.color.white));
        text_1.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_3.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn2.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn1.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn3.setBackgroundColor(getResources().getColor(R.color.white));
        mIndexView.setVisibility(View.GONE);
        mTopcTv.setVisibility(View.GONE);
        OweCmp cmp = new OweCmp();
        Collections.sort(datas, cmp);
        customerListTwoAdapter.setData(datas);
        kehu_listview.setAdapter(customerListTwoAdapter);
        customerListTwoAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bbtn3)
    void bbtn3() {
        type = 3;
        selectBbtn3();
    }

    private void selectBbtn3() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_3.setTextColor(getResources().getColor(R.color.white));
        text_1.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_2.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn3.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn1.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn2.setBackgroundColor(getResources().getColor(R.color.white));
        sortData();
        mIndexView.setVisibility(View.VISIBLE);
        selectCustomerAdapter.setData(datas);
        kehu_listview.setAdapter(selectCustomerAdapter);
        mIndexView.bringToFront();
        mTopcTv.bringToFront();
        mIndexView.setDelegate(new IndexView.Delegate() {
            @Override
            public void onIndexViewSelectedChanged(IndexView indexView, String text) {
                int position = selectCustomerAdapter.getPositionForCategory(text.charAt(0));
                if (position != -1) {
                    // position的item滑动到ListView的第一个可见条目
                    kehu_listview.setSelection(position);
                    mTopcTv.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(100, 1000);
                }
            }
        });

        kehu_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (selectCustomerAdapter.getCount() > 0) {
                    mTopcTv.setText(selectCustomerAdapter.getItem(firstVisibleItem).getTopic());
                }
            }
        });
        selectCustomerAdapter.notifyDataSetChanged();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            datas = new ArrayList<>();
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            if (jsonArray.size() > 0) {

                                sp.edit().putString("customers", result.get("result").toString()).commit();
                                kehu_num.setText("(" + jsonArray.size() + ")");
                            }
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(), Customer.class);
                                String name = "";
                                if (TextUtils.isEmpty(customer.getName())) {
                                    name = "-";
                                } else {
                                    name = customer.getName();
                                }
                                customer.setName(name);
                                datas.add(customer);
                            }
                            ReceivableCmp cmp = new ReceivableCmp();
                            Collections.sort(datas, cmp);
                            DataStorageUtils.getInstance().setCustomerLists(datas);

                            if (type == 1) {
                                customerListAdapter.setData(datas);
                                customerListAdapter.notifyDataSetChanged();
                            } else if (type == 2) {
                                selectBbtn2();
                            } else if (type == 3) {
                                selectBbtn3();
                            }
                            if (customerListAdapter.getData().size() > 0) {
                                no_data_view.setVisibility(View.GONE);
                            } else {
                                no_data_view.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(getActivity(), LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("信息", result.toString());

                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(getActivity(), LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("欠货信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(getActivity(), LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("客户信息", result.toString());
                    break;
                case 1004:

                    break;
                case 1005:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(getActivity(), LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("上下架信息", result.toString());
                    break;
                case 100:
                    mTopcTv.setVisibility(View.GONE);
                    break;
                case 1007:
                    ToastUtil.show("出错了哟~");
                    break;
                case 9999:
                    ToastUtil.show("网络有问题哟~");
                    break;
            }
        }
    };

    @Override
    public void onRefresh() {
        refreshData(false);
    }

    public void refreshData(boolean isFrist) {
        if (isFrist) {
            ProgressUtil.showDialog(getActivity());
        }
        orderEasyPresenter.getCustomerList();
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
        Message message = new Message();
        store_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
        switch (type) {
            case 0:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:

                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;

                }
                break;
            case 2:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;
                }
                break;
            case 3:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1004;

                }

                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            updataCustomer();
        }
    }

    void sortData() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        CharacterParser characterParser = CharacterParser.getInstance();
        for (Customer indexModel : datas) {
            String name = "";
            if (TextUtils.isEmpty(indexModel.getName())) {
                name = "#";
            } else {
                name = indexModel.getName();
            }
            indexModel.setTopic(characterParser.getSelling(name).substring(0, 1).toUpperCase());
        }
        Collections.sort(datas, pinyinComparator);
    }


    public class ReceivableCmp implements Comparator {

        public int compare(Object o1, Object o2) {
            Customer a1 = (Customer) o1;
            Customer a2 = (Customer) o2;

            if (a1.getReceivable() > a2.getReceivable()) {
                return -1;
            } else {
                if (a1.getReceivable() == a2.getReceivable()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    }

    public class OweCmp implements Comparator {

        public int compare(Object o1, Object o2) {
            Customer a1 = (Customer) o1;
            Customer a2 = (Customer) o2;

            if (a1.getOwe_sum() > a2.getOwe_sum()) {
                return -1;
            } else {
                if (a1.getOwe_sum() == a2.getOwe_sum()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        }
    }
}
