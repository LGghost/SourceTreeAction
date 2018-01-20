package cn.order.ordereasy.bean;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Mr.Pan on 2017/9/27.
 */

public class InventoryInfo extends BaseEntity {
    Map<String, Object> info = new HashMap<>();
    List<Map<String, Object>> list = new ArrayList<>();

    public Map<String, Object> getInfo() {
        return info;
    }

    public void setInfo(Map<String, Object> info) {
        this.info = info;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public List<Map<String, Object>> goodToMap(List<Goods> goods) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (Goods good : goods) {
            if (good.getNum() < 1)
                continue;
            List<Product> products = good.getProduct_list();
            if (products == null) products = new ArrayList<>();
            for (Product product : products) {
                Map<String, Object> map = new HashMap<String, Object>();
                Log.e("InventoryInfo", "Product_id:" + product.getProduct_id());
                map.put("operate_num", product.getOperate_num());
                map.put("product_id", product.getProduct_id());
                list.add(map);
            }
        }
        return list;
    }
}
