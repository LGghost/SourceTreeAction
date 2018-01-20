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
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;

/**
 * Created by mrpan on 2017/9/10.
 */

public class StockSearchGoodsAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public StockSearchGoodsAdapter(Context context) {
        super(context, R.layout.stock_guanli_item);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_catalog);
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_city);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Goods model) {

        if(model!=null){
            viewHolderHelper.setText(R.id.good_name, model.getTitle());
            int store_num=0,owe_num=0;
            for(Product p:model.getProduct_list()){
                store_num += p.getStore_num();
                owe_num+=p.getOwe_num();
            }
            viewHolderHelper.setText(R.id.good_qianhuo,store_num+"");
            viewHolderHelper.setText(R.id.good_fahuo,owe_num+"");

            ImageView imageView=viewHolderHelper.getImageView(R.id.good_image);
            ImageLoader.getInstance().displayImage(Config.URL_HTTP+"/"+model.getCover(),imageView);
        }
    }


    public void closeOpenedSwipeItemLayout() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.close();
        }
        mOpenedSil.clear();
    }


}