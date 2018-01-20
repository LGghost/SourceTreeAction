package cn.order.ordereasy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Statement;

public class ExportHistoryAdapter extends BaseAdapter {
    private List<Statement> lists;
    private Context context;
    private DownloadListener lister;

    public ExportHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lists == null ? 0 : lists.size();
    }

    public void setData(List<Statement> lists) {
        this.lists = lists;
        notifyDataSetChanged();
    }

    public List<Statement> getData() {
        return lists;
    }

    @Override
    public Object getItem(int i) {
        return lists.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ExportHistoryViewHold holder;
        final Statement my = lists.get(i);
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.export_history_adapter_item, null);
            holder = new ExportHistoryViewHold();
            holder.item_name = (TextView) view.findViewById(R.id.item_name);
            holder.item_title = (TextView) view.findViewById(R.id.item_title);
            holder.item_download = (TextView) view.findViewById(R.id.item_download);
            view.setTag(holder);
        } else {
            holder = (ExportHistoryViewHold) view.getTag();
        }
        String name = "";
        if (my.getType() == 1) {
            name = context.getResources().getString(R.string.financial_statements);
        } else if (my.getType() == 2) {
            name = context.getResources().getString(R.string.detailed_statement_of_goods);
        } else if (my.getType() == 3) {
            name = context.getResources().getString(R.string.goods_sale);
        }
        holder.item_name.setText(name);
        holder.item_title.setText(my.getLog_title());
        holder.item_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lister != null){
                    lister.Download(my.getLog_id(),my.getLog_title());
                }
            }
        });
        return view;
    }

    class ExportHistoryViewHold {
        TextView item_name;
        TextView item_title;
        TextView item_download;
    }

    public void setDownloadListener(DownloadListener lister) {
        this.lister = lister;
    }

    public interface DownloadListener {
        void Download(int log_id,String title);
    }
}
