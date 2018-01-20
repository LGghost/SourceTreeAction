package cn.order.ordereasy.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.order.ordereasy.bean.Goods;

public class DataCompareUtils {
    private static void getList(List<Goods> list, final int type) {
        Log.e("DataCompareUtils", "type:" + type);
        Collections.sort(list, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Goods stu1 = (Goods) o1;
                Goods stu2 = (Goods) o2;
                if (type == 1) {
                    long time1 = Long.parseLong(stu1.getCreate_time());
                    long time2 = Long.parseLong(stu2.getCreate_time());
                    if (time1 > time2) {
                        return -1;
                    } else if (time1 == time2) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (type == 2) {
                    long time1 = Long.parseLong(stu1.getUpdate_time());
                    long time2 = Long.parseLong(stu2.getUpdate_time());
                    if (time1 > time2) {
                        return -1;
                    } else if (time1 == time2) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (type == 3) {
                    if (stu1.getSale_num() > stu2.getSale_num()) {
                        return -1;
                    } else if (stu1.getSale_num() == stu2.getSale_num()) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else if (type == 4) {
                    if (stu1.getSale_num() > stu2.getSale_num()) {
                        return 1;
                    } else if (stu1.getSale_num() == stu2.getSale_num()) {
                        return 0;
                    } else {
                        return -1;
                    }

                } else if (type == 5) {
                    if (stu1.getStore_num() > stu2.getStore_num()) {
                        return -1;
                    } else if (stu1.getStore_num() == stu2.getStore_num()) {
                        return 0;
                    } else {
                        return 1;
                    }
                } else {
                    if (stu1.getStore_num() > stu2.getStore_num()) {
                        return 1;
                    } else if (stu1.getStore_num() == stu2.getStore_num()) {
                        return 0;
                    } else {
                        return -1;
                    }

                }
            }
        });
    }

    public static List<Goods> screenData(List<Goods> datas, int sort, int state, int array) {
        List<Goods> sortList = new ArrayList<>();
        List<Goods> stateList = new ArrayList<>();
        List<Goods> screenList = new ArrayList<>();
        if (sort == -1) {
            sortList = datas;
        } else {
            for (Goods good : datas) {
                if (good.getCategory_id() == sort) {
                    sortList.add(good);
                }
            }

        }
        if (state == -1) {
            stateList = sortList;
        } else {
            for (Goods good : sortList) {
                if (good.getStatus() == state) {
                    stateList.add(good);
                }
            }
        }
        if (array == -1) {
            screenList = stateList;
        } else {
            screenList.clear();
            screenList.addAll(stateList);
            getList(screenList, array);
        }
        return screenList;
    }
    public static List<Goods> screenStockData(List<Goods> datas, int sort, int state, int array) {
        List<Goods> sortList = new ArrayList<>();
        List<Goods> stateList = new ArrayList<>();
        List<Goods> screenList = new ArrayList<>();
        if (sort == -1) {
            sortList = datas;
        } else {
            for (Goods good : datas) {
                if (good.getCategory_id() == sort) {
                    sortList.add(good);
                }
            }

        }
        if (state == -1) {
            stateList = sortList;
        } else if(state == 1){
            for (Goods good : sortList) {
                if (good.getStore_num() < good.getMin_stock_warn_num()) {
                    stateList.add(good);
                }
            }
        }else if(state == 2){
            for (Goods good : sortList) {
                if (good.getStore_num() > good.getMax_stock_warn_num()) {
                    stateList.add(good);
                }
            }
        }
        if (array == -1) {
            screenList = stateList;
        } else {
            screenList.clear();
            screenList.addAll(stateList);
            getList(screenList, array);
        }
        return screenList;
    }
}