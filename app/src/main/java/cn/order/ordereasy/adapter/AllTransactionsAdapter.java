package cn.order.ordereasy.adapter;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.StockListBean;

/**
 * Created by mrpan on 2017/9/10.
 */

public class AllTransactionsAdapter extends BGAAdapterViewAdapter<StockListBean> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public AllTransactionsAdapter(Context context) {
        super(context, R.layout.a_turnover_item);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {

    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, StockListBean model) {

    }

}