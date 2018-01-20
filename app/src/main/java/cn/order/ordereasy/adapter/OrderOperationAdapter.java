package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.OrderOperation;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.utils.TimeUtil;

public class OrderOperationAdapter extends BaseAdapter {
    private Context context;
    private List<OrderOperation> data = new ArrayList<>();
    private int order_type;

    public OrderOperationAdapter(Context context, int order_type) {
        this.context = context;
        this.order_type = order_type;
    }

    public void setData(List<OrderOperation> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<OrderOperation> getData() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public OrderOperation getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final OperationViewHolder viewHolder;
        OrderOperation operation = data.get(position);
        if (convertView == null) {
            viewHolder = new OperationViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.order_operation_item, parent, false);
            viewHolder.line = convertView.findViewById(R.id.line_view);
            viewHolder.name = (TextView) convertView.findViewById(R.id.order_operation_name);
            viewHolder.time = (TextView) convertView.findViewById(R.id.order_operation_time);
            viewHolder.operation = (TextView) convertView.findViewById(R.id.order_operation_number);
            viewHolder.next = (ImageView) convertView.findViewById(R.id.order_operation_next);
            viewHolder.type_image = (ImageView) convertView.findViewById(R.id.type_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (OperationViewHolder) convertView.getTag();
        }

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(viewHolder.line.getLayoutParams());
        if (position == 0) {
            lp.topMargin = ScreenUtil.dip2px(context, 35);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            viewHolder.line.setLayoutParams(lp);
        } else {
            lp.topMargin = ScreenUtil.dip2px(context, 0);
            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            viewHolder.line.setLayoutParams(lp);
        }
        int drawable = R.drawable.img_kaidan;
        String number = "";
        switch (operation.getLog_type()) {
            case 0://开单
                viewHolder.next.setVisibility(View.GONE);
                drawable = R.drawable.img_kaidan;
                if (order_type == 1) {
                    number = context.getString(R.string.sales_order) + operation.getLog_data().getOrder_num() + " (¥ " + operation.getLog_data().getOrder_sum() + ")";
                } else if (order_type == 2) {
                    number = context.getString(R.string.sales_order_return) + operation.getLog_data().getOrder_num() + " (¥ " + operation.getLog_data().getOrder_sum() + ")";
                } else if (order_type == 3) {
                    number = context.getString(R.string.sales_order_return_owe) + operation.getLog_data().getOrder_num() + " (¥ " + operation.getLog_data().getOrder_sum() + ")";
                } else {
                    number = context.getString(R.string.sales_order) + operation.getLog_data().getOrder_num() + " (¥ " + operation.getLog_data().getOrder_sum() + ")";
                }
                break;
            case 1:
                viewHolder.next.setVisibility(View.GONE);
                drawable = R.drawable.img_gaijia;
                number = context.getString(R.string.change_the_price) + operation.getLog_data().getPayable();
                break;
            case 2:
            case 3:
                viewHolder.next.setVisibility(View.GONE);
                drawable = R.drawable.img_shoukuan;
                String way = "";
                if (operation.getLog_data().getPayment_way() == 1) {
                    way = context.getString(R.string.cash);
                } else if (operation.getLog_data().getPayment_way() == 2) {
                    way = context.getString(R.string.alipay);
                } else if (operation.getLog_data().getPayment_way() == 3) {
                    way = context.getString(R.string.wechat);
                } else if (operation.getLog_data().getPayment_way() == 4) {
                    way = context.getString(R.string.bank_card);
                } else if (operation.getLog_data().getPayment_way() == 5) {
                    way = context.getString(R.string.other);
                }
                if (operation.getLog_type() == 2) {
                    number = context.getString(R.string.receivables) + operation.getLog_data().getMoney() + " (" + way + ")";
                } else {
                    number = context.getString(R.string.refund) + operation.getLog_data().getMoney() + " (" + way + ")";
                }
                break;
            case 4:
                viewHolder.next.setVisibility(View.VISIBLE);
                drawable = R.drawable.img_fahuo;
                number = context.getString(R.string.deliver_goods) + operation.getLog_data().getOperate_num();
                break;
            case 5:
                viewHolder.next.setVisibility(View.VISIBLE);
                drawable = R.drawable.img_tuiqian;
                number = context.getString(R.string.return_the_goods) + operation.getLog_data().getOperate_num();
                break;
            case 6:
                drawable = R.drawable.img_close;
                number = context.getString(R.string.order_closure);
                break;
        }
        if (operation.getUser_name() != null) {
            viewHolder.name.setText(operation.getUser_name());
        }
        viewHolder.time.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(operation.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
        viewHolder.operation.setText(number);
        viewHolder.type_image.setImageResource(drawable);
        return convertView;
    }

    class OperationViewHolder {
        public TextView name;
        public TextView time;
        public TextView operation;
        public ImageView next;
        public ImageView type_image;
        public View line;
    }

}