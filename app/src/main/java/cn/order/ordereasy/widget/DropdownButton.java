package cn.order.ordereasy.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.order.ordereasy.R;


/**
 * Created by Administrator on 2015/5/28.
 */
public class DropdownButton extends RelativeLayout {
    TextView textView;
    View bottomLine;

    public DropdownButton(Context context) {
        this(context, null);
    }

    public DropdownButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropdownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dropdown_tab_button, this, true);
        textView = (TextView) view.findViewById(R.id.textView);
//        bottomLine = view.findViewById(R.id.bottomLine);
    }


    public void setText(CharSequence text) {
        textView.setText(text);
    }

    public void setChecked(boolean checked) {
//        if (checked) {
//            textView.setTextColor(getResources().getColor(R.color.lanse));
//            bottomLine.setVisibility(VISIBLE);
//        } else {
//            textView.setTextColor(getResources().getColor(R.color.font_black_content));
//            bottomLine.setVisibility(GONE);
//        }
////        textView.setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null);
    }


}
