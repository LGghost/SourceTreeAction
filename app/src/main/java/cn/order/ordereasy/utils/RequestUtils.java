package cn.order.ordereasy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cn.order.ordereasy.service.OrderEasyApi;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.ConnectionSpec;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.TlsVersion;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Mr.Pan on 2017/2/27.
 */

public class RequestUtils {
    private static RequestUtils instance = null;
    public static final String BASE_URL = "https://www.dinghuo5u.com/";
    public static final String BASE_URL_V1 = "https://api.dinghuo5u.com/";
    public static String URL = "https://api.dinghuo5u.com/";
    public static final int old_url = 0;
    public static final int new_url = 1;
    private Retrofit retrofit = null;
    private OkHttpClient client = null;
    private Context context;

    public RequestUtils(final Context context) {
        this.context = context;
    }

    public static synchronized RequestUtils getInstance(Context context) {
        if (instance == null) {
            instance = new RequestUtils(context);
        }
        return instance;
    }

    public OrderEasyApi getKoalaApiService() {//以前网络请求调用的方法
        client = getNewHttpClient(context, 0);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(OrderEasyApi.class);
    }

    public OrderEasyApi getKoalaApiService(int type) {//新网络请求调用的方法
        if (type == 0) {
            URL = BASE_URL;//老接口
        } else {
            URL = BASE_URL_V1;//新接口
        }
        client = getNewHttpClient(context, type);
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(OrderEasyApi.class);
    }

    private OkHttpClient getNewHttpClient(final Context context, final int type) {
        File cacheFile = new File(Config.DIR_CACHE_PATH);
        final Cache cache = new Cache(cacheFile, 1024 * 1024 * 50);
        final Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                if (type == 0) {//老接口没有添加请求头
                    if (NetWorkUtils.isNetworkConnected(context)) {
                        SharedPreferences spPreferences = context.getSharedPreferences("user", 0);
                        String token = spPreferences.getString("token", "");
                        if (!TextUtils.isEmpty(token)) {
                            request = request.newBuilder()
                                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                                    .addHeader("token", token)
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        } else {
                            request = request.newBuilder()
                                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }

                    }
                } else {//新接口添加了请求头
                    if (NetWorkUtils.isNetworkConnected(context)) {
                        SharedPreferences spPreferences = context.getSharedPreferences("user", 0);
                        String token = spPreferences.getString("token", "");
                        if (!TextUtils.isEmpty(token)) {
                            request = request.newBuilder()
                                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                                    .addHeader("token", token)
                                    .addHeader("platform", "android")
                                    .addHeader("deviceId", SystemfieldUtils.getDeviceId(context))
                                    .addHeader("modal", SystemfieldUtils.getSystemModel() + "/" + SystemfieldUtils.getDeviceBrand() + "/" + SystemfieldUtils.getSystemVersion())
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        } else {
                            request = request.newBuilder()
                                    .addHeader("Content-Type", "application/json;charset=UTF-8")
                                    .addHeader("platform", "android")
                                    .addHeader("deviceId", SystemfieldUtils.getDeviceId(context))
                                    .addHeader("modal", SystemfieldUtils.getSystemModel() + "/" + SystemfieldUtils.getDeviceBrand() + "/" + SystemfieldUtils.getSystemVersion())
                                    .cacheControl(CacheControl.FORCE_CACHE)
                                    .build();
                        }

                    }
                }
                Response response = chain.proceed(request);
                if (NetWorkUtils.isNetworkConnected(context)) {
                    int maxAge = 0 * 60;
                    // 有网络时 设置缓存超时时间0个小时
                    response.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                            .build();
                } else {
                    // 无网络时，设置超时为4周
                    int maxStale = 60 * 60 * 24 * 28;
                    response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .removeHeader("Pragma")
                            .build();
                }
                return response;
            }
        };
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .addNetworkInterceptor(interceptor)
                .cache(cache)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);
        return enableTls12OnPreLollipop(client).build();
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()));
                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2).build();
                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);
                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }
        return client;
    }
}

