package cn.order.ordereasy.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.order.ordereasy.R;
import cn.order.ordereasy.view.activity.BaseActivity;

/**
 * Created by Administrator on 2016-05-11.
 */

public class PopWinShare extends PopupWindow {

    private View conentView;
    private Context context;
    private TextView btn_status;
    private ImageView image_1;
    private TextView btn_bottom;
    private ImageView image_2;

    public PopWinShare(final Activity context, View.OnClickListener listener) {

        LayoutInflater inflater = (LayoutInflater) context   .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.popwin_share, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w / 2 - 100);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimTools);
        LinearLayout addTaskLayout = (LinearLayout) conentView  .findViewById(R.id.add_task_layout);
        LinearLayout teamMemberLayout = (LinearLayout) conentView   .findViewById(R.id.team_member_layout);
        btn_status =(TextView)conentView.findViewById(R.id.btn_status);
        image_1 = (ImageView) conentView.findViewById(R.id.image_1);
        btn_bottom = (TextView)conentView.findViewById(R.id.btn_bottom);
        image_2 = (ImageView) conentView.findViewById(R.id.image_2);
        addTaskLayout.setOnClickListener(listener);
        teamMemberLayout.setOnClickListener(listener);
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     */
    public void showPopupWindow(View parent) {

        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 18);

        } else {
            this.dismiss();
        }
    }
    public void setBtnStatusText(String text){
        btn_status.setText(text);
    }
    public void setBtnBottomText(String text){
        btn_bottom.setText(text);
    }
    public void setFristImageView(int resid){
        image_1.setBackgroundResource(resid);
    }
    public void setSecondImageView(int resid){
        image_2.setBackgroundResource(resid);
    }
}