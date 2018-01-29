package cn.order.ordereasy.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.SupplierBean;
import cn.order.ordereasy.bean.SupplierIndex;
import cn.order.ordereasy.view.activity.SupplierDetailsActivity;
import cn.order.ordereasy.view.activity.SupplierManagementActivity;
import cn.order.ordereasy.view.activity.SupplierPaymentActivity;
import cn.order.ordereasy.widget.PinnedSectionListView;

//这里实现了自定义Listview中的PinnedSectionListAdapter接口，实现悬浮功能
public class SupplierAdapter extends BaseAdapter implements
        PinnedSectionListView.PinnedSectionListAdapter {
    //子Item标识
    public static final int ITEM = 0;
    //分组Item标识
    public static final int SECTION = 1;

    private LayoutInflater inflater;
    private List<SupplierIndex> phoneBookIndexs = new ArrayList<SupplierIndex>();
    private Context context;

    public SupplierAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<SupplierIndex> phoneBookIndexs) {
        this.phoneBookIndexs = phoneBookIndexs;
    }

    public void addDataToList(List<SupplierIndex> phoneBookIndexs) {
        this.phoneBookIndexs.addAll(phoneBookIndexs);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return phoneBookIndexs.size();
    }

    public SupplierIndex getItem(int position) {
        return phoneBookIndexs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SupplierIndex phoneBookIndex = phoneBookIndexs.get(position);
        if (phoneBookIndex.getType() == SECTION) {
            IndexViewHolder indexViewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.index_section_layout, null);
                indexViewHolder = new IndexViewHolder();
                indexViewHolder.index = (TextView) convertView.findViewById(R.id.label_text);
                convertView.setTag(indexViewHolder);
            } else {
                indexViewHolder = (IndexViewHolder) convertView.getTag();
            }
            indexViewHolder.index.setText(phoneBookIndex.getIndex());
        } else {
            PhoneBookViewHolder phoneBookViewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.supplier_adapter_item, null);
                phoneBookViewHolder = new PhoneBookViewHolder();
                phoneBookViewHolder.name = (TextView) convertView.findViewById(R.id.item_name_text);
                phoneBookViewHolder.arrears = (TextView) convertView.findViewById(R.id.item_arrears_text);
                phoneBookViewHolder.user = (TextView) convertView.findViewById(R.id.item_user_text);
                phoneBookViewHolder.payment = (TextView) convertView.findViewById(R.id.item_payment);
                phoneBookViewHolder.line = convertView.findViewById(R.id.item_line);
                convertView.setTag(phoneBookViewHolder);
            } else {
                phoneBookViewHolder = (PhoneBookViewHolder) convertView.getTag();
            }
            int nextPositon = position + 1;
            if (nextPositon < phoneBookIndexs.size()) {
                SupplierIndex nextPhoneBookIndex = phoneBookIndexs.get(nextPositon);
                int nextType = nextPhoneBookIndex.getType();
                if (nextType == SECTION) {
                    phoneBookViewHolder.line.setVisibility(View.GONE);
                } else {
                    phoneBookViewHolder.line.setVisibility(View.VISIBLE);
                }
            }
            final SupplierBean phoneBook = phoneBookIndex.getSupplierBean();
            phoneBookViewHolder.name.setText(phoneBook.getName());
            phoneBookViewHolder.arrears.setText("欠供应商款：" + phoneBook.getArrears());
            phoneBookViewHolder.user.setText("负责人：" + phoneBook.getUser());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SupplierDetailsActivity.class);
                    intent.putExtra("data", phoneBook);
                    context.startActivity(intent);
                }
            });
            phoneBookViewHolder.payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SupplierPaymentActivity.class);
                    intent.putExtra("data", phoneBook);
                    context.startActivity(intent);
                }
            });
        }
        return convertView;
    }

    //2表示2个层次
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    //这里获取是组Item还是子Item
    @Override
    public int getItemViewType(int position) {
        return phoneBookIndexs.get(position).getType();
    }

    //判断是组Item就悬浮
    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == SECTION;
    }

    final class IndexViewHolder {
        TextView index;
    }

    final class PhoneBookViewHolder {
        TextView name;
        TextView arrears;
        TextView user;
        TextView payment;
        View line;
    }

    public int getPositionForCategory(int category) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = getItem(i).getIndex();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == category) {
                return i;
            }
        }
        return -1;

    }
}

