package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class OperationRecordActivity extends BaseActivity implements OrderEasyView {

    int id;
    OrderEasyPresenter orderEasyPresenter;
    List<Goods> datas = new ArrayList<>();
    OperateGoodsListAdapter adapter;
    int order_id = -1;
    private String delivery_name;
    private String delivery_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.operation_record);
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
    @InjectView(R.id.see_yuandan)
    TextView see_yuandan;
    @InjectView(R.id.type)
    TextView type;
    @InjectView(R.id.caozuo_state)//操作状态
    TextView caozuo_state;
    //操作人姓名
    @InjectView(R.id.caozuoren_name)
    TextView caozuoren_name;
    //本次退货
    @InjectView(R.id.benci_num)
    TextView benci_num;
    //货品总数
    @InjectView(R.id.gong_num)
    TextView gong_num;
    //客户姓名
    @InjectView(R.id.kehu_name)
    TextView kehu_name;
    //查看物流
    @InjectView(R.id.wuliu_button_onclick)
    TextView wuliu_button_onclick;
    @InjectView(R.id.jilu_name)
    TextView jilu_name;
    //操作类型
    @InjectView(R.id.caozuo_type)
    TextView caozuo_type;
    @InjectView(R.id.caozuo_jilu_listview)
    ListView caozuo_jilu_listview;


    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        OperationRecordActivity.this.finish();
    }

    //查看w物流
    @OnClick(R.id.wuliu_button_onclick)
    void wuliu_button_onclick() {
        Intent intent =new Intent( OperationRecordActivity.this,LogisticsMessageActivity.class);
        intent.putExtra("delivery_name",delivery_name);
        intent.putExtra("delivery_no",delivery_no);
        startActivity(intent);

    }

    @OnClick(R.id.see_yuandan)
    void see_yuandan() {
        if (order_id != -1) {
            Intent intent = new Intent(this, OrderNoDetailsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("id", order_id);
            intent.putExtras(bundle);
            startActivity(intent);
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
        Log.e("OperationRecord", data.toString());
        if (type == 0) {
            //获取商品列表
            JsonArray array = data.getAsJsonObject("result").get("goods_list").getAsJsonArray();
            List<Goods> goods = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                Goods good = (Goods) GsonUtils.getEntity(array.get(i).toString(), Goods.class);
                //获取信息
                int num = 0;
                for (Product product : good.getProduct_list()) {
                    num += product.getOperate_num();
                }
                good.setNum(num);
                goods.add(good);
            }
            datas = goods;
            data_time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(data.getAsJsonObject("result").get("create_time").getAsString()), "yyyy-MM-dd HH:mm:ss"));
            caozuoren_name.setText(data.getAsJsonObject("result").get("user_name").getAsString());
            kehu_name.setText(data.getAsJsonObject("result").get("customer_name").getAsString());
            int operate_type = data.getAsJsonObject("result").get("operate_type").getAsInt();
            delivery_name = data.getAsJsonObject("result").get("delivery_name").getAsString();
            delivery_no = data.getAsJsonObject("result").get("delivery_no").getAsString();
            see_yuandan.setVisibility(View.GONE);
            if (operate_type == Config.Operate_TYPE_DELIVER) {
                see_yuandan.setVisibility(View.VISIBLE);
                this.type.setText("本次总发货");
                benci_num.setText(data.getAsJsonObject("result").get("out_number").getAsString());
                caozuo_type.setText("发货");
                order_id = data.getAsJsonObject("result").get("order_id").getAsInt();
                caozuo_state.setText("已发货");
                jilu_name.setText("发货记录");
            } else if (operate_type == Config.Operate_TYPE_REDELIVER) {
                order_id = -1;
                this.type.setText("本次总退货");
                benci_num.setText(data.getAsJsonObject("result").get("in_number").getAsString());
                caozuo_type.setText("退货");
                caozuo_state.setText("已退货");
                jilu_name.setText("退货记录");
            }

            gong_num.setText(String.valueOf(goods.size()));
            adapter = new OperateGoodsListAdapter(this, operate_type);
            adapter.setData(goods);
            caozuo_jilu_listview.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
