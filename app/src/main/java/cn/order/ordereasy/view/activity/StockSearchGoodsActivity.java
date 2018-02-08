package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OrderSelectGoodsAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/17.
 * <p>
 * 库存搜索货品
 */

public class StockSearchGoodsActivity extends BaseActivity implements OrderEasyView {

    OrderEasyPresenter orderEasyPresenter;
    OrderSelectGoodsAdapter orderSelectGoodsAdapter;
    List<Goods> datas = new ArrayList<>();
    List<Goods> selectedDatas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.stock_search_goods);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        orderSelectGoodsAdapter = new OrderSelectGoodsAdapter(this,selectedDatas);
        sousuo_listview.setAdapter(orderSelectGoodsAdapter);
        sousuo_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goods good = orderSelectGoodsAdapter.getData().get(position);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", good);
                intent.putExtras(bundle);
                setResult(1002, intent);
                finish();
            }
        });
        initData();
    }

    private void initData() {
        if (DataStorageUtils.getInstance().getShelvesGoods().size() > 0) {
            datas = DataStorageUtils.getInstance().getShelvesGoods();
        } else {
            ProgressUtil.showDialog(this);
            orderEasyPresenter.getGoodsList();
        }
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    List<Goods> list = Goods.likeString2(datas, s.toString());
                    if (list.size() > 0) {
                        orderSelectGoodsAdapter.setData(list);
                        orderSelectGoodsAdapter.notifyDataSetChanged();
                    } else {
                        orderSelectGoodsAdapter.setData(new ArrayList<Goods>());
                        orderSelectGoodsAdapter.notifyDataSetChanged();
                    }

                } else {
                    orderSelectGoodsAdapter.setData(new ArrayList<Goods>());
                    orderSelectGoodsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //扫一扫
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;
    //ListView
    @InjectView(R.id.sousuo_listview)
    ListView sousuo_listview;
    @InjectView(R.id.et_search)
    EditText et_search;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        StockSearchGoodsActivity.this.finish();
    }


    //扫一扫点击事件
    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        }else{
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {
        if (type == 2) {
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

                        } else {
                            if (status == -7) {
                                ToastUtil.show(getString(R.string.landfall_overdue));
                                Intent intent = new Intent(StockSearchGoodsActivity.this, LoginActity.class);
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
                            JsonArray jsonArray = result.get("result").getAsJsonArray();
                            for (int i = 0; i < jsonArray.size(); i++) {
                                Goods good = (Goods) GsonUtils.getEntity(jsonArray.get(i).toString(), Goods.class);
                                datas.add(good);
                            }
                            DataStorageUtils.getInstance().setShelvesGoods(datas);
                        }
                    }
                    Log.e("商品信息", result.toString());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫一扫结果
        if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            if (TextUtils.isEmpty(res)) {
                showToast("未扫描到任何结果");
            } else {
                for (Goods good : datas) {
                    if (String.valueOf(good.getGoods_no()).equals(res)) {
                        Log.e("onActivityResult","Goods_no:"+good.getGoods_no());
                        Log.e("onActivityResult","res:"+res);
                        if (good.getStatus() == 1) {
                            Intent intent = new Intent();
                            Bundle bundle1 = new Bundle();
                            bundle1.putSerializable("data", good);
                            intent.putExtras(bundle1);
                            setResult(1002, intent);
                            finish();
                        } else {
                            showToast("已下架货品不能操作");
                        }
                    }
                }

            }
            MyLog.e("扫一扫返回数据", res);
        }
    }
}
