package cn.order.ordereasy.view.fragment;

/**
 * 首页
 **/

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.utils.UpdataApp;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.BillingActivity;
import cn.order.ordereasy.view.activity.DailyActivity;
import cn.order.ordereasy.view.activity.DataExportActivity;
import cn.order.ordereasy.view.activity.ExperienceInterfaceActivity;
import cn.order.ordereasy.view.activity.LoginActity;
import cn.order.ordereasy.view.activity.MoreActivity;
import cn.order.ordereasy.view.activity.PrintActivity;
import cn.order.ordereasy.view.activity.PurchaseActivity;
import cn.order.ordereasy.view.activity.ScanActivity;
import cn.order.ordereasy.view.activity.SetupAvtivity;
import cn.order.ordereasy.view.activity.ShangHuoActivity;
import cn.order.ordereasy.view.activity.StaffActivity;
import cn.order.ordereasy.view.activity.StockManageActivity;
import cn.order.ordereasy.view.activity.StoreSettingsActivity;
import cn.order.ordereasy.view.activity.WebViewAcitvity;
import cn.order.ordereasy.view.fragment.gooddetailsfragment.DetailsGoodsActivity;
import cn.order.ordereasy.widget.GuideDialog;

public class FragmentStore extends Fragment implements OrderEasyView, SwipeRefreshLayout.OnRefreshListener {

    private View rootView;// 缓存Fragment view
    private static final String TAG = FragmentStore.class.getSimpleName();
    private OrderEasyPresenter orderEasyPresenter;
    private AlertDialog alertDialog;
    int time = 10;
    private String is_boss;
    private String info1;
    private String url;

    public interface setFragmentPageListen {
        public void setFragmentPage(int type);
    }

