package cn.order.ordereasy.widget;

/**
 * Created by mrpan on 2017/9/22.
 */
import java.util.Comparator;

import cn.order.ordereasy.bean.Customer;


public class PinyinComparator implements Comparator<Customer> {

    public int compare(Customer o1, Customer o2) {
        if (o1.getTopic().equals("@") || o2.getTopic().equals("#")) {
            return -1;
        } else if (o1.getTopic().equals("#") || o2.getTopic().equals("@")) {
            return 1;
        } else {
            return o1.getTopic().compareTo(o2.getTopic());
        }
    }

}