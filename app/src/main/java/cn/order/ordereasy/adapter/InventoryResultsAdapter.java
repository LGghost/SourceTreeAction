package cn.order.ordereasy.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
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
 * Created by Mr.Pan on 2017/9/27.
 */

public class InventoryResultsAdapter extends BaseExpandableListAdapter {

    // 集合
    private List<Goods> goods = new ArrayList<>();
    private MyItemClickListener mItemClickListener;

    // 创建布局使用
    private Activity activity;

    public InventoryResultsAdapter(List<Goods> goods, Activity activity) {
        this.goods = goods;
        this.activity = activity;
    }

    @Override
    public int getGroupCount() {
        return goods.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
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
                    R.layout.inventory_results_item, parent, false);
            viewHolder.good_image = (ImageView) convertView.findViewById(R.id.stock_image);
            viewHolder.good_name = (TextView) convertView.findViewById(R.id.stock_name);
            viewHolder.pandian_num = (TextView) convertView.findViewById(R.id.pandian_num);
            viewHolder.zm_kucun_num = (TextView) convertView.findViewById(R.id.zm_kucun_num);
            viewHolder.pankui_num = (TextView) convertView.findViewById(R.id.pankui_num);
            viewHolder.panying_num = (TextView) convertView.findViewById(R.id.panying_num);
            viewHolder.pandian_zhengchang = (TextView) convertView.findViewById(R.id.pandian_zhengchang);
            viewHolder.expanded_menu = (ImageView) convertView.findViewById(R.id.expanded_menu);
            viewHolder.pankui_layout = (LinearLayout) convertView.findViewById(R.id.pankui_layout);
            viewHolder.panying_layout = (LinearLayout) convertView.findViewById(R.id.panying_layout);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        Goods good = this.goods.get(groupPosition);
        int storenum = 0, operatornum = 0, panying = 0, pankui = 0;

        for (Product product : good.getProduct_list()) {
            operatornum += product.getOperate_num();
            storenum += product.getStore_num();
            int number = product.getOperate_num() - product.getStore_num();
            if (number > 0) {
                panying += number;
            } else {
                pankui += number;
            }
        }

        if (panying > 0) {
            viewHolder.panying_layout.setVisibility(View.VISIBLE);
            viewHolder.panying_num.setText(String.valueOf(panying));
            viewHolder.pandian_zhengchang.setVisibility(View.GONE);
        } else {
            viewHolder.panying_layout.setVisibility(View.GONE);
        }
        if (pankui < 0) {
            viewHolder.pandian_zhengchang.setVisibility(View.GONE);
            viewHolder.pankui_layout.setVisibility(View.VISIBLE);
            viewHolder.pankui_num.setText(String.valueOf(Math.abs(pankui)));
        } else {
            viewHolder.pankui_layout.setVisibility(View.GONE);
        }
        if (panying == 0 && pankui == 0) {
            viewHolder.pandian_zhengchang.setVisibility(View.VISIBLE);
            viewHolder.pandian_zhengchang.setText("库存正常");
        }
        viewHolder.good_name.setText(good.getGoods_no() + " (" + good.getTitle() + ")");
        viewHolder.pandian_num.setText(String.valueOf(operatornum));
        viewHolder.zm_kucun_num.setText(String.valueOf(storenum));
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), viewHolder.good_image);
        int num = 0;
        for (Product product : good.getProduct_list()) {
            num += product.getNum();
        }
        final int n = num;
        final int pos = groupPosition;
        if (mItemClickListener != null) {
            viewHolder.shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v, pos, n);
                }
            });
        }

        if (!isExpanded) {
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_down);
        } else {
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
                    R.layout.inventory_results_item_item, parent, false);
            viewHolder.guige_nun = (TextView) convertView.findViewById(R.id.guige_nun);
            viewHolder.pandian_kucun = (TextView) convertView.findViewById(R.id.pandian_kucun);
            viewHolder.zhangmian_kucun = (TextView) convertView.findViewById(R.id.zhangmian_kucun);
            viewHolder.jieguo = (TextView) convertView.findViewById(R.id.jieguo);
            viewHolder.lay_2 = (LinearLayout) convertView.findViewById(R.id.lay_2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        final int gpos = groupPosition;
        final int cpos = childPosition;
        Product data = this.goods.get(groupPosition).getProduct_list().get(childPosition);
        List<String> spec_datas = data.getSpec_data();
        if (spec_datas == null) spec_datas = new ArrayList<>();
        if (spec_datas.size() == 2) {
            viewHolder.guige_nun.setText(data.getSpec_data().get(0) + "/" + data.getSpec_data().get(1));
        } else if (spec_datas.size() == 1) {
            viewHolder.guige_nun.setText(data.getSpec_data().get(0));
        } else {
            viewHolder.guige_nun.setText("无");
        }
        if (childPosition == 0) {
            viewHolder.lay_2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lay_2.setVisibility(View.GONE);
        }

        int storenum = data.getStore_num();
        int operatornum = data.getOperate_num();
        viewHolder.zhangmian_kucun.setText(String.valueOf(storenum));
        viewHolder.pandian_kucun.setText(String.valueOf(operatornum));
        int diff = operatornum - storenum;
        if (diff > 0) {
            viewHolder.zhangmian_kucun.setTextColor(activity.getResources().getColor(R.color.lanse));
            viewHolder.pandian_kucun.setTextColor(activity.getResources().getColor(R.color.lanse));
            viewHolder.jieguo.setTextColor(activity.getResources().getColor(R.color.lanse));
            viewHolder.jieguo.setText("盘盈" + diff);
        } else if (diff < 0) {
            viewHolder.zhangmian_kucun.setTextColor(activity.getResources().getColor(R.color.shouye_hongse));
            viewHolder.pandian_kucun.setTextColor(activity.getResources().getColor(R.color.shouye_hongse));
            viewHolder.jieguo.setTextColor(activity.getResources().getColor(R.color.shouye_hongse));
            viewHolder.jieguo.setText("盘亏" + Math.abs(diff));
        } else if (diff == 0) {
            viewHolder.zhangmian_kucun.setTextColor(activity.getResources().getColor(R.color.touzi_huise));
            viewHolder.pandian_kucun.setTextColor(activity.getResources().getColor(R.color.touzi_huise));
            viewHolder.jieguo.setTextColor(activity.getResources().getColor(R.color.touzi_huise));
            viewHolder.jieguo.setText("库存正常");
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
        public TextView pandian_num;
        public TextView zm_kucun_num;
        public TextView pankui_num;
        public TextView panying_num;
        public ImageView shanchu;
        public LinearLayout pankui_layout;
        public LinearLayout panying_layout;
        public ImageView expanded_menu;
        public TextView pandian_zhengchang;


    }

    class ProductsViewHolder {
        public TextView guige_nun;
        public TextView pandian_kucun;
        public TextView zhangmian_kucun;
        public TextView jieguo;
        public LinearLayout lay_2;

    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int childpostion, int groupposition);
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    int initData(int position) {
        Goods good = goods.get(position);
        int num = 0;
        for (Product p : good.getProduct_list()) {
            num += p.getNum();
        }
        good.setNum(num);
        goods.set(position, good);
        return num;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}