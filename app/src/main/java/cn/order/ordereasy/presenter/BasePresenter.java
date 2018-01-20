package cn.order.ordereasy.presenter;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Mr.Pan on 2017/9/6.
 */

public class BasePresenter {


    protected CompositeSubscription mCompositeSubscription;

    //RXjava取消注册，以避免内存泄露
    public void onUnsubscribe() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    //RXjava注册
    public void addSubscription(Subscription subscriber) {
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscriber);
    }
}
