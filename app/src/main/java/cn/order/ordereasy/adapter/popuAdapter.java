package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.TopicLabelObject;

public class popuAdapter extends BaseAdapter {
    private List<TopicLabelObject> list;
    public Context context;
    public MyItemClickListener listener;

    public popuAdapter(Context context, List<TopicLabelObject> list) {
        this.context = context;
        this.list = list;
        if(list != null) {
            if (!list.get(0).getName().equals("全部员工")) {
                TopicLabelObject topicLabelObject = new TopicLabelObject(-1, 0, "全部员工", 0);
                list.add(0, topicLabelObject);
            }
        }
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PopuViewHold holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.popu_window_item, null);
            holder = new PopuViewHold();
            holder.imageView = (ImageView) convertView.findViewById(R.id.popu_window_imageview);
            holder.textView = (TextView) convertView.findViewById(R.id.popu_window_text);
            convertView.setTag(holder);
        } else {
            holder = (PopuViewHold) convertView.getTag();
        }
        if (list.get(position).count == 0) {
            holder.imageView.setVisibility(View.VISIBLE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.lanse));
        } else {
            holder.imageView.setVisibility(View.GONE);
            holder.textView.setTextColor(context.getResources().getColor(R.color.heise));
        }
        holder.textView.setText(list.get(position).getName());
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    if (list.get(position).getCount() == 1) {
                        listener.selected(list.get(position), position);
                    }
                }
                selectItem(position);
            }
        });
        return convertView;
    }

    private void selectItem(int position) {
        for (int i = 0; i < list.size(); i++) {
            if (i == position) {
                list.get(i).setCount(0);
            } else {
                list.get(i).setCount(1);
            }
        }
    }

    class PopuViewHold {
        ImageView imageView;
        TextView textView;
    }

    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.listener = myItemClickListener;
    }

    public interface MyItemClickListener {
        void selected(TopicLabelObject labelObject, int position);
    }
}