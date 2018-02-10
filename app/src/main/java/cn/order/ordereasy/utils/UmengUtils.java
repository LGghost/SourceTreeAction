package cn.order.ordereasy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class UmengUtils {
    private static UmengUtils umengUtils = null;

    public static UmengUtils getInstance() {
        if (umengUtils == null)
            umengUtils = new UmengUtils();
        return umengUtils;
    }

    public void register(Context context) {
        PlatformConfig.setWeixin("wx623cc05880057431", "b95277272197f8fe1f43483b799cb8d9");
        PlatformConfig.setQQZone("101418088", "52bffdc2e8fe93ccce9c10833c21c690");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
        UMShareAPI.get(context);
    }

    public void share(Context context, String title, UMShareListener listener) {
        share(context, -1, null, title, listener);
    }

    public String getUrl(String key, int id, boolean isPreview) {
        String url = Config.WX_SERVER_URL + "/shop/" + key + "/goods-details?id=" + id;
        if (isPreview) {
            url = url + "&preview=true";
        }
        return url;
    }

    public String shareUrl(Context context) {
        SharedPreferences spPreferences = context.getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        String url = "";
        if (!TextUtils.isEmpty(shopinfo)) {
            final JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String key = shop.get("shop_key").getAsString();
            url = Config.WX_SERVER_URL + "/shop/" + key;
        }
        return url;
    }

    public void share(final Context context, int id, String cover, final String title, final UMShareListener listener) {
        SharedPreferences spPreferences = context.getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            final JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
            String key = shop.get("shop_key").getAsString();
            final String url;
            String imgUrl;
            if (id == -1) {
                url = Config.WX_SERVER_URL + "/shop/" + key;
            } else {
                url = getUrl(key, id, false);
            }
            Log.e("UmengUtils", "url：" + url);
            Log.e("UmengUtils", "图片：" + cover);
            if (cover == null) {
                imgUrl = Config.URL_HTTP + "/" + shop.get("logo").getAsString();
            } else {
                imgUrl = Config.URL_HTTP + "/" + cover;
            }
            DownImage downImage = new DownImage(imgUrl);
            downImage.setCompletionListener(new DownImage.CompletionListener() {
                @Override
                public void getCompletion(Bitmap bitmap) {
                    UMWeb web = new UMWeb(url);
                    if (bitmap != null) {
                        UMImage thumb = new UMImage(context, bitmap);
                        web.setThumb(thumb);  //缩略图
                    }
                    web.setTitle(shop.get("name").getAsString());//标题
                    web.setDescription(title);//描述
                    new ShareAction((Activity) context)
                            .withMedia(web)
                            .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                            .setCallback(listener)
                            .open();
                }
            });
        }
    }

    public void share(Context context, int id, UMShareListener listener) {
        SharedPreferences spPreferences = context.getSharedPreferences("user", 0);
        String shopinfo = spPreferences.getString("shopinfo", "");
        if (!TextUtils.isEmpty(shopinfo)) {
            JsonObject shop = (JsonObject) GsonUtils.getObj(shopinfo, JsonObject.class);
//            String shop_id = shop.get("shop_id").getAsString();
            String shop_key = shop.get("shop_key").getAsString();
//            String singKey = id + shop_id + shop_key;
            String shareUrl;
            shareUrl = Config.WX_SERVER_URL + "/shop/" + shop_key + "/book";
            UMWeb web = new UMWeb(shareUrl);
            web.setTitle(shop.get("name").getAsString());//标题
            web.setDescription("欠款历史");//描述
            new ShareAction((Activity) context)
                    .withMedia(web)
                    .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                    .setCallback(listener)
                    .open();
        }
    }

}