package cn.order.ordereasy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.ContactInfo;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.utils.PinyinUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class TelListAdapter extends BGAAdapterViewAdapter<ContactInfo> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public TelListAdapter(Context context) {
        super(context, R.layout.item_index_tel);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        // viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_catalog);
        //viewHolderHelper.setItemChildClickListener(R.id.tv_item_index_city);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, ContactInfo model) {
        if (isCategory(position)) {
            viewHolderHelper.setVisibility(R.id.tv_item_index_catalog, View.VISIBLE);
            viewHolderHelper.setText(R.id.tv_item_index_catalog, model.getTopic());
        } else {
            viewHolderHelper.setVisibility(R.id.tv_item_index_catalog, View.GONE);
        }
        int isCheck = model.getIsCheck();
        if (isCheck == 1) {
            viewHolderHelper.setChecked(R.id.ck_tel, true);
        } else {
            viewHolderHelper.setChecked(R.id.ck_tel, false);
        }
        viewHolderHelper.setText(R.id.tv_tel_name, model.getName());
        viewHolderHelper.setText(R.id.tv_tel_number, model.getNumber());
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    public void closeOpenedSwipeItemLayout() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.close();
        }
        mOpenedSil.clear();
    }

    /*public boolean isCategory(int position) {
        String name=getItem(position).getName();
        if(TextUtils.isEmpty(name)){
            name="无";
        }
        int category =name.charAt(0);
        return position == getPositionForCategory(category);
    }

    public int getPositionForCategory(int category) {

        for (int i = 0; i < getCount(); i++) {
            String name=getItem(i).getName();
            if(TextUtils.isEmpty(name)){
                name="无";
            }
            String sortStr = name;
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == category) {
                return i;
            }
        }
        return -1;
    }*/
    public boolean isCategory(int position) {
        int category = getItem(position).getTopic().charAt(0);
        return position == getPositionForCategory(category);
    }

    public int getPositionForCategory(int category) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getData().get(i).getTopic();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == category) {
                return i;
            }
        }
        return -1;
    }
}