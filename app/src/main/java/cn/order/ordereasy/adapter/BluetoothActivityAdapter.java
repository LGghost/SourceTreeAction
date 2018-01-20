package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.order.ordereasy.R;

public class BluetoothActivityAdapter extends BaseAdapter {

    private List<HashMap<String,String>> list = new ArrayList<>();
    private Context context;
    private LayoutInflater inflater;

    public BluetoothActivityAdapter(Context context, List<HashMap<String,String>> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public HashMap<String,String> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BluetoothViewHold holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.bluetooth_adapter_item, parent,
                    false);
            holder = new BluetoothViewHold();
            holder.name = (TextView) convertView.findViewById(R.id.item_name);
            holder.address = (TextView) convertView.findViewById(R.id.item_address);
            convertView.setTag(holder);
        } else {
            holder = (BluetoothViewHold) convertView.getTag();
        }
        holder.name.setText(list.get(position).get("name"));
        holder.address.setText(list.get(position).get("address"));
        return convertView;
    }

    class BluetoothViewHold {
        TextView name;
        TextView address;
    }
}