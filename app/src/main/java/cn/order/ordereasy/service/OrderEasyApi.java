package cn.order.ordereasy.service;

import com.google.gson.JsonObject;

import java.util.Map;

import cn.order.ordereasy.utils.Config;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;


public interface OrderEasyApi {


    /**
     * 请求
     * @param route
     * @return
     */
    @POST
    Observable<JsonObject> request(@Url String url, @Body RequestBody route);

    @Multipart
    @POST
    Observable<JsonObject> uploadImgs(@Url String url,@PartMap Map<String, RequestBody> map, @Part MultipartBody.Part body);

    @GET
    Observable<JsonObject> requestGet(@Url String url);

    @POST
    Observable<JsonObject> foo(@Header("Accept-Language") String lang);


    @POST("api/user/login/register")
    Observable<JsonObject> reateCommit(@Body RequestBody route);

}