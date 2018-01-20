package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.FileUtils;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DetailSpecsAdapter extends BaseAdapter {

    Context context;
    private List<Product> data = null;

    private MyItemClickListener mItemClickListener;

    public DetailSpecsAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.kcxl_item_one_item_top, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.spec_name);
            viewHolder.kucun = (TextView) convertView.findViewById(R.id.spec_kucun);
            viewHolder.qianhuo = (TextView) convertView.findViewById(R.id.spec_qianhuo);
            viewHolder.spec_spec = (TextView) convertView.findViewById(R.id.spec_spec);
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

        viewHolder.kucun.setText(data.getStore_num() + "");
        viewHolder.qianhuo.setText(data.getSale_num() + "（" + data.getOwe_num() + "）");
        viewHolder.spec_spec.setText(data.getSell_price() + "");

        return convertView;
    }

    class ViewHolder {

        public TextView name;
        public TextView kucun;
        public TextView qianhuo;
        public TextView spec_spec;

    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
