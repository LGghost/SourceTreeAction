package cn.order.ordereasy.widget;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.TopicLabelObject;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;

@SuppressLint("NewApi")
/**
 * 下拉列表框控件
 */
public class DownListView extends LinearLayout {

    private TextView editText;
    private ImageView imageView;
    private TextView number;
    private PopupWindow popupWindow = null;
    private List<TopicLabelObject> dataList;
    private DownItemClickListener listener;
    private Context context;
    private boolean isHigh = false;
    private int select = 0;

    public DownListView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public DownListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public DownListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        // TODO Auto-generated constructor stub
        initView();
    }

    public void initView() {
        LayoutInflater.from(context).inflate(R.layout.dropdownlist_view, this);
        editText = (TextView) findViewById(R.id.text);
        imageView = (ImageView) findViewById(R.id.btn);
        number = (TextView) findViewById(R.id.text_number);
        this.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (isHigh) {
                    dataList = DataStorageUtils.getInstance().getGenreGoods();
                    if (dataList.get(0).getId() != -1) {
                        TopicLabelObject topicLabel = new TopicLabelObject(-1, -1, "全部分类", 1);
                        dataList.add(0, topicLabel);
                    }
                }
                if (popupWindow == null) {
                    changTextColor(true);
                    showPopWindow();
                    if (listener != null) {
                        listener.onClick(true);
                    }
                } else {
                    changTextColor(false);
                    closePopWindow();
                    if (listener != null) {
                        listener.onClick(false);
                    }
                }
            }
        });
    }

    private void changTextColor(boolean select) {
        if (select) {
            editText.setTextColor(context.getResources().getColor(R.color.lanse));
        } else {
            editText.setTextColor(context.getResources().getColor(R.color.heise));
        }
    }

    private void selectItme(int position) {
        for (int i = 0; i < dataList.size(); i++) {
            if (i == position) {
                dataList.get(i).setIsSelect(1);
            } else {
                dataList.get(i).setIsSelect(0);
            }
        }
    }

    /**
     * 打开下拉列表弹窗
     */
    private void showPopWindow() {
        // 加载popupWindow的布局文件
        View contentView = LayoutInflater.from(context).inflate(R.layout.dropdownlist_popupwindow, null, false);
        ListView listView = (ListView) contentView.findViewById(R.id.listView);
        final DownAdapter adapter = new DownAdapter(getContext(), dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select = position;
                editText.setText(dataList.get(position).getName());
                popupWindow.dismiss();
                if (listener != null) {
                    listener.selected(dataList.get(position));
                }
                adapter.notifyDataSetChanged();
            }
        });
        if (isHigh) {
            popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, ScreenUtil.getWindowsH(context) / 2);
        } else {
            popupWindow = new PopupWindow(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }
//        SystemfieldUtils.setBackgroundAlpha((Activity) context, 0.8f);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);
        popupWindow.showAsDropDown(this, 0, ScreenUtil.px2dip(context, 15));
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                changTextColor(false);
                if (listener != null) {
                    listener.onClick(false);
                }
//                SystemfieldUtils.setBackgroundAlpha((Activity) context, 1.0f);
                editText.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        popupWindow = null;
                    }
                }, 300);
            }
        });
    }

    /**
     * 关闭下拉列表弹窗
     */
    private void closePopWindow() {
        popupWindow.dismiss();
        popupWindow = null;
    }

    public void setEditText(int number) {
        this.number.setText("(" + number + ")");
        this.number.setVisibility(View.VISIBLE);
    }

    public void setText(String text) {
        this.editText.setText(text);
    }

    public void setHigh(boolean isHigh) {
        this.isHigh = isHigh;
    }

    /**
     * 设置数据
     *
     * @param list
     */
    public void setItemsData(List<TopicLabelObject> list, int sign) {
        dataList = list;
        if (isHigh) {
            if (dataList.get(0).getId() != -1) {
                TopicLabelObject topicLabel = new TopicLabelObject(-1, -1, "全部分类", 1);
                list.add(0, topicLabel);
            }
        }
        if (sign == 1) {
            select = 1;
            editText.setText(list.get(1).getName());
        } else {
            editText.setText(list.get(0).getName());
        }
    }

    public void setEditData(List<TopicLabelObject> list) {
        if (dataList.get(0).getId() != -1) {
            TopicLabelObject topicLabel = new TopicLabelObject(-1, -1, "全部分类", 1);
            list.add(0, topicLabel);
        }
    }

    public void setDownItemClickListener(DownItemClickListener downItemClickListener) {
        this.listener = downItemClickListener;
    }

    public interface DownItemClickListener {
        void selected(TopicLabelObject topic);

        void onClick(boolean isShow);
    }

    public class DownAdapter extends BaseAdapter {
        private List<TopicLabelObject> list;
        public Context context;

        public DownAdapter(Context context, List<TopicLabelObject> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list != null ? list.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final DownViewHold holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.popu_window_item, null);
                holder = new DownViewHold();
                holder.imageView = (ImageView) convertView.findViewById(R.id.popu_window_imageview);
                holder.textView = (TextView) convertView.findViewById(R.id.popu_window_text);
                convertView.setTag(holder);
            } else {
                holder = (DownViewHold) convertView.getTag();
            }
            holder.textView.setText(list.get(position).getName());
            if (position == select) {
                holder.imageView.setVisibility(View.VISIBLE);
                holder.textView.setTextColor(context.getResources().getColor(R.color.lanse));
            } else {
                holder.imageView.setVisibility(View.GONE);
                holder.textView.setTextColor(context.getResources().getColor(R.color.heise));
            }
            return convertView;
        }

        class DownViewHold {
            ImageView imageView;
            TextView textView;
        }

    }
}