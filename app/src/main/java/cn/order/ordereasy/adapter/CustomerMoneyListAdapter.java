package cn.order.ordereasy.adapter;

import android.content.Context;
import android.widget.TextView;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Money;
import cn.order.ordereasy.utils.TimeUtil;

/**
 * Created by mrpan on 2017/9/10.
 */

public class CustomerMoneyListAdapter extends BGAAdapterViewAdapter<Money> {


    /**
     * 当前处于打开状态的item
     */

    public CustomerMoneyListAdapter(Context context) {
        super(context, R.layout.customer_homepage_item_two);
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {

    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Money model) {
        TextView kehu_name = viewHolderHelper.getTextView(R.id.kehu_name);
        TextView money_num = viewHolderHelper.getTextView(R.id.money_num);
        if (model.getPayment_type() == 1) {
            kehu_name.setText(model.getCustomer_name() + "-付款");
            money_num.setText("+ " + String.valueOf(model.getMoney()) + "元");
            money_num.setTextColor(mContext.getResources().getColor(R.color.lvse));
        } else {
            kehu_name.setText("退款给" + model.getCustomer_name());
            money_num.setText("- " + String.valueOf(model.getMoney()) + "元");
            money_num.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        viewHolderHelper.setText(R.id.data_time, TimeUtil.getTimeStamp2Str(Long.parseLong(model.getCreate_time()), "yyyy-MM-dd HH:mm:ss"));

        switch (model.getPayment_way()) {
            case 1:
                viewHolderHelper.setText(R.id.zhifu_type, "现金");
                break;
            case 2:
                viewHolderHelper.setText(R.id.zhifu_type, "支付宝");
                break;
            case 3:
                viewHolderHelper.setText(R.id.zhifu_type, "微信");
                break;
            case 4:
                viewHolderHelper.setText(R.id.zhifu_type, "银行卡");
                break;
            default:
                viewHolderHelper.setText(R.id.zhifu_type, "其他");
                break;

        }

    }
}