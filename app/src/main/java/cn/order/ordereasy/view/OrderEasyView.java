package cn.order.ordereasy.view;

import com.google.gson.JsonObject;

/**
 * Created by mrpan on 2017/9/4.
 */

public interface OrderEasyView {
    void showProgress(int type);
    void hideProgress(int type);
    void loadData(JsonObject data,int type);
}
