package cn.order.ordereasy.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mr.Pan on 2017/9/28.
 */

public class ListUtils {
    public static <T> List<T> filter(List<T> list, ListUtilsHook<T> hook) {
        ArrayList<T> r = new ArrayList<T>();
        for (T t : list) {
            if (hook.compare(t)) {
                r.add(t);
            }
        }
        r.trimToSize();
        return r;
    }
}
