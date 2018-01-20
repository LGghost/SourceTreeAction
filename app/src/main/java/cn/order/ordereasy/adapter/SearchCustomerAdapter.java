package cn.order.ordereasy.adapter;

import android.content.Context;
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
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.PinyinUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class SearchCustomerAdapter extends BGAAdapterViewAdapter<Customer> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public SearchCustomerAdapter(Context context) {
        super(context, R.layout.kehu_item_one);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_catalog);
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_city);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Customer model) {

        String name = model.getName();
        if (!TextUtils.isEmpty(name)) {
            name = String.valueOf(PinyinUtil.getFirstStr(name));
        }
        viewHolderHelper.setText(R.id.shouzimu, name);
        viewHolderHelper.setText(R.id.kehu_name, model.getName());
        viewHolderHelper.setText(R.id.kehu_phone_number, model.getTelephone());
        viewHolderHelper.setText(R.id.qiankuan_num, String.valueOf(model.getReceivable()));
        if (model.getReceivable() == 0) {
            viewHolderHelper.setVisibility(R.id.lay_tip, View.GONE);
        } else {
            viewHolderHelper.setVisibility(R.id.lay_tip, View.VISIBLE);
        }
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }


}