    setFragmentPageListen s;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_store, null);

        }
        ButterKnife.inject(this, rootView);

        // 缓存的rootView需要判断是否已经被加过parent，
        // 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        initRefreshLayout();
        SharedPreferences spPreferences = getActivity().getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        is_boss = spPreferences.getString("is_boss", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            long expire = shop.get("expire").getAsLong();
            data_time.setText(TimeUtil.getTimeStamp2Str(expire, "yyyy-MM-dd"));
        }
        //新手引导
        new GuideDialog(1, getActivity());
        return rootView;
    }


    //下拉刷新
    @InjectView(R.id.store_refresh)
    SwipeRefreshLayout store_refresh;

    private void initRefreshLayout() {
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        store_refresh.setOnRefreshListener(this);
        orderEasyPresenter.isUpdataApp();
        orderEasyPresenter.getCustomerList1();
    }

    @Override
    public void onRefresh() {
        //首页下拉刷新，只需要更新今日交易额，今日开单数，当前欠款数，当前欠货数的数据显示
        if (is_boss.equals("1")) {
            orderEasyPresenter.getNumToday2(1);
        } else {
            store_refresh.setRefreshing(false);
        }
    }

    /**
     * 找到控件ID
     ***/
    @InjectView(R.id.setup)
    public ImageView setup;

    //顶部logo
    @InjectView(R.id.logo_click)
    public ImageView logo_click;

    //顶部logo 可点击
    @InjectView(R.id.logo_click2)
    public LinearLayout logo_click2;
    //店铺名称
    @InjectView(R.id.shop_name)
    public TextView shop_name;

    //扫一扫
    @InjectView(R.id.saoyisao)
    public LinearLayout saoyisao;
    //开单
    @InjectView(R.id.kaidan)
    public LinearLayout kaidan;
    //上货
    @InjectView(R.id.shanghuo)
    public LinearLayout shanghuo;

    //黄色提醒View
    @InjectView(R.id.lay_onclick)
    public LinearLayout lay_onclick;


    /***中间的4个点击跳转**/
    @InjectView(R.id.view_1)
    public LinearLayout view_1;

    @InjectView(R.id.view_2)
    public LinearLayout view_2;

    @InjectView(R.id.view_3)
    public LinearLayout view_3;

    @InjectView(R.id.view_4)
    public LinearLayout view_4;

    /***中间的4个数据展示控件ID***/
    //交易额
    @InjectView(R.id.jiaoyie)
    public TextView jiaoyie;
    //开单数
    @InjectView(R.id.kaidan_num)
    public TextView kaidan_num;
    //欠款数
    @InjectView(R.id.qiankuan_money)
    public TextView qiankuan_money;
    //欠货数
    @InjectView(R.id.qianhuo)
    public TextView qianhuo;

    /**
     * 底部8个按钮控件ID
     **/
    @InjectView(R.id.click_1)
    public LinearLayout click_1;
    @InjectView(R.id.click_2)
    public LinearLayout click_2;
    @InjectView(R.id.click_3)
    public LinearLayout click_3;
    @InjectView(R.id.click_4)
    public LinearLayout click_4;
    @InjectView(R.id.click_5)
    public LinearLayout click_5;
    @InjectView(R.id.click_6)
    public LinearLayout click_6;
    @InjectView(R.id.click_7)
    public LinearLayout click_7;
    @InjectView(R.id.click_8)
    public LinearLayout click_8;

    @InjectView(R.id.data_time)
    public TextView data_time;

    /**
     * 控件点击事件
     ***/
    //跳转到设置界面
    @OnClick(R.id.setup)
    void setup() {
        Intent intent = new Intent(getActivity(), SetupAvtivity.class);
        startActivity(intent);

    }

    //顶部logo点击跳转
    @OnClick(R.id.logo_click2)
    void logo_click2() {
        SharedPreferences spPreferences = getActivity().getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String key = shop.get("shop_key").getAsString();
            Uri uri = Uri.parse("https://m.dinghuo5u.com/wx/" + key);
            Intent intent = new Intent(getActivity(), WebViewAcitvity.class);
            intent.putExtra("key", "https://m.dinghuo5u.com/wx/" + key);
            startActivity(intent);
        }

    }

    //扫一扫点击事件
    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(getActivity(), ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    //开单点击事件
    @OnClick(R.id.kaidan)
    void kaidan() {
        Intent intent = new Intent(getActivity(), BillingActivity.class);
        startActivity(intent);
    }

    //上货点击事件
    @OnClick(R.id.shanghuo)
    void shanghuo() {
        Intent intent = new Intent(getActivity(), ShangHuoActivity.class);
        startActivity(intent);
    }

    //体验时间view
    @OnClick(R.id.lay_onclick)
    void lay_onclick() {
        SharedPreferences spPreferences = getActivity().getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            Intent intent = new Intent(getActivity(), ExperienceInterfaceActivity.class);
            long remain_time = shop.get("remain_time").getAsLong();
            long expire = shop.get("expire").getAsLong();
            Bundle bundle = new Bundle();
            bundle.putLong("remain_time", remain_time);
            bundle.putLong("expire", expire);
            bundle.putString("flag", "guoqi");
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    /***中间四个view 的点击事件*/
    @OnClick(R.id.view_1)
    void view_1() {
        Intent intent = new Intent(getActivity(), AccountBookActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.view_2)
    void view_2() {
        s = (setFragmentPageListen) getActivity();
        s.setFragmentPage(2);
    }

    @OnClick(R.id.view_3)
    void view_3() {
        s = (setFragmentPageListen) getActivity();
        s.setFragmentPage(3);
    }

    @OnClick(R.id.view_4)
    void view_4() {
        s = (setFragmentPageListen) getActivity();
        s.setFragmentPage(3);
    }

    /***底部8个view点击事件**/
    @OnClick(R.id.click_1)
    void click_1() {
        Intent intent = new Intent(getActivity(), StockManageActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_2)
    void click_2() {
        Intent intent = new Intent(getActivity(), AccountBookActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_3)
    void click_3() {
        Intent intent = new Intent(getActivity(), DailyActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_4)
    void click_4() {
        Intent intent = new Intent(getActivity(), StaffActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_5)
    void click_5() {
        Intent intent = new Intent(getActivity(), PurchaseActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_6)
    void click_6() {
        Intent intent = new Intent(getActivity(), DataExportActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.click_7)
    void click_7() {
        SharedPreferences spPreferences = getActivity().getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String key = shop.get("shop_key").getAsString();
            Uri uri = Uri.parse("https://m.dinghuo5u.com/wx/" + key);
            Intent intent = new Intent(getActivity(), WebViewAcitvity.class);
            intent.putExtra("key", "https://m.dinghuo5u.com/wx/" + key);
            startActivity(intent);
        }
    }

    //更多界面
    @OnClick(R.id.click_8)
    void click_8() {
        Intent intent = new Intent(getActivity(), MoreActivity.class);
        startActivity(intent);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            if (TextUtils.isEmpty(res)) {
                ToastUtil.show("没有识别到二维码信息");
            } else {
                if (DataStorageUtils.getInstance().getShelvesGoods().size() > 0) {
                    List<Goods> datas = DataStorageUtils.getInstance().getShelvesGoods();
                    boolean isExist = false;
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
                        startActivity(intent);
                    } else {
                        ToastUtil.show("没有找到该货品");
                    }
                } else {
                    ToastUtil.show("没有找到该货品");
                }

            }
            MyLog.e("扫一扫返回数据", res);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("FragmentStore", "onResume");
        lay_onclick.setVisibility(View.VISIBLE);
        Countdown();
        //首页左上角的店铺的logo和名称
        orderEasyPresenter.getStoreInfo();

        //首页今日交易额，今日开单数，当前欠款数，当前欠货数的数据显示
        orderEasyPresenter.getStoreData();
    }


    @Override
    public void showProgress(int type) {
//		if(!store_refresh.getCurrentRefreshStatus().equals(BGARefreshLayout.RefreshStatus.REFRESHING)){
//			store_refresh.beginRefreshing();
//		}
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
        switch (type) {
            case 0:
                ProgressUtil.dissDialog();
                if (data.get("code").getAsInt() == -7) {
                    message.what = 1001;
                } else {
                    message.what = 1002;
                }
                break;

            case 1:
                ProgressUtil.dissDialog();
                if (data.get("code").getAsInt() == -7) {
                    message.what = 1001;
                } else {
                    message.what = 1003;
                }
                break;

            case 2:
                ProgressUtil.dissDialog();
                if (data.get("code").getAsInt() == -7) {
                    message.what = 1001;
                } else {
                    message.what = 1004;
                }
                break;
            case 3:
                ProgressUtil.dissDialog();
                if (data.get("code").getAsInt() == -7) {
                    message.what = 1001;
                } else {
                    message.what = 1005;
                }
                break;
            case 4:
                ProgressUtil.dissDialog();
                if (data.get("code").getAsInt() == -7) {
                    message.what = 1001;
                } else {
                    message.what = 1006;
                }
                break;
        }

        message.obj = data;
        myHandler.sendMessage(message);
    }

    public Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1001) {

            }
            if (msg.what == 1002) {
                JsonObject jo1 = (JsonObject) msg.obj;
                JsonObject jo2 = jo1.getAsJsonObject("result");
                if (jo2 == null) return;
                JsonObject jo3 = jo2.getAsJsonObject("shop_info");

                ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + jo3.get("logo").getAsString(), logo_click);
                shop_name.setText(jo3.get("name").getAsString());
            }
            if (msg.what == 1003) {
                JsonObject jo1 = (JsonObject) msg.obj;
                JsonObject jo2 = jo1.getAsJsonObject("result");
                if (jo2 == null) {
                    jiaoyie.setText("...");
                    kaidan_num.setText("...");
                    qiankuan_money.setText("...");
                    qianhuo.setText("...");
                } else {
                    jiaoyie.setText(jo2.get("trade_sum").getAsString());
                    kaidan_num.setText(jo2.get("bill_num").getAsString());
                    qiankuan_money.setText(jo2.get("receivable").getAsString());
                    qianhuo.setText(jo2.get("owe_sum").getAsString());
                }
            }
            if (msg.what == 1004) {
                JsonObject jo1 = (JsonObject) msg.obj;
                JsonObject jo2 = jo1.getAsJsonObject("result");
                if (jo2 == null) return;
                JsonObject jo3 = jo2.getAsJsonObject("total");
            }
            if (msg.what == 1005) {
                JsonObject data = (JsonObject) msg.obj;
                if (data != null) {
                    if (data.get("code").getAsInt() == 1) {
                        String version1 = data.getAsJsonObject("result").get("ver").getAsString();
                        info1 = data.getAsJsonObject("result").get("info").getAsString();
                        url = data.getAsJsonObject("result").get("url").getAsString();
                        if (SystemfieldUtils.getVersion(getActivity()) < SystemfieldUtils.getVersionId(version1)) {
                            showUpdatadialogs();
                        }
                    }
                }
            }
            if (msg.what == 1006) {
                JsonObject result = (JsonObject) msg.obj;
                if (result != null) {
                    int status = result.get("code").getAsInt();
                    if (status == 1) {
                        //成功
                        Log.e("FragmentStore", "result:" + result.toString());
                        List<Customer> datas = new ArrayList<>();
                        JsonArray jsonArray = result.get("result").getAsJsonArray();
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
                        DataStorageUtils.getInstance().setCustomerLists(datas);
                    } else {
                        if (status == -7) {
                            ToastUtil.show(getString(R.string.landfall_overdue));
                            Intent intent = new Intent(getActivity(), LoginActity.class);
                            startActivity(intent);
                        }
                    }
                }
                Log.e("信息", result.toString());
            }
            if (msg.what == 1008) {
                //隐藏view 实例化
                lay_onclick.setVisibility(View.GONE);
            }


        }
    };

    /**
     * 60秒倒计时
     */
    private void Countdown() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                for (int i = 5; i > 0; i--) {
                    time--;
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message message = new Message();
                message.what = 1008;
                myHandler.sendMessage(message);
            }
        }.start();
    }

    private void showUpdatadialogs() {
        alertDialog = new AlertDialog.Builder(getActivity()).create();
        View view = View.inflate(getActivity(), R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        //设置点击屏幕不让消失
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("检测到新版本可供升级");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        text_conten.setText(info1 + "您确定要更新版本？");


        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setText("取消");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setText("去升级");
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdataApp(getActivity(), url);
                alertDialog.dismiss();
            }
        });
    }
}
