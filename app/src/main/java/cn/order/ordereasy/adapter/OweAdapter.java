package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Trade;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.view.activity.allTradeChildAdapter;

public class OweAdapter extends BaseAdapter {
    public List<Trade> lists = new ArrayList<>();
    public Context context;

    public OweAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    public void setData(List<Trade> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final AllTradeViewHold holder;
        Trade my = lists.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.all_trade_item, null);
            holder = new AllTradeViewHold();
            holder.all_imageview = (ImageView) view.findViewById(R.id.all_imageview);
            holder.all_id = (TextView) view.findViewById(R.id.all_id);
            holder.all_sales = (TextView) view.findViewById(R.id.all_sales);
            holder.all_listview = (ListView) view.findViewById(R.id.all_listview);
            view.setTag(holder);
        } else {
            holder = (AllTradeViewHold) view.getTag();
        }
        if (my != null) {
            ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + my.getCover(), holder.all_imageview);
            holder.all_id.setText(my.getGoods_no() + " (" + my.getTitle() + ")");
            holder.all_sales.setText("欠货：" + my.getOwe_num());
            OweChildAdapter adapter = new OweChildAdapter(context);
            adapter.setData(my.getProduct_list());
            holder.all_listview.setAdapter(adapter);
            holder.all_listview.setVisibility(View.GONE);
            final int pos = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.all_listview.getVisibility() == View.GONE) {
                        holder.all_listview.setVisibility(View.VISIBLE);
                    } else {
                        holder.all_listview.setVisibility(View.GONE);
                    }
                }
            });
        }
        return view;
    }

    class AllTradeViewHold {
        ImageView all_imageview;
        TextView all_id;
        TextView all_sales;
        ListView all_listview;
    }
}