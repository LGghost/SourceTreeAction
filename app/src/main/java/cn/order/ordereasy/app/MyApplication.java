package cn.order.ordereasy.app;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.util.List;

import cn.bingoogolapple.swipebacklayout.BGASwipeBackManager;
import cn.order.ordereasy.R;
import cn.order.ordereasy.service.OrderEasyApi;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.RequestUtils;
import cn.order.ordereasy.utils.UmengUtils;


/**
 * Created by Mr.Pan on 2017/2/17.
 */

public class MyApplication extends Application {
    private static MyApplication mInstance;
    public Context mContext;

    public static MyApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mInstance = this;
        configImageLoader();
        BGASwipeBackManager.getInstance().init(this);
        UmengUtils.getInstance().register(this);
        CrashReport.initCrashReport(getApplicationContext(), "30f0d4873e", false);

    }

    public OrderEasyApi getService() {
        return RequestUtils.getInstance(this).getKoalaApiService();
    }

    public OrderEasyApi getService(int type) {
        return RequestUtils.getInstance(this).getKoalaApiService(type);

    }

    /**
     * 配置ImageLoder
     */
    private void configImageLoader() {
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.dhwy_bg_image) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.dhwy_bg_image) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.dhwy_bg_image) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
    }

}
