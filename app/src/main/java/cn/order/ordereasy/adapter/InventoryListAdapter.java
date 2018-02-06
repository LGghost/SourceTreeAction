package cn.order.ordereasy.adapter;

import android.content.Context;
import android.opengl.Visibility;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Inventory;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class InventoryListAdapter extends BGAAdapterViewAdapter<Inventory> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public InventoryListAdapter(Context context) {
        super(context, R.layout.stock_inventory_item);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.addr_edit);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Inventory model) {
        viewHolderHelper.setText(R.id.pandian_data, TimeUtil.getTimeStamp2Str(Long.parseLong(model.getCreate_time()),"yyyy-MM-dd"));
        viewHolderHelper.setText(R.id.pandian_name, model.getUser_name());

        if(model.getIs_boss()==1){
            viewHolderHelper.setVisibility(R.id.pandian_zhiwei, View.VISIBLE);
        }else{
            viewHolderHelper.setVisibility(R.id.pandian_zhiwei,View.GONE);
        }
        if(model.getIs_complete()==1){
            viewHolderHelper.setText(R.id.pandian_status,"已完成");
        }else{
            viewHolderHelper.setText(R.id.pandian_status,"盘点中");
        }
        ImageView imageView=viewHolderHelper.getImageView(R.id.pandian_image);
        if(!TextUtils.isEmpty(model.getAvatar())) {
            ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + model.getAvatar(), imageView);
        }else{
            imageView.setImageResource(R.drawable.bg_user);
        }

    }

}