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

    public ConfirmBillingGoodsItemAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
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
            viewHolder.num = (TextView) convertView.findViewById(R.id.guige_num);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.shuliang);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Product data = this.data.get(position);
        if (data.getSpec_data().size() > 0) {
            if (data.getSpec_data().size() == 1) {
                viewHolder.name.setText(FileUtils.cutOutString(6, data.getSpec_data().get(0)));
            } else if (data.getSpec_data().size() == 2) {
                viewHolder.name.setText(FileUtils.cutOutString(4, data.getSpec_data().get(0)) + "/" + FileUtils.cutOutString(4, data.getSpec_data().get(1)));
            }
        } else {
            viewHolder.name.setText("无");
        }
        if (data.getDefault_price() != -1) {
            viewHolder.num.setText(data.getDefault_price() + "");
        } else {
            viewHolder.num.setText(data.getSell_price() + "");
        }
        viewHolder.shuliang.setText("x" + data.getNum());

        return convertView;
    }

    class ViewHolder {

        public TextView name;
        public TextView num;
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
