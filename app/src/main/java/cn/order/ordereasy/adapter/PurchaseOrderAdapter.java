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
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.utils.TimeUtil;

public class PurchaseOrderAdapter extends BaseAdapter {
    public List<OrderList> lists1;
    public Context context;

    public PurchaseOrderAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists1 == null ? 0 : lists1.size();
    }

    public void setData(List<OrderList> lists1) {
        this.lists1 = lists1;
        notifyDataSetChanged();
    }

    public List<OrderList> getData() {
        return lists1;
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
        PurchaseOrderViewHold holder;
        OrderList my = lists1.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.purchase_order_adapter_item, null);
            holder = new PurchaseOrderViewHold();
            holder.order_code = (TextView) view.findViewById(R.id.order_code);
            holder.type_image = (ImageView) view.findViewById(R.id.type_image);
            holder.kehu_name = (TextView) view.findViewById(R.id.kehu_name);
            holder.data_time = (TextView) view.findViewById(R.id.data_time);
            holder.ruku_num = (TextView) view.findViewById(R.id.ruku_num);
            holder.order_amount = (TextView) view.findViewById(R.id.order_amount);
            holder.order_money = (TextView) view.findViewById(R.id.order_money);
            holder.kaidan_num = (TextView) view.findViewById(R.id.kaidan_num);

            view.setTag(holder);
        } else {
            holder = (PurchaseOrderViewHold) view.getTag();
        }
        holder.order_code.setText(my.getOrder_no());
        holder.kehu_name.setText(my.getSupplier_name());
        holder.data_time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(my.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
        holder.ruku_num.setText(my.getOwe_num() + "");
        holder.order_money.setText(my.getPayable() + "");
        holder.kaidan_num.setText(my.getOrder_num() + "");
        holder.order_amount.setTextColor(context.getResources().getColor(R.color.heise));
        holder.order_amount.setText("订单金额：¥:");
        //更换右上角图标
        switch (my.getOrder_type()) {
            case 1:
                holder.type_image.setImageResource(R.drawable.img_dingdan);
                if (my.getOwe_num() <= 0) {
                    holder.ruku_num.setTextColor(context.getResources().getColor(R.color.touzi_huise));
                    holder.ruku_num.setText("已完成");
                } else {
                    holder.ruku_num.setTextColor(context.getResources().getColor(R.color.shouye_hongse));
                    holder.ruku_num.setText("未入库数：" + String.valueOf(my.getOwe_num()));
                }

                break;
            case 2:
            case 3:
                holder.type_image.setImageResource(R.drawable.img_tuidan_sign);
                holder.ruku_num.setVisibility(View.GONE);
                holder.order_amount.setTextColor(context.getResources().getColor(R.color.touzi_huise));
                holder.order_money.setTextColor(context.getResources().getColor(R.color.touzi_huise));
                holder.order_amount.setText("本次应退：¥:");
                break;
        }
        return view;
    }

    class PurchaseOrderViewHold {
        TextView order_code;
        ImageView type_image;
        TextView kehu_name;
        TextView data_time;
        TextView ruku_num;
        TextView order_amount;
        TextView order_money;
        TextView kaidan_num;

    }
}