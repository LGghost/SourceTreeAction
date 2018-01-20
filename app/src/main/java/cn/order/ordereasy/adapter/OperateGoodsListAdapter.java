package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.androidcommon.adapter.BGAAdapterViewAdapter;
import cn.bingoogolapple.androidcommon.adapter.BGAViewHolderHelper;
import cn.bingoogolapple.swipeitemlayout.BGASwipeItemLayout;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;

/**
 * Created by mrpan on 2017/9/10.
 */

public class OperateGoodsListAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();
    private Context context;
    private int type;

    public OperateGoodsListAdapter(Context context, int type) {
        super(context, R.layout.operation_record_item);
        this.context = context;
        this.type = type;
    }


    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
        viewHolderHelper.setItemChildClickListener(R.id.kaidan_shanchu);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, final Goods model) {

        viewHolderHelper.setText(R.id.huopin_name, model.getGoods_no() + " (" + model.getTitle() + ")");
        if (type == 1) {
            viewHolderHelper.setText(R.id.huopin_num, "本次入库数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "入库数量");
        } else if (type == 2) {
            viewHolderHelper.setText(R.id.huopin_num, "本次出库数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "出库数量");
        } else if (type == 3) {
            viewHolderHelper.setText(R.id.huopin_num, "本次调整数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "调整数量");
        } else if (type == 4) {
            viewHolderHelper.setText(R.id.huopin_num, "本次发货数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "发货数量");
        } else if (type == 5) {
            viewHolderHelper.setText(R.id.huopin_num, "本次退货数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "退货数量");
        } else if (type == 6) {
            if (model.getStore_num() == 0) {
                viewHolderHelper.setText(R.id.huopin_num, "本次调整数量：" + model.getNum());
            } else {
                viewHolderHelper.setText(R.id.huopin_num, "本次调整数量：" + "+" + model.getNum() + "(-" + model.getStore_num() + ")");
            }
            viewHolderHelper.setText(R.id.huopin_type, "调整数量");
        } else if (type == 7) {
            viewHolderHelper.setText(R.id.huopin_num, "本次采购入库数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "采购入库数量");
        } else if (type == 8) {
            viewHolderHelper.setText(R.id.huopin_num, "本次采购退货数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "采购退货数量");
        } else {
            viewHolderHelper.setText(R.id.huopin_num, "本次操作数量：" + model.getNum());
            viewHolderHelper.setText(R.id.huopin_type, "操作数量");
        }

        ImageView imageView = viewHolderHelper.getImageView(R.id.img_view);
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + model.getCover(), imageView);
        List<Product> products = model.getProduct_list();
        ListView listView = viewHolderHelper.getView(R.id.listviewz);
        OperateGoodsSpecsAdapter adapter = new OperateGoodsSpecsAdapter(context, products);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
    }


    public void closeOpenedSwipeItemLayout() {
        for (BGASwipeItemLayout sil : mOpenedSil) {
            sil.close();
        }
        mOpenedSil.clear();
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

}