package cn.order.ordereasy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.FileUtils;

public class StockChildAdapter extends BaseAdapter {

    Context context;
    private List<Product> data = null;
    private AlertDialog alertDialog;
    private onItemClickListener mItemClickListener;
    private int index = -1;
    private int sign;

    public StockChildAdapter(Context context, List<Product> data, int sign) {
        this.context = context;
        this.data = data;
        this.sign = sign;
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(onItemClickListener listener) {
        this.mItemClickListener = listener;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ProductsViewHolder viewHolder;
        Log.e("StockChildAdapter", "变化:");
        if (convertView == null) {
            viewHolder = new ProductsViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.ruku_item_two, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.shuliang);
            viewHolder.yuan_kucun = (TextView) convertView.findViewById(R.id.yuan_kucun);
            viewHolder.jia = (ImageView) convertView.findViewById(R.id.jia);
            viewHolder.jian = (ImageView) convertView.findViewById(R.id.jian);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        final Product product = this.data.get(position);
        if (product.getSpec_data().size() > 0) {
            if (product.getSpec_data().size() == 1) {
                viewHolder.name.setText(product.getSpec_data().get(0));
            } else if (product.getSpec_data().size() == 2) {
                viewHolder.name.setText(product.getSpec_data().get(0) + "/" + product.getSpec_data().get(1));

            }
        } else {
            viewHolder.name.setText("无");
        }
        viewHolder.yuan_kucun.setText(String.valueOf(product.getStore_num()));
        if (product.getNum() != 0) {
            viewHolder.shuliang.setText(product.getNum() + "");
        }
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getNum();
                number++;
                product.setNum(number);
                viewHolder.shuliang.setText(number + "");
                Totalprice();
            }
        });
        viewHolder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getNum();
                number--;
                if (sign != 2) {
                    if (number < 0) {
                        number = 0;
                    }
                }
                product.setNum(number);
                viewHolder.shuliang.setText(number + "");
                Totalprice();
            }
        });

        viewHolder.shuliang.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    index = position;
                    showdialogs(viewHolder.shuliang);
                }
                return false;
            }
        });

        return convertView;
    }

    //弹出框
    private void showdialogs(final TextView text) {
        alertDialog = new AlertDialog.Builder(context).create();
        View view = View.inflate(context, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入");
        //输入框
        final EditText ed_type_name = (EditText) window.findViewById(R.id.ed_type_name);

        //给 输入空间 添加焦点监听
        ed_type_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
        //让 数据框 请求焦点
        ed_type_name.requestFocus();
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);

        //hint内容
        ed_type_name.setHint("输入操作数最大为9999");
        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});

        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        quxiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        //按钮2确认点击事件
        final TextView queren = (TextView) window.findViewById(R.id.queren);
        queren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = ed_type_name.getText().toString();
                if (!TextUtils.isEmpty(number)) {
                    int num = Integer.parseInt(number);
                    if (num > 9999) {
                        num = 9999;
                    }
                    if (sign != 2) {
                        if (num < 0) {
                            num = 0;
                        }
                    }
                    Product p = data.get(index);
                    p.setNum(num);
                    text.setText(ed_type_name.getText().toString());
                    Totalprice();
                }
                alertDialog.dismiss();
            }
        });
    }

    public void Totalprice() {
        int tNumber = 0;
        for (Product product : getData()) {
            tNumber += Math.abs(product.getNum());
        }
        Log.e("StockChildAdapter", "tNumber1:" + tNumber);
        if (mItemClickListener != null) {
            mItemClickListener.onInputClick(tNumber);
        }
    }

    class ProductsViewHolder {
        public TextView name;
        public TextView yuan_kucun;
        public TextView shuliang;
        public ImageView jia;
        public ImageView jian;

    }

    public interface onItemClickListener {
        void onInputClick(int tNumber);
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}