package cn.order.ordereasy.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import cn.order.ordereasy.R;

public class GuideDialog {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private int type;
    private AlertDialog alertDialog;
    private int index = 0;
    private int[] shopImages = {R.drawable.sygnt01, R.drawable.sygnt02, R.drawable.sygnt03, R.drawable.sygnt04};
    private int[] shelvesImages = {R.drawable.hjydt01, R.drawable.hjydt02, R.drawable.hjydt03, R.drawable.hjydt04};
    private int[] shelvesDetails = {R.drawable.hjxqyd01, R.drawable.hjxqyd02, R.drawable.hjxqyd03, R.drawable.hjxqyd04};
    private int[] orderImages = {R.drawable.ddydt01, R.drawable.ddydt02, R.drawable.ddydt03, R.drawable.ddydt04};
    private int[] orderDetails = {R.drawable.ddxqyd01, R.drawable.ddxqyd02, R.drawable.ddxqyd03, R.drawable.ddxqyd04};
    private int[] customerImages = {R.drawable.khydt01, R.drawable.khydt02, R.drawable.khydt03, R.drawable.khydt04};
    private int[] customerHomepage = {R.drawable.khzyyd01, R.drawable.khzyyd02, R.drawable.khzyyd03, R.drawable.khzyyd04};

    public GuideDialog(int type, Context context) {
        this.type = type;
        sharedPreferences = context.getSharedPreferences("guidedata",
                context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (!isFrist()) {
            return;
        }
        alertDialog = new MyDialog(context);
        View view = View.inflate(context, R.layout.tanchuang_view, null);
        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.guide_shop_layout);
        setShopView(window);

    }
    public void setGuide(){
        editor.putBoolean("shop", true);
        editor.putBoolean("shelves", true);
        editor.putBoolean("shelvesDetails", true);
        editor.putBoolean("order", true);
        editor.putBoolean("orderDetails", true);
        editor.putBoolean("customer", true);
        editor.putBoolean("customerHomepage", true);
        editor.commit();
    }
    private boolean isFrist() {
        switch (type) {
            case 1://店铺主页
                if (sharedPreferences.getBoolean("shop", true)) {
                    return true;
                }
                break;
            case 2://货架
                if (sharedPreferences.getBoolean("shelves", true)) {
                    return true;
                }
                break;
            case 3://货架详情
                if (sharedPreferences.getBoolean("shelvesDetails", true)) {
                    return true;
                }
                break;
            case 4://订单
                if (sharedPreferences.getBoolean("order", true)) {
                    return true;
                }
                break;
            case 5://订单详情
                if (sharedPreferences.getBoolean("orderDetails", true)) {
                    return true;
                }
                break;
            case 6://客户
                if (sharedPreferences.getBoolean("customer", true)) {
                    return true;
                }
                break;
            case 7://客户主页
                if (sharedPreferences.getBoolean("customerHomepage", true)) {
                    return true;
                }
                break;
        }
        return false;
    }

    private void setShopView(Window window) {
        final ImageView image = (ImageView) window.findViewById(R.id.guide_dialog_image);
        setImageResource(image, 0);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < 3) {
                    index++;
                    setImageResource(image, index);
                } else {
                    saveGuideState();
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void setImageResource(ImageView imageView, int index) {
        switch (type) {
            case 1://店铺主页
                imageView.setImageResource(shopImages[index]);
                break;
            case 2://货架
                imageView.setImageResource(shelvesImages[index]);
                break;
            case 3://货架详情
                imageView.setImageResource(shelvesDetails[index]);
                break;
            case 4://订单
                imageView.setImageResource(orderImages[index]);
                break;
            case 5://订单详情
                imageView.setImageResource(orderDetails[index]);
                break;
            case 6://客户
                imageView.setImageResource(customerImages[index]);
                break;
            case 7://客户主页
                imageView.setImageResource(customerHomepage[index]);
                break;
        }

    }

    private void saveGuideState() {
        switch (type) {
            case 1://店铺主页
                editor.putBoolean("shop", false);
                editor.commit();
                break;
            case 2://货架
                editor.putBoolean("shelves", false);
                editor.commit();
                break;
            case 3://货架详情
                editor.putBoolean("shelvesDetails", false);
                editor.commit();
                break;
            case 4://订单
                editor.putBoolean("order", false);
                editor.commit();
                break;
            case 5://订单详情
                editor.putBoolean("orderDetails", false);
                editor.commit();
                break;
            case 6://客户
                editor.putBoolean("customer", false);
                editor.commit();
                break;
            case 7://客户主页
                editor.putBoolean("customerHomepage", false);
                editor.commit();
                break;
        }
    }
}
