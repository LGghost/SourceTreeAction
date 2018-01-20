package cn.order.ordereasy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.PinyinUtil;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DetailOrdersAdapter extends BaseAdapter {

    Context context;
    private List<Order> data = null;

    private MyItemClickListener mItemClickListener;

    public DetailOrdersAdapter(Context context, List<Order> data) {
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
        Log.e("DetailOrdersAdapter", "" + data.size());
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
            convertView = LayoutInflater.from(context).inflate(R.layout.kcxl_item_one_item_buttom, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.order_name);
            viewHolder.no = (TextView) convertView.findViewById(R.id.order_no);
            viewHolder.time = (TextView) convertView.findViewById(R.id.order_time);
            viewHolder.owenum = (TextView) convertView.findViewById(R.id.order_owenum);
            viewHolder.subtotal = (TextView) convertView.findViewById(R.id.order_money);
            viewHolder.num = (TextView) convertView.findViewById(R.id.order_num);
            viewHolder.shouzimu = (TextView) convertView.findViewById(R.id.shouzimu);
            viewHolder.order_type = (ImageView) convertView.findViewById(R.id.order_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order data = this.data.get(position);
//        viewHolder.name.setText(data.);
        if (data != null) {
            if (data.getIs_wechat() == 1) {
                viewHolder.order_type.setImageResource(R.drawable.img_weixin);
            } else {
                viewHolder.order_type.setImageResource(R.drawable.img_dingdan);
            }
            String name = data.getCustomer_name();
            if (!TextUtils.isEmpty(name)) {
                name = String.valueOf(PinyinUtil.getFirstStr(name));
            }
            viewHolder.shouzimu.setText(name);

            viewHolder.name.setText(data.getCustomer_name());
            viewHolder.no.setText(data.getOrder_no());
            viewHolder.time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(data.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
            viewHolder.owenum.setText(String.valueOf(data.getOwe_num()));

            //你为什么也不显示
            viewHolder.subtotal.setText(String.valueOf(data.getOrder_sum()));
            viewHolder.num.setText(String.valueOf(data.getOrder_num()));
        }
        return convertView;
    }


    class ViewHolder {

        public TextView name;
        public TextView no;
        public TextView time;
        public TextView owenum;
        public TextView subtotal;
        public TextView num;
        public TextView shouzimu;
        public ImageView order_type;

    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }
}
