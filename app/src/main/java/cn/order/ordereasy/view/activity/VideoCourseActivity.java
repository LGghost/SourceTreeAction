package cn.order.ordereasy.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.PinnedHeaderExpandableAdapter;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.VideoBean;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.widget.PinnedHeaderExpandableListView;

public class VideoCourseActivity extends BaseActivity implements ExpandableListView.OnChildClickListener {
    //    private int expandFlag = -1;//控制列表的展开
    private PinnedHeaderExpandableAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.video_course_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        String foodJson = FileUtils.getJson(this, "dh5u_video_course.json");
        List<VideoBean> datas = new ArrayList<>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(foodJson);
            for (int i = 0; i < jsonArray.length(); i++) {
                VideoBean videoBean = (VideoBean) GsonUtils.getEntity(jsonArray.getJSONObject(i).toString(), VideoBean.class);
                datas.add(videoBean);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //设置悬浮头部VIEW
        explistview.setHeaderView(getLayoutInflater().inflate(R.layout.group_head,
                explistview, false));
        adapter = new PinnedHeaderExpandableAdapter(datas, getApplicationContext(), explistview);
        explistview.setAdapter(adapter);
        explistview.setOnChildClickListener(this);
        //设置单个分组展开
        //explistview.setOnGroupClickListener(new GroupClickListener());
    }

    @InjectView(R.id.explistview)
    PinnedHeaderExpandableListView explistview;

    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        String url = adapter.getDatas().get(groupPosition).getContent().get(childPosition).getUrl();
        Log.e("VideoCourseActivity", "url：" + url);
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(it);
        return true;
    }

//    class GroupClickListener implements ExpandableListView.OnGroupClickListener {
//        @Override
//        public boolean onGroupClick(ExpandableListView parent, View v,
//                                    int groupPosition, long id) {
//            if (expandFlag == -1) {
//                // 展开被选的group
//                explistview.expandGroup(groupPosition);
//                // 设置被选中的group置于顶端
//                explistview.setSelectedGroup(groupPosition);
//                expandFlag = groupPosition;
//            } else if (expandFlag == groupPosition) {
//                explistview.collapseGroup(expandFlag);
//                expandFlag = -1;
//            } else {
//                explistview.collapseGroup(expandFlag);
//                // 展开被选的group
//                explistview.expandGroup(groupPosition);
//                // 设置被选中的group置于顶端
//                explistview.setSelectedGroup(groupPosition);
//                expandFlag = groupPosition;
//            }
//            return true;
//        }
//    }

}
