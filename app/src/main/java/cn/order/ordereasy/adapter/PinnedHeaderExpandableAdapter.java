package cn.order.ordereasy.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.VideoBean;
import cn.order.ordereasy.widget.PinnedHeaderExpandableListView;


public class PinnedHeaderExpandableAdapter extends BaseExpandableListAdapter implements PinnedHeaderExpandableListView.HeaderAdapter {
    private List<VideoBean> datas = new ArrayList<>();
    private Context context;
    private PinnedHeaderExpandableListView listView;
    private LayoutInflater inflater;

    public PinnedHeaderExpandableAdapter(List<VideoBean> datas, Context context, PinnedHeaderExpandableListView listView) {
        this.datas = datas;
        this.context = context;
        this.listView = listView;
        inflater = LayoutInflater.from(this.context);
    }

    public List<VideoBean> getDatas() {
        return datas;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return datas.get(groupPosition).getContent().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createChildrenView();
        }
        TextView text = (TextView) view.findViewById(R.id.childto);
        text.setText(datas.get(groupPosition).getContent().get(childPosition).getSubtitle());
        return view;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return datas.get(groupPosition).getContent().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return datas.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return datas.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = createGroupView();
        }

        ImageView iv = (ImageView) view.findViewById(R.id.groupIcon);

        if (isExpanded) {
            iv.setImageResource(R.drawable.icon_sanjiao_down);
        } else {
            iv.setImageResource(R.drawable.icon_sanjiao_right);
        }

        TextView text = (TextView) view.findViewById(R.id.groupto);
        text.setText(datas.get(groupPosition).getTitle());
        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private View createChildrenView() {
        return inflater.inflate(R.layout.child, null);
    }

    private View createGroupView() {
        return inflater.inflate(R.layout.group, null);
    }

    @Override
    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = getChildrenCount(groupPosition);
        if (childPosition == childCount - 1) {
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !listView.isGroupExpanded(groupPosition)) {
            return PINNED_HEADER_GONE;
        } else {
            return PINNED_HEADER_VISIBLE;
        }
    }

    @Override
    public void configureHeader(View header, int groupPosition,
                                int childPosition, int alpha) {
        String groupData = datas.get(groupPosition).getTitle();
        ((TextView) header.findViewById(R.id.groupto)).setText(groupData);

    }

    @Override
    public void setGroupClickStatus(int groupPosition, int status) {
        datas.get(groupPosition).setIsSelcet(status);
    }

    @Override
    public int getGroupClickStatus(int groupPosition) {
        return datas.get(groupPosition).getIsSelcet();
    }
}
