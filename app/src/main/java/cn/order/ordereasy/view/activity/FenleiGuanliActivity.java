package cn.order.ordereasy.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
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
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.SwipeAdapterViewAdapter;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/5.
 * 分类管理activity
 */

public class FenleiGuanliActivity extends BaseActivity implements OrderEasyView, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, BGAOnItemChildClickListener, BGAOnItemChildLongClickListener {

    private SwipeAdapterViewAdapter mAdapter;
    private OrderEasyPresenter orderEasyPresenter;

    private String type = "";

    AlertDialog alertDialog;
    private List<TopicLabelObject> list;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fenlei_guanli);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String flag = bundle.getString("flag");
            type = flag;
        }
        this.orderEasyPresenter = new OrderEasyPresenterImp(this);
        fenlei_listview.setOnItemClickListener(this);
        fenlei_listview.setOnItemLongClickListener(this);
        fenlei_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
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

        mAdapter = new SwipeAdapterViewAdapter(this);
        mAdapter.setOnItemChildClickListener(this);
        //  mAdapter.setOnItemChildLongClickListener(this);
        fenlei_listview.setAdapter(mAdapter);
        fenlei_listview.setEmptyView(no_data_view);
        mContext = FenleiGuanliActivity.this;

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DataStorageUtils.getInstance().getGenreGoods().size() > 0) {
            list = DataStorageUtils.getInstance().getGenreGoods();
            if (list.get(0).getId() == -1) {
                list.remove(0);
            }
            List<Map<String, Object>> mapList = new ArrayList<>();
            for (TopicLabelObject labelObject : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("title", labelObject.getName());
                map.put("id", labelObject.getId());
                map.put("num", labelObject.getCount());
                mapList.add(map);
            }
            mAdapter.setData(mapList);
        } else {
            orderEasyPresenter.getCategoryInfo();
        }
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    @InjectView(R.id.fenlei_listview)
    ListView fenlei_listview;

    //添加分类
    @InjectView(R.id.add_fenlei)
    LinearLayout add_fenlei;

    @InjectView(R.id.no_data_view)
    ImageView no_data_view;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        FenleiGuanliActivity.this.finish();
    }

    //添加分类
    @OnClick(R.id.add_fenlei)
    void add_fenlei() {
        showdialogs();
    }

    private static final int REQUEST_CODE_SELECTTYPE_PREVIEW = 1001;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //showToast("点击了条目 " + mAdapter.getItem(position).get("title"));
        if (type.equals("shanghuo")) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            String name = mAdapter.getItem(position).get("title").toString();
            String idstr = mAdapter.getItem(position).get("id").toString();
            bundle.putString("type", name);
            bundle.putString("id", idstr);
            intent.putExtras(bundle);
            setResult(REQUEST_CODE_SELECTTYPE_PREVIEW, intent);
            finish();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        //showToast("长按了条目 " + mAdapter.getItem(position).get("title"));
        return true;
    }

    @Override
    public void onItemChildClick(ViewGroup parent, View childView, int position) {
        final int id = (int) mAdapter.getData().get(position).get("id");
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            if (position != 0) {
                orderEasyPresenter.delCategoryInfo(id);
                // 作为ListView的item使用时，如果删除了某一个item，请先关闭已经打开的item，否则其他item会显示不正常（RecyclerView不会有这个问题）
                mAdapter.closeOpenedSwipeItemLayout();
                mAdapter.removeItem(position);
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.show("未分类不能删除");
            }

        } else if (childView.getId() == R.id.fenlei_edit) {

            alertDialog = new AlertDialog.Builder(this).create();
            View view = View.inflate(this, R.layout.tanchuang_view, null);
            alertDialog.setView(view);

            alertDialog.show();
            Window window = alertDialog.getWindow();
            //window.setContentView(view);
            window.setContentView(R.layout.tanchuang_view);
            //标题
            TextView title_name = (TextView) window.findViewById(R.id.title_name);
            title_name.setText("请输入分类名称");
            //输入框
            final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
            String text = mAdapter.getData().get(position).get("title").toString();
            ed_type_name.setText(text);

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

            //给 输入空间 添加焦点监听
            ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            });
            //让 数据框 请求焦点
            ed_type_name.requestFocus();
            //按钮2确认点击事件
            final TextView queren = (TextView) window.findViewById(R.id.queren);
            queren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderEasyPresenter.updateCategoryInfo(id, ed_type_name.getText().toString());
                    alertDialog.dismiss();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //得到InputMethodManager的实例
                    if (imm.isActive()) {//如果开启
                        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);//关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
                    }
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


            CharSequence charSequence = text;
            if (charSequence instanceof Spannable) {
                Spannable spanText = (Spannable) charSequence;
                Selection.setSelection(spanText, text.length());
            }
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
        title_name.setText("请输入分类名称");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);
        //hint内容
        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        ed_type_name.setFocusable(true);


        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
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
                orderEasyPresenter.addCategoryInfo(ed_type_name.getText().toString());
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
                    queren.setClickable(true);
                } else {
                    queren.setTextColor(getResources().getColor(R.color.touzi_huise));
                    queren.setEnabled(false);
                    queren.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public boolean onItemChildLongClick(ViewGroup parent, View childView, int position) {
        if (childView.getId() == R.id.tv_item_swipe_delete) {
            showToast("长按了删除 " + mAdapter.getItem(position).get("title"));
            return true;
        }
        return false;
    }

    @Override
    public void showProgress(int type) {

        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        if (type != 1) {
            ToastUtil.show("网络连接失败");
        }
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
                    message.what = 1004;

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
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            List<Map<String, Object>> mapList = new ArrayList<>();
                            List<TopicLabelObject> mapList1 = new ArrayList<>();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                                TopicLabelObject topicLabelObject1 = new TopicLabelObject(jsonObject.get("category_id").getAsInt(), jsonObject.get("goods_num").getAsInt(), jsonObject.get("name").getAsString(), 0);
                                mapList1.add(topicLabelObject1);

                                Map<String, Object> map = new HashMap<>();
                                map.put("title", jsonObject.get("name").getAsString());
                                map.put("id", jsonObject.get("category_id").getAsInt());
                                map.put("num", jsonObject.get("goods_num").getAsString());
                                mapList.add(map);
                            }
                            DataStorageUtils.getInstance().setGenreGoods(mapList1);
                            mAdapter.setData(mapList);

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
                            orderEasyPresenter.getCategoryInfo();
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
                            orderEasyPresenter.getCategoryInfo();
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
                            orderEasyPresenter.getCategoryInfo();
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

    /**
     * EditText获取焦点并显示软键盘
     */
    public static void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
