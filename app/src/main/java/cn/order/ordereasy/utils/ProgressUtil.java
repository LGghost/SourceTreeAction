package cn.order.ordereasy.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.order.ordereasy.R;
import cn.order.ordereasy.widget.LoadingDialog;

/**
 * Created by mrpan on 2017/9/4.
 */

public class ProgressUtil {
    private static LoadingDialog dialog;

    public static void showDialog(Context context) {
        if (isNetworkAvailable(context)) {
            if (dialog != null && dialog.isShowing()) {
               return;
            }
            dialog = new LoadingDialog(context, R.layout.view_tips_loading2);
            dialog.setCancelable(false);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    }

    public static void dissDialog() {

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }
    public static void setContent(String str){
        dialog.setTextVisibility(str);
    }
}
