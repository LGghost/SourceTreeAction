package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Map;

import cn.order.ordereasy.R;

/**
 * Created by Administrator on 2017/9/17.
 *
 * 订单确认
 *
 */

public class OrderNoConfirmAdapter extends BaseAdapter {

    Context context;
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PriceAdapter.ViewHolder viewHolder = null;
//
//        if(convertView==null){
//            viewHolder=new OrderNoConfirmAdapter().ViewHolder();



//            convertView = LayoutInflater.from(context).inflate( R.layout.orderno_confirm_item, parent, false);
//            viewHolder.order_money= (TextView) convertView.findViewById(R.id.order_money);
//            viewHolder.order_image= (ImageView) convertView.findViewById(R.id.order_image);
//            viewHolder.order_code=(TextView)convertView.findViewById(R.id.order_code);
//            viewHolder.order_number=(TextView)convertView.findViewById(R.id.order_number);
//            viewHolder.order_danjia=(TextView)convertView.findViewById(R.id.order_danjia);
//
//            viewHolder.ggsx_name=(TextView)convertView.findViewById(R.id.ggsx_name);
//            viewHolder.ggsx_money=(TextView)convertView.findViewById(R.id.ggsx_money);
//            viewHolder.ggsx_num=(TextView)convertView.findViewById(R.id.ggsx_num);
//            convertView.setTag(viewHolder);



//        } else {
//            viewHolder = (PriceAdapter.ViewHolder) convertView.getTag();
//        }
//        Map<String,Object> map =this.data.get(position);
//        String cb=map.get("cb").toString();
//        viewHolder.cb.setText(cb);
//        String xs=map.get("xs").toString();
//        viewHolder.xs.setText(xs);
//        String name=map.get("name").toString();
//        viewHolder.name.setText(name);
//        final int p =position;
//        viewHolder.cb_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemClickListener.onItemClick(v,p);
//            }
//        });
//        viewHolder.xs_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemClickListener.onItemClick(v,p);
//            }
//        });


        return convertView;

    }

    class ViewHolder {

        public TextView order_money;
        public ImageView order_image;
        public TextView order_code;
        public TextView order_number;
        public TextView order_danjia;

        public TextView ggsx_name;
        public TextView ggsx_money;
        public TextView ggsx_num;
    }
}
