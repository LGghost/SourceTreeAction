package cn.order.ordereasy.utils;

import java.lang.reflect.Field;

        import android.app.Activity;
        import android.app.ActivityManager;
        import android.content.Context;
        import android.util.DisplayMetrics;
        import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import cn.order.ordereasy.app.MyApplication;

/**
 * 屏幕尺寸, 像素 ,
 * @author Administrator
 *
 */
public final class ScreenUtil {

    /**
     * dp 转 px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px 转 sp
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp 转 px
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    @SuppressWarnings("deprecation")
    public static int getWindowsW(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }

    public static float getScreenDensity(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();

        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm.density;

    }

    @SuppressWarnings("deprecation")
    public static int getWindowsH(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getHeight();
    }

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            Log.e("ScreenUtil", "get status bar height fail");
            e1.printStackTrace();
        }
        Log.i("status_height", "" + sbar);
        return sbar;
    }

    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        return context.getPackageName().equals(
                am.getRunningTasks(1).get(0).topActivity.getPackageName());
    }


}
