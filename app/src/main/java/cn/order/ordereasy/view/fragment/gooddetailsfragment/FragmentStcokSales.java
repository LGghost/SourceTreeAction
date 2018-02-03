package cn.order.ordereasy.view.fragment.gooddetailsfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Order;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.Config;

public class FragmentStcokSales extends Fragment {
    private FragmentManager manager;
    private FragmentChildStcok childStcokFragment;
    private FragmentCustomer customerFragment;
    private Fragment mCurrentFragment = null; // 记录当前显示的Fragment
    private Goods good;
    private int goodId = 0;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.goods_details_left, container, false);
        ButterKnife.inject(this, view);
        initData();
        return view;
    }

    private void initData() {
        childStcokFragment = new FragmentChildStcok();
        customerFragment = new FragmentCustomer();
        customerFragment.setGoodId(goodId);
        mCurrentFragment = childStcokFragment;
        manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.stock_owe_frame_layout, mCurrentFragment, mCurrentFragment.getClass().getName());
        transaction.commitAllowingStateLoss();

    }

    public void setData(Goods goods) {
        this.good = goods;
        int store_num = 0, sale_num = 0, owe_num = 0;
        for (Product p : good.getProduct_list()) {
            store_num += p.getStore_num();
            sale_num += p.getSale_num();
            owe_num += p.getOwe_num();
        }
        //库存
        goods_kucun.setText(String.valueOf(store_num));
        //销量
        goods_xiaoliang.setText(String.valueOf(sale_num));
        if (good.getMax_price() == good.getMin_price()) {
            goods_min_money.setText(String.valueOf(good.getMin_price()));
            goods_max_money.setVisibility(View.GONE);
            goods_details_space.setVisibility(View.GONE);
        } else {
            goods_max_money.setVisibility(View.VISIBLE);
            goods_details_space.setVisibility(View.VISIBLE);
            goods_min_money.setText(String.valueOf(good.getMin_price()));
            goods_max_money.setText(String.valueOf(good.getMax_price()));
        }
        if (goods.getStatus() != 1) {
            //下架图片
            goods_image2.setVisibility(View.VISIBLE);
        } else {
            //上架图片
            goods_image2.setVisibility(View.GONE);
        }
        if(goods.getIs_hidden_price() == 0){
            hide_text.setVisibility(View.GONE);
        }else{
            hide_text.setVisibility(View.VISIBLE);
        }
        goods_no.setText(String.valueOf(good.getGoods_no()));
        goods_name.setText(good.getTitle());
        ImageLoader.getInstance().displayImage(Config.URL_HTTP + "/" + good.getCover(), goods_image);
        products = good.getProduct_list();
        if (products.size() < 1) {
            Product product = new Product();
            product.setSpec_data(Arrays.asList(new String[]{"无"}));
            products.add(product);
        }
        childStcokFragment.setData(products, store_num, owe_num);
        childStcokFragment.endRefreshing();
    }

    public void setOrderList(List<Order> orders) {
        childStcokFragment.setOrdersList(orders);
        childStcokFragment.endRefreshing();
    }
    public void setGoodId(int goodId){
        this.goodId = goodId;
    }
    private FragmentTransaction switchFragment(Fragment targetFragment) {

        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        if (!targetFragment.isAdded()) {
            //第一次使用switchFragment()时currentFragment为null，所以要判断一下
            if (mCurrentFragment != null) {
                transaction.hide(mCurrentFragment);
            }
            transaction.add(R.id.stock_owe_frame_layout, targetFragment, targetFragment.getClass().getName());
        } else {
            transaction
                    .hide(mCurrentFragment)
                    .show(targetFragment);
        }
        mCurrentFragment = targetFragment;
        return transaction;
    }

    @InjectView(R.id.goods_image)
    ImageView goods_image;
    @InjectView(R.id.goods_image2)
    ImageView goods_image2;
    @InjectView(R.id.goods_name)
    TextView goods_name;
    @InjectView(R.id.goods_no)
    TextView goods_no;
    @InjectView(R.id.goods_kucun)
    TextView goods_kucun;
    @InjectView(R.id.hide_text)
    TextView hide_text;
    @InjectView(R.id.goods_xiaoliang)
    TextView goods_xiaoliang;
    @InjectView(R.id.goods_min_money)
    TextView goods_min_money;
    @InjectView(R.id.goods_max_money)
    TextView goods_max_money;
    @InjectView(R.id.stock_owe)
    TextView stock_owe;
    @InjectView(R.id.customer_text)
    TextView customer_text;
    @InjectView(R.id.goods_details_space)
    TextView goods_details_space;

    //库存及欠货
    @OnClick(R.id.stock_owe)
    void stock_owe() {
        stock_owe.setTextColor(getResources().getColor(R.color.white));
        customer_text.setTextColor(getResources().getColor(R.color.lanse));
        // 背景颜色
        stock_owe.setBackgroundColor(getResources().getColor(R.color.lanse));
        customer_text.setBackgroundColor(getResources().getColor(R.color.white));
        switchFragment(childStcokFragment).commit();
    }

    //库存及欠货
    @OnClick(R.id.customer_text)
    void customer_text() {
        stock_owe.setTextColor(getResources().getColor(R.color.lanse));
        customer_text.setTextColor(getResources().getColor(R.color.white));
        // 背景颜色
        stock_owe.setBackgroundColor(getResources().getColor(R.color.white));
        customer_text.setBackgroundColor(getResources().getColor(R.color.lanse));
        switchFragment(customerFragment).commit();
    }
    public void endRefreshing(){
        childStcokFragment.endRefreshing();
    }
}