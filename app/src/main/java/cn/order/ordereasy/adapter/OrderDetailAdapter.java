package cn.order.ordereasy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.FileUtils;

/**
 * Created by mrpan on 2017/9/25.
 */

public class OrderDetailAdapter extends BaseExpandableListAdapter {

    // 集合
    private List<Goods> goods = new ArrayList<>();
    private DeliverGoodsAdapter.MyItemClickListener mItemClickListener;

    // 创建布局使用
    private Activity activity;
    private int type;
    private boolean isClose;

    public OrderDetailAdapter(List<Goods> goods, Activity activity, int type, boolean isClose) {
        this.goods = goods;
        this.activity = activity;
        this.type = type;
        this.isClose = isClose;
    }

    @Override
    public int getGroupCount() {
        return goods.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (goods.size() != 0 && goods.size() <= groupPosition) {
            return 0;
        }
        return goods.get(groupPosition).getProduct_list().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return goods.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // 获取对应一级条目下二级条目的对应数据  感觉没什么用
        return goods.get(groupPosition).getProduct_list().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // 直接返回，没什么用
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // 直接返回，没什么用
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // 谁知道这个是干什么。。。。
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 获取对应一级条目的View  和ListView 的getView相似
        GoodsViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new GoodsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.orderno_details_item, parent, false);
            viewHolder.good_image = (ImageView) convertView.findViewById(R.id.good_image);
            viewHolder.good_name = (TextView) convertView.findViewById(R.id.good_name);
            viewHolder.good_qianhuo = (TextView) convertView.findViewById(R.id.good_qianhuo);
            viewHolder.good_fahuo = (TextView) convertView.findViewById(R.id.good_fahuo);
            viewHolder.qianhuo_text = (TextView) convertView.findViewById(R.id.qianhuo_text);
            viewHolder.expanded_menu = (ImageView) convertView.findViewById(R.id.expanded_menu);
            viewHolder.expanded_text = (TextView) convertView.findViewById(R.id.expanded_text);
            viewHolder.huopin_text = (TextView) convertView.findViewById(R.id.huopin_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        Goods good = this.goods.get(groupPosition);
        viewHolder.good_name.setText(good.getGoods_no() + " (" + good.getTitle() + ")");


        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), viewHolder.good_image);
        int num = 0, flag = 0;
        double money = 0;
        int qianhuo = 0;
        for (Product product : good.getProduct_list()) {
            if (product.getNum() != product.getOwe_num()) {
                flag = 1;
            }
            money += product.getSell_price() * product.getOperate_num();
            num += product.getOperate_num();
            qianhuo += product.getOwe_num();
        }

        switch (type) {
            case 1:
                viewHolder.huopin_text.setText("要货：");
                viewHolder.qianhuo_text.setVisibility(View.VISIBLE);
                if (qianhuo == 0) {
                    viewHolder.qianhuo_text.setVisibility(View.GONE);
                } else {
                    viewHolder.qianhuo_text.setText("欠货:" + qianhuo);
                }
                break;
            case 2:
                viewHolder.qianhuo_text.setVisibility(View.GONE);
                viewHolder.huopin_text.setText("退货：");
                break;
            case 3:
                viewHolder.qianhuo_text.setVisibility(View.GONE);
                viewHolder.huopin_text.setText("退欠货：");
                break;
        }
        if (isClose) {
            viewHolder.qianhuo_text.setVisibility(View.GONE);
        }
        money = (double) Math.round(money * 100) / 100;
        viewHolder.good_qianhuo.setText(String.valueOf(num));
        viewHolder.good_fahuo.setText(String.valueOf(money));

        if (!isExpanded) {
            viewHolder.expanded_text.setText("展开");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_down);
        } else {

            viewHolder.expanded_text.setText("收起");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_up_blue);

        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 获取对应二级条目的View  和ListView 的getView相似
        ProductsViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ProductsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.dingdan_xiangqing_item_item, parent, false);
            viewHolder.guige_name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.yaohuo_num = (TextView) convertView.findViewById(R.id.yaohuo_num_text);
            viewHolder.qianhuo = (TextView) convertView.findViewById(R.id.qianhuo_num_text);
            viewHolder.lay_2 = (LinearLayout) convertView.findViewById(R.id.lay_2);
            viewHolder.yaohuo_bar = (TextView) convertView.findViewById(R.id.yaohuo_bar);
            viewHolder.qianhuo_bar = (TextView) convertView.findViewById(R.id.qianhuo_bar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        if (isClose) {
            viewHolder.qianhuo_bar.setVisibility(View.GONE);
            viewHolder.qianhuo.setVisibility(View.GONE);
        } else {
            viewHolder.qianhuo_bar.setVisibility(View.VISIBLE);
            viewHolder.qianhuo.setVisibility(View.VISIBLE);
        }
        switch (type) {
            case 1:
                viewHolder.yaohuo_bar.setVisibility(View.VISIBLE);
                viewHolder.yaohuo_num.setVisibility(View.VISIBLE);
                viewHolder.qianhuo_bar.setText("欠货数");
                break;
            case 2:
                viewHolder.yaohuo_bar.setVisibility(View.GONE);
                viewHolder.yaohuo_num.setVisibility(View.GONE);
                viewHolder.qianhuo_bar.setText("退货数");
                break;
            case 3:
                viewHolder.yaohuo_bar.setVisibility(View.GONE);
                viewHolder.yaohuo_num.setVisibility(View.GONE);
                viewHolder.qianhuo_bar.setText("退欠货数");
                break;
        }


        Product data = this.goods.get(groupPosition).getProduct_list().get(childPosition);
        List<String> spec_datas = data.getSpec_data();
        if (spec_datas == null) spec_datas = new ArrayList<>();
        if (spec_datas.size() == 2) {
            viewHolder.guige_name.setText(FileUtils.cutOutString(4, data.getSpec_data().get(0)) + "/" + FileUtils.cutOutString(4, data.getSpec_data().get(1)));
        } else if (spec_datas.size() == 1) {
            viewHolder.guige_name.setText(FileUtils.cutOutString(6, data.getSpec_data().get(0)));
        } else {
            viewHolder.guige_name.setText("无");
        }
        if (childPosition == 0) {
            viewHolder.lay_2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lay_2.setVisibility(View.GONE);
        }
        viewHolder.yaohuo_num.setText(data.getOperate_num() + "(" + data.getSell_price() + ")");
        if (type == 2) {
            viewHolder.qianhuo.setText(data.getOperate_num() + "");
        } else {
            viewHolder.qianhuo.setText(data.getOwe_num() + "");
        }


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 根据方法名，此处应该表示二级条目是否可以被点击   先返回true 再讲
        return true;
    }


    public class GoodsViewHolder {

        public ImageView good_image;
        public TextView good_name;
        public TextView good_qianhuo;
        public TextView qianhuo_text;
        public TextView good_fahuo;
        public CheckBox gooo_ck;
        public TextView expanded_text;
        public TextView huopin_text;
        public ImageView expanded_menu;


    }

    class ProductsViewHolder {
        public LinearLayout lay_2;
        public TextView guige_name;
        public TextView yaohuo_num;
        public TextView qianhuo;
        public TextView yaohuo_bar;
        public TextView qianhuo_bar;
    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int childpostion, int groupposition);
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(DeliverGoodsAdapter.MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}