package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.PastDailyAdapter;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * 往期日报Activity
 * <p>
 * Created by Administrator on 2017/9/3.
 */

public class PastDailyActivity extends BaseActivity {

    OrderEasyPresenter orderEasyPresenter;
    PastDailyAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.past_daily);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        adapter = new PastDailyAdapter(this);
        ribao_listview.setAdapter(adapter);
        List<String> data = TimeUtil.getDateList(90);
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        ribao_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String date = adapter.getData().get(position);
                Intent intent = new Intent(PastDailyActivity.this, DailyActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("date", date);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //listview
    @InjectView(R.id.ribao_listview)
    ListView ribao_listview;

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        PastDailyActivity.this.finish();
    }


}
