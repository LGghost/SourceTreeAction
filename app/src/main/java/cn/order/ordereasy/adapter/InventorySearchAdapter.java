package cn.order.ordereasy.adapter;

import android.app.Activity;
import android.content.Context;
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

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;

/**
 * Created by Mr.Pan on 2017/9/27.
 */

public class InventorySearchAdapter extends BaseExpandableListAdapter {

    // 集合
    private List<Goods> goods=new ArrayList<>();
    private MyItemClickListener mItemClickListener;

    // 创建布局使用
    private Activity activity;

    public InventorySearchAdapter(List<Goods> goods, Activity activity) {
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

        if(convertView==null){
            viewHolder=new GoodsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.pandian_item_one, parent, false);
            viewHolder.good_image= (ImageView) convertView.findViewById(R.id.good_image);
            viewHolder.good_name= (TextView) convertView.findViewById(R.id.good_name);
            viewHolder.pandian_num=(TextView)convertView.findViewById(R.id.pandian_num);
            viewHolder.shanchu=(ImageView) convertView.findViewById(R.id.shanchu);
            viewHolder.expanded_menu=(ImageView)convertView.findViewById(R.id.expanded_menu);
            viewHolder.expanded_text=(TextView)convertView.findViewById(R.id.expanded_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        Goods good =this.goods.get(groupPosition);
        viewHolder.good_name.setText(good.getTitle());
        viewHolder.pandian_num.setText(String.valueOf(good.getNum()));
        ImageLoader.getInstance().displayImage(Config.URL_HTTP+"/"+good.getCover(),viewHolder.good_image);
        int num =0;
        for(Product product:good.getProduct_list()){
            num+=product.getNum();
        }
        final int n=num;
        final int pos = groupPosition;
        if(mItemClickListener!=null){
            viewHolder.shanchu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onItemClick(v,pos,n);
                }
            });
        }

        if(!isExpanded){
            viewHolder.expanded_text.setText("展开");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_down);
        }else{

            viewHolder.expanded_text.setText("收起");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_up_blue);

        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 获取对应二级条目的View  和ListView 的getView相似
        ProductsViewHolder viewHolder = null;

        if(convertView==null){
            viewHolder=new ProductsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.pandian_item_two, parent, false);
            viewHolder.name=(TextView)convertView.findViewById(R.id.guige_name);
//            viewHolder.num=(TextView)convertView.findViewById(R.id.guige_num);
            viewHolder.shuliang=(TextView)convertView.findViewById(R.id.shuliang);
            viewHolder.lay_2=(LinearLayout)convertView.findViewById(R.id.lay_2);


//            viewHolder.jiage=(TextView)convertView.findViewById(R.id.jiage);
            viewHolder.jia=(ImageView)convertView.findViewById(R.id.jia);
            viewHolder.jian=(ImageView) convertView.findViewById(R.id.jian);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        final int gpos=groupPosition;
        final int cpos=childPosition;
        Product data =this.goods.get(groupPosition).getProduct_list().get(childPosition);
        List<String> spec_datas=data.getSpec_data();
        if(spec_datas==null) spec_datas=new ArrayList<>();
        if(spec_datas.size()==2){
            viewHolder.name.setText(data.getSpec_data().get(0)+"/"+data.getSpec_data().get(1));
        }else if(spec_datas.size()==1){
            viewHolder.name.setText(data.getSpec_data().get(0));
        }else{
            viewHolder.name.setText("无");
        }
        if(childPosition==0){
            viewHolder.lay_2.setVisibility(View.VISIBLE);
        }else{
            viewHolder.lay_2.setVisibility(View.GONE);
        }

//        viewHolder.num.setText(data.getStore_num()+"");
//        viewHolder.jiage.setText(data.getOwe_num()+"");
        viewHolder.shuliang.setText(data.getNum()+"");
//        viewHolder.jia.setVisibility(View.GONE);
//        viewHolder.jian.setVisibility(View.GONE);
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product=goods.get(gpos).getProduct_list().get(cpos);
                int num = product.getNum();
                num++;
                product.setNum(num);
               // goods.get(gpos).getProduct_list().get(cpos).getNum();
                goods.get(gpos).getProduct_list().set(cpos,product);
                int n=initData(gpos);
                notifyDataSetChanged();
                mItemClickListener.onItemClick(v,cpos,n);
            }
        });
        viewHolder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product=goods.get(gpos).getProduct_list().get(cpos);
                int num = product.getNum();

                if(num>0){
                    num--;
                    product.setNum(num);
                    // goods.get(gpos).getProduct_list().get(cpos).getNum();
                    goods.get(gpos).getProduct_list().set(cpos,product);
                    int n=initData(gpos);
                    notifyDataSetChanged();
                    mItemClickListener.onItemClick(v,cpos,n);
                }
            }
        });


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
        public ImageView shanchu;

        public TextView expanded_text;
        public ImageView expanded_menu;



    }

    class ProductsViewHolder {
        public TextView name;
        public TextView num;
        public TextView jiage;
        public TextView shuliang;
        public ImageView jia;
        public ImageView jian;
        public LinearLayout lay_2;

    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int childpostion,int groupposition);
    }
    /**
     * 设置Item点击监听、、、、
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }

    int initData(int position){
        Goods good=goods.get(position);
        int num=0;
        for(Product p:good.getProduct_list()){
            num+=p.getNum();
        }
        good.setNum(num);
        goods.set(position,good);
        return num;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}