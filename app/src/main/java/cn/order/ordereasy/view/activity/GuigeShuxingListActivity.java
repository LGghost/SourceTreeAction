package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildClickListener;
import cn.bingoogolapple.androidcommon.adapter.BGAOnItemChildLongClickListener;
import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SpecViewAdapter;
import cn.order.ordereasy.adapter.SpecsAdapter;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/10.
 * <p>
 * 规格属性列表
 */

public class GuigeShuxingListActivity extends BaseActivity implements OrderEasyView, AdapterView.OnItemClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {

    AlertDialog alertDialog;
    List<Map<String, Object>> data = new ArrayList<>();
    private SpecsAdapter mAdapter;
    String id = "", name = "";
    JsonArray jsonArrayData = null;
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guige_shuxing_list);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        guige_listview.setOnItemClickListener(this);
        mAdapter = new SpecsAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
        guige_listview.setAdapter(mAdapter);
        guige_listview.setEmptyView(no_data_view);
        Bundle bundle = getIntent().getExtras();
        this.id = bundle.getString("id");
        name = bundle.getString("name");
        shuxing_name.setText(name);
        SharedPreferences sp = getSharedPreferences("specs", 0);
        String data = sp.getString(id, "");
        if (!TextUtils.isEmpty(data)) {
            try {
                JsonArray array = (JsonArray) GsonUtils.getObj(data, JsonArray.class);
                jsonArrayData = array;
                for (int i = 0; i < array.size(); i++) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", array.get(i).getAsString());
                    this.data.add(map);
                }
            } catch (Exception e) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", "无");
                this.data.add(map);
            }
        }

        initData();
    }

    private void initData() {
        mAdapter.setData(data);
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
        ed_type_name.setHint("最多输入12个字符");
        //限制输入长度
//        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
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
                name = ed_type_name.getText().toString();
                if (name.equals("无") || name.equals("无规格")) {
                    ToastUtil.show(name + "不能用于规格名称");
                    alertDialog.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.show("属性不能为空");
                    alertDialog.dismiss();
                    return;
                } else {
                    if (name.length() > 12) {
                        ToastUtil.show("规格属性最多输入12个字符");
                        alertDialog.dismiss();
                        return;
                    }
                }
                orderEasyPresenter.addSpecValueInfo(Integer.parseInt(id), ed_type_name.getText().toString());
                alertDialog.dismiss();
            }
        });
    }


    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //listview
    @InjectView(R.id.guige_listview)
    ListView guige_listview;

    //添加新规格
    @InjectView(R.id.button_click)
    LinearLayout button_click;
    @InjectView(R.id.shuxing_name)
    TextView shuxing_name;
    @InjectView(R.id.no_data_view)
    ImageView no_data_view;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        GuigeShuxingListActivity.this.finish();
    }

    @OnClick(R.id.button_click)
    void add() {
        showdialogs();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        final String name = mAdapter.getData().get(position).get("title").toString();
        if (childView.getId() == R.id.tv_item_swipe_delete) {

            orderEasyPresenter.delSpecValueInfo(Integer.parseInt(id), name);
            SharedPreferences sp = getSharedPreferences("specs", MODE_MULTI_PROCESS);
            jsonArrayData.remove(position);
            sp.edit().putString(id, jsonArrayData.toString()).commit();
            // 作为ListView的item使用时，如果删除了某一个item，请先关闭已经打开的item，否则其他item会显示不正常（RecyclerView不会有这个问题）
            mAdapter.closeOpenedSwipeItemLayout();
            mAdapter.removeItem(position);
            mAdapter.notifyDataSetChanged();

        }
    }

    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        return false;
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
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
                    message.what = 1003;

                }

                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
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
                            Map<String, Object> map = new HashMap<>();
                            map.put("title", name);
                            data.add(map);
                            if (jsonArrayData != null) {
                                jsonArrayData.add(name);
                            } else {
                                jsonArrayData = new JsonArray();
                                jsonArrayData.add(name);
                            }
                            SharedPreferences sp = getSharedPreferences("specs", 0);
                            Log.e("GuigeShuxing", jsonArrayData.toString());
                            sp.edit().putString(id, jsonArrayData.toString()).commit();
                            name = "";
                            mAdapter.notifyDataSetChanged();

                        }
                    }
                    Log.e("信息", result.toString());
                    break;
            }
        }
    };
}
