package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SupplierBean;

public class ReconciliationAdapter extends BaseAdapter {
    public List<SupplierBean> lists1;
    public Context context;

    public ReconciliationAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists1 == null ? 0 : lists1.size();
    }

    public void setData(List<SupplierBean> lists1) {
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
        ReconciliationViewHold holder;
        SupplierBean my = lists1.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.reconciliation_adapter_item, null);
            holder = new ReconciliationViewHold();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.money = (TextView) view.findViewById(R.id.money);
            view.setTag(holder);
        } else {
            holder = (ReconciliationViewHold) view.getTag();
        }
        holder.name.setText(my.getName());
        holder.money.setText("¥" + my.getDebt());
        return view;
    }

    class ReconciliationViewHold {
        TextView name;
        TextView money;
    }
}