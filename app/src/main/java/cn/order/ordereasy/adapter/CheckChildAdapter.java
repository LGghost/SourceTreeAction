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
import android.widget.TextView;

import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.FileUtils;


public class CheckChildAdapter extends BaseAdapter {

    Context context;
    private List<Product> data = null;
    private onItemClickListener mItemClickListener;
    private int index = -1;
    private AlertDialog alertDialog;

    public CheckChildAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
        Log.e("CheckChildAdapter", "data:" + data.size());
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
                    R.layout.check_child_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.shuliang = (EditText) convertView.findViewById(R.id.shuliang);
            viewHolder.jia = (ImageView) convertView.findViewById(R.id.jia);
            viewHolder.jian = (ImageView) convertView.findViewById(R.id.jian);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ProductsViewHolder) convertView.getTag();
        }
        final Product product = this.data.get(position);
        if (product.getSpec_data().size() > 0) {
            if (product.getSpec_data().size() == 1) {
                viewHolder.name.setText(FileUtils.cutOutString(6, product.getSpec_data().get(0)));
            } else if (product.getSpec_data().size() == 2) {
                viewHolder.name.setText(FileUtils.cutOutString(4, product.getSpec_data().get(0)) + "/" + FileUtils.cutOutString(4, product.getSpec_data().get(1)));
            }
        } else {
            viewHolder.name.setText("无");
        }

        if (product.getOperate_num() != 0) {
            viewHolder.shuliang.setText(product.getOperate_num() + "");
        }
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getOperate_num();
                number++;
                product.setOperate_num(number);
                viewHolder.shuliang.setText(number + "");
                viewHolder.shuliang.setSelection(String.valueOf(number).length());
                Totalprice();
            }
        });
        viewHolder.jian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getOperate_num();
                number--;
                if (number < 0) {
                    number = 0;
                }
                product.setOperate_num(number);
                viewHolder.shuliang.setText(number + "");
                viewHolder.shuliang.setSelection(String.valueOf(number).length());
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
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER);

        //hint内容
        ed_type_name.setHint("输入盘点数最大为9999");
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
                    Product p = data.get(index);
                    p.setOperate_num(num);
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
            tNumber += product.getOperate_num();
        }
        Log.e("StockChildAdapter", "tNumber1:" + tNumber);
        if (mItemClickListener != null) {
            mItemClickListener.onInputClick(tNumber);
        }
    }

    class ProductsViewHolder {
        public TextView name;
        public EditText shuliang;
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