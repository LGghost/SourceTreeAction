package cn.order.ordereasy.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.ArrearsBean;
import cn.order.ordereasy.bean.Mylist1;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.view.activity.CustomerHomepageActivity;
import cn.order.ordereasy.view.activity.OrderNoDetailsActivity;

public class ArrearsAdapter extends BaseAdapter {
    public List<ArrearsBean> lists;
    public Context context;

    public ArrearsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    public void setData(List<ArrearsBean> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public List<ArrearsBean> getData() {
        return lists;
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
        ArrearsViewHold holder;
        final ArrearsBean my = lists.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.arrears_adapter_item, null);
            holder = new ArrearsViewHold();
            holder.type_image = (ImageView) view.findViewById(R.id.type_image);
            holder.time = (TextView) view.findViewById(R.id.item_time);
            holder.arrear_number = (TextView) view.findViewById(R.id.item_arrear_number);
            holder.receivable_number = (TextView) view.findViewById(R.id.item_receivable_number);
            holder.shoukuan_number = (TextView) view.findViewById(R.id.item_shoukuan_number);
            holder.type_text = (TextView) view.findViewById(R.id.type_text);
            holder.tiaozheng_text = (TextView) view.findViewById(R.id.tiaozheng_text);
            holder.beizhu_text = (TextView) view.findViewById(R.id.beizhu_text);
            holder.tiaozheng_layout = (LinearLayout) view.findViewById(R.id.tiaozheng_layout);
            holder.dindan_layout = (LinearLayout) view.findViewById(R.id.dindan_layout);
            view.setTag(holder);
        } else {
            holder = (ArrearsViewHold) view.getTag();
        }
        Log.e("JJF", "my.getCreate_time():" + my.getCreate_time());
        String time = TimeUtil.getTimeStamp2Str(Long.parseLong(my.getCreate_time()), "yyyy-MM-dd HH:mm:ss");
        holder.time.setText(time);
        holder.arrear_number.setText("客户累计欠款：¥ " + my.getTotal_debt());
        double payableSum = 0, receivableSum = 0;
        String typeStr = "";
        int typeImage = R.drawable.img_dingdan;
        if (my.getIs_adjustment() == 1) {
            holder.dindan_layout.setVisibility(View.GONE);
            holder.tiaozheng_layout.setVisibility(View.VISIBLE);
            holder.tiaozheng_text.setText("老板将欠款调整为：¥" + my.getTotal_debt());
            holder.beizhu_text.setText("备注：" + my.getRemark());
        } else {
            holder.dindan_layout.setVisibility(View.VISIBLE);
            holder.tiaozheng_layout.setVisibility(View.GONE);
        }
        switch (my.getType()) {
            case 1:
                payableSum = my.getMoney();
                typeStr = "下单";
                typeImage = R.drawable.img_dingdan;
                break;
            case 2:
                payableSum = -my.getMoney();
                typeStr = "退货";
                typeImage = R.drawable.img_tuidan_sign;
                break;
            case 3:
                receivableSum = my.getMoney();
                typeStr = "收款";
                typeImage = R.drawable.img_refund;
                break;
            case 4:
                receivableSum = -my.getMoney();
                typeStr = "退款";
                typeImage = R.drawable.img_refund;
                break;
            default:
                break;
        }

        holder.receivable_number.setText("¥ " + payableSum);
        holder.shoukuan_number.setText("¥ " + receivableSum);
        holder.type_text.setText(typeStr);
        holder.type_image.setImageResource(typeImage);
        return view;
    }

    class ArrearsViewHold {
        TextView time;
        TextView arrear_number;
        TextView receivable_number;
        TextView shoukuan_number;
        ImageView type_image;
        TextView type_text;
        TextView beizhu_text;
        TextView tiaozheng_text;
        LinearLayout dindan_layout;
        LinearLayout tiaozheng_layout;
    }
}