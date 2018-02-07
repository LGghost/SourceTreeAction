package cn.order.ordereasy.model;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.bean.Delivery;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Redelivery;
import cn.order.ordereasy.service.OrderEasyApiService;
import cn.order.ordereasy.utils.Md5Utils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Mr.Pan on 2017/9/4.
 */

public class OrderEasyApiModelImp implements OrderEasyApiModel {

    private OrderEasyOnListener orderEasyOnListener;

    public OrderEasyApiModelImp(OrderEasyOnListener orderEasyOnListener) {
        this.orderEasyOnListener = orderEasyOnListener;
    }

    @Override
    public Subscription login(String username, String pwd) {

        Observable<JsonObject> request = OrderEasyApiService.login(username, Md5Utils.StrToMd5(pwd));

        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription register(String telephone, String password, String smscode, String code, String yaoqcode) {
        Observable<JsonObject> request = OrderEasyApiService.register(telephone, Md5Utils.StrToMd5(password), smscode, code, yaoqcode);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription forgot(String telephone, String password, String smscode, String code) {
        Observable<JsonObject> request = OrderEasyApiService.forgot(telephone, Md5Utils.StrToMd5(password), smscode, code);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getSmsCode(String telephone, String type, String code) {
        Observable<JsonObject> request = OrderEasyApiService.getSmsCode(telephone, type, code);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCode() {
        Observable<JsonObject> request = OrderEasyApiService.getCode();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription operateRecordDetail(int operate_id) {
        Observable<JsonObject> request = OrderEasyApiService.operateRecordDetail(operate_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }


    @Override
    public Subscription setupStore(String mobile, String name, String province, String city, String district, String address, String telephone, String wechat, String notice) {
        Observable<JsonObject> request = OrderEasyApiService.setupStore(mobile, name, province, city, district, address, telephone, wechat, notice);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getStoreInfo() {
        Observable<JsonObject> request = OrderEasyApiService.getStoreInfo();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription uploadStoreImg(String type, String file) {
        Observable<JsonObject> request = OrderEasyApiService.uploadStoreImg(type, file);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        orderEasyOnListener.onFailure(e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        orderEasyOnListener.onSuccess(jsonObject, 3);
                    }
                });
        return sub;
    }

    @Override
    public Subscription uploadShoopImg(String file) {
        Observable<JsonObject> request = OrderEasyApiService.uploadGoodImg(file);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        orderEasyOnListener.onFailure(e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        orderEasyOnListener.onSuccess(jsonObject, 3);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCategoryInfo() {
        Observable<JsonObject> request = OrderEasyApiService.getCategoryInfo();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addCategoryInfo(String name) {
        Observable<JsonObject> request = OrderEasyApiService.addCategoryInfo(name);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription updateCategoryInfo(int cateId, String name) {
        Observable<JsonObject> request = OrderEasyApiService.updateCategoryInfo(cateId, name);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription delCategoryInfo(int cateId) {
        Observable<JsonObject> request = OrderEasyApiService.delCategoryInfo(cateId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getSpecInfo() {
        Observable<JsonObject> request = OrderEasyApiService.getSpecInfo();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addSpecInfo(String name) {
        Observable<JsonObject> request = OrderEasyApiService.addSpecInfo(name);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addSpecValueInfo(int sepcId, String value) {
        Observable<JsonObject> request = OrderEasyApiService.addSpecValueInfo(sepcId, value);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription delSpecInfo(int cateId) {
        Observable<JsonObject> request = OrderEasyApiService.delSpecInfo(cateId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription delSpecValueInfo(int cateId, String value) {
        Observable<JsonObject> request = OrderEasyApiService.delSpecValueInfo(cateId, value);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getGoodsList() {
        Observable<JsonObject> request = OrderEasyApiService.getGoodsList();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addGoods(Goods good) {
        Observable<JsonObject> request = OrderEasyApiService.addGoods(good);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription updateGoods(Goods good) {
        Observable<JsonObject> request = OrderEasyApiService.updateGoods(good);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 4);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getGoodsInfo(int goodId) {
        Observable<JsonObject> request = OrderEasyApiService.getGoodsInfo(goodId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getOweGoodsList(int goodId) {
        Observable<JsonObject> request = OrderEasyApiService.getOweGoodsList(goodId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getGoodsCustomers(int goodId) {
        Observable<JsonObject> request = OrderEasyApiService.getGoodsCustomers(goodId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription makeGoodsStatus(int goodId, int status) {
        Observable<JsonObject> request = OrderEasyApiService.makeGoodsStatus(goodId, status);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 4);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getGoodsBuyHistories(int goodId, int days, String date) {
        Observable<JsonObject> request = OrderEasyApiService.getGoodsBuyHistory(goodId, days, date);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription Add_Odder(Order order) {
        Observable<JsonObject> request = OrderEasyApiService.Add_Odder(order);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription closeOrder(int orderId) {
        Observable<JsonObject> request = OrderEasyApiService.closeOrder(orderId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getOrdersList(int page, String filter_type, String user_id, String start_time, String end_time) {
        Observable<JsonObject> request = OrderEasyApiService.getOrdersList(page, filter_type, user_id, start_time, end_time);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getOrderInfo(int orderId) {
        Observable<JsonObject> request = OrderEasyApiService.getOrderInfo(orderId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getOrderLogs(int orderId) {
        Observable<JsonObject> request = OrderEasyApiService.getOrderLogs(orderId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getOweOrdersList(int custId, int order_id) {
        Observable<JsonObject> request = OrderEasyApiService.getOweOrdersList(custId, order_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription updateOrderAddr(int orderId, String addr, final int type) {
        Observable<JsonObject> request = OrderEasyApiService.updateOrderAddr(orderId, addr);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, type);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCustomerList() {
        Observable<JsonObject> request = OrderEasyApiService.getCustomerList();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCustomerList1() {
        Observable<JsonObject> request = OrderEasyApiService.getCustomerList1();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 4);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addCustomer(Customer customer) {
        Observable<JsonObject> request = OrderEasyApiService.addCustomer(customer);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription updateCustomer(Customer customer) {
        Observable<JsonObject> request = OrderEasyApiService.updateCustomer(customer);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription delCustomer(int customerId) {
        Observable<JsonObject> request = OrderEasyApiService.delCustomer(customerId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription importCustomers(List<Customer> customerList) {
        Observable<JsonObject> request = OrderEasyApiService.importCustomers(customerList);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCustomerInfo(int customerId) {
        Observable<JsonObject> request = OrderEasyApiService.getCustomerInfo(customerId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCustSalesGoods(int customer_id, int is_owe) {
        Observable<JsonObject> request = OrderEasyApiService.getCustSalesGoods(customer_id, is_owe);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCustomerOweLog(int customer_id) {
        Observable<JsonObject> request = OrderEasyApiService.getCustomerOweLog(customer_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription adjustmentOweInfo(int customer_id, double money, String remark) {
        Observable<JsonObject> request = OrderEasyApiService.adjustmentOweInfo(customer_id, money, remark);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getCustomerAccountInfo(String sign, int customer_id, int shop_id) {
        Observable<JsonObject> request = OrderEasyApiService.getCustomerAccountInfo(sign, customer_id, shop_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //库存列表
    @Override
    public Subscription getStockList(int operate_type) {
        Observable<JsonObject> request = OrderEasyApiService.getStockList(operate_type);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //客户主页--订单记录
    @Override
    public Subscription getRecordList(int customer_id, String page) {
        Observable<JsonObject> request = OrderEasyApiService.getRecordList(customer_id, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription searchRecordList(int page, String keyword) {
        Observable<JsonObject> request = OrderEasyApiService.searchRecordList(page, keyword);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //客户主页--收银记录
    @Override
    public Subscription getOrderRecordLlist(int customer_id, String page) {
        Observable<JsonObject> request = OrderEasyApiService.getOrderRecordLlist(customer_id, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //客户主页--操作（发货）记录
    @Override
    public Subscription getOperationRecordList(int customer_id, String page) {
        Observable<JsonObject> request = OrderEasyApiService.getOperationRecordList(customer_id, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //库存调整记录
    @Override
    public Subscription getOperationRecordList(int page, int operate_type, int user_id, String begindate, String enddate) {
        Observable<JsonObject> request = OrderEasyApiService.getOperationRecordList(page, operate_type, user_id, begindate, enddate);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addOrderPay(int customer_id, int payment_type, double cash, double wechat, double alipay, double bank_card, double other) {
        Observable<JsonObject> request = OrderEasyApiService.addOrderPay(customer_id, payment_type, cash, wechat, alipay, bank_card, other);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }


    //客户主页--操作（发货）记录
    @Override
    public Subscription getDaily(String date) {
        Observable<JsonObject> request = OrderEasyApiService.getDaily(date);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription redeliver(Redelivery redelivery) {
        Observable<JsonObject> request = OrderEasyApiService.redeliver(redelivery);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription deliver(Delivery delivery) {
        Observable<JsonObject> request = OrderEasyApiService.deliver(delivery);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription delivers(List<Delivery> deliveries) {
        Observable<JsonObject> request = OrderEasyApiService.delivers(deliveries);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //获取今日数据
    @Override
    public Subscription getNumToday(int type) {
        Observable<JsonObject> request = OrderEasyApiService.getNumToday(type);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //获取欠的数据
    @Override
    public Subscription getNumToday2(int type) {
        Observable<JsonObject> request = OrderEasyApiService.getNumToday2(type);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 获取员工列表
     */
    @Override
    public Subscription getEmployeeNum(final int type) {
        Observable<JsonObject> request = OrderEasyApiService.getEmployee();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, type);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 添加员工列表
     */
    @Override
    public Subscription addEmployeeNum(String name, String tel, String pass, List<Integer> list) {
        Observable<JsonObject> request = OrderEasyApiService.addEmployee(name, tel, Md5Utils.StrToMd5(pass), list);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getInventoryList(int page) {
        Observable<JsonObject> request = OrderEasyApiService.inventoryList(page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getInventoryInfo(int inventory_id, int user_id) {
        Observable<JsonObject> request = OrderEasyApiService.getInventoryInfo(inventory_id, user_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getInventory(int inventory_id) {
        Observable<JsonObject> request = OrderEasyApiService.getInventory(inventory_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription addInventory() {
        Observable<JsonObject> request = OrderEasyApiService.addInventoryInfo();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription commitInventoryInfo(int inventory_id, String remark, int is_adjust) {
        Observable<JsonObject> request = OrderEasyApiService.commitInventoryInfo(inventory_id, remark, is_adjust);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription saveInventoryInfo(List<Map<String, Object>> inventoryInfo) {
        Observable<JsonObject> request = OrderEasyApiService.saveInventoryInfo(inventoryInfo);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getInventoryInfo(int inventory_id, int user_id, int page) {
        Observable<JsonObject> request = OrderEasyApiService.getInventoryInfo(inventory_id, user_id, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 删除员工
     */
    @Override
    public Subscription deleteEmployee(String user_id) {
        Observable<JsonObject> request = OrderEasyApiService.deleteEmployee(user_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 获取员工信息
     */
    @Override
    public Subscription getUserInfo() {
        Observable<JsonObject> request = OrderEasyApiService.getUserInfo();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription updateUserInfo(int id, String name,List<Integer> list) {
        Observable<JsonObject> request = OrderEasyApiService.updateUserInfo(id, name,list);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription updateUserPass(int id, String pass, String pass1) {
        Observable<JsonObject> request = OrderEasyApiService.updateUserPass(id, Md5Utils.StrToMd5(pass), Md5Utils.StrToMd5(pass1));
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription booking2(int type, int page) {
        Observable<JsonObject> request = OrderEasyApiService.booking2(type, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription booking4(int days, String starttime, String endtime) {
        Observable<JsonObject> request = OrderEasyApiService.booking4(days, starttime, endtime);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription booking3(int days, String starttime, String endtime) {
        Observable<JsonObject> request = OrderEasyApiService.booking3(days, starttime, endtime);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription booking1(int days, int page, int type, String user_id, String starttime, String endtime) {
        Observable<JsonObject> request = OrderEasyApiService.booking1(days, page, type, user_id, starttime, endtime);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 4);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription initStoreInfo(String shopname, String bossname) {
        Observable<JsonObject> request = OrderEasyApiService.initStoreInfo(shopname, bossname);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //清空店铺数据
    @Override
    public Subscription getUserFeedback(String content) {
        Observable<JsonObject> request = OrderEasyApiService.getUserFeedback(content);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    //清空店铺数据
    @Override
    public Subscription getEmptyStoreData(String goods, String customer, String user, String password) {
        Observable<JsonObject> request = OrderEasyApiService.getEmptyStoreData(goods, customer, user, password);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription getStoreData() {
        Observable<JsonObject> request = OrderEasyApiService.getStoreData();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription uploadUserAvatar(String file) {
        Observable<JsonObject> request = OrderEasyApiService.uploadUserAvatar(file);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<JsonObject>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        orderEasyOnListener.onFailure(e);
                    }

                    @Override
                    public void onNext(JsonObject jsonObject) {
                        orderEasyOnListener.onSuccess(jsonObject, 3);
                    }
                });
        return sub;
    }

    /**
     * 客户交易记录
     *
     * @param customer_id
     * @param days
     * @param is_owe
     * @param page
     * @return
     */
    @Override
    public Subscription customerTransactionRecord(int customer_id, int days, int is_owe, int page) {
        Observable<JsonObject> request = OrderEasyApiService.customerTransactionRecord(customer_id, days, is_owe, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 欠款历史记录
     *
     * @param customer_id
     * @param page
     * @return
     */
    @Override
    public Subscription arrearsHistory(int customer_id, int page) {
        Observable<JsonObject> request = OrderEasyApiService.arrearsHistory(customer_id, page);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 调整欠款
     *
     * @param customer_id
     * @param money
     * @return
     */
    @Override
    public Subscription adjustmentArrears(int customer_id, double money, String remark) {
        Observable<JsonObject> request = OrderEasyApiService.adjustmentArrears(customer_id, money, remark);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription isUpdataApp() {
        Observable<JsonObject> request = OrderEasyApiService.isUpdataApp();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription customerRankList() {
        Observable<JsonObject> request = OrderEasyApiService.customerRankList();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription customerdelRank(int rank_id) {
        Observable<JsonObject> request = OrderEasyApiService.customerdelRank(rank_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription customerditRank(int rank_id, String rank_name, int rank_discount) {
        Observable<JsonObject> request = OrderEasyApiService.customerditRank(rank_id, rank_name, rank_discount);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription customeraddRank(int rank_id, String rank_name, List<String> customer_ids) {
        Observable<JsonObject> request = OrderEasyApiService.customeraddRank(rank_id, rank_name, customer_ids);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription customertoRank(List<String> customer_ids, int rank_id) {
        Observable<JsonObject> request = OrderEasyApiService.customertoRank(customer_ids, rank_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription exportPayList(String start_time, String end_time) {
        Observable<JsonObject> request = OrderEasyApiService.exportPayList(start_time, end_time);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription exportSaleProductList(String start_time) {
        Observable<JsonObject> request = OrderEasyApiService.exportSaleProductList(start_time);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }
    @Override
    public Subscription exportSaleProductCount(String start_time,String end_time) {
        Observable<JsonObject> request = OrderEasyApiService.exportSaleProductCount(start_time,end_time);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 3);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription exportLog() {
        Observable<JsonObject> request = OrderEasyApiService.exportLog();
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 1);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }


    @Override
    public Subscription exportStatus(int log_id) {
        Observable<JsonObject> request = OrderEasyApiService.exportStatus(log_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription loanAsk(String telephone, String name, String purpose, String identity, String gender) {
        Observable<JsonObject> request = OrderEasyApiService.loanAsk(telephone,name,purpose,identity,gender);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription orderConfirm(Order order) {
        Observable<JsonObject> request = OrderEasyApiService.orderConfirm(order);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription goodsDel(int goodId) {
        Observable<JsonObject> request = OrderEasyApiService.goodsDel(goodId);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    @Override
    public Subscription inventoryInfo(int inventory_id) {
        Observable<JsonObject> request = OrderEasyApiService.inventoryInfo(inventory_id);
        Subscription sub = request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JsonObject>() {
                    @Override
                    public void call(JsonObject data) {
                        orderEasyOnListener.onSuccess(data, 2);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        orderEasyOnListener.onFailure(throwable);
                    }
                });
        return sub;
    }

    /**
     * 回调接口
     */
    public interface OrderEasyOnListener {
        void onSuccess(JsonObject res, int type);

        void onFailure(Throwable e);
    }
}
