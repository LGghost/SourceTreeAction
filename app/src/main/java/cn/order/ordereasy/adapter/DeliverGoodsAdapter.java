package cn.order.ordereasy.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.app.MyApplication;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.utils.ToastUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class DeliverGoodsAdapter extends BaseExpandableListAdapter {

    // 集合
    private List<Goods> goods = new ArrayList<>();
    private MyItemClickListener mItemClickListener;
    private int index = -1;
    private int position = -1;
    // 创建布局使用
    private Activity activity;
    private AlertDialog alertDialog;

    public DeliverGoodsAdapter(List<Goods> goods, Activity activity) {
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
        final GoodsViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new GoodsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.deliver_goods_item, parent, false);
            viewHolder.good_image = (ImageView) convertView.findViewById(R.id.good_image);
            viewHolder.good_name = (TextView) convertView.findViewById(R.id.good_name);
            viewHolder.good_qianhuo = (TextView) convertView.findViewById(R.id.good_qianhuo);
            viewHolder.good_fahuo = (TextView) convertView.findViewById(R.id.good_fahuo);
            viewHolder.gooo_ck = (ImageView) convertView.findViewById(R.id.checkbox_click);
            viewHolder.checkbox_layout = (LinearLayout) convertView.findViewById(R.id.checkbox_layout);
            viewHolder.expanded_menu = (ImageView) convertView.findViewById(R.id.expanded_menu);
            viewHolder.expanded_text = (TextView) convertView.findViewById(R.id.expanded_text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        Goods good = this.goods.get(groupPosition);
        viewHolder.good_name.setText(good.getGoods_no() + " (" + good.getTitle() + ")");
        if (good.getNum() == 0) {
            viewHolder.good_fahuo.setVisibility(View.GONE);
        } else {
            viewHolder.good_fahuo.setVisibility(View.VISIBLE);
        }
        viewHolder.good_fahuo.setText(String.valueOf("(发货" + good.getNum() + ")"));
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), viewHolder.good_image);
        int num = 0, flag = 0;
        for (Product product : good.getProduct_list()) {
            if (product.getNum() != product.getOwe_num()) {
                flag = 1;
            }
            num += product.getOwe_num();
        }

        if (!isExpanded) {
            viewHolder.expanded_text.setText("展开");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_down);
        } else {

            viewHolder.expanded_text.setText("收起");
            viewHolder.expanded_menu.setBackgroundResource(R.drawable.icon_up_blue);

        }

        if (flag == 1) {
            viewHolder.gooo_ck.setBackgroundResource(R.drawable.icon_ridio_defaul);
        } else {
            viewHolder.gooo_ck.setBackgroundResource(R.drawable.icon_ridio_selected);
        }
        final int gpos = groupPosition;
        viewHolder.checkbox_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAll = isAll(gpos);
                int goodnum = 0;
                Goods good = goods.get(gpos);
                List<Product> products = good.getProduct_list();
                for (Product product : products) {
                    if (!isAll) {
                        viewHolder.gooo_ck.setBackgroundResource(R.drawable.icon_ridio_selected);
                        product.setNum(product.getOwe_num());
                        goodnum += product.getOwe_num();
                    } else {
                        viewHolder.gooo_ck.setBackgroundResource(R.drawable.icon_ridio_defaul);
                        product.setNum(0);
                    }
                }
                good.setProduct_list(products);
                good.setNum(goodnum);
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick();
                }
                notifyDataSetChanged();
            }
        });
        viewHolder.good_qianhuo.setText(String.valueOf(num));

        return convertView;
    }

    private boolean isAll(int position) {
        for (Product product : goods.get(position).getProduct_list()) {
            if (product.getNum() != product.getOwe_num()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 获取对应二级条目的View  和ListView 的getView相似
        final ProductsViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ProductsViewHolder();
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.fahuo_item_listview_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.num = (TextView) convertView.findViewById(R.id.guige_num);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.shuliang);
            viewHolder.jiage = (TextView) convertView.findViewById(R.id.jiage);
            viewHolder.jia = (ImageView) convertView.findViewById(R.id.jia);
            viewHolder.title = (LinearLayout) convertView.findViewById(R.id.lay_2);
            viewHolder.jian = (ImageView) convertView.findViewById(R.id.jian);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        final int gpos = groupPosition;
        final int cpos = childPosition;
        final Product data = this.goods.get(groupPosition).getProduct_list().get(childPosition);
        if (childPosition == 0) {
            viewHolder.title.setVisibility(View.VISIBLE);
        } else {
            viewHolder.title.setVisibility(View.GONE);
        }
        List<String> spec_datas = data.getSpec_data();
        if (spec_datas == null) spec_datas = new ArrayList<>();
        if (spec_datas.size() == 2) {
            viewHolder.name.setText(data.getSpec_data().get(0) + "/" +  data.getSpec_data().get(1));
        } else {
            viewHolder.name.setText( data.getSpec_data().get(0));
        }

        viewHolder.num.setText(data.getStore_num() + "");
        viewHolder.jiage.setText(data.getOwe_num() + "");
        viewHolder.shuliang.setText(data.getNum() + "");
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = data.getNum();
                number++;
                if (number > data.getOwe_num()) {
                    return;
                }
                data.setNum(number);
                viewHolder.shuliang.setText(number + "");
                TotalNumber(gpos);
            }
        });
        viewHolder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = data.getNum();
                number--;
                if (number < 0) {
                    number = 0;
                }
                data.setNum(number);
                viewHolder.shuliang.setText(number + "");
                TotalNumber(gpos);
            }
        });

        viewHolder.shuliang.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = childPosition;
                    position = groupPosition;
                    showdialogs(viewHolder.shuliang);
                }
                return false;
            }
        });

        return convertView;
    }


    //弹出框
    private void showdialogs(final TextView text) {
        alertDialog = new AlertDialog.Builder(activity).create();
        View view = View.inflate(activity, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);

        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER);

        //hint内容
        ed_type_name.setHint("发货数不能大于欠货数");
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Product data = goods.get(position).getProduct_list().get(index);
                data.setNum(Integer.parseInt(ed_type_name.getText().toString()));
                text.setText(ed_type_name.getText().toString());
                TotalNumber(position);
                alertDialog.dismiss();
            }
        });
        //监听edittext
        ed_type_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Product data = goods.get(position).getProduct_list().get(index);
                if (s.length() > 0) {
                    int number = Integer.valueOf(s.toString());
                    if (number > data.getOwe_num()) {
                        ed_type_name.setText(data.getOwe_num() + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void TotalNumber(int groupPosition) {
        Goods good = goods.get(groupPosition);
        List<Product> products = good.getProduct_list();
        int fahuo = 0;
        for (Product product : products) {
            fahuo += product.getNum();
        }
        Log.e("TotalNumber", "fahuo:" + fahuo);
        good.setNum(fahuo);
        if (mItemClickListener != null) {
            mItemClickListener.onItemClick();
        }
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
        public ImageView gooo_ck;
        public TextView expanded_text;
        public ImageView expanded_menu;
        public LinearLayout checkbox_layout;

    }

    class ProductsViewHolder {
        public TextView name;
        public TextView num;
        public TextView jiage;
        public TextView shuliang;
        public ImageView jia;
        public ImageView jian;
        public LinearLayout title;

    }

    public interface MyItemClickListener {
        public void onItemClick();
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