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
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.FileUtils;

/**
 * Created by Administrator on 2017/9/12.
 */

public class ConfirmBillingGoodsItemAdapter extends BaseAdapter {

    Context context;
    private List<Product> data = null;
    private int type;

    public ConfirmBillingGoodsItemAdapter(Context context, List<Product> data, int type) {
        this.context = context;
        this.data = data;
        this.type = type;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.confirm_billing_goods_list_item_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.shuliang);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product data = this.data.get(position);
        String price;
        if (type == 0) {
            if (data.getDefault_price() != -1) {
                price = "(¥" + data.getDefault_price() + ")";
            } else {
                price = "(¥" + data.getSell_price() + ")";
            }
        }else{
            price = "(¥" + data.getCost_price() + ")";
        }
        if (data.getSpec_data().size() > 0) {
            if (data.getSpec_data().size() == 1) {

                viewHolder.name.setText(data.getSpec_data().get(0) + price);
            } else if (data.getSpec_data().size() == 2) {
                viewHolder.name.setText(data.getSpec_data().get(0) + "/" + data.getSpec_data().get(1) + price);
            }
        } else {
            viewHolder.name.setText("无");
        }
        viewHolder.shuliang.setText("x" + data.getNum());

        return convertView;
    }

    class ViewHolder {
        public TextView name;
        public TextView jiage;
        public TextView shuliang;
        public ImageView jia;
        public ImageView jian;

    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
