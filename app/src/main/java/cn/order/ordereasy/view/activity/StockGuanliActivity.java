package cn.order.ordereasy.view.activity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SearchAdapter;
import cn.order.ordereasy.adapter.StockListAdapter;
import cn.order.ordereasy.bean.Bean;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.StockListBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SearchView;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CustomExpandableListView;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;

/**
 * Created by Administrator on 2017/9/5.
 * <p>
 * 库存管理activity
 */

public class StockGuanliActivity extends BaseActivity implements OrderEasyView, BGARefreshLayout.BGARefreshLayoutDelegate, PopupWindow.OnDismissListener {

    //数据获取
    OrderEasyPresenter orderEasyPresenter;
    //适配器
    StockListAdapter stockListAdapter;
    List<Goods> datas = new ArrayList<>();

    BGARefreshLayout refresh;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //输入框不把底部顶上去
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.stock_guanli_two);
        setColor(this, this.getResources().getColor(R.color.lanse));
        //数据获取初始化
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        ButterKnife.inject(this);
        initRefreshLayout();
        //初始化adapter
        stockListAdapter = new StockListAdapter(datas, this);
        //listview设置adapter
        list_view.setAdapter(stockListAdapter);
        list_view.setGroupIndicator(null);
        //获取列表信息
        orderEasyPresenter.getGoodsList();
        ProgressUtil.showDialog(StockGuanliActivity.this);

        stockListAdapter.setOnItemClickListener(new StockListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int childpostion, int groupposition) {
                initPopupWindow(view, groupposition);
            }
        });

    }


    private void initRefreshLayout() {
        // 为BGARefreshLayout 设置代理
        store_refresh.setDelegate(this);
        // 设置下拉刷新和上拉加载更多的风格     参数1：应用程序上下文，参数2：是否具有上拉加载更多功能
        // BGARefreshViewHolder refreshViewHolder = new XXXImplRefreshViewHolder(this, false);
        // 设置下拉刷新和上拉加载更多的风格
        // store_refresh.setRefreshViewHolder(refreshViewHolder);
        store_refresh.setRefreshViewHolder(new NormalRefreshViewHolder(mApp, true));
    }

    //下拉刷新控件
    @InjectView(R.id.store_refresh)
    BGARefreshLayout store_refresh;
    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    //更多按钮
    @InjectView(R.id.more)
    TextView more;
    //顶部库存量显示
    @InjectView(R.id.kucun_num)
    TextView kucun_num;
    //顶部库存量显示

    @InjectView(R.id.no_data_image)
    ImageView no_data_image;

    @InjectView(R.id.et_search)
    EditText et_search;

    /**
     * 三个选项卡控件
     **/
    //第一个按钮
    @InjectView(R.id.bbtn1)
    LinearLayout bbtn1;
    //第2个按钮
    @InjectView(R.id.bbtn2)
    LinearLayout bbtn2;
    //第3个按钮
    @InjectView(R.id.bbtn3)
    LinearLayout bbtn3;

    //第一个按钮文字
    @InjectView(R.id.text_1)
    TextView text_1;
    //第2个按钮文字
    @InjectView(R.id.text_2)
    TextView text_2;
    //第3个按钮文字
    @InjectView(R.id.text_3)
    TextView text_3;


    //listview
    @InjectView(R.id.list_view)
    CustomExpandableListView list_view;

    /**
     * 底部三个按钮控件
     **/
    //第一个按钮文字f

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        StockGuanliActivity.this.finish();
    }

    /**
     * 三个选项卡点击事件
     **/
    @OnClick(R.id.bbtn1)
    void bbtn1() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_1.setTextColor(getResources().getColor(R.color.white));
        text_2.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_3.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn1.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn2.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn3.setBackgroundColor(getResources().getColor(R.color.white));
//        content_view.removeAllViews();
//        view = LayoutInflater.from(this).inflate(R.layout.the_books_view_one, null);
//        content_view.addView(view);

        Collections.sort(datas, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Goods stu1 = (Goods) o1;
                Goods stu2 = (Goods) o2;
                long time1 = Long.parseLong(stu1.getCreate_time());
                long time2 = Long.parseLong(stu2.getCreate_time());
                if (time1 > time2) {
                    return -1;
                } else if (time1 == time2) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        stockListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bbtn2)
    void bbtn2() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_2.setTextColor(getResources().getColor(R.color.white));
        text_1.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_3.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn2.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn1.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn3.setBackgroundColor(getResources().getColor(R.color.white));
