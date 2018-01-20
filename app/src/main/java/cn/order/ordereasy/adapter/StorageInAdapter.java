package cn.order.ordereasy.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;

/**
 * Created by Mr.Pan on 2017/9/27.
 */

public class StorageInAdapter extends BaseExpandableListAdapter {

    // 集合
    private List<Goods> goods = new ArrayList<>();
    private MyItemClickListener mItemClickListener;
    private int index = -1;
    private int groupIndex = -1;
    // 创建布局使用
    private Activity activity;
    private int sign;
    private myWatcher mWatcher;

    public StorageInAdapter(List<Goods> goods, Activity activity, int sign) {
        this.goods = goods;
        this.activity = activity;
        this.sign = sign;
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
                    R.layout.ruku_item_one, parent, false);
            viewHolder.good_image = (ImageView) convertView.findViewById(R.id.good_image);
            viewHolder.good_name = (TextView) convertView.findViewById(R.id.good_name);
            viewHolder.pandian_num = (TextView) convertView.findViewById(R.id.pandian_num);
            viewHolder.shanchu = (ImageView) convertView.findViewById(R.id.shanchu);
            viewHolder.ruku_text = (TextView) convertView.findViewById(R.id.ruku_text);
            viewHolder.expanded_menu = (ImageView) convertView.findViewById(R.id.expanded_menu);
            viewHolder.expanded_text = (TextView) convertView.findViewById(R.id.expanded_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        if (sign == 0) {
            viewHolder.ruku_text.setText("入库数：");
        } else if (sign == 1) {
            viewHolder.ruku_text.setText("出库数：");
        } else {
            viewHolder.ruku_text.setText("调整数：");
        }

        Goods good = this.goods.get(groupPosition);
        viewHolder.good_name.setText(good.getTitle());
        viewHolder.pandian_num.setText(String.valueOf(good.getNum()));
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
        final ProductsViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ProductsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.ruku_item_two, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.shuliang = (EditText) convertView.findViewById(R.id.shuliang);
            viewHolder.yuan_kucun = (TextView) convertView.findViewById(R.id.yuan_kucun);
            viewHolder.lay_2 = (LinearLayout) convertView.findViewById(R.id.lay_2);
            viewHolder.jia = (ImageView) convertView.findViewById(R.id.jia);
            viewHolder.jian = (ImageView) convertView.findViewById(R.id.jian);
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
            viewHolder.name.setText(data.getSpec_data().get(0) + "/" + data.getSpec_data().get(1));
        } else if (spec_datas.size() == 1) {
            viewHolder.name.setText(data.getSpec_data().get(0));
        } else {
            viewHolder.name.setText("无");
        }
        if (childPosition == 0) {
            viewHolder.lay_2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lay_2.setVisibility(View.GONE);
        }
        viewHolder.yuan_kucun.setText(String.valueOf(data.getStore_num()));
        viewHolder.shuliang.setText(data.getNum() + "");
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Product product = goods.get(gpos).getProduct_list().get(cpos);
                int num = product.getNum();
                num++;
                product.setNum(num);
                int n = initData(gpos);
                notifyDataSetChanged();
                mItemClickListener.onItemClick(v, cpos, n);
            }
        });
        viewHolder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product product = goods.get(gpos).getProduct_list().get(cpos);
                int num = product.getNum();
                if (num > 0) {
                    num--;
                    product.setNum(num);
                    int n = initData(gpos);
                    notifyDataSetChanged();
                    mItemClickListener.onItemClick(v, cpos, n);
                }
            }
        });
        viewHolder.shuliang.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = cpos;
                    groupIndex = gpos;
                }
                return false;
            }
        });
        viewHolder.shuliang.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            //设置焦点监听，当获取到焦点的时候才给它设置内容变化监听解决卡的问题

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText et = (EditText) v;
                if (mWatcher == null) {
                    mWatcher = new myWatcher(v);
                }
                if (hasFocus) {
                    et.addTextChangedListener(mWatcher);
                } else {
                    et.removeTextChangedListener(mWatcher);
                }

            }
        });
        viewHolder.shuliang.clearFocus();//防止点击以后弹出键盘，重新getview导致的焦点丢失
        if (index != -1 && index == cpos) {
            // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
            viewHolder.shuliang.requestFocus();

        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // 根据方法名，此处应该表示二级条目是否可以被点击   先返回true 再讲
        return true;
    }

    class myWatcher implements TextWatcher {
        private View v;

        public myWatcher(View v) {
            this.v = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                int number = Integer.valueOf(s.toString());
                Product p = goods.get(groupIndex).getProduct_list().get(index);
                p.setNum(number);
                p.setPrice(number * p.getSell_price());
            } else {
                Product p = goods.get(groupIndex).getProduct_list().get(index);
                p.setNum(0);
                p.setPrice(0);
            }
            int n = initData(groupIndex);
            notifyDataSetChanged();
            mItemClickListener.onItemClick(v, index, n);
        }
    }

    public class GoodsViewHolder {

        public ImageView good_image;
        public TextView good_name;
        public TextView pandian_num;
        public ImageView shanchu;
        public TextView ruku_text;
        public TextView expanded_text;
        public ImageView expanded_menu;


    }

    class ProductsViewHolder {
        public TextView name;
        public TextView num;
        public TextView yuan_kucun;
        public EditText shuliang;
        public ImageView jia;
        public ImageView jian;
        public LinearLayout lay_2;

    }

    public interface MyItemClickListener {
        void onItemClick(View view, int childpostion, int groupposition);
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
        return num;
    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }
}