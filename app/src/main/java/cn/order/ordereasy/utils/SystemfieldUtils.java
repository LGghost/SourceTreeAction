package cn.order.ordereasy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.Context.TELEPHONY_SERVICE;

public class SystemfieldUtils {


    public static String getDeviceId(Context context) {
        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!TextUtils.isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                return deviceId.toString();
            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!TextUtils.isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!TextUtils.isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!TextUtils.isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", 0);
        String uuid = "";
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (!TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            SharedPreferences sp = context.getSharedPreferences("sysCacheMap", 0);
            sp.edit().putString("uuid", uuid).commit();
        }
        return uuid;
    }

    //手机型号
    public static String getSystemModel() {
        Log.e("SystemfieldUtils", "Model:" + android.os.Build.MODEL);
        return android.os.Build.MODEL;
    }

    //手机厂商操作系统版本号
    public static String getDeviceBrand() {
        Log.e("SystemfieldUtils", "DeviceBrand:" + android.os.Build.BRAND);
        return android.os.Build.BRAND;
    }

    //系统版本号
    public static String getSystemVersion() {
        Log.e("SystemfieldUtils", "Version:" + android.os.Build.VERSION.RELEASE);
        return android.os.Build.VERSION.RELEASE;

    }

    public static int getVersion(Context context) {
        String version;
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = info.versionName;
        return getVersionId(version);
    }

    public static int getVersionId(String str) {
        String s1 = str.replaceAll("\\.", "");
        return Integer.parseInt(s1);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha; //0.0-1.0
        activity.getWindow().setAttributes(lp);
    }


    public static boolean isCameraUseable() {
        boolean canUse = true;
        Camera mCamera = null;
        try {
            mCamera = Camera.open();
            Camera.Parameters mParameters = mCamera.getParameters();
            mCamera.setParameters(mParameters);
        } catch (Exception e) {
            canUse = false;
        }
        if (mCamera != null) {
            mCamera.release();
        }
        return canUse;

    }

    /**
     * 判断PrinterShare是否安装
     *
     * @param context
     * @param
     * @return
     */
    public static boolean isAppInstalled(Context context) {
        String packageName = "com.dynamixsoftware.printershare";
        PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (packageName.equals(pn)) {
                    return true;
                }
            }
        }
        return false;
    }
}
