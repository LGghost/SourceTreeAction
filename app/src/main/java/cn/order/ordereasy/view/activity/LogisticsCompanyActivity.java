package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.CompanyAdapter;

public class LogisticsCompanyActivity extends BaseActivity implements BGAOnItemChildClickListener, AdapterView.OnItemClickListener {
    private AlertDialog alertDialog;
    private List<String> addrs = new ArrayList<>();
    private CompanyAdapter adapter;
    private SharedPreferences sp;
    private String companyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logistics_company_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new CompanyAdapter(this);
        sp = this.getSharedPreferences("company", 0);
        companyString = sp.getString("companyaddrs", "");
        if (!TextUtils.isEmpty(companyString)) {
            addrs = getList(companyString);
        } else {
            addrs = getList1(this.getResources().getStringArray(R.array.logistics_array));
            saveList(addrs);
        }
        adapter.setData(addrs);
        adapter.setOnItemChildClickListener(this);
        lisview.setAdapter(adapter);
        lisview.setOnItemClickListener(this);
    }

    private List<String> getList1(String[] list) {
        List<String> topicList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            topicList.add(list[i]);
        }
        return topicList;
    }

    private List<String> getList(String str) {
        String[] list = str.split(",");
        return getList1(list);
    }

    private void saveList(List<String> list) {
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += list.get(i) + ",";
        }
        sp.edit().putString("companyaddrs", str).commit();
    }

    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.add_company)
    LinearLayout add_company;
    @InjectView(R.id.lisview)
    ListView lisview;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.add_company)
    void add_company() {
        showdialogs();
    }

    //弹出框
    private void showdialogs() {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入属性名称");
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
        //hint内容
        ed_type_name.setHint("最多输入10个字符");
        //限制输入长度
        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_type_name.getText().toString();
                if (!TextUtils.isEmpty(name)) {
                    addrs.add(0,name);
                    adapter.setData(addrs);
                    saveList(addrs);
                }
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        addrs.remove(position);
        saveList(addrs);
        adapter.closeOpenedSwipeItemLayout();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("addrs", addrs.get(position));
        setResult(1001, intent);
        finish();
    }
}