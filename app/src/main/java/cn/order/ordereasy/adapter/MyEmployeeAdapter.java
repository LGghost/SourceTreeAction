package cn.order.ordereasy.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Image;
import cn.order.ordereasy.bean.MyEmployee;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.view.activity.EmployeeInformationActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static cn.order.ordereasy.R.id.empimg;

/**
 * Created by Administrator on 17-9-26.
 */
public class MyEmployeeAdapter extends BaseAdapter {
    public List<MyEmployee> list;
    public Context context;
    public int flag = 0;
    public ListView list_view;
    public int current;

    public MyEmployeeAdapter(List<MyEmployee> list, Context context, ListView list_view) {
        this.list = list;
        this.context = context;
        this.list_view = list_view;
    }

    public List<MyEmployee> getData() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View layout = LayoutInflater.from(context).inflate(R.layout.employee_adapter, null);

        TextView bore = (TextView) layout.findViewById(R.id.bore);
        ImageView delete = (ImageView) layout.findViewById(R.id.delete);
        ImageView imageView = (ImageView) layout.findViewById(empimg);
        TextView name = (TextView) layout.findViewById(R.id.name);
        imageView.setBackgroundResource(R.drawable.bg_user);
        name.setText(list.get(i).name);
        List<Integer> role = list.get(i).getAuth_group_ids();
        String boss = "";
        String shop_keeper = "";
        String salesperson = "";
        String godown_man = "";
        if (list.get(i).is_boss == 1) {
            boss = context.getString(R.string.employee_boss);
            delete.setVisibility(View.GONE);
            bore.setText("(" + boss + ")");
            bore.setTextColor(context.getResources().getColor(R.color.shouye_hongse));
        } else {
            delete.setVisibility(View.VISIBLE);
            for (int m = 0; m < role.size(); m++) {
                if (role.get(m) == 0) {
                } else if (role.get(m) == 1) {
                    shop_keeper = context.getString(R.string.shop_keeper) + "/";
                } else if (role.get(m) == 2) {
                    salesperson = context.getString(R.string.salesperson) + "/";
                } else if (role.get(m) == 3) {
                    godown_man = context.getString(R.string.godown_man);
                }
            }
            String power = shop_keeper + salesperson + godown_man;
            if (power.endsWith("/")) {
                power = power.substring(0, power.length() - 1);
            }
            bore.setText("(" + power + ")");
            bore.setTextColor(context.getResources().getColor(R.color.shouye_lanse));
        }
        if (!TextUtils.isEmpty(list.get(i).getAvatar())) {
            ImageLoader.getInstance().displayImage(list.get(i).getAvatar(), imageView);
        } else {
            imageView.setImageResource(R.drawable.bg_user);
        }
        return layout;
    }
}
