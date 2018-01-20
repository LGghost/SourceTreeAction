package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;

/**
 * Created by mrpan on 2017/9/10.
 */

public class PastDailyAdapter extends BGAAdapterViewAdapter<String> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public PastDailyAdapter(Context context) {
        super(context, R.layout.past_daily_item);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, String model) {
        viewHolderHelper.setText(R.id.data,
                model);
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

}