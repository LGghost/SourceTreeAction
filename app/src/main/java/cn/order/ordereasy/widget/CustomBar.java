package cn.order.ordereasy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.order.ordereasy.R;
import cn.order.ordereasy.utils.ScreenUtil;
import cn.order.ordereasy.view.fragment.AccountBookActivity;


public class CustomBar extends LinearLayout implements OnClickListener {
    private Context context;
    private View View;
    private LayoutInflater inflater;
    private LinearLayout layout;
    //选中的功能
    private int index = 0;
    //选中是的接口
    SelectListener mlistener;

    public CustomBar(Context context) {
        super(context, null);
    }

    public CustomBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
        addView(View);
    }

    private void initView() {
        inflater = LayoutInflater.from(context);
        View = inflater.inflate(R.layout.custom_bar, null);
        layout = (LinearLayout) View.findViewById(R.id.custom_bar_layout);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
                ScreenUtil.dip2px(context, 30));
        layout.setLayoutParams(params);
        //循环设置监听
        for (int i = 0; i < layout.getChildCount(); i++) {
            if (i % 2 == 0) {
                layout.getChildAt(i).setOnClickListener(this);
            }
        }
        setChange(0);
    }

    //改变背景图片和字体颜色(表示选中)
    public void setChange(int i) {
        LinearLayout linearLayout;
        TextView textView;
        for (int j = 0; j < layout.getChildCount(); j++) {
            if (j % 2 == 0) {
                linearLayout = (LinearLayout) layout.getChildAt(j);
                textView = (TextView) linearLayout.getChildAt(0);
                if (j == i) {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.white));
                    textView.setTextColor(getResources().getColor(R.color.lanse));
                } else {
                    linearLayout.setBackgroundColor(getResources().getColor(R.color.lanse));
                    textView.setTextColor(getResources().getColor(R.color.white));
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.custom_bar_trade:
                index = 0;
                break;
            case R.id.custom_bar_owenumber:
                index = 2;
                break;
            case R.id.custom_bar_receivables:
                index = 4;
                break;
            case R.id.custom_bar_stock:
                index = 6;
                break;
        }
        setChange(index);
        //接口触发
        if (mlistener != null) {
            mlistener.getOnclick(index);
        }

    }

    //接口调用
    public void setSelectListener(AccountBookActivity mlistener) {
        this.mlistener = mlistener;
    }


    //初始化接口
    public interface SelectListener {
        void getOnclick(int i);
    }

}
