package cn.order.ordereasy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hp.hpl.sparta.Text;
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

public class StockAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();
    private Context context;
    private int sign;
    private MyItemClickListener mItemClickListener;
    private int index = 0;
    private StockChildAdapter adapter;
    private CheckChildAdapter checkAdapter;
    private AlertDialog alertDialog;

    public StockAdapter(Context context, int sign) {
        super(context, R.layout.ruku_item_one);
        this.context = context;
        this.sign = sign;
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, final int position, final Goods model) {
        helper.setText(R.id.good_name, model.getGoods_no() + " (" + model.getTitle() + ")");
        final TextView pandian_num = helper.getTextView(R.id.pandian_num);
        final LinearLayout gon_layout = helper.getView(R.id.gon_layout);
        LinearLayout lay_2 = helper.getView(R.id.lay_2);
        LinearLayout lay_3 = helper.getView(R.id.lay_3);
        ImageView shanchu = helper.getImageView(R.id.shanchu);
        LinearLayout lay_onclick = helper.getView(R.id.lay_onclick);
        final TextView expanded_text = helper.getTextView(R.id.expanded_text);
        final ImageView expanded_menu = helper.getImageView(R.id.expanded_menu);
        TextView ruku_text = helper.getTextView(R.id.ruku_text);
        pandian_num.setText(String.valueOf(model.getNum()));
        if (sign == 0) {
            lay_2.setVisibility(View.VISIBLE);
            lay_3.setVisibility(View.GONE);
            ruku_text.setText("入库数：");
        } else if (sign == 1) {
            lay_2.setVisibility(View.VISIBLE);
            lay_3.setVisibility(View.GONE);
            ruku_text.setText("出库数：");
        } else if (sign == 2) {
            lay_2.setVisibility(View.VISIBLE);
            lay_3.setVisibility(View.GONE);
            ruku_text.setText("调整数：");
        } else {
            lay_2.setVisibility(View.GONE);
            lay_3.setVisibility(View.VISIBLE);
            ruku_text.setText("盘点数：");
        }
        ImageView imageView = helper.getImageView(R.id.good_image);
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + model.getCover(), imageView);
        final List<Product> products = model.getProduct_list();
        final ListView listView = helper.getView(R.id.kaidan_specs);
        if (sign != 3) {
            adapter = new StockChildAdapter(context, products, sign);
            listView.setAdapter(adapter);
            adapter.setOnItemClickListener(new StockChildAdapter.onItemClickListener() {
                @Override
                public void onInputClick(int tNumber) {
                    model.setNum(tNumber);
                    pandian_num.setText(tNumber + "");
                    Totalnumber();
                }
            });
        } else {
            checkAdapter = new CheckChildAdapter(context, products);
            listView.setAdapter(checkAdapter);
            Totalprice(products, pandian_num, model);
            checkAdapter.setOnItemClickListener(new CheckChildAdapter.onItemClickListener() {

                @Override
                public void onInputClick(int tNumber) {
                    model.setNum(tNumber);
                    pandian_num.setText(tNumber + "");
                    Totalnumber();
                }
            });
        }
        lay_onclick.setTag(position);
        if (position == index) {
            gon_layout.setVisibility(View.VISIBLE);
            expanded_text.setText("收起");
            expanded_menu.setBackgroundResource(R.drawable.icon_down);
        } else {
            gon_layout.setVisibility(View.GONE);
            expanded_text.setText("展开");
            expanded_menu.setBackgroundResource(R.drawable.icon_up_blue);
        }


        shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogs(position);
            }
        });
        lay_onclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expanded_text.getText().equals("收起")) {
                    gon_layout.setVisibility(View.GONE);
                    expanded_text.setText("展开");
                    expanded_menu.setBackgroundResource(R.drawable.icon_up_blue);
                } else {
                    gon_layout.setVisibility(View.VISIBLE);
                    expanded_text.setText("收起");
                    expanded_menu.setBackgroundResource(R.drawable.icon_down);
                }
            }
        });
        setListViewHeightBasedOnChildren(listView);
        if (sign != 3) {
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } else {
            if (checkAdapter != null) {
                checkAdapter.notifyDataSetChanged();
            }
        }

    }

    public void Totalprice(List<Product> products, TextView v, Goods good) {
        int tNumber = 0;
        for (Product product : products) {
            tNumber += product.getOperate_num();
        }
        good.setNum(tNumber);
        v.setText(tNumber + "");
        Totalnumber();
    }

    public void closeOpenedSwipeItemLayout() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.close();
        }
        mOpenedSil.clear();
    }

    public void Totalnumber() {
        int n = 0;
        for (Goods good : getData()) {
            n += good.getNum();
        }
        if (mItemClickListener != null) {
            mItemClickListener.changeData(n, getData().size());
        }

    }

    public interface MyItemClickListener {
        void changeData(int number, int typeBumber);
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    private void showdialogs(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.tanchuang_view_textview, null);
        //标题
        final TextView title_name = (TextView) view.findViewById(R.id.title_name);
        final TextView text_conten = (TextView) view.findViewById(R.id.text_conten);
        final TextView quxiao = (TextView) view.findViewById(R.id.quxiao);
        final TextView queren = (TextView) view.findViewById(R.id.queren);
        builder.setView(view);
        alertDialog = builder.create();
        title_name.setVisibility(View.GONE);
        text_conten.setText("确认要删除货品吗？");
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        //按钮2确认点击事件
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
                Totalnumber();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
