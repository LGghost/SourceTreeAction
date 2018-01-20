package cn.order.ordereasy.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.Config;

public class ShangHuoAdapter extends
        RecyclerView.Adapter<ShangHuoAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> mDatas = new ArrayList<String>();
    private ArrayList<String> mLocal = new ArrayList<>();
    private boolean isLocal;
    private List<Map<String, Object>> images;
    public ShangHuoAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        ImageView mDelete;
    }

    public void setData(ArrayList<String> datats, boolean isLocal) {
        this.isLocal = isLocal;
        this.mDatas.addAll(datats);
        if (isLocal){
            mLocal.removeAll(datats);
            mLocal.addAll(datats);
        }
        notifyDataSetChanged();
    }
    public void setImages(List<Map<String, Object>> images){
        this.images = images;
    }
    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public ArrayList<String> getmData() {
        return mDatas;
    }

    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {

        View view = mInflater.inflate(R.layout.shang_huo_item,
                viewGroup, false);
        final  ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.shang_huo_item_image);
        viewHolder.mDelete = (ImageView) view
                .findViewById(R.id.shang_huo_item_delete);

        return viewHolder;
    }

    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        if (mLocal.contains(mDatas.get(i))){
            ImageLoader.getInstance().displayImage("file://" + mDatas.get(i), viewHolder.mImg);
        }else{
            ImageLoader.getInstance().displayImage(mDatas.get(i), viewHolder.mImg);
        }
        //如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }
        viewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ShangHuoAdapter","item:"+ i);
                mDatas.remove(i);
                if (!isLocal){
                    images.remove(i);
                }
                notifyDataSetChanged();
            }
        });
    }

    /**
     * ItemClick的回调接口
     */
    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

}