//        content_view.removeAllViews();
//        view = LayoutInflater.from(this).inflate(R.layout.the_books_view_two, null);
//        content_view.addView(view);
        Collections.sort(datas, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Goods stu1 = (Goods) o1;
                Goods stu2 = (Goods) o2;
                if (stu1.getSale_num() > stu2.getSale_num()) {
                    return -1;
                } else if (stu1.getSale_num() == stu2.getSale_num()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        stockListAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.bbtn3)
    void bbtn3() {
        // 按钮点击时改变颜色和背景
        // 文字颜色
        text_3.setTextColor(getResources().getColor(R.color.white));
        text_1.setTextColor(getResources().getColor(R.color.shouye_lanse));
        text_2.setTextColor(getResources().getColor(R.color.shouye_lanse));
        // 背景颜色
        bbtn3.setBackgroundColor(getResources().getColor(R.color.shouye_lanse));
        bbtn1.setBackgroundColor(getResources().getColor(R.color.white));
        bbtn2.setBackgroundColor(getResources().getColor(R.color.white));
//
//        content_view.removeAllViews();
//        view = LayoutInflater.from(this).inflate(R.layout.the_books_view_three, null);
//        content_view.addView(view);
        Collections.sort(datas, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Goods stu1 = (Goods) o1;
                Goods stu2 = (Goods) o2;
                if (stu1.getStore_num() > stu2.getStore_num()) {
                    return -1;
                } else if (stu1.getStore_num() == stu2.getStore_num()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        stockListAdapter.notifyDataSetChanged();
    }

    /**
     * 底部三个按钮点击事件
     **/

    //批量入库按钮
    @OnClick(R.id.piliang_ruku)
    void Piliang_ruku() {

        Intent intent = new Intent(StockGuanliActivity.this, StorageAvtivity.class);
        startActivity(intent);
    }

    //批量出库按钮
    @OnClick(R.id.piliang_chuku)
    void piliang_chuku() {
        Intent intent = new Intent(StockGuanliActivity.this, TheLibraryAityity.class);
        startActivity(intent);
    }

    //库存盘点
    @OnClick(R.id.kucun_pandian)
    void kucun_pandian() {
        Intent intent = new Intent(StockGuanliActivity.this, StockInventoryActivity.class);
        startActivity(intent);
    }

    //搜索按钮
    @OnClick(R.id.et_search)
    void et_search() {
        et_search.setInputType(InputType.TYPE_NULL);
        Intent intent = new Intent(StockGuanliActivity.this, StockSearchGoodsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        Message message = new Message();
        switch (type) {

            case 0:

                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:
                ProgressUtil.dissDialog();
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

        if (refresh != null) {
            refresh.endRefreshing();
            refresh = null;
        }
    }

    int dataType = 0;

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

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(StockGuanliActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    if (refresh != null) {
                        refresh.endRefreshing();
                        refresh = null;
                    }
                    Log.e("库存信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            //处理返回的数据
                            JsonArray jsonArray = result.get("result").getAsJsonArray();

                            kucun_num.setText("(" + jsonArray.size() + ")");
                            for (int i = 0; i < jsonArray.size(); i++) {
                                String object = jsonArray.get(i).getAsJsonObject().toString();
                                Goods good = (Goods) GsonUtils.getEntity(object, Goods.class);
                                int store_num = 0, owe_num = 0;
                                if (good.getProduct_list() != null) {
                                    for (Product p : good.getProduct_list()) {
                                        store_num += p.getStore_num();
                                        owe_num += p.getOwe_num();
                                    }
                                    good.setStore_num(store_num);
                                    good.setOwe_num(owe_num);
                                }
                                datas.add(good);
                            }
                            // 为适配器设置数据
                            stockListAdapter.setGoods(datas);

                            stockListAdapter.notifyDataSetChanged();
                            if (stockListAdapter.getGoods().size() > 0) {
                                no_data_image.setVisibility(View.GONE);
                            } else {
                                no_data_image.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    if (refresh != null) {
                        refresh.endRefreshing();
                        refresh = null;
                    }
                    Log.e("信息", result.toString());
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
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderEasyPresenter.getGoodsList();

//        SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd");
//        Date date=new Date();
//        if(dataType==0){
//            orderEasyPresenter.getRecordList(customer.getCustomer_id(),"1");
//        }else if(dataType==1){
//            orderEasyPresenter.getOrderRecordLlist(customer.getCustomer_id(),"1");
//        }else if(dataType==2){
//            orderEasyPresenter.getOperationRecordList(customer.getCustomer_id(),"1");
//        }


        refresh = refreshLayout;
    }

    /**
     * Description: 初始化PopWindow，以及给PopWindow中的ListView设置adapter,以及Item监听
     */
    private PopupWindow mPopupWindow;

    private void initPopupWindow(View v, int pos) {
        View view;
        view = LayoutInflater.from(this).inflate(R.layout.kucun_item_view, null);
        view.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));


        final Goods good = datas.get(pos);
        LinearLayout btn1 = (LinearLayout) view.findViewById(R.id.click_1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockGuanliActivity.this, StorageAvtivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        LinearLayout btn2 = (LinearLayout) view.findViewById(R.id.click_2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockGuanliActivity.this, TheLibraryAityity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });
        LinearLayout btn3 = (LinearLayout) view.findViewById(R.id.click_3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StockGuanliActivity.this, TiaoZhengAityity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                startActivity(intent);
                mPopupWindow.dismiss();
            }
        });

        mPopupWindow = new PopupWindow(view, 600, 170);   //生成PopWindow
        backgroundAlpha(0.5f);
        mPopupWindow.setOutsideTouchable(true);
        /** 为其设置背景，使得其内外焦点都可以获得 */
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x000000));
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(this);
        //popupwindow相对view位置x轴偏移量
        View viewTemp = mPopupWindow.getContentView();
        viewTemp.measure(0, 0);
        //控件大小
        int width = viewTemp.getMeasuredWidth();
        int xOffset = (v.getWidth() - width) / 2;
        mPopupWindow.showAsDropDown(v, xOffset, 0);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha; //0.0-1.0
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        backgroundAlpha(1.0f);
    }
}
