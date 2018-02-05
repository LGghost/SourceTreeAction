package cn.order.ordereasy.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import java.text.DecimalFormat;
import java.util.List;

import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.utils.FileUtils;

/**
 * Created by Administrator on 2017/9/12.
 */

public class GoodsSpecsAdapter extends BaseAdapter {

    Context context;
    private List<Product> data = null;

    private MyItemClickListener mItemClickListener;
    private int index = -1;
    private AlertDialog alertDialog;
    private int discount = 100;

    public GoodsSpecsAdapter(Context context, List<Product> data) {
        this.context = context;
        this.data = data;
    }

    /**
     * 设置Item点击监听、、、、
     *
     * @param listener
     */
    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
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
        final ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.kaidan_item_listview_item, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.guige_name);
            viewHolder.shuliang = (TextView) convertView.findViewById(R.id.shuliang);
            viewHolder.jiage = (TextView) convertView.findViewById(R.id.jiage);
            viewHolder.jia = (ImageView) convertView.findViewById(R.id.jia);
            viewHolder.jian = (ImageView) convertView.findViewById(R.id.jian);
            viewHolder.cb_edit = (ImageView) convertView.findViewById(R.id.cb_edit);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Product product = this.data.get(position);
        if (product.getSpec_data().size() > 0) {
            if (product.getSpec_data().size() == 1) {
                viewHolder.name.setText(product.getSpec_data().get(0) + "(" + product.getOwe_num() + ")");
            } else if (product.getSpec_data().size() == 2) {
                viewHolder.name.setText(product.getSpec_data().get(0) + "/" + product.getSpec_data().get(1) + "(" + product.getOwe_num() + ")");
            }
        } else {
            viewHolder.name.setText("无");
        }
        if (product.getNum() != 0) {
            viewHolder.shuliang.setText(product.getNum() + "");
        }
        if (discount != 100) {
            double price = discount / 100.0;
            DecimalFormat df = new DecimalFormat("0.00");
            String result = df.format(price * product.getSell_price());
            product.setDefault_price(Double.parseDouble(result));
            product.setPrice(product.getNum() * product.getDefault_price());
        } else {
            if (product.getDefault_price() != -1) {
                product.setPrice(product.getNum() * product.getDefault_price());
            } else {
                product.setPrice(product.getNum() * product.getSell_price());
            }
        }
        if (product.getDefault_price() != -1) {
            viewHolder.jiage.setText(product.getDefault_price() + "");
        } else {
            viewHolder.jiage.setText(product.getSell_price() + "");
        }
        Totalprice();
        viewHolder.jia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int number = product.getNum();
                number++;
                if (product.getDefault_price() != -1) {
                    product.setPrice(number * product.getDefault_price());
                } else {
                    product.setPrice(number * product.getSell_price());
                }
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
                if (number < 0) {
                    number = 0;
                }
                if (product.getDefault_price() != -1) {
                    product.setPrice(number * product.getDefault_price());
                } else {
                    product.setPrice(number * product.getSell_price());
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
        viewHolder.cb_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialogs(viewHolder.jiage, position);
            }
        });
        return convertView;
    }

    //弹出框
    private void showdialogs(final TextView text, int pos) {
        alertDialog = new AlertDialog.Builder(context).create();
        View view = View.inflate(context, R.layout.tanchuang_view, null);
        alertDialog.setView(view);
        alertDialog.show();
        final Product product = data.get(pos);
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
        ed_type_name.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        //hint内容
        ed_type_name.setText(text.getText().toString());
        ed_type_name.setSelection(ed_type_name.getText().length());
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
                String str = ed_type_name.getText().toString();
                if (!TextUtils.isEmpty(str)) {
                    double number = Double.parseDouble(str);
                    product.setDefault_price(number);
                    product.setPrice(product.getNum() * number);
                    text.setText(ed_type_name.getText().toString());
                    Totalprice();
                    alertDialog.dismiss();
                } else {
                    alertDialog.dismiss();
                }
            }
        });

    }

    //弹出框
    private void showdialogs(final TextView text) {
        alertDialog = new AlertDialog.Builder(context).create();
        View view = View.inflate(context, R.layout.tanchuang_view, null);
        alertDialog.setView(view);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.tanchuang_view);
        final Product p = data.get(index);
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
        if (p.getNum() == 0) {
            ed_type_name.setHint("输入最大值为9999");
        } else {
            ed_type_name.setText(text.getText().toString());
            ed_type_name.setSelection(text.getText().length());
        }
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
                int number = Integer.parseInt(ed_type_name.getText().toString());
                p.setNum(number);
                if (p.getDefault_price() != -1) {
                    p.setPrice(number * p.getDefault_price());
                } else {
                    p.setPrice(number * p.getSell_price());
                }
                text.setText(ed_type_name.getText().toString());
                Totalprice();
                alertDialog.dismiss();
            }
        });
        //监听edittext
        ed_type_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int number = Integer.valueOf(s.toString());
                    if (number > 9999) {
                        ed_type_name.setText(9999 + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void Totalprice() {
        double tPrice = 0;
        int tNumber = 0;
        for (Product product : getData()) {
            tPrice += product.getPrice();
            tNumber += product.getNum();
        }
        if (mItemClickListener != null) {
            mItemClickListener.onInputClick(tPrice, tNumber);
        }
    }

    class ViewHolder {

        public TextView name;
        public TextView jiage;
        public TextView shuliang;
        public ImageView jia;
        public ImageView jian;
        public ImageView cb_edit;

    }

    public interface MyItemClickListener {
        void onInputClick(double tPrice, int tNumber);
    }

    public List<Product> getData() {
        return data;
    }

    public void setData(List<Product> data) {
        this.data = data;
    }
}
