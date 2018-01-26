package cn.order.ordereasy.utils;

import android.content.Context;
import android.util.Log;

import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class XGPushUtils {
    private static XGPushUtils xgpushUtils = null;
    private final static String USER_ID = "user_18";

    public static XGPushUtils getInstance() {
        if (xgpushUtils == null)
            xgpushUtils = new XGPushUtils();
        return xgpushUtils;
    }

    public void register(Context context) {
        //为测试方便设置，发布上线时设置为false
        XGPushConfig.enableDebug(context, false);
        //注册方法
        XGPushManager.registerPush(context, USER_ID, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                Log.e("TPush", "注册成功,Token值为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                Log.e("TPush", "注册失败,错误码为：" + errCode + ",错误信息：" + msg);
            }
        });
    }
    public void unRegister(Context context) {
        XGPushManager.registerPush(context, "*", new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                Log.e("TPush", "解绑成功,Token值为：" + o);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                Log.e("TPush", "注册失败,错误为：" + s);
            }
        });

    }
}