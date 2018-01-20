package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.PriceAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.Spec;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/5.
 * 价格设置 activity
 */

public class PriceSetActivity extends BaseActivity implements OrderEasyView {

    SharedPreferences sp;
    AlertDialog alertDialog;
    OrderEasyPresenter orderEasyPresenter;
    private List<Map<String, Object>> data = new ArrayList<>();
    private List<Map<String, Object>> data1 = new ArrayList<>();
    DecimalFormat df = new DecimalFormat("#0.00");
    private PriceAdapter priceAdatper;
    private static final int REQUEST_CODE_PRICE_PREVIEW = 1004;
    private boolean isFirst;
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.price_set);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        flag = bundle.getString("flag");
        Goods good = (Goods) bundle.getSerializable("good");
        sp = getSharedPreferences("price", 0);
        priceAdatper = new PriceAdapter(this);
        priceAdatper.setOnItemClickListener(new PriceAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                switch (view.getId()) {
                    case R.id.te_cb_jia_layout:
                        showdialogs(3, postion);
                        break;
                    case R.id.te_xs_jia_layout:
                        showdialogs(4, postion);
                        break;
                }
            }
        });
        price_listview.setAdapter(priceAdatper);
        if (flag.equals("detail")) {
            initDetail(good);
            initData(good);
        } else {
            initData(good);
        }
    }

    private void initDetail(Goods good) {
        List<Product> products = good.getProduct_list();
        for (Product product : products) {
            Map<String, Object> map = new HashMap<>();
            List<String> str = product.getSpec_data();
            if (str.size() == 2) {
                String name = str.get(0) + "/" + str.get(1);
                map.put("name", name);
                map.put("cb", df.format(product.getCost_price()));
                map.put("xs", df.format(product.getSell_price()));
                data1.add(map);
            } else {
                String name = str.get(0);
                map.put("name", name);
                map.put("cb", df.format(product.getCost_price()));
                map.put("xs", df.format(product.getSell_price()));
                data1.add(map);
            }
            sp.edit().putString(map.get("name").toString(), df.format(product.getCost_price()) + "," + df.format(product.getSell_price())).commit();
        }
    }

    private void initData(Goods good) {

        List<Spec> specs = good.getSpec();
        if (specs.size() > 0) {
            //选择了规格
            if (specs.size() == 1) {
                List<String> values = specs.get(0).getValues();
                if (values.size() < 1) return;
                for (String str : values) {
                    Map<String, Object> map = new HashMap<>();
                    String guige = sp.getString(str, "");
                    if (TextUtils.isEmpty(guige)) {
                        map.put("name", str);
                        map.put("cb", df.format(0));
                        map.put("xs", df.format(0));
                    } else {
                        String[] prices = guige.split(",");
                        map.put("name", str);
                        map.put("cb", df.format(Double.parseDouble(prices[0])));
                        map.put("xs", df.format(Double.parseDouble(prices[1])));
                    }
                    data.add(map);

                }
            } else {
                List<String> values = specs.get(0).getValues();
                List<String> values1 = specs.get(1).getValues();
                if (values.size() == 0) {
                    for (String str : values1) {
                        Map<String, Object> map = new HashMap<>();
                        String guige = sp.getString(str, "");
                        if (TextUtils.isEmpty(guige)) {
                            map.put("name", str);
                            map.put("cb", df.format(0));
                            map.put("xs", df.format(0));
                        } else {
                            String[] prices = guige.split(",");
                            map.put("name", str);
                            map.put("cb", df.format(Double.parseDouble(prices[0])));
                            map.put("xs", df.format(Double.parseDouble(prices[1])));
                        }
                        data.add(map);
                    }
                }
                if (values1.size() == 0) {
                    for (String str : values) {
                        Map<String, Object> map = new HashMap<>();
                        String guige = sp.getString(str, "");
                        if (TextUtils.isEmpty(guige)) {
                            map.put("name", str);
                            map.put("cb", df.format(0));
                            map.put("xs", df.format(0));
                        } else {
                            String[] prices = guige.split(",");
                            map.put("name", str);
                            map.put("cb", df.format(Double.parseDouble(prices[0])));
                            map.put("xs", df.format(Double.parseDouble(prices[1])));
                        }
                        data.add(map);
                    }

                }
                if (values1.size() != 0 && values.size() != 0) {
                    for (int i = 0; i < values.size(); i++) {
                        for (int j = 0; j < values1.size(); j++) {
                            Map<String, Object> map = new HashMap<>();
                            String name = values.get(i) + "/" + values1.get(j);
                            Log.e("PriceSetActivity", name);
                            String guige = sp.getString(name, "");
                            if (TextUtils.isEmpty(guige)) {
                                map.put("name", name);
                                map.put("cb", df.format(0));
                                map.put("xs", df.format(0));
                            } else {
                                String[] prices = guige.split(",");
                                map.put("name", name);
                                map.put("cb", df.format(Double.parseDouble(prices[0])));
                                map.put("xs", df.format(Double.parseDouble(prices[1])));
                            }
                            data.add(map);
                        }
                    }
                }

            }
        } else {
            //没有选择规格
            data = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            String guige = sp.getString("无", "");
            if (TextUtils.isEmpty(guige)) {
                map.put("name", "无");
                map.put("cb", df.format(0));
                map.put("xs", df.format(0));
            } else {
                String[] prices = guige.split(",");
                map.put("name", "无");
                map.put("cb", df.format(Double.parseDouble(prices[0])));
                map.put("xs", df.format(Double.parseDouble(prices[1])));
            }
            data.add(map);

        }
        priceAdatper.setData(data);
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //确认按钮
    @InjectView(R.id.queren)
    TextView queren;

    //设置成本价
    @InjectView(R.id.chengben_jia)
    LinearLayout chengben_jia;

    //设置销售价
    @InjectView(R.id.xiaoshou_jia)
    LinearLayout xiaoshou_jia;


    //规格价格列表
    @InjectView(R.id.price_listview)
    ListView price_listview;

//    //成本价显示
//    @InjectView(R.id.te_cb_jia)
//    TextView te_cb_jia;
//    //销售价显示
//    @InjectView(R.id.te_xs_jia)
//    TextView te_xs_jia;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        PriceSetActivity.this.finish();
    }

    //确认按钮
    @OnClick(R.id.queren)
    void queren() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
       /* String desc=description.getText().toString();
        bundle.putString("desc",desc);*/

        intent.putExtras(bundle);
        setResult(REQUEST_CODE_PRICE_PREVIEW, intent);
        finish();
    }

    //设置成本价按钮
    @OnClick(R.id.chengben_jia)
    void chengben_jia() {
        showdialogs(1, 0);

    }

    //设置销售价按钮
    @OnClick(R.id.xiaoshou_jia)
    void xiaoshou_jia() {
        showdialogs(2, 0);
    }

    //弹出框
    private void showdialogs(final int type, final int pos) {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请设置统一价格");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);

        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
        //设置编辑文本光标在内容后面

//        ed_type_name.setSelection(ed_type_name.getText().length());
        if (type == 1) {

        } else if (type == 2) {

        } else if (type == 3) {
            ed_type_name.setText(data.get(pos).get("cb").toString());
        } else if (type == 4) {
            ed_type_name.setText(data.get(pos).get("xs").toString());
        }
        //限制输入只能为数字
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //限制输入长度
//        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Double.parseDouble(ed_type_name.getText().toString()) < 0) {
                    ed_type_name.setText(0 + "");
                }
                if (TextUtils.isEmpty(ed_type_name.getText().toString())) {
                    showToast("价格不能为空！");
                    return;
                }
                if (type == 1) {
                    //统一改变成本价
                    for (Map<String, Object> map : data) {
                        //改变adapter数据
                        sp.edit().putString(map.get("name").toString(), ed_type_name.getText().toString() + "," + map.get("xs").toString()).commit();
                        map.put("cb", df.format(Double.parseDouble(ed_type_name.getText().toString())));
                    }
                } else if (type == 2) {
                    //统一改变销售价
                    for (Map<String, Object> map : data) {
                        //改变adapter数据
                        sp.edit().putString(map.get("name").toString(), map.get("cb").toString() + "," + ed_type_name.getText().toString()).commit();
                        map.put("xs", df.format(Double.parseDouble(ed_type_name.getText().toString())));
                    }
                } else if (type == 3) {
                    //改变单行的成本
                    sp.edit().putString(data.get(pos).get("name").toString(), ed_type_name.getText().toString() + "," + data.get(pos).get("xs").toString()).commit();
                    String amount = df.format(Double.parseDouble(ed_type_name.getText().toString()));
                    data.get(pos).put("cb", amount);
                } else if (type == 4) {
                    //改变单行的销售
                    sp.edit().putString(data.get(pos).get("name").toString(), data.get(pos).get("cb").toString() + "," + ed_type_name.getText().toString()).commit();
                    data.get(pos).put("xs", df.format(Double.parseDouble(ed_type_name.getText().toString())));
                }
                alertDialog.dismiss();
                priceAdatper.notifyDataSetChanged();
            }
        });
        //监听edittext
        ed_type_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    queren.setTextColor(getResources().getColor(R.color.lanse));
                    queren.setEnabled(true);
                } else {
                    queren.setTextColor(getResources().getColor(R.color.touzi_huise));
                    queren.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        CharSequence text = ed_type_name.getText();
        if (text instanceof Spannable) {
            Spannable spannable = (Spannable) text;
            Selection.setSelection(spannable, text.length());
        }
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {

    }
}
