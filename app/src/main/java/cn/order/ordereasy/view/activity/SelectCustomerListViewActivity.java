package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SelcetSortAdapter;
import cn.order.ordereasy.adapter.SelectCustomerListAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.SelectCustomer;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.widget.CharacterParser;
import cn.order.ordereasy.widget.IndexView;
import cn.order.ordereasy.widget.PinyinComparator;

public class SelectCustomerListViewActivity extends BaseActivity implements TextWatcher, AdapterView.OnItemClickListener, IndexView.Delegate, AbsListView.OnScrollListener {

    private SelectCustomerListAdapter adapter;
    List<Customer> list = new ArrayList<>();
    private SelcetSortAdapter selcetAdapter;
    private boolean isCurrent = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_customer_listview_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initView();
        adapter = new SelectCustomerListAdapter(this);
        selcetAdapter = new SelcetSortAdapter(this);
        list = DataStorageUtils.getInstance().getCustomerLists();
        sortData();
        indexview.setDelegate(this);
        adapter.setData(getState(list));
        xzkh_listviwe.setAdapter(adapter);
        sousuo_listviwe.setAdapter(selcetAdapter);
        sousuo_listviwe.setOnItemClickListener(this);
        xzkh_listviwe.setOnScrollListener(this);
    }

    private void initView() {
        Animation slide_in_left = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation slide_out_right = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);
        view_switcher.setInAnimation(slide_in_left);
        view_switcher.setOutAnimation(slide_out_right);
        et_search.addTextChangedListener(this);

    }

    private List<SelectCustomer> getState(List<Customer> list) {
        List<SelectCustomer> state = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SelectCustomer sCustomer = new SelectCustomer();
            sCustomer.setCustomer(list.get(i));
            sCustomer.setPosition(i);
            sCustomer.setSelect(false);
            state.add(sCustomer);
        }
        Log.e("fillData", "state:" + state.size());
        return state;
    }

    private void sortData() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        CharacterParser characterParser = CharacterParser.getInstance();
        for (Customer indexModel : list) {
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
        Collections.sort(list, pinyinComparator);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //ListView
    @InjectView(R.id.xzkh_listviwe)
    ListView xzkh_listviwe;
    @InjectView(R.id.sousuo_listviwe)
    ListView sousuo_listviwe;
    @InjectView(R.id.et_search)
    EditText et_search;
    @InjectView(R.id.customer_cancel)
    TextView customer_cancel;
    @InjectView(R.id.indexview)
    IndexView indexview;
    @InjectView(R.id.view_switcher)
    ViewSwitcher view_switcher;
    @InjectView(R.id.view_tip)
    TextView view_tip;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.customer_cancel)
    void customer_cancel() {
        isCurrent = true;
        customer_cancel.setVisibility(View.GONE);
        view_switcher.setDisplayedChild(0);
    }

    @OnClick(R.id.customer_confirm)
    void customer_confirm() {
        List<Customer> data = new ArrayList<>();
        for (SelectCustomer sCustomer : adapter.getData()) {
            if (sCustomer.isSelect()) {
                data.add(sCustomer.getCustomer());
            }
        }
        DataStorageUtils.getInstance().setSelectCustomer(data);
        setResult(1001);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            if (isCurrent) {
                isCurrent = false;
                view_switcher.setDisplayedChild(1);
            }
            customer_cancel.setVisibility(View.VISIBLE);
            List<Customer> cusList = Customer.likeString2(list, s.toString());
            if (list.size() > 0) {
                selcetAdapter.setData(cusList);
            } else {
                selcetAdapter.setData(new ArrayList<Customer>());
            }

        } else {
            if (!isCurrent) {
                isCurrent = true;
                view_switcher.setDisplayedChild(0);
                customer_cancel.setVisibility(View.GONE);
            }
            selcetAdapter.setData(new ArrayList<Customer>());
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        List<Customer> data = new ArrayList<>();
        Customer customer = selcetAdapter.getData().get(position);
        data.add(customer);
        DataStorageUtils.getInstance().setSelectCustomer(data);
        setResult(1001);
        finish();
    }

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

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 100:
                    view_tip.setVisibility(View.GONE);
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
            view_tip.setText(adapter.getItem(firstVisibleItem).getCustomer().getTopic());
        }
    }
}
