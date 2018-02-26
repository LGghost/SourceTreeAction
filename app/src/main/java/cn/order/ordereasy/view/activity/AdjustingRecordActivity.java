package cn.order.ordereasy.view.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.OperateGoodsListAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.view.OrderEasyView;


/**
 * Created by Administrator on 2017/9/21.
 * 操作记录
 */

public class AdjustingRecordActivity extends BaseActivity implements OrderEasyView {

    int id;
    OrderEasyPresenter orderEasyPresenter;
    List<Goods> datas = new ArrayList<>();
    OperateGoodsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.adjusting_record_activity_layout);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getInt("id");
            orderEasyPresenter.operateRecordDetail(id);
        }

    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //日期时间
    @InjectView(R.id.data_time)
    TextView data_time;
    //操作人姓名
    @InjectView(R.id.caozuoren_name)
    TextView caozuoren_name;
    //本次调整
    @InjectView(R.id.benci_num)
    TextView benci_num;
    //货品总数
    @InjectView(R.id.gong_num)
    TextView gong_num;

    @InjectView(R.id.type)
    TextView typeText;
    //操作类型
    @InjectView(R.id.caozuo_type)
    TextView caozuo_type;
    @InjectView(R.id.caozuo_jilu_listview)
    ListView caozuo_jilu_listview;
    @InjectView(R.id.jilu_name)
    TextView jilu_name;
    @InjectView(R.id.caozuo_state)
    TextView caozuo_state;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("OperationRecord", data.toString());
        if (type == 0) {
            //获取商品列表
            JsonArray array = data.getAsJsonObject("result").get("goods_list").getAsJsonArray();
            List<Goods> goods = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                Goods good = (Goods) GsonUtils.getEntity(array.get(i).toString(), Goods.class);
                //获取信息
                int num = 0;
                int num1 = 0;
                for (Product product : good.getProduct_list()) {
                    int number = product.getOperate_num();
                    if (number > 0) {
                        num += number;
                    } else {
                        num1 -= number;
                    }
                }
                good.setStore_num(num1);
                good.setNum(num);
                goods.add(good);
            }
            datas = goods;
            data_time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(data.getAsJsonObject("result").get("create_time").getAsString()), "yyyy-MM-dd HH:mm:ss"));
            caozuoren_name.setText(data.getAsJsonObject("result").get("user_name").getAsString());
            int operate_type = data.getAsJsonObject("result").get("operate_type").getAsInt();
            switch (operate_type) {
                case 1:
                    typeText.setText("本次总入库");
                    benci_num.setText(data.getAsJsonObject("result").get("in_number").getAsString());
                    jilu_name.setText("入库记录");
                    caozuo_state.setText("已入库");
                    break;
                case 2:
                    typeText.setText("本次总出库");
                    benci_num.setText(data.getAsJsonObject("result").get("out_number").getAsString());
                    jilu_name.setText("出库记录");
                    caozuo_state.setText("已出库");
                    break;
                case 3:
                case 6:
                    typeText.setText("本次总调整");
                    if (data.getAsJsonObject("result").get("in_number").getAsInt() == 0) {
                        benci_num.setText("-" + data.getAsJsonObject("result").get("out_number").getAsString());
                    } else if (data.getAsJsonObject("result").get("out_number").getAsInt() == 0) {
                        benci_num.setText("+" + data.getAsJsonObject("result").get("in_number").getAsString());
                    } else {
                        benci_num.setText("+" + data.getAsJsonObject("result").get("in_number").getAsString() + "(-" + data.getAsJsonObject("result").get("out_number").getAsString() + ")");
                    }
                    jilu_name.setText("调整记录");
                    caozuo_state.setText("已调整");
                    break;
                case 7:
                    typeText.setText("本次采购入库");
                    benci_num.setText(data.getAsJsonObject("result").get("out_number").getAsString());
                    jilu_name.setText("入库记录");
                    caozuo_state.setText("已入库");
                    break;
                case 8:
                    typeText.setText("本次采购退货");
                    benci_num.setText(data.getAsJsonObject("result").get("in_number").getAsString());
                    jilu_name.setText("采购退货记录");
                    caozuo_state.setText("已退货");
                    break;
                default:
                    typeText.setText("本次操作");
                    benci_num.setText(data.getAsJsonObject("result").get("out_number").getAsString());
                    break;
            }
            caozuo_type.setText(data.getAsJsonObject("result").get("remark").getAsString());
            gong_num.setText(String.valueOf(goods.size()));
            adapter = new OperateGoodsListAdapter(this, operate_type);
            adapter.setData(goods);
            caozuo_jilu_listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
