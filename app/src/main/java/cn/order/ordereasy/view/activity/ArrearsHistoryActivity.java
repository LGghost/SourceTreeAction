package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
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
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ArrearsAdapter;
import cn.order.ordereasy.bean.ArrearsBean;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.PopWinShare;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UmengUtils;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.LoadMoreListView;

/**
 * Created by Administrator on 2017/9/21.
 * <p>
 * 欠款历史
 */

public class ArrearsHistoryActivity extends BaseActivity implements OrderEasyView, AdapterView.OnItemClickListener, LoadMoreListView.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private ArrearsAdapter adapter;
    private OrderEasyPresenter orderEasyPresenter;
    private PopWinShare popwindows;
    private int customer_id;
    private String customer_name;
    private String customer_tel;
    private int pageCurrent = 1, pageTotal = 1;
    private List<ArrearsBean> lsit = new ArrayList<>();
    private boolean isChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.arrears_history);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            customer_id = bundle.getInt("customer_id");
            customer_name = bundle.getString("customer_name");
            customer_tel = bundle.getString("customer_tel");
        }

        listView.setOnItemClickListener(this);
        listView.setOnLoadMoreListener(this);
        store_refresh.setOnRefreshListener(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        refreshData(true);
    }

    //找到控件ID
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        if (isChange) {
            setResult(1002);
            finish();
        } else {
            finish();
        }
    }

    //更多按钮
    @InjectView(R.id.more)
    TextView more;

    @OnClick(R.id.more)
    void more() {

        popwindows = new PopWinShare(this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.add_task_layout) {

                    Intent intent = new Intent();
                    intent.setClass(ArrearsHistoryActivity.this, AdjustmentActivity.class);
                    intent.putExtra("customer_id", customer_id);
                    intent.putExtra("customer_name", customer_name);
                    if (lsit.size() > 0) {
                        intent.putExtra("Money", adapter.getData().get(0).getTotal_debt());
                    } else {
                        intent.putExtra("Money", 0.00);
                    }
                    ArrearsHistoryActivity.this.startActivityForResult(intent, 1001);
                } else if (v.getId() == R.id.team_member_layout) {
                    UmengUtils.getInstance().share(ArrearsHistoryActivity.this, customer_id, shareListener);
                }
                popwindows.dismiss();
            }
        });
        popwindows.setFristImageView(R.drawable.icon_adjustment);
        popwindows.setSecondImageView(R.drawable.icon_share_popu);
        popwindows.setBtnStatusText("调整欠款");
        popwindows.setBtnBottomText("分享欠款");
        popwindows.showPopupWindow(more);
    }

    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;
    @InjectView(R.id.listView)
    LoadMoreListView listView;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    @Override
    public void showProgress(int type) {
    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
            ToastUtil.show("网络连接失败");
        }
        ProgressUtil.dissDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            isChange = true;
            refreshData(false);
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        int dataSize = 0;
        ProgressUtil.dissDialog();
        listView.setLoadCompleted();
        store_refresh.setRefreshing(false);
        if (type == 0) {
            if (data.get("code").getAsInt() == 1)//表示请求成功
            {
                //分页处理
                JsonObject page = data.getAsJsonObject("result").getAsJsonObject("page");
                if (pageCurrent == 1) {
                    if (lsit.size() > 0) {
                        lsit.clear();
                    }
                    pageTotal = page.get("page_total").getAsInt();
                    JsonArray jsonArray = data.getAsJsonObject("result").getAsJsonArray("page_list");
                    if (pageCurrent != 1) {
                        dataSize = lsit.size();
                    }
                    if (jsonArray.size() > 0) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            ArrearsBean arrearsBean = (ArrearsBean) GsonUtils.getEntity(jsonArray.get(i).toString(), ArrearsBean.class);
                            lsit.add(arrearsBean);
                        }
                        if (adapter == null) {
                            adapter = new ArrearsAdapter(this);
                            adapter.setData(lsit);
                            listView.setAdapter(adapter);
                        } else {
                            adapter.setData(lsit);
                            listView.setAdapter(adapter);
                        }
                        if (pageCurrent != 1) {
                            listView.setSelection(dataSize - 1);
                        } else {
                            listView.setSelection(0);
                        }
                        if (lsit.size() > 0) {
                            no_data_view.setVisibility(View.GONE);
                        } else {
                            no_data_view.setVisibility(View.VISIBLE);
                        }

                    } else {
                        if (lsit.size() > 0) {
                            lsit.clear();
                        }
                        no_data_view.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    public void refreshData(boolean isShow) {
        if (isShow) {
            ProgressUtil.showDialog(this);
        }
        orderEasyPresenter.arrearsHistory(customer_id, pageCurrent);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrearsBean arrearsBean = adapter.getData().get(position);
        if (arrearsBean.getIs_adjustment() == 1) {
            ToastUtil.show("调整记录不可点击");
            return;
        }
        switch (arrearsBean.getType()) {
            case 1:
            case 2:
                if (arrearsBean.getOrder_id() > 1) {
                    Intent intent = new Intent(this, OrderNoDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", arrearsBean.getOrder_id());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
            case 3:
            case 4:
                Intent intent1 = new Intent(this, CashierDetailsActivity.class);
                Bundle bundle = new Bundle();
                Money money = new Money();
                money.setCustomer_name(customer_name);
                money.setCustomer_id(customer_id);
                if (arrearsBean.getType() == 3) {
                    money.setPayment_type(1);
                } else {
                    money.setPayment_type(0);
                }
                money.setMoney(arrearsBean.getMoney());
                money.setCreate_time(arrearsBean.getCreate_time());
                money.setUser_name(arrearsBean.getUser_name());
                money.setPayment_way(0);
                bundle.putSerializable("data", money);
                bundle.putString("tel", customer_tel);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    @Override
    public void onloadMore() {
        //上拉加载更多 完成时关闭

        if (pageTotal == pageCurrent) {
            ToastUtil.show("没有更多数据了");
            listView.setIsLoading(true);
        } else {
            pageCurrent++;
            refreshData(false);
        }
    }

    @Override
    public void onRefresh() {
        //下拉刷新 完成时关闭
        pageCurrent = 1;
        refreshData(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (isChange) {
                setResult(1002);
                finish();
            } else {
                finish();
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

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
            Toast.makeText(ArrearsHistoryActivity.this, "分享成功", Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ArrearsHistoryActivity.this, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ArrearsHistoryActivity.this, "取消了", Toast.LENGTH_LONG).show();
        }
    };
}
