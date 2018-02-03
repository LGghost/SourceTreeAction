package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SelectCustomerAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CharacterParser;
import cn.order.ordereasy.widget.IndexView;
import cn.order.ordereasy.widget.PinyinComparator;

/**
 * Created by Administrator on 2017/9/17.
 * <p>
 * 选择客户
 */

public class SelectCustomersActivity extends BaseActivity implements OrderEasyView, IndexView.Delegate, AbsListView.OnScrollListener {

    SelectCustomerAdapter adapter;
    List<Customer> data = new ArrayList<>();
    OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_customers);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new SelectCustomerAdapter(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        xzkh_listviwe.setAdapter(adapter);
        indexview.setDelegate(this);
        xzkh_listviwe.setOnScrollListener(this);
        if (DataStorageUtils.getInstance().getCustomerLists().size() > 0) {
            data = DataStorageUtils.getInstance().getCustomerLists();
            adapter.setData(this.data);
        } else {
            orderEasyPresenter.getCustomerList();
        }

        xzkh_listviwe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Customer customer = new Customer();
                customer.setCustomer_id(adapter.getData().get(position).getCustomer_id());
                customer.setCustomer_name(adapter.getData().get(position).getName());
                customer.setTelephone(adapter.getData().get(position).getTelephone());
                customer.setAddress(adapter.getData().get(position).getAddress());
                customer.setReceivable(adapter.getData().get(position).getReceivable());
                customer.setRank_discount(adapter.getData().get(position).getRank_discount());
                bundle.putSerializable("data", customer);
                intent.putExtras(bundle);
                setResult(1004, intent);
                finish();
            }
        });

        sortData();

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //ListView
    @InjectView(R.id.xzkh_listviwe)
    ListView xzkh_listviwe;
    @InjectView(R.id.indexview)
    IndexView indexview;
    @InjectView(R.id.view_tip)
    TextView view_tip;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        SelectCustomersActivity.this.finish();
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

    void sortData() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        CharacterParser characterParser = CharacterParser.getInstance();
        for (Customer indexModel : data) {
            String name = "";
            if (TextUtils.isEmpty(indexModel.getName())) {
                name = "-";
            } else {
                name = indexModel.getName();
            }

            String char_name = characterParser.getSelling(name).substring(0, 1).toUpperCase();
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            Matcher matcher = pattern.matcher(char_name);
            //当条件满足时，将返回true，否则返回false
            if (matcher.matches()) {
                indexModel.setTopic(characterParser.getSelling(name).substring(0, 1).toUpperCase());
            } else {
                indexModel.setTopic("#");
            }
        }
        Collections.sort(data, pinyinComparator);
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

                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            if (jsonArray.size() > 0) {
                                SharedPreferences sp = getSharedPreferences("customers", 0);
                                sp.edit().putString("customers", result.get("result").toString()).commit();
                            }
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Customer customer = (Customer) GsonUtils.getEntity(jsonArray.get(i).toString(), Customer.class);
                                data.add(customer);
                            }
                            adapter.setData(data);
                            sortData();
                            adapter.notifyDataSetChanged();
                        } else {

                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {

                        } else {

                        }
                    }
                    Log.e("保存信息", result.toString());
                    break;
                case 100:
                    view_tip.setVisibility(View.GONE);
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
    public void onIndexViewSelectedChanged(IndexView indexView, String text) {
        int position = adapter.getPositionForCategory(text.charAt(0));
        if (position != -1) {
            // position的item滑动到ListView的第一个可见条目
            xzkh_listviwe.setSelection(position);
            view_tip.setVisibility(View.VISIBLE);
            handler.sendEmptyMessageDelayed(100, 1000);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (adapter.getCount() > 0) {
            view_tip.setText(adapter.getItem(firstVisibleItem).getTopic());
        }
    }
}
