package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
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
import cn.order.ordereasy.adapter.ShuxingSelectViewAdapter;
import cn.order.ordereasy.adapter.SpecViewAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;

/**
 * Created by Administrator on 2017/9/10.
 * <p>
 * 规格属性选择列表
 */

public class GuigeShuxingSelectListActivity extends BaseActivity implements OrderEasyView, AdapterView.OnItemClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {

    AlertDialog alertDialog;
    List<Map<String, Object>> shuxingData = new ArrayList<>();
    private ShuxingSelectViewAdapter mAdapter;
    String id = "", name = "", flag = "";
    int code = 0;
    JsonArray jsonArrayData = null;
    private OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guige_shuxing_select_list);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        guige_listview.setOnItemClickListener(this);
        mAdapter = new ShuxingSelectViewAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
        guige_listview.setAdapter(mAdapter);
        guige_listview.setEmptyView(no_data_view);
        Bundle bundle = getIntent().getExtras();
        this.id = bundle.getString("id");
        this.flag = bundle.getString("flag");
        this.code = bundle.getInt("code", 0);
        SharedPreferences sp = getSharedPreferences("specs", MODE_MULTI_PROCESS);
        String data = sp.getString(id, "");

        if (!TextUtils.isEmpty(data) && !data.equals("[\"\"]")) {
            shuxingData.clear();
            JsonArray array = (JsonArray) GsonUtils.getObj(data, JsonArray.class);
            jsonArrayData = array;
            Log.e("GuigeShuxing", jsonArrayData.toString());
            for (int i = 0; i < array.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", array.get(i).getAsString());
                map.put("isChecked", 2);
                shuxingData.add(map);
            }
            mAdapter.setData(shuxingData);
        } else {
            ToastUtil.show("没有找到属性库该规格可能已经从规格库中删除");
        }

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
                orderEasyPresenter.addSpecValueInfo(Integer.parseInt(id), ed_type_name.getText().toString());
                name = ed_type_name.getText().toString();
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

    @InjectView(R.id.shuxing_queding)
    TextView shuxing_queding;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        GuigeShuxingSelectListActivity.this.finish();
    }

    @OnClick(R.id.shuxing_queding)
    void queding() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        String data = "";
        for (Map<String, Object> map : mAdapter.getData()) {

            String isChecked = map.get("isChecked").toString();
            if (isChecked != null) {
                if (isChecked.equals("1")) {
                    data += map.get("title").toString();
                    if (!TextUtils.isEmpty(data)) data += ",";
                }
            }
        }
        bundle.putSerializable("data", data);
        intent.putExtras(bundle);
        setResult(code, intent);
        finish();
    }

    @OnClick(R.id.button_click)
    void add() {
        showdialogs();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (flag.equals("guige")) {
            String isChecked = mAdapter.getData().get(position).get("isChecked").toString();
            if (isChecked == null) return;
            if (isChecked.equals("1")) {
                mAdapter.getData().get(position).put("isChecked", "2");
            } else {
                mAdapter.getData().get(position).put("isChecked", "1");
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
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
        Log.e("GuigeShuxing", "loadData:" + type);
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
                            map.put("isChecked", 2);
                            shuxingData.add(map);
                            if (jsonArrayData != null) {
                                jsonArrayData.add(name);
                            } else {
                                jsonArrayData = new JsonArray();
                                jsonArrayData.add(name);
                            }
                            SharedPreferences sp = getSharedPreferences("specs", MODE_MULTI_PROCESS);
                            Log.e("GuigeShuxing", jsonArrayData.toString());
                            sp.edit().putString(id, jsonArrayData.toString()).commit();
                            name = "";
                            mAdapter.notifyDataSetChanged();
                        } else {
                            String message = result.get("message").getAsString();
                            ToastUtil.show(message);
                            if (status == -7) {
//                                Intent intent = new Intent(SpecificationsGuanliActivity.this, LoginActity.class);
//                                startActivity(intent);
                            }
                        }
                    }
                    Log.e("信息", result.toString());
                    break;
                case 1002:

                    break;
                case 1003:

                    break;
                case 1004:

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

}
