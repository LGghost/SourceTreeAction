package cn.order.ordereasy.utils;

import android.support.annotation.StringRes;
import android.widget.Toast;

import cn.order.ordereasy.app.MyApplication;


public class ToastUtil {
    // Toast对象
    private static Toast toast = null;

    private ToastUtil() {
    }

    public static void show(CharSequence text) {
        if (toast == null) {
            toast = Toast.makeText(MyApplication.getInstance(), "", Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void show(@StringRes int resId) {
        show(MyApplication.getInstance().getString(resId));
    }

}