package cn.order.ordereasy.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.app.MyApplication;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.FileUtils;
import cn.order.ordereasy.view.activity.BillingActivity;

import static cn.order.ordereasy.view.activity.BillingActivity.*;

/**
 * Created by mrpan on 2017/9/10.
 */

public class OrderSelectGoodsListAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();
    private Context context;

    private MoneyClickLister lister;
    private int index = 0;
    private int discount = 100;

    public OrderSelectGoodsListAdapter(Context context) {
        super(context, R.layout.kaidan_item);
        this.context = context;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnMoneyItemClickListener(MoneyClickLister listener) {
        this.lister = listener;
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.kaidan_shanchu);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, final int position, final Goods model) {
        viewHolderHelper.setText(R.id.kaidan_goodname, model.getGoods_no() + " (" + model.getTitle() + ")");
        final LinearLayout gon_layout = viewHolderHelper.getView(R.id.gon_layout);

        LinearLayout lay_1 = viewHolderHelper.getView(R.id.lay_1);
        final TextView kaidan_isshow = viewHolderHelper.getTextView(R.id.kaidan_isshow);
        final ImageView zhankai_img = viewHolderHelper.getImageView(R.id.zhankai_img);
        final TextView mNumber = viewHolderHelper.getTextView(R.id.kaidan_goodnum);
        mNumber.setText(model.getNum() + "");
        final TextView mPrice = viewHolderHelper.getTextView(R.id.kaidan_money);
        mPrice.setText(FileUtils.getMathNumber(model.getPrice()));
        ImageView imageView = viewHolderHelper.getImageView(R.id.kaidan_goodimg);
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + model.getCover(), imageView);
        final List<Product> products = model.getProduct_list();
        final ListView listView = viewHolderHelper.getView(R.id.kaidan_specs);
        final GoodsSpecsAdapter adapter = new GoodsSpecsAdapter(context, products);
        adapter.setDiscount(discount);
        listView.setAdapter(adapter);
        lay_1.setTag(position);
        if (position == index) {
            gon_layout.setVisibility(View.VISIBLE);
            kaidan_isshow.setText("收起");
            zhankai_img.setBackgroundResource(R.drawable.icon_down);
        } else {
            gon_layout.setVisibility(View.GONE);
            kaidan_isshow.setText("展开");
            zhankai_img.setBackgroundResource(R.drawable.icon_up_blue);
        }

        adapter.setOnItemClickListener(new GoodsSpecsAdapter.MyItemClickListener() {

            @Override
            public void onInputClick(double tPrice, int tNumber) {
                model.setNum(tNumber);
                model.setPrice(tPrice);
                mNumber.setText(tNumber + "");
                mPrice.setText(FileUtils.getMathNumber(tPrice));
                changeData();
            }

        });
        lay_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kaidan_isshow.getText().equals("收起")) {
                    gon_layout.setVisibility(View.GONE);
                    kaidan_isshow.setText("展开");
                    zhankai_img.setBackgroundResource(R.drawable.icon_up_blue);
                } else {
                    gon_layout.setVisibility(View.VISIBLE);
                    kaidan_isshow.setText("收起");
                    zhankai_img.setBackgroundResource(R.drawable.icon_down);
                }
            }
        });
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();
    }

    public void changeData() {
        double p = 0;
        int n = 0;
        for (Goods good : getData()) {
            p += good.getPrice();
            n += good.getNum();
        }
        if (lister != null) {
            lister.changeData(p, n);
        }
    }

    public void closeOpenedSwipeItemLayout() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.close();
        }
        mOpenedSil.clear();
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

    public interface MoneyClickLister {
        void changeData(double price, int num);
    }

}