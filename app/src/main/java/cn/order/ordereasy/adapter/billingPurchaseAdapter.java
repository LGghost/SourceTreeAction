package cn.order.ordereasy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
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

public class billingPurchaseAdapter extends BaseExpandableListAdapter {
    private int index = -1;
    // 集合
    private List<Goods> goods = new ArrayList<>();
    private MyItemClickListener mItemClickListener;
    private MoneyClickLister lister;
    // 创建布局使用
    private Context context;
    private AlertDialog alertDialog;

    public billingPurchaseAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Goods> goods) {
        this.goods = goods;
        notifyDataSetChanged();
    }

    public List<Goods> getData() {
        return goods;
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 获取对应一级条目的View  和ListView 的getView相似
        final GoodsViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new GoodsViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.billing_purchase_frist_item, parent, false);
            viewHolder.good_image = (ImageView) convertView.findViewById(R.id.kaidan_goodimg);
            viewHolder.good_name = (TextView) convertView.findViewById(R.id.kaidan_goodname);
            viewHolder.good_num = (TextView) convertView.findViewById(R.id.kaidan_goodnum);
            viewHolder.money = (TextView) convertView.findViewById(R.id.kaidan_money);
            viewHolder.zhankai = (LinearLayout) convertView.findViewById(R.id.zhankai);
            viewHolder.shanchu = (ImageView) convertView.findViewById(R.id.kaidan_shanchu);
            viewHolder.zhankai_img = (ImageView) convertView.findViewById(R.id.zhankai_img);
            viewHolder.kaidan_isshow = (TextView) convertView.findViewById(R.id.kaidan_isshow);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GoodsViewHolder) convertView.getTag();
        }
        final Goods good = this.goods.get(groupPosition);
        viewHolder.good_name.setText(good.getGoods_no() + " (" + good.getTitle() + ")");
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), viewHolder.good_image);
        //这两个为什么不显示数据？
        viewHolder.good_num.setText(String.valueOf(good.getNum()));
        viewHolder.money.setText(FileUtils.getMathNumber(good.getPrice()));
        if (!isExpanded) {
            viewHolder.kaidan_isshow.setText("展开");
            viewHolder.zhankai_img.setBackgroundResource(R.drawable.icon_down);
        } else {

            viewHolder.kaidan_isshow.setText("收起");
            viewHolder.zhankai_img.setBackgroundResource(R.drawable.icon_up_blue);

        }
        viewHolder.shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goods.remove(groupPosition);
                changeData();
                if (lister != null) {
                    lister.delete(groupPosition);
                }
                notifyDataSetChanged();
            }
        });
        setOnItemClickListener(new MyItemClickListener() {
            @Override
            public void onInputClick(double tPrice, int tNumber) {
                Log.e("JJF", "进入tPrice:" + tPrice + "tNumber:" + tNumber);
                good.setNum(tNumber);
                good.setPrice(tPrice);
                notifyDataSetChanged();
                changeData();
            }
        });
        return convertView;
    }

    public void changeData() {
        double p = 0;
        int n = 0;
        for (Goods good : this.goods) {
            p += good.getPrice();
            n += good.getNum();
        }
        if (lister != null) {
            lister.changeData(p, n);
        }
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 获取对应二级条目的View  和ListView 的getView相似
        final ProductsViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ProductsViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.billiing_purchase_second_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.shuliang);
            viewHolder.jiage = (TextView) convertView.findViewById(R.id.jiage);
            viewHolder.jia = (ImageView) convertView.findViewById(R.id.jia);
            viewHolder.jian = (ImageView) convertView.findViewById(R.id.jian);
            viewHolder.lay_2 = (LinearLayout) convertView.findViewById(R.id.lay_2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }

        //加上顶部标题
        if (childPosition == 0) {
            viewHolder.lay_2.setVisibility(View.VISIBLE);
        } else {
            viewHolder.lay_2.setVisibility(View.GONE);
        }
        final Product product = this.goods.get(groupPosition).getProduct_list().get(childPosition);
        if (product.getSpec_data().size() > 0) {
            if (product.getSpec_data().size() == 1) {
                viewHolder.name.setText(product.getSpec_data().get(0) + "(" + product.getOwe_num() + ")");
            } else if (product.getSpec_data().size() == 2) {
                viewHolder.name.setText(product.getSpec_data().get(0) + "/" + product.getSpec_data().get(1) + "(" + product.getOwe_num() + ")");
            }
        } else {
            viewHolder.name.setText("无");
        }
        viewHolder.shuliang.setText(product.getNum() + "");
        viewHolder.jiage.setText(product.getCost_price() + "");
        Totalprice(groupPosition);
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getNum();
                number++;
                product.setPrice(number * product.getCost_price());
                product.setNum(number);
                viewHolder.shuliang.setText(number + "");
                Totalprice(groupPosition);
            }
        });
        viewHolder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getNum();
                number--;
                if (number < 0) {
                    number = 0;
                }
                product.setPrice(number * product.getCost_price());
                product.setNum(number);
                viewHolder.shuliang.setText(number + "");
                Totalprice(groupPosition);
            }
        });

        viewHolder.shuliang.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = childPosition;
                    showdialogs(viewHolder.shuliang, product, groupPosition);
                }
                return false;
            }
        });
        return convertView;
    }

    //弹出框
    private void showdialogs(final TextView text, final Product product, final int groupPosition) {
        alertDialog = new AlertDialog.Builder(context).create();
        View view = View.inflate(context, R.layout.tanchuang_view, null);
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
        if (product.getNum() == 0) {
            ed_type_name.setHint("输入最大值为9999");
        } else {
            ed_type_name.setText(text.getText().toString());
            ed_type_name.setSelection(text.getText().length());
        }
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
                int number = Integer.parseInt(ed_type_name.getText().toString());
                product.setNum(number);
                product.setPrice(number * product.getCost_price());
                text.setText(ed_type_name.getText().toString());
                Totalprice(groupPosition);
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
                if (s.length() > 0) {
                    int number = Integer.valueOf(s.toString());
                    if (number > 9999) {
                        ed_type_name.setText(9999 + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void Totalprice(int groupPosition) {
        double tPrice = 0;
        int tNumber = 0;
        List<Product> productList = this.goods.get(groupPosition).getProduct_list();
        for (Product product : productList) {
            tPrice += product.getPrice();
            tNumber += product.getNum();
        }
        if (mItemClickListener != null) {
            mItemClickListener.onInputClick(tPrice, tNumber);
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
        public TextView good_num;
        public TextView money;
        public LinearLayout zhankai;
        public ImageView shanchu;
        public ImageView zhankai_img;
        public TextView kaidan_isshow;


    }

    class ProductsViewHolder {
        public TextView name;
        public TextView jiage;
        public TextView shuliang;
        public ImageView jia;
        public ImageView jian;
        public LinearLayout lay_2;

    }

    public List<Goods> getGoods() {
        return goods;
    }

    public void setGoods(List<Goods> goods) {
        this.goods = goods;
    }

    public interface MyItemClickListener {
        void onInputClick(double tPrice, int tNumber);
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    public void setOnMoneyItemClickListener(MoneyClickLister listener) {
        this.lister = listener;
    }

    public interface MoneyClickLister {
        void changeData(double price, int num);

        void delete(int postion);
    }
}