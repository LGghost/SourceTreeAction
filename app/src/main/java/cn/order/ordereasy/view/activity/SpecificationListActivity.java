package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
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
import cn.order.ordereasy.adapter.SpecSelectViewAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Spec;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.NormalRefreshViewHolder;

/**
 * Created by Administrator on 2017/9/9.
 * <p>
 * 选择规格管理列表
 */

public class SpecificationListActivity extends BaseActivity implements OrderEasyView, AdapterView.OnItemClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener, SwipeRefreshLayout.OnRefreshListener {

    AlertDialog alertDialog;
    private OrderEasyPresenter orderEasyPresenter;
    private SpecSelectViewAdapter mAdapter;
    private int flag = 1;
    private String type = "";
    private boolean isFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.specification_list);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String flag = bundle.getString("flag");
            type = flag;
        }
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        //下拉刷新
        specification_list_refresh.setOnRefreshListener(this);
        guige_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
                    mAdapter.closeOpenedSwipeItemLayoutWithAnim();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
        guige_listview.setOnItemClickListener(this);
        mAdapter = new SpecSelectViewAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        mAdapter.setOnItemChildLongClickListener(this);
        guige_listview.setAdapter(mAdapter);
        if (DataStorageUtils.getInstance().getGuigeLists().size() > 0) {
            for (Map<String, Object> map : DataStorageUtils.getInstance().getGuigeLists()) {
                map.put("isChecked", "2");
            }
            mAdapter.setData(DataStorageUtils.getInstance().getGuigeLists());
        } else {
            orderEasyPresenter.getSpecInfo();
        }
    }

    //下拉刷新
    @InjectView(R.id.specification_list_refresh)
    SwipeRefreshLayout specification_list_refresh;

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //编辑
    @InjectView(R.id.bianji)
    TextView bianji;

    //完成
    @InjectView(R.id.wancheng)
    TextView wancheng;

    //规格
    @InjectView(R.id.guige_listview)
    ListView guige_listview;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        SpecificationListActivity.this.finish();
    }

    //编辑
    @OnClick(R.id.bianji)
    void bianji() {
        if (flag == 1) {
            bianji.setText("选择");
            flag = 2;
            for (Map<String, Object> map : mAdapter.getData()) {
                map.put("isChecked", "0");
            }

        } else {
            bianji.setText("编辑");
            for (Map<String, Object> map : mAdapter.getData()) {
                map.put("isChecked", "2");
            }
            flag = 1;

        }
        guige_listview.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    //完成
    @OnClick(R.id.wancheng)
    void wancheng() {
        List<Spec> data = new ArrayList<>();
        int checkedCount = 0;
        for (Map<String, Object> map : mAdapter.getData()) {
            Spec spec = new Spec();
            if (map.get("isChecked").equals("1")) {
                checkedCount++;
                spec.setName(map.get("title").toString());
                spec.setSpec_id(Integer.parseInt(map.get("id").toString()));
                data.add(spec);
            }

        }
        if (checkedCount > 2) {
            showToast("只能选择两种规格！");
            return;
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Goods good = new Goods();
        good.setSpec(data);

        bundle.putSerializable("data", good);
        intent.putExtras(bundle);
        setResult(1001, intent);
        finish();
    }

    //添加新规格
    @OnClick(R.id.button_click)
    void button_click() {
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
        title_name.setText("请输入规格名称");
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
//        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ed_type_name.getText().toString();
                if (name.length() > 12) {
                    ToastUtil.show("规格名称最多输入12个字符");
                    alertDialog.dismiss();
                    return;
                }
                ProgressUtil.showDialog(SpecificationListActivity.this);
                orderEasyPresenter.addSpecCategoryInfo(ed_type_name.getText().toString());
                alertDialog.dismiss();
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
    }


    @Override
    public void showProgress(int type) {
        if (isFirst) {
            isFirst = false;
            ProgressUtil.showDialog(this);
        }
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
        specification_list_refresh.setRefreshing(false);
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {
        specification_list_refresh.setRefreshing(false);
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
                            bianji.setText("编辑");
                            flag = 1;
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            SharedPreferences sp = getSharedPreferences("specs", MODE_MULTI_PROCESS);
                            SharedPreferences.Editor edit = sp.edit();
                            List<Map<String, Object>> mapList = new ArrayList<>();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                Map<String, Object> map = new HashMap<>();
                                map.put("title", jsonObject.get("name").getAsString());
                                map.put("isChecked", "2");
                                map.put("id", jsonObject.get("spec_id").getAsInt());
                                map.put("values", jsonObject.get("values").getAsJsonArray().toString());
                                edit.putString(jsonObject.get("spec_id").getAsString(), jsonObject.get("values").getAsJsonArray().toString());
                                mapList.add(map);
                            }
                            edit.commit();
                            DataStorageUtils.getInstance().setGuigeLists(mapList);
                            mAdapter.setData(mapList);
                            if (mapList.size() > 0) {
                                no_data_view.setVisibility(View.GONE);
                            } else {
                                no_data_view.setVisibility(View.VISIBLE);
                            }

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(SpecificationListActivity.this, LoginActity.class);
                                startActivity(intent);
                            }
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
                            showToast("新增成功！");
                            orderEasyPresenter.getSpecInfo();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("新增信息", result.toString());
                    break;
                case 1003:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        //String message=result.get("message").getAsString();
                        if (status == 1) {
                            showToast("修改成功！");
                            orderEasyPresenter.getSpecInfo();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
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
                            showToast("删除成功！");
                            orderEasyPresenter.getSpecInfo();
                        } else {
                            String message = result.get("message").getAsString();
                            showToast(message);
                        }
                    }
                    Log.e("保存信息", result.toString());
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
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        final int id = (int) mAdapter.getData().get(position).get("id");
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            ProgressUtil.showDialog(SpecificationListActivity.this);
            orderEasyPresenter.delSpecInfo(id);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (flag == 2) {
            String spec_id = mAdapter.getItem(position).get("id").toString();
            Intent intent = new Intent(this, GuigeShuxingListActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("name", mAdapter.getItem(position).get("title").toString());
            bundle.putString("id", spec_id);
            intent.putExtras(bundle);
            startActivity(intent);
        } else {
            String isChecked = mAdapter.getData().get(position).get("isChecked").toString();
            if (isChecked.equals("1")) {
                mAdapter.getData().get(position).put("isChecked", "2");
            } else if (isChecked.equals("2")) {
                int checkedCount = 0;
                for (Map<String, Object> map : mAdapter.getData()) {
                    if (map.get("isChecked").equals("1")) {
                        checkedCount++;
                    }
                }
                Log.e("SpecificationList", checkedCount + "");
                if (checkedCount > 1) {
                    showToast("只能选择两种规格！");
                    return;
                }
                mAdapter.getData().get(position).put("isChecked", "1");
            } else {
                showToast("规格选择异常，请退出重新选择");
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onRefresh() {
        orderEasyPresenter.getSpecInfo();
    }
}
