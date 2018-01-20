package cn.order.ordereasy.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.TradeCustomer;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.view.activity.AllTradeActivity;
import cn.order.ordereasy.view.activity.CustomerHomepageActivity;


public class TradeCustomerAdapter extends BaseAdapter {
    public List<TradeCustomer> lists1;
    public Context context;
    public int sign;

    public TradeCustomerAdapter(Context context, int sign) {
        this.context = context;
        this.sign = sign;
    }

    @Override
    public int getCount() {
        return lists1 == null ? 0 : lists1.size();
    }

    public void setData(List<TradeCustomer> lists1) {
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
        TradeCustomer my = lists1.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.trade_customer_item, null);
            holder = new BookingViewHold();
            holder.layout = (RelativeLayout) view.findViewById(R.id.right_layout);
            holder.iv1 = (TextView) view.findViewById(R.id.iv1);
            holder.tv3 = (TextView) view.findViewById(R.id.tv3);
            holder.tv4 = (TextView) view.findViewById(R.id.tv4);
            holder.tv5 = (TextView) view.findViewById(R.id.tv5);
            view.setTag(holder);
        } else {
            holder = (BookingViewHold) view.getTag();
        }
        holder.iv1.setText(my.getCustomer_name().subSequence(0, 1));
        holder.tv3.setText(my.getCustomer_name());
        if (sign == 1) {
            holder.tv4.setText("交易额：¥" + my.getTrade_sum());
            holder.tv5.setText("销售量：" + my.getSale_num());
        } else {
            holder.tv4.setText("欠款：¥" + my.getTrade_sum());
            holder.tv5.setText("欠货：" + my.getSale_num());

        }

        final int pos = i;
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sign == 1) {
                    Intent intent = new Intent();
                    intent.setClass(context, AllTradeActivity.class);
                    intent.putExtra("customer_name", lists1.get(pos).getCustomer_name());
                    intent.putExtra("customer_id", lists1.get(pos).getCustomer_id());
                    context.startActivity(intent);
                } else {
                    Customer customer1 = new Customer();
                    List<Customer> customerList = DataStorageUtils.getInstance().getCustomerLists();
                    for (Customer customer : customerList) {
                        if (lists1.get(pos).getCustomer_id() == customer.getCustomer_id()) {
                            customer1 = customer;
                        }
                    }
                    Intent intent = new Intent(context, CustomerHomepageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("data", customer1);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            }
        });
        return view;
    }

    class BookingViewHold {
        RelativeLayout layout;
        TextView iv1;
        TextView tv3;
        TextView tv4;
        TextView tv5;
    }
}
