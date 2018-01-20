package cn.order.ordereasy.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.PinyinUtil;

/**
 * Created by Administrator on 2017/9/12.
 */

public class DetailCustomersAdapter extends BaseAdapter {

    Context context;
    private List<Customer> data=null;

    private MyItemClickListener mItemClickListener;

    public DetailCustomersAdapter(Context context, List<Customer> data){
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
                    R.layout.goumai_kehu_item, parent, false);
            viewHolder.name=(TextView)convertView.findViewById(R.id.customer_name);
            viewHolder.num=(TextView)convertView.findViewById(R.id.customer_buynum);
            viewHolder.kehu_shou_zimu=(TextView)convertView.findViewById(R.id.kehu_shou_zimu);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Customer data =this.data.get(position);
        String name=data.getCustomer_name();
        if(!TextUtils.isEmpty(name)){
            name= String.valueOf(PinyinUtil.getFirstStr(name));
        }
        viewHolder.kehu_shou_zimu.setText(name);

        viewHolder.name.setText(data.getCustomer_name());
        viewHolder.num.setText(data.getSale_num()+"");

        return convertView;
    }

    class ViewHolder {

        public TextView name;
        public TextView num;
        public TextView kehu_shou_zimu;

    }

    public interface MyItemClickListener {
        public void onItemClick(View view, int postion);
    }

    public List<Customer> getData() {
        return data;
    }

    public void setData(List<Customer> data) {
        this.data = data;
    }

}
