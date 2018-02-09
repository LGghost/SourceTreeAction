package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.order.ordereasy.R;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Spec;
import cn.order.ordereasy.bean.SpecBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.GsonUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;

/**
 * Created by Administrator on 2017/9/5.
 * <p>
 * 规格管理Activity
 */

public class GuigeGuanliActivity extends BaseActivity implements OrderEasyView {

    int i = 1001;
    OrderEasyPresenter orderEasyPresenter;
    List<Spec> datas = new ArrayList<>();
    List<Spec> datas1 = new ArrayList<>();
    List<String> oldValue1 = new ArrayList<>();
    List<String> oldValue2 = new ArrayList<>();
    private String flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guige_xuanze);
        setColor(this, this.getResources().getColor(R.color.lanse));

        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        Bundle bundle = getIntent().getExtras();

        guige_xuanze_view.setVisibility(View.GONE);
        guige_xuanze_view2.setVisibility(View.GONE);
        if (bundle != null) {
            Goods good = (Goods) bundle.getSerializable("data");
            SpecBean specBean = (SpecBean) bundle.getSerializable("spec");
            flag = bundle.getString("flag");
            datas = good.getSpec();
            datas1 = specBean.getSpec();
            initView();
            initData();
        }
    }

    private void initData() {
        if (datas == null) datas = new ArrayList<>();
        if (datas1.size() == 1) {
            List<String> values = datas1.get(0).getValues();
            if (flag.equals("detail")) {
                oldValue1.addAll(values);
            }
        }else if(datas1.size() == 2){
            List<String> values1 = datas1.get(0).getValues();
            List<String> values2 = datas1.get(1).getValues();
            if (flag.equals("detail")) {
                oldValue1.addAll(values1);
                oldValue2.addAll(values2);
            }
        }
        if (datas.size() == 1) {
            List<String> values = datas.get(0).getValues();
            if (values == null) values = new ArrayList<>();
            if (values.size() > 0) {
                //填充属性标签
                //layout.removeAllViews();
                for (String str : values) {
                    addTag(str);
                }
            }
        } else if (datas.size() == 2) {
            List<String> values1 = datas.get(0).getValues();
            List<String> values2 = datas.get(1).getValues();
            if (values1 == null) values1 = new ArrayList<>();
            if (values2 == null) values2 = new ArrayList<>();
            if (values1.size() > 0) {
                //填充属性标签1
                //layout.removeAllViews();
                for (String str : values1) {
                    addTag(str);
                }
            }
            if (values2.size() > 0) {
                //填充属性标签2
                //layout2.removeAllViews();
                for (String str : values2) {
                    addTag2(str);
                }
            }
        }
    }

    private void initView() {
        //填充选择的数据
        if (datas.size() == 1) {
            guige_xuanze_view.setVisibility(View.VISIBLE);
            guige_xuanze_view2.setVisibility(View.GONE);
            Spec spec = datas.get(0);
            guige_name.setText(spec.getName());
            shuxingku_click.setTag(String.valueOf(spec.getSpec_id()));

        } else if (datas.size() == 2) {
            guige_xuanze_view.setVisibility(View.VISIBLE);
            guige_xuanze_view2.setVisibility(View.VISIBLE);
            Spec spec1 = datas.get(0);
            Spec spec2 = datas.get(1);
            shuxingku_click.setTag(String.valueOf(spec1.getSpec_id()));
            shuxingku_click2.setTag(String.valueOf(spec2.getSpec_id()));
            guige_name.setText(spec1.getName());
            guige_name2.setText(spec2.getName());
        } else {
            guige_xuanze_view.setVisibility(View.GONE);
            guige_xuanze_view2.setVisibility(View.GONE);
        }
    }

    //找到控件ID
    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;

    //保存按钮
    @InjectView(R.id.baocun)
    TextView baocun;

    //规格名称
    @InjectView(R.id.guige_name)
    TextView guige_name;
    @InjectView(R.id.guige_name2)
    TextView guige_name2;

   /* //选择规格
    @InjectView(R.id.text_click)
    TextView text_click;*/

    //删除该规格
    @InjectView(R.id.shanchu)
    ImageView shanchu;
    @InjectView(R.id.shanchu2)
    ImageView shanchu2;

    //添加属性按钮
    @InjectView(R.id.add_shuxing)
    LinearLayout add_shuxing;
    @InjectView(R.id.add_shuxing2)
    LinearLayout add_shuxing2;

    @InjectView(R.id.specs_layout)
    FlowLayout layout;
    @InjectView(R.id.specs_layout2)
    FlowLayout layout2;

    @InjectView(R.id.guige_xuanze_view)
    LinearLayout guige_xuanze_view;
    @InjectView(R.id.guige_xuanze_view2)
    LinearLayout guige_xuanze_view2;

    @InjectView(R.id.shuxingku_click)
    TextView shuxingku_click;
    @InjectView(R.id.shuxingku_click2)
    TextView shuxingku_click2;


    //跳转到选择规格管理列表
    @InjectView(R.id.button_click)
    LinearLayout button_click;


    @OnClick(R.id.shanchu)
    void delete() {
        if (!flag.equals("detail")) {
            Log.e("Guige", "删除1：" + datas.size());
            List<Spec> specs = datas;
            if (datas.get(0).getValues() != null) {
                datas.get(0).getValues().clear();
            }
            specs.remove(0);
            datas = specs;
            Log.e("Guige", "删除1.1：" + datas.size());
            guige_xuanze_view.setVisibility(View.GONE);
        } else {
            ToastUtil.show("编辑不能删除规格");
        }
    }

    @OnClick(R.id.shanchu2)
    void delete2() {
        if (!flag.equals("detail")) {
            List<Spec> specs = datas;
            Log.e("Guige", "删除2：" + datas.size());
            if (datas.size() == 1) {
                if (datas.get(0).getValues() != null) {
                    datas.get(0).getValues().clear();
                }
                specs.remove(0);
                guige_xuanze_view2.setVisibility(View.GONE);
            } else {
                if (datas.get(1).getValues() != null) {
                    datas.get(1).getValues().clear();
                }
                specs.remove(1);
                guige_xuanze_view2.setVisibility(View.GONE);
            }
            datas = specs;
            Log.e("Guige", "删除2.2：" + datas.size());
        } else {
            ToastUtil.show("编辑不能删除规格");
        }
    }

    private void removeView() {
        if (layout.getChildCount() > 1) {
            layout.removeAllViews();
            View view = View.inflate(this, R.layout.guiguanli_add_view, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialogs(1, datas.get(0).getSpec_id());
                }
            });
            layout.addView(view);
        }
        if (layout2.getChildCount() > 1) {
            layout2.removeAllViews();
            View view = View.inflate(this, R.layout.guiguanli_add_view, null);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showdialogs(2, datas.get(1).getSpec_id());
                }
            });
            layout2.addView(view);
        }
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        GuigeGuanliActivity.this.finish();
    }

    //跳转到选择规格管理列表
    @OnClick(R.id.button_click)
    void button_click() {
        if (!flag.equals("detail")) {
            Intent intent = new Intent(GuigeGuanliActivity.this, SpecificationListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, 1001);
        } else {
            ToastUtil.show("编辑时不能变更规格");
        }
    }

    @OnClick(R.id.shuxingku_click)
    void shuxingku_click() {
        Intent intent = new Intent(GuigeGuanliActivity.this, GuigeShuxingSelectListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "guige");
        bundle.putInt("code", 1002);
        bundle.putString("id", shuxingku_click.getTag().toString());
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, 1002);
    }

    @OnClick(R.id.shuxingku_click2)
    void shuxingku_click2() {
        Intent intent = new Intent(GuigeGuanliActivity.this, GuigeShuxingSelectListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", "guige");
        bundle.putInt("code", 1003);
        bundle.putString("id", shuxingku_click2.getTag().toString());
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, 1003);
    }


    @OnClick(R.id.add_shuxing)
    void setAdd_shuxing() {
        showdialogs(1, datas.get(0).getSpec_id());
    }

    @OnClick(R.id.add_shuxing2)
    void setAdd_shuxing2() {
        showdialogs(2, datas.get(1).getSpec_id());
    }

    private static final int REQUEST_CODE_SELECTSPEC_PREVIEW = 1002;

    @OnClick(R.id.baocun)
    void baocun() {
        if (datas.size() < 1) {
            showToast("请选择规格！");
            return;
        }
        for (Spec s : datas) {
            List<String> values = s.getValues();
            if (values == null) values = new ArrayList<>();
            if (values.size() == 0) {
                showToast("请选择一个属性！");
                return;
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        Goods goods = new Goods();
        goods.setSpec(datas);
        bundle.putSerializable("good", goods);
        intent.putExtras(bundle);
        setResult(REQUEST_CODE_SELECTSPEC_PREVIEW, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1001) {
            //获取数据
            Bundle bundle = data.getExtras();
            Goods good = (Goods) bundle.getSerializable("data");
            List<Spec> specs = good.getSpec();
            if (datas.size() == 2) {
                showToast("最多只能选择两个规格，已经做出过滤");
                return;
            }

            if (specs.size() > 0) {
                //选择了规格，判断一下是否已经有此规格，并过滤
                for (Spec s : specs) {
                    boolean f = false;
                    if (datas.size() == 2) {
                        showToast("最多只能选择两个规格，已经做出过滤");
                        break;
                    }
                    if (datas.size() > 0) {
                        for (Spec sp : datas) {
                            if (sp.getSpec_id() == s.getSpec_id()) {
                                f = true;
                            } else {
                                datas.add(s);
                                Log.e("添加：", s.getName());
                            }
                        }
                    } else {
                        datas.add(s);
                    }
                    if (f) {
                        showToast("已经做出过滤");
                    }

                }
            }
            removeView();
            initView();
            initData();
        } else if (resultCode == 1002) {
            //属性1选择
            //获取数据
            Bundle bundle = data.getExtras();
            String v = bundle.getString("data");
            if (TextUtils.isEmpty(v)) return;
            String[] vs = v.split(",");
            if (vs.length < 1) return;
            List<String> oldValues = datas.get(0).getValues();
            if (oldValues == null) oldValues = new ArrayList<>();
            boolean flag = false;
            for (String value : vs) {
                if (oldValues.contains(value)) {
                    flag = true;
                } else {
                    oldValues.add(value);
                    addTag(value);
                }
            }
            if (flag) {
                showToast("相同属性的，已经做出过滤");
            }
            datas.get(0).setValues(oldValues);
            // initView();
        } else if (resultCode == 1003) {
            //属性2选择
            //获取数据
            Bundle bundle = data.getExtras();
            String v = bundle.getString("data");
            List<String> oldValues;
            if (TextUtils.isEmpty(v)) return;
            String[] vs = v.split(",");
            if (vs.length < 1) return;
            if (datas.size() == 1) {
                oldValues = datas.get(0).getValues();
            } else {
                oldValues = datas.get(1).getValues();
            }
            if (oldValues == null) oldValues = new ArrayList<>();
            boolean flag = false;
            for (String value : vs) {
                if (oldValues.contains(value)) {
                    flag = true;
                } else {
                    oldValues.add(value);
                    addTag2(value);
                }
            }
            if (flag) {
                showToast("相同属性的，已经做出过滤");
            }
            if (datas.size() == 1) {
                datas.get(0).setValues(oldValues);
            } else {
                datas.get(1).setValues(oldValues);
            }

        }
    }

    public void addTag(String text) {
        RelativeLayout relativeLayout = new RelativeLayout(this);
        View view = View.inflate(this, R.layout.tag_view, null);
        int x = (int) (Math.random() * 1000);
        relativeLayout.setId(x);
        ImageView delete = (ImageView) view.findViewById(R.id.delete_image_view1);
        TextView shuxing = (TextView) view.findViewById(R.id.selected_text_view1);
        shuxing.setText(text);
        delete.setTag(x);
        if (!oldValue1.contains(text)) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View hiddenView = findViewById((Integer) v.getTag());  //在hidden_view.xml中hidden_layout是root layout
                String text = ((TextView) hiddenView.findViewById(R.id.selected_text_view1)).getText().toString();
                List<String> values = datas.get(0).getValues();
                if (flag.equals("detail")) {
                    if (!oldValue1.contains(text)) {
                        values.remove(text);
                    } else {
                        ToastUtil.show("不能删除原有的属性");
                        return;
                    }
                } else {
                    if (values.contains(text)) values.remove(text);
                }
                datas.get(0).setValues(values);
                if (null != hiddenView) {
                    ViewGroup parent = (ViewGroup) hiddenView.getParent();
                    parent.removeView(hiddenView);
                }
                layout.removeView(v);

            }
        });
        relativeLayout.addView(view);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layout.addView(relativeLayout, lp1);
    }

    public void addTag2(String text) {
        RelativeLayout relativeLayout = new RelativeLayout(this);
        View view = View.inflate(this, R.layout.tag_view, null);
        int x = (int) (Math.random() * 1000);
        relativeLayout.setId(x);
        ImageView delete = (ImageView) view.findViewById(R.id.delete_image_view1);
        delete.setTag(x);
        TextView shuxing = (TextView) view.findViewById(R.id.selected_text_view1);
        shuxing.setText(text);
        if (!oldValue2.contains(text)) {
            delete.setVisibility(View.VISIBLE);
        } else {
            delete.setVisibility(View.GONE);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View hiddenView = findViewById((Integer) v.getTag());  //在hidden_view.xml中hidden_layout是root layout
                String text = ((TextView) hiddenView.findViewById(R.id.selected_text_view1)).getText().toString();
                if (datas.size() > 1) {
                    List<String> values = datas.get(1).getValues();
                    if (flag.equals("detail")) {
                        if (!oldValue2.contains(text)) {
                            values.remove(text);
                        } else {
                            ToastUtil.show("不能删除原有的属性");
                            return;
                        }
                    } else {
                        if (values.contains(text)) values.remove(text);
                    }
                    datas.get(1).setValues(values);
                } else {
                    List<String> values = datas.get(0).getValues();
                    if (flag.equals("detail")) {
                        if (!oldValue2.contains(text)) {
                            values.remove(text);
                        } else {
                            ToastUtil.show("不能删除原有的属性");
                            return;
                        }
                    } else {
                        if (values.contains(text)) values.remove(text);
                    }
                    datas.get(0).setValues(values);
                }
                if (null != hiddenView) {
                    ViewGroup parent = (ViewGroup) hiddenView.getParent();
                    parent.removeView(hiddenView);
                }
                layout2.removeView(v);


            }
        });
        relativeLayout.addView(view);
        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        layout2.addView(relativeLayout, lp1);
    }

    AlertDialog alertDialog;

    private void showdialogs(final int type, final int id) {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("请输入属性名称");
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
        //hint内容
        ed_type_name.setHint("最多输入12个字符");
        //限制输入长度
//        ed_type_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(12)});
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
                String name = ed_type_name.getText().toString();
                if (ed_type_name.getText().toString().equals("无") || ed_type_name.getText().toString().equals("无规格")) {
                    ToastUtil.show(ed_type_name.getText().toString() + "不能用于属性名称");
                    alertDialog.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.show("属性不能为空");
                    alertDialog.dismiss();
                    return;
                } else {
                    if (name.length() > 12) {
                        ToastUtil.show("规格属性最多输入12个字符");
                        alertDialog.dismiss();
                        return;
                    }
                }
                if (type == 1) {
                    List<String> oldValues = datas.get(0).getValues();
                    if (oldValues == null) oldValues = new ArrayList<>();
                    if (oldValues.contains(ed_type_name.getText().toString())) {
                        showToast("相同属性的，已经做出过滤");
                        return;
                    }
                    oldValues.add(ed_type_name.getText().toString());
                    datas.get(0).setValues(oldValues);
                    addTag(ed_type_name.getText().toString());
                } else if (type == 2) {
                    List<String> oldValues = datas.get(1).getValues();
                    if (oldValues == null) oldValues = new ArrayList<>();
                    if (oldValues.contains(ed_type_name.getText().toString())) {
                        showToast("相同属性的，已经做出过滤");
                        return;
                    }
                    oldValues.add(ed_type_name.getText().toString());
                    datas.get(1).setValues(oldValues);
                    addTag2(ed_type_name.getText().toString());
                }
//                orderEasyPresenter.addSpecValueInfo(id, ed_type_name.getText().toString());
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
                    queren.setTextColor(getResources().getColor(R.color.lanse));
                    queren.setEnabled(true);
                } else {
                    queren.setTextColor(getResources().getColor(R.color.touzi_huise));
                    queren.setEnabled(false);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void showProgress(int type) {
        ProgressUtil.showDialog(this);
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
    }

    @Override
    public void loadData(JsonObject data, int type) {

    }
}
