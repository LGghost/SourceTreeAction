package cn.order.ordereasy.utils;

/**
 * Created by Mr.Pan on 2017/4/25.
 */

import android.text.TextUtils;

import com.google.gson.Gson;

import cn.order.ordereasy.bean.BaseEntity;

/**
 * Created by mrpan on 15/12/7.
 */
public class GsonUtils {

    public static BaseEntity getEntity(String content, Class<?> clazz) {

        if (TextUtils.isEmpty(content))
            return null;

        Gson gson = new Gson();
        try {
            BaseEntity baseEntity = (BaseEntity) gson.fromJson(content.replace("\uFEFF", "").trim(), clazz);

            return baseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getJsonStr(Object baseEntity){
        String str="";
        try {
            Gson gson = new Gson();
            str = gson.toJson(baseEntity);
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    public static Object getObj(String content,Class<?> clazz){
        if (TextUtils.isEmpty(content))
            return null;

        Gson gson = new Gson();
        try {
            Object baseEntity = gson.fromJson(content.replace("\uFEFF", "").trim(), clazz);

            return baseEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}