package cn.order.ordereasy.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.order.ordereasy.R;

/**
 * Created by Mr.Pan on 2017/5/2.
 */

public class LoadingDialog extends AlertDialog {

    private TextView tips_loading_msg;
    private int layoutResId;
    private String message = null;
    private TextView content;
    /**
     * 构造方法
     *
     * @param context     上下文
     * @param layoutResId 要传入的dialog布局文件的id
     */
    public LoadingDialog(Context context, int layoutResId) {
        super(context);
        this.layoutResId = layoutResId;
        //message = context.getResources().getString(R.string.loading2);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layoutResId);
        tips_loading_msg = (TextView) findViewById(R.id.tips_loading_msg);
        // tips_loading_msg.setText(this.message);
        content = (TextView) findViewById(R.id.text_conten);
    }
    public void setTextVisibility(String str){
        content.setVisibility(View.VISIBLE);
        content.setText(str);
    }
}
