package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;

public class StockStatisticsAdapter extends BaseAdapter {
    private List<Goods> datas;
    private Context context;

    public StockStatisticsAdapter(Context context, List<Goods> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        StockViewHold holder;
        Goods goods = datas.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.stock_staticstics_adapter_item, null);
            holder = new StockViewHold();
            holder.item_number = (TextView) convertView.findViewById(R.id.item_number);
            holder.item_owe = (TextView) convertView.findViewById(R.id.item_owe);
            holder.item_stock = (TextView) convertView.findViewById(R.id.item_stock);
            convertView.setTag(holder);
        } else {
            holder = (StockViewHold) convertView.getTag();
        }
        int Owe_num = 0;
        for (Product product : goods.getProduct_list()) {
            Owe_num += product.getOwe_num();
        }
        holder.item_number.setText(goods.getGoods_no() + "(" + goods.getTitle() + ")");
        holder.item_owe.setText(String.valueOf(Owe_num));
        holder.item_stock.setText(goods.getStore_num() + "");
        return convertView;
    }

    class StockViewHold {
        TextView item_number;
        TextView item_owe;
        TextView item_stock;
    }
}
