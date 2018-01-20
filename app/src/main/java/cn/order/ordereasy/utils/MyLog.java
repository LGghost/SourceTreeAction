package cn.order.ordereasy.utils;

import android.util.Log;

/**
 * Created by mrpan on 15/12/7.
 */
public class MyLog {
    private static final String TAG = "Ann";

    public static void i(String tag, String msg) {
        if(Config.IS_DEBUG){
            Log.i(TAG, "[" + tag + "]" + msg);
        }

    }

    public static void e(String tag, String msg) {
        if(Config.IS_DEBUG){
            Log.e(TAG, "[" + tag + "]" + msg);
        }

    }

}
