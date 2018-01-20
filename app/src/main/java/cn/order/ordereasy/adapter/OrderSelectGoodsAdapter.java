package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.ChineseInital;
import cn.order.ordereasy.utils.Config;

/**
 * Created by mrpan on 2017/9/10.
 */

public class OrderSelectGoodsAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public OrderSelectGoodsAdapter(Context context) {
        super(context, R.layout.search_goods_item);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_catalog);
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_city);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Goods model) {

        if (model != null) {
            viewHolderHelper.setText(R.id.goods_name, model.getGoods_no() + " (" + model.getTitle() + ")");
            int store_num = 0;
            for (Product p : model.getProduct_list()) {
                store_num += p.getStore_num();
            }
            viewHolderHelper.setText(R.id.goods_kucun, store_num + "");
            ImageView imageView = viewHolderHelper.getImageView(R.id.goods_image);
            TextView min_price = viewHolderHelper.getTextView(R.id.min_price);
            TextView max_price = viewHolderHelper.getTextView(R.id.max_price);
            TextView sousuo_space = viewHolderHelper.getTextView(R.id.sousuo_space);
            if (model.getMax_price() == model.getMin_price()) {
                min_price.setText(model.getMin_price() + "");
                max_price.setVisibility(View.GONE);
                sousuo_space.setVisibility(View.GONE);
            } else {
                max_price.setVisibility(View.VISIBLE);
                sousuo_space.setVisibility(View.VISIBLE);
                min_price.setText(model.getMin_price() + "");
                max_price.setText(model.getMax_price() + "");
            }

            ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + model.getCover(), imageView);
            if (model.getIsSelected() == 1) {
                viewHolderHelper.setVisibility(R.id.goods_isSelected, View.VISIBLE);
            } else {
                viewHolderHelper.setVisibility(R.id.goods_isSelected, View.GONE);
            }
            if (model.getStatus() != 1) {
                viewHolderHelper.setVisibility(R.id.buff, View.VISIBLE);
            } else {
                viewHolderHelper.setVisibility(R.id.buff, View.GONE);
            }
        }
    }


    public void closeOpenedSwipeItemLayout() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.close();
        }
        mOpenedSil.clear();
    }


}