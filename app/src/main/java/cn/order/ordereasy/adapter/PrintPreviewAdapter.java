package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.PrintInfo;

public class PrintPreviewAdapter extends BaseAdapter {
    private List<PrintInfo> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public PrintPreviewAdapter(Context context, List<PrintInfo> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);

    }

    public List<PrintInfo> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
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
        PrintViewHold holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.print_preview_item,parent,
                    false);
            holder = new PrintViewHold();
            holder.serial_number = (TextView) convertView.findViewById(R.id.serial_number);
            holder.item_code = (TextView) convertView.findViewById(R.id.item_code);
            holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
            holder.specification_model = (TextView) convertView.findViewById(R.id.specification_model);
            holder.unit_price = (TextView) convertView.findViewById(R.id.unit_price);
            holder.item_money = (TextView) convertView.findViewById(R.id.item_money);
            convertView.setTag(holder);
        } else {
            holder = (PrintViewHold) convertView.getTag();
        }
        holder.serial_number.setText(position + "");
        holder.item_code.setText(list.get(position).getItemCode() + "");
        holder.goods_name.setText(list.get(position).getGoodsName());
        holder.specification_model.setText(list.get(position).getSpecificationModel());
        holder.unit_price.setText(list.get(position).getUnitPrice());
        holder.item_money.setText("¥"+list.get(position).getMoney());
        return convertView;
    }

    class PrintViewHold {
        TextView serial_number;
        TextView item_code;
        TextView goods_name;
        TextView specification_model;
        TextView unit_price;
        TextView item_money;
    }
}