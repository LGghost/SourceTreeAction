package cn.order.ordereasy.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.TimeUtil;
import cn.order.ordereasy.view.activity.DeliverGoodsActivity;
import cn.order.ordereasy.widget.CustomExpandableListView;

/**
 * Created by Mr.Pan on 2017/9/23.
 */

public class DeliverListAdapter extends BaseExpandableListAdapter {
    private List<Order> orders;

    private Activity activity;
    private String flag = "deliver";
    public DeliverListAdapter(List<Order> orders, Activity activity) {
        this.orders = orders;
        this.activity = activity;
    }

    private OrderClickLister lister;
    public void setFlag(String flag){
        this.flag = flag;
    }
    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnOrderItemClickListener(OrderClickLister listener) {
        this.lister = listener;
    }

    @Override
    public int getGroupCount() {
        return orders.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // 很关键，，一定要返回  1
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return orders.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return orders.get(groupPosition).getGoods_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.deliver_goods_list_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.order_code);
            viewHolder.orderno_data = (TextView) convertView.findViewById(R.id.orderno_data);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Order data = this.orders.get(groupPosition);
        viewHolder.name.setText(data.getOrder_no());
        viewHolder.orderno_data.setText(TimeUtil.getTimeStamp2Str(Long.parseLong(data.getCreate_time()), "yyyy-MM-dd"));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return getGenericExpandableListView(orders.get(groupPosition), childPosition);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {

        public TextView name, orderno_data;

    }

    /**
     * 返回子ExpandableListView 的对象  此时传入的是该大学下所有班级的集合。
     *
     * @param order
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public ExpandableListView getGenericExpandableListView(final Order order, final int childPos) {
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        CustomExpandableListView view = new CustomExpandableListView(activity);
        view.setGroupIndicator(null);
        List<Goods> goods = order.getGoods_list();
        List<Goods> list = new ArrayList<>();
        for (int i = 0; i < goods.size(); i++) {
            int num = 0;
            for (Product product : goods.get(i).getProduct_list()) {
                num += product.getOwe_num();
            }
            if (num == 0) {
                list.add(goods.get(i));
            }
        }
        goods.removeAll(list);
        if (goods == null) goods = new ArrayList<>();

        // 加载班级的适配器
        final DeliverGoodsAdapter adapter = new DeliverGoodsAdapter(goods, activity,flag);

        view.setAdapter(adapter);
        view.setPadding(0, 0, 0, 0);
        adapter.setOnItemClickListener(new DeliverGoodsAdapter.MyItemClickListener() {
            @Override
            public void onItemClick() {
                int fahuoCount = 0;
                for (Order o : orders) {
                    List<Goods> goodsList = o.getGoods_list();
                    for (Goods g : goodsList) {
                        fahuoCount += g.getNum();
                    }
                }
                if (lister != null) {
                    lister.changeData(fahuoCount);
                }
            }
        });
        return view;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public interface OrderClickLister {
        void changeData(int num);
    }
}
