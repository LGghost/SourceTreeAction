package cn.order.ordereasy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.utils.PinyinUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class CustomerListTwoAdapter extends BGAAdapterViewAdapter<Customer> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public CustomerListTwoAdapter(Context context) {
        super(context, R.layout.kehu_item_two);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Customer model) {
        String name=model.getName();
        if(!TextUtils.isEmpty(name)){
            name= String.valueOf(PinyinUtil.getFirstStr(name));
        }
        viewHolderHelper.setText(R.id.shouzimu,name);
        viewHolderHelper.setText(R.id.kehu_name, model.getName());
        viewHolderHelper.setText(R.id.kehu_phone_number,model.getTelephone());

        viewHolderHelper.setText(R.id.qianhuo_num ,String.valueOf(model.getOwe_sum()));
        if(model.getOwe_sum()==0){
            viewHolderHelper.setVisibility(R.id.lay_tip, View.GONE);
        }else{
            viewHolderHelper.setVisibility(R.id.lay_tip, View.VISIBLE);
        }
//        viewHolderHelper.setText(R.id.qiankuan_num,String.valueOf(model.getReceivable()));
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

    public boolean isCategory(int position) {
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
    }

}