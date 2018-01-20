package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.utils.Config;

public class StockWarningAdapter extends BaseAdapter {
    private List<Goods> list;
    private Context context;

    public StockWarningAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<Goods> lists) {
        this.list = lists;
        notifyDataSetChanged();
    }

    public List<Goods> getData() {
        return list;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        WarningViewHold holder;
        final Goods good = list.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.stock_warning_adapter_item, null);
            holder = new WarningViewHold();
            holder.good_name = (TextView) convertView.findViewById(R.id.good_name);
            holder.good_no = (TextView) convertView.findViewById(R.id.good_no);
            holder.good_kucun = (TextView) convertView.findViewById(R.id.good_kucun);
            holder.good_xiaoliang = (TextView) convertView.findViewById(R.id.good_xiaoliang);
            holder.stock_security = (TextView) convertView.findViewById(R.id.stock_security);
            holder.stock_state = (TextView) convertView.findViewById(R.id.stock_state);
            holder.good_img = (ImageView) convertView.findViewById(R.id.good_img);
            convertView.setTag(holder);
        } else {
            holder = (WarningViewHold) convertView.getTag();
        }
        holder.good_name.setText(good.getTitle() + "");
        holder.good_no.setText(good.getGoods_no() + "");
        holder.good_kucun.setText(good.getStore_num() + "");
        holder.good_xiaoliang.setText(good.getSale_num() + "");
        holder.stock_security.setText("安全库存：" + good.getMin_stock_warn_num() + "~" + good.getMax_stock_warn_num());
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), holder.good_img);

        if (good.getStore_num() > good.getMax_stock_warn_num()) {
            holder.stock_state.setText("库存过高");
            holder.stock_state.setTextColor(context.getResources().getColor(R.color.shouye_lanse));
            holder.stock_security.setTextColor(context.getResources().getColor(R.color.shouye_lanse));
        }
        if (good.getStore_num() < good.getMin_stock_warn_num()) {
            holder.stock_state.setText("库存过低");
            holder.stock_state.setTextColor(context.getResources().getColor(R.color.shouye_hongse));
            holder.stock_security.setTextColor(context.getResources().getColor(R.color.shouye_hongse));
        }
        if (good.getStore_num() > good.getMin_stock_warn_num() && good.getStore_num() < good.getMax_stock_warn_num()) {
            holder.stock_state.setText("库存合理");
            holder.stock_state.setTextColor(context.getResources().getColor(R.color.touzi_huise));
            holder.stock_security.setTextColor(context.getResources().getColor(R.color.touzi_huise));
        }
        return convertView;
    }

    class WarningViewHold {
        TextView good_name;
        TextView good_no;
        TextView good_kucun;
        TextView good_xiaoliang;
        TextView stock_security;
        TextView stock_state;
        ImageView good_img;
    }
}