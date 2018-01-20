package cn.order.ordereasy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Mylist2;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.view.fragment.gooddetailsfragment.DetailsGoodsActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 17-9-29.
 */

public class BookingAdapter2 extends BaseAdapter {
    public List<Mylist2> lists1;
    public Context context;

    public BookingAdapter2(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists1 == null ? 0 : lists1.size();
    }

    public void setData(List<Mylist2> lists1) {
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
        BookingViewHold holder;
        Mylist2 my = lists1.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.booking_adapter, null);
            holder = new BookingViewHold();
            holder.iv1 = (ImageView) view.findViewById(R.id.iv1);
            holder.tv3 = (TextView) view.findViewById(R.id.tv3);
            holder.tv4 = (TextView) view.findViewById(R.id.tv4);
            holder.tv5 = (TextView) view.findViewById(R.id.tv5);
            view.setTag(holder);
        } else {
            holder = (BookingViewHold) view.getTag();
        }
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + my.cover, holder.iv1);
//        tv1.setText("商品名称："+my.title);
//        tv2.setText("商品编号ID："+my.goods_id);
        holder.tv3.setText(my.goods_no + " (" + my.title + ")");
        holder.tv4.setText("交易额：¥" + my.trade_sum);
        holder.tv5.setText("销售量：" + my.sale_num);

        final int pos = i;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, DetailsGoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isSales", false);
                bundle.putInt("goodId", lists1.get(pos).goods_id);
                intent.putExtras(bundle);
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        return view;
    }

    class BookingViewHold {
        ImageView iv1;
        TextView tv3;
        TextView tv4;
        TextView tv5;
    }
}
