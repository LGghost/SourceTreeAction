package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class GoodListAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */

    public GoodListAdapter(Context context) {
        super(context, R.layout.goods_shelves_item);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {

        viewHolderHelper.setItemChildClickListener(R.id.good_tuiguang);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Goods model) {
       /* viewHolderHelper.setText(R.id.tv_item_swipe_title,
                model.get("title").toString());
        //String title = model.get("title").toString();
        viewHolderHelper.getView(R.id.tv_item_swipe_title).setFocusable(false);*/
        if(model!=null){
            viewHolderHelper.setText(R.id.good_name,model.getTitle());//商品名称
            viewHolderHelper.setText(R.id.good_no,model.getGoods_no());
            viewHolderHelper.setText(R.id.good_kucun,model.getStore_num()+"");//库存
            viewHolderHelper.setText(R.id.good_xiaoliang,model.getSale_num()+"");//销量
            viewHolderHelper.setText(R.id.good_date, TimeUtil.getTimeStamp2Str(Long.parseLong(model.getCreate_time()), "yyyy-MM-dd"));//销量

            TextView highText = viewHolderHelper.getTextView(R.id.good_price_2);
            TextView space = viewHolderHelper.getTextView(R.id.good_space);
            TextView hide_text = viewHolderHelper.getTextView(R.id.hide_text);

            if (model.getMin_price() == model.getMax_price() ){
                viewHolderHelper.setText(R.id.good_price,model.getMin_price()+"");//最低价格
                highText.setVisibility(View.GONE);
                space.setVisibility(View.GONE);

            }else{
                highText.setVisibility(View.VISIBLE);
                space.setVisibility(View.VISIBLE);
                viewHolderHelper.setText(R.id.good_price,model.getMin_price()+"");//最低价格
                viewHolderHelper.setText(R.id. good_price_2,model.getMax_price()+"");//最高价格
            }
            if(model.getIs_hidden_price() == 0){
                hide_text.setVisibility(View.GONE);
            }else{
                hide_text.setVisibility(View.VISIBLE);
            }
            ImageView imageView=viewHolderHelper.getImageView(R.id.good_img);
            ImageLoader.getInstance().displayImage(Config.URL_HTTP+"/"+model.getCover(),imageView);
            if (model.getStatus() != 1) {
                viewHolderHelper.setVisibility(R.id.buff, View.VISIBLE);
            } else {
                viewHolderHelper.setVisibility(R.id.buff, View.GONE);
            }
        }

    }

}