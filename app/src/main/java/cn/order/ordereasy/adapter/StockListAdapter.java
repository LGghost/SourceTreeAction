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
import cn.order.ordereasy.utils.PopWinShare;

/**
 * Created by mrpan on 2017/9/10.
 */

public class StockListAdapter extends BaseExpandableListAdapter {
    private PopWinShare popwindows;
    // 集合
    private List<Goods> goods = new ArrayList<>();
    private MyItemClickListener mItemClickListener;

    // 创建布局使用
    private Activity activity;

    public StockListAdapter(List<Goods> goods, Activity activity) {
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
                    R.layout.stock_guanli_item, parent, false);
            viewHolder.good_image = (ImageView) convertView.findViewById(R.id.good_image);
            viewHolder.good_name = (TextView) convertView.findViewById(R.id.good_name);
            viewHolder.good_qianhuo = (TextView) convertView.findViewById(R.id.good_qianhuo);
            viewHolder.good_fahuo = (TextView) convertView.findViewById(R.id.good_fahuo);
            viewHolder.expanded_menu = (ImageView) convertView.findViewById(R.id.expanded_menu);
            viewHolder.expanded_text = (TextView) convertView.findViewById(R.id.expanded_text);
            viewHolder.stock_more = (ImageView) convertView.findViewById(R.id.stock_more);
            viewHolder.buff = (ImageView) convertView.findViewById(R.id.buff);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        Goods good = this.goods.get(groupPosition);
        viewHolder.good_name.setText(good.getGoods_no() + " (" + good.getTitle() + ")");
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), viewHolder.good_image);
        if (good.getStatus() != 1) {
            viewHolder.buff.setVisibility(View.VISIBLE);
        } else {
            viewHolder.buff.setVisibility(View.GONE);
        }
        //这两个为什么不显示数据？
        viewHolder.good_qianhuo.setText(String.valueOf(good.getStore_num()));
        int Owe_num = 0;
        for (Product product : good.getProduct_list()) {
            Owe_num += product.getOwe_num();
        }
        viewHolder.good_fahuo.setText(String.valueOf(Owe_num));


        if (!isExpanded) {
            viewHolder.expanded_text.setText("展开");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_down);
        } else {

            viewHolder.expanded_text.setText("收起");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_up_blue);

        }
        final int gpos = groupPosition;
        viewHolder.stock_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClick(v, 0, gpos);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 获取对应二级条目的View  和ListView 的getView相似
        ProductsViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ProductsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.stock_guanli_item_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.num = (TextView) convertView.findViewById(R.id.guige_num);
            viewHolder.title = (LinearLayout) convertView.findViewById(R.id.lay_2);
            viewHolder.jiage = (TextView) convertView.findViewById(R.id.jiage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        Product data = this.goods.get(groupPosition).getProduct_list().get(childPosition);

        //加上顶部标题
        if (childPosition == 0) {
            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            viewHolder.title.setVisibility(View.GONE);
        }

        List<String> spec_datas = data.getSpec_data();
        if (spec_datas == null) spec_datas = new ArrayList<>();
        if (spec_datas.size() == 2) {
            viewHolder.name.setText(data.getSpec_data().get(0) + "/" + data.getSpec_data().get(1));
        } else if (spec_datas.size() == 1) {
            viewHolder.name.setText(data.getSpec_data().get(0));
        } else {
            viewHolder.name.setText("无");
        }

        viewHolder.num.setText(data.getStore_num() + "");
        viewHolder.jiage.setText(data.getOwe_num() + "");


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
        public TextView good_fahuo;
        public CheckBox gooo_ck;
        public TextView expanded_text;
        public ImageView expanded_menu;
        public ImageView stock_more;
        public ImageView buff;


    }

    class ProductsViewHolder {
        public TextView name;
        public TextView num;
        public TextView jiage;
        public TextView shuliang;
        public LinearLayout jia;
        public LinearLayout title;
        public LinearLayout jian;


    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
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

}