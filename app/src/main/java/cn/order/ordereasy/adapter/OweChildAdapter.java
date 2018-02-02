package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.TradeChild;
import cn.order.ordereasy.utils.FileUtils;

public class OweChildAdapter extends BaseAdapter {
    public List<TradeChild> lists1;
    public Context context;

    public OweChildAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists1 == null ? 0 : lists1.size();
    }

    public void setData(List<TradeChild> lists1) {
        this.lists1 = lists1;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return lists1.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ChildViewHold holder;
        TradeChild my = lists1.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.all_trade_child_item, null);
            holder = new ChildViewHold();
            holder.child_spec = (TextView) view.findViewById(R.id.child_spec);
            holder.child_sum = (TextView) view.findViewById(R.id.child_sum);
            holder.title_name = (TextView) view.findViewById(R.id.title_name);
            holder.top_layout = (LinearLayout) view.findViewById(R.id.top_layout);
            view.setTag(holder);
        } else {
            holder = (ChildViewHold) view.getTag();
        }
        holder.title_name.setText("欠货");
        if(i == 0){
            holder.top_layout.setVisibility(View.VISIBLE);
        }else{
            holder.top_layout.setVisibility(View.GONE);
        }
        if (my.getSpec_data().size() > 1) {
            holder.child_spec.setText( my.getSpec_data().get(0) + "/" +  my.getSpec_data().get(1));
        } else {
            holder.child_spec.setText( my.getSpec_data().get(0));
        }
        holder.child_sum.setText("欠 " + my.getOwe_num() + "  (库存 " + my.getStore_num() + ")");
        return view;
    }

    class ChildViewHold {
        LinearLayout top_layout;
        TextView child_spec;
        TextView child_sum;
        TextView title_name;
    }
}
