package cn.order.ordereasy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.OrderList;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.PinyinUtil;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class CustomerOrderListAdapter extends BGAAdapterViewAdapter<OrderList> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    private DetailCustomersAdapter.MyItemClickListener mItemClickListener;
    private Context context;

    public CustomerOrderListAdapter(Context context) {
        super(context, R.layout.customer_homepage_item_one);
        this.context = context;
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.addr_edit);
        viewHolderHelper.setItemChildClickListener(R.id.addr_edit);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, OrderList model) {
        viewHolderHelper.setText(R.id.order_code, model.getOrder_no());
        String name = model.getCustomer_name();
        if (!TextUtils.isEmpty(name)) {
            name = String.valueOf(PinyinUtil.getFirstStr(name));
        } else if (name == null) {
            viewHolderHelper.setText(R.id.name_shou_zimu, "订");
        }
        viewHolderHelper.setText(R.id.name_shou_zimu, name);
        viewHolderHelper.setText(R.id.kehu_name, model.getCustomer_name());
        viewHolderHelper.setText(R.id.data_time, TimeUtil.getTimeStamp2Str(Long.parseLong(model.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));
        viewHolderHelper.setText(R.id.kaidan_num, String.valueOf(model.getOrder_num()));

        viewHolderHelper.setText(R.id.order_money, String.valueOf(model.getPayable()));
        TextView qianhuo = viewHolderHelper.getTextView(R.id.qiankuan_money);
        TextView order_amount = viewHolderHelper.getTextView(R.id.order_amount);
        order_amount.setText(context.getString(R.string.order_amount));
        //更换右上角图标
        switch (model.getOrder_type()) {
            case 1:
                if (model.getIs_wechat() == 1) {
                    viewHolderHelper.setImageResource(R.id.type_image, R.drawable.img_weixin);
                } else {
                    viewHolderHelper.setImageResource(R.id.type_image, R.drawable.img_dingdan);
                }
                viewHolderHelper.setVisibility(R.id.kaidan_layout, View.VISIBLE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money1, View.GONE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money, View.VISIBLE);
                if (model.getIs_wechat() == 1) {
                    qianhuo.setTextColor(mContext.getResources().getColor(R.color.shouye_hongse));
                    if (model.getOrder_status() == 1) {
                        qianhuo.setText("待审核");
                    } else {
                        if (model.getOwe_num() <= 0) {
                            qianhuo.setTextColor(mContext.getResources().getColor(R.color.touzi_huise));
                            qianhuo.setText("已完成");
                        } else {
                            qianhuo.setTextColor(mContext.getResources().getColor(R.color.shouye_hongse));
                            qianhuo.setText("欠货：" + String.valueOf(model.getOwe_num()));
                        }
                    }
                } else {
                    if (model.getOwe_num() <= 0) {
                        qianhuo.setTextColor(mContext.getResources().getColor(R.color.touzi_huise));
                        qianhuo.setText("已完成");
                    } else {
                        qianhuo.setTextColor(mContext.getResources().getColor(R.color.shouye_hongse));
                        qianhuo.setText("欠货：" + String.valueOf(model.getOwe_num()));
                    }
                }


                if (model.getReturn_num() == 0) {
                    viewHolderHelper.setVisibility(R.id.tuiqianhuo_text, View.GONE);
                } else {
                    viewHolderHelper.setVisibility(R.id.tuiqianhuo_text, View.VISIBLE);
                    viewHolderHelper.setText(R.id.tuiqianhuo_text, "退欠货：" + String.valueOf(model.getReturn_num()));
                }
                break;
            case 2:
                viewHolderHelper.setImageResource(R.id.type_image, R.drawable.img_tuidan_sign);
                viewHolderHelper.setVisibility(R.id.kaidan_layout, View.GONE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money, View.GONE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money1, View.VISIBLE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money1, View.VISIBLE);
                viewHolderHelper.setText(R.id.qiankuan_money1, "退货：" + String.valueOf(model.getOrder_num()));
                order_amount.setText(context.getString(R.string.order_refunds));
                break;
            case 3:
                viewHolderHelper.setImageResource(R.id.type_image, R.drawable.img_change_sign);
                viewHolderHelper.setVisibility(R.id.kaidan_layout, View.GONE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money, View.GONE);
                viewHolderHelper.setVisibility(R.id.qiankuan_money1, View.VISIBLE);

                viewHolderHelper.setVisibility(R.id.qiankuan_money1, View.VISIBLE);
                viewHolderHelper.setText(R.id.qiankuan_money1, "退欠货：" + String.valueOf(model.getOrder_num()));
                break;
        }
        if (model.getIs_close() == 1) {
            viewHolderHelper.setVisibility(R.id.order_no_type_image, View.VISIBLE);
            viewHolderHelper.setVisibility(R.id.kaidan_layout, View.VISIBLE);
            viewHolderHelper.setVisibility(R.id.qiankuan_money, View.GONE);
            viewHolderHelper.setVisibility(R.id.qiankuan_money1, View.GONE);
            viewHolderHelper.setVisibility(R.id.tuiqianhuo_text, View.GONE);
        } else {
            viewHolderHelper.setVisibility(R.id.order_no_type_image, View.GONE);
        }

    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(DetailCustomersAdapter.MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

}