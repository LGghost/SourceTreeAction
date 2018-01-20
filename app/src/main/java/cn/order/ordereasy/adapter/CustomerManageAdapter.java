package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.DiscountCustomer;

public class CustomerManageAdapter extends BGAAdapterViewAdapter<DiscountCustomer> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();
    private int index = -1;
    private String flag;
    public CustomerManageAdapter(Context context,String flag) {
        super(context, R.layout.customer_manage_adapter_item);
        this.flag = flag;
    }
    public void setIndex(int index){
        this.index = index;
    }
    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_swipe_root);
        swipeItemLayout.setDelegate(new BGASwipeItemLayout.BGASwipeItemLayoutDelegate() {
            @Override
            public void onBGASwipeItemLayoutOpened(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onBGASwipeItemLayoutClosed(BGASwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onBGASwipeItemLayoutStartOpen(BGASwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_swipe_delete);
    }

    @Override
    protected void fillData(BGAViewHolderHelper helper, int position, DiscountCustomer model) {
        helper.setText(R.id.item_name, model.getRank_name());
        ImageView imageView = helper.getImageView(R.id.item_image);
        if (flag.equals("newCustomer")) {
            if (index == model.getRank_id()) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_task_status_list_check);
            }else {
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setImageResource(R.drawable.youjiantou);
        }

        DecimalFormat df = new DecimalFormat("0.0");
        String result = df.format((double) model.getRank_discount() / 10);
        if (Double.parseDouble(result) == 10) {
            helper.setText(R.id.item_discount, "无折扣");
        } else {
            helper.setText(R.id.item_discount, result + "折");
        }
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
