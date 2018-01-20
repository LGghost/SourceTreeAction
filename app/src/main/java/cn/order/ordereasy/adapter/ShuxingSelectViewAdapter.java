package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

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

public class ShuxingSelectViewAdapter extends BGAAdapterViewAdapter<Map<String,Object>> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();

    public ShuxingSelectViewAdapter(Context context) {
        super(context, R.layout.item_select_shuxing);
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
        //viewHolderHelper.setItemChildClickListener(R.id.fenlei_edit);
        viewHolderHelper.setItemChildClickListener(R.id.tv_item_swipe_delete);
        viewHolderHelper.setItemChildLongClickListener(R.id.tv_item_swipe_delete);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, Map<String,Object> model) {
        viewHolderHelper.setText(R.id.tv_item_swipe_title,
                model.get("title").toString());
        //String title = model.get("title").toString();
        viewHolderHelper.getView(R.id.tv_item_swipe_title).setFocusable(false);
        CheckBox checkBox=viewHolderHelper.getView(R.id.ck_specs);
        BGASwipeItemLayout swipeItemLayout = viewHolderHelper.getView(R.id.sil_item_swipe_root);
        if(model.get("isChecked") != null) {
            if (model.get("isChecked").equals("1")) {
                checkBox.setChecked(true);
                swipeItemLayout.setSwipeAble(false);
            } else if (model.get("isChecked").equals("2")) {
                checkBox.setChecked(false);
                swipeItemLayout.setSwipeAble(false);
            } else {
                swipeItemLayout.setSwipeAble(false);
            }
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