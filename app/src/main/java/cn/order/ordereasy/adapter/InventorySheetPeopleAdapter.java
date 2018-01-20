package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.Map;

import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.Config;

/**
 * Created by Administrator on 2017/9/12.
 */

public class InventorySheetPeopleAdapter extends BaseAdapter {

    Context context;
    private List<Map<String,Object>> data=null;

    private MyItemClickListener mItemClickListener;

    public InventorySheetPeopleAdapter(Context context, List<Map<String, Object>> data){
        this.context= context;
        this.data= data;
    }
    /**
     * 设置Item点击监听、、、、
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener){
        this.mItemClickListener = listener;
    }



    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.inventory_sheet_item, parent, false);
            viewHolder.huop_img= (ImageView) convertView.findViewById(R.id.huop_img);
            viewHolder.name=(TextView)convertView.findViewById(R.id.name);
            viewHolder.zhiwei=(TextView)convertView.findViewById(R.id.zhiwei);
            viewHolder.huopin_num=(TextView)convertView.findViewById(R.id.huopin_num);
            viewHolder.pandian_num=(TextView)convertView.findViewById(R.id.pandian_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map<String,Object> map =this.data.get(position);
        ImageLoader.getInstance().displayImage(Config.URL_HTTP+"/"+map.get("avatar").toString(),viewHolder.huop_img);
        viewHolder.huopin_num.setText(map.get("goods_num").toString());
        viewHolder.pandian_num.setText(map.get("operate_num").toString());
        viewHolder.name.setText(map.get("user_name").toString());
        if(map.get("is_boss").toString().equals("1")){
            viewHolder.zhiwei.setText("老板");
        }else{
            viewHolder.zhiwei.setText("员工");
        }

        return convertView;
    }

    class ViewHolder {
        public ImageView huop_img;
        public TextView name;
        public TextView zhiwei;
        public TextView huopin_num;
        public TextView pandian_num;

    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
}
