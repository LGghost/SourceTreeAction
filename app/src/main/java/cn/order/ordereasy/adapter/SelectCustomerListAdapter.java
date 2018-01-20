package cn.order.ordereasy.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SelectCustomer;

public class SelectCustomerListAdapter extends BGAAdapterViewAdapter<SelectCustomer> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public SelectCustomerListAdapter(Context context) {
        super(context, R.layout.select_customer_list_item);
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, final SelectCustomer model) {
        Log.e("fillData","position:"+position);
        final CheckBox item_checkbox = viewHolderHelper.getView(R.id.item_checkbox);
        LinearLayout item_layout = viewHolderHelper.getView(R.id.item_layout);
        if (isCategory(position)) {
            viewHolderHelper.setVisibility(R.id.tv_item_index_catalog, View.VISIBLE);
            viewHolderHelper.setText(R.id.tv_item_index_catalog, model.getCustomer().getTopic());
        } else {
            viewHolderHelper.setVisibility(R.id.tv_item_index_catalog, View.GONE);
        }
        viewHolderHelper.setText(R.id.tv_item_index_city, model.getCustomer().getName());
        viewHolderHelper.setText(R.id.kehu_phone_number, model.getCustomer().getTelephone());
        item_checkbox.setChecked( model.isSelect());
        item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSelect = item_checkbox.isChecked();
                item_checkbox.setChecked(!isSelect);
                model.setSelect(!isSelect);
            }
        });
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
        int category = getItem(position).getCustomer().getTopic().charAt(0);
        return position == getPositionForCategory(category);
    }

    public int getPositionForCategory(int category) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getItem(i).getCustomer().getTopic();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == category) {
                return i;
            }
        }
        return -1;
    }
}