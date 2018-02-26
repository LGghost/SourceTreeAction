package cn.order.ordereasy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Fahuo;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class CustomerThingsListAdapter extends BGAAdapterViewAdapter<Fahuo> {

    /**
     * 当前处于打开状态的item
     */
    private Context context;

    public CustomerThingsListAdapter(Context context) {
        super(context, R.layout.customer_homepage_item_three);
        this.context = context;
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Fahuo model) {
        viewHolderHelper.setText(R.id.kehu_name, model.getUser_name());
        TextView fahuo = viewHolderHelper.getTextView(R.id.fahuo);
        String type = "其他";
        int number = 0;
        switch (model.getOperate_type()) {
            case 1:
                type = "入库";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.forestgreen));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getIn_number();
                break;
            case 2:
                type = "出库";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.dimgrey));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getOut_number();
                break;
            case 3:
                type = "调整";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.dodgerblue));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getIn_number();
                break;
            case 4:
                type = "发货";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.hongse));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getOut_number();
                break;
            case 5:
                type = "退货";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.dimgrey));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getIn_number();
                break;
            case 6:
                type = "调整";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.dodgerblue));
                fahuo.setVisibility(View.VISIBLE);
                break;
            case 7:
                type = "采购入库";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.forestgreen));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getOut_number();
                break;
            case 8:
                type = "采购退货";
                viewHolderHelper.setTextColor(R.id.fahuo, context.getResources().getColor(R.color.dimgrey));
                fahuo.setVisibility(View.VISIBLE);
                number = model.getIn_number();
                break;
        }
        fahuo.setText(type);
        if (model.getOperate_type() == 3 || model.getOperate_type() == 6) {
            if (model.getIn_number() == 0) {
                viewHolderHelper.setText(R.id.goods_num, "-" + model.getOut_number());
            } else if (model.getOut_number() == 0) {
                viewHolderHelper.setText(R.id.goods_num, "+" + model.getIn_number());
            } else {
                viewHolderHelper.setText(R.id.goods_num, "+" + model.getIn_number() + "(-" + model.getOut_number() + ")");
            }

        } else {
            viewHolderHelper.setText(R.id.goods_num, String.valueOf(number));
        }
        viewHolderHelper.setText(R.id.data_time, TimeUtil.getTimeStamp2Str(Long.parseLong(model.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
    }

}