package cn.order.ordereasy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.FileUtils;

/**
 * Created by Administrator on 2017/9/12.
 */

public class PriceAdapter extends BaseAdapter {

    Context context;
    private List<Map<String, Object>> data = new ArrayList<>();

    private MyItemClickListener mItemClickListener;

    public PriceAdapter(Context context) {
        this.context = context;
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
        notifyDataSetChanged();
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
                    R.layout.price_set_item, parent, false);
            viewHolder.cd_layout = (LinearLayout) convertView.findViewById(R.id.te_cb_jia_layout);
            viewHolder.xs_layout = (LinearLayout) convertView.findViewById(R.id.te_xs_jia_layout);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige);
            viewHolder.cb = (TextView) convertView.findViewById(R.id.te_cb_jia);
            viewHolder.xs = (TextView) convertView.findViewById(R.id.te_xs_jia);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String, Object> map = this.data.get(position);
        String cb = map.get("cb").toString();
        viewHolder.cb.setText(cb);
        String xs = map.get("xs").toString();
        viewHolder.xs.setText(xs);
        String name = map.get("name").toString();
        Log.e("PriceAdapter", FileUtils.cutOutString(name));
        viewHolder.name.setText(FileUtils.cutOutString(name));
        final int p = position;
        viewHolder.cd_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v, p);
            }
        });
        viewHolder.xs_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v, p);
            }
        });

        return convertView;
    }

    class ViewHolder {
        public LinearLayout cd_layout;
        public LinearLayout xs_layout;
        public TextView name;
        public TextView cb;
        public TextView xs;

    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }
}
