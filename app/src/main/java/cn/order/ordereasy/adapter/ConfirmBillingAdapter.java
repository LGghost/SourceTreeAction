package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
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

public class ConfirmBillingAdapter extends BGAAdapterViewAdapter<Goods> {
    /**
     * 当前处于打开状态的item
     */
    private List<BGASwipeItemLayout> mOpenedSil = new ArrayList<>();
    private Context context;
    private  int type;

    public ConfirmBillingAdapter(Context context,int type) {
        super(context, R.layout.confirm_billing_item);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void setItemChildListener(BGAViewHolderHelper viewHolderHelper) {
//        viewHolderHelper.setItemChildClickListener(R.id.kaidan_shanchu);
    }

    @Override
    public void fillData(BGAViewHolderHelper viewHolderHelper, int position, final Goods model) {

        viewHolderHelper.setText(R.id.kaidan_goodname, model.getGoods_no() + " (" + model.getTitle() + ")");
        viewHolderHelper.setText(R.id.kaidan_goodnum, model.getNum() + "");
        DecimalFormat df = new DecimalFormat("0.00");
        viewHolderHelper.setText(R.id.kaidan_money, df.format(model.getPrice()) + "");
        ImageView imageView = viewHolderHelper.getImageView(R.id.kaidan_goodimg);
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + model.getCover(), imageView);
        final List<Product> products = model.getProduct_list();
        final ListView listView = viewHolderHelper.getView(R.id.kaidan_specs);
        final ConfirmBillingGoodsItemAdapter adapter = new ConfirmBillingGoodsItemAdapter(context, products,type);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();
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