package cn.order.ordereasy.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.ShangHuoAdapter;
import cn.order.ordereasy.bean.Goods;
import cn.order.ordereasy.bean.Product;
import cn.order.ordereasy.bean.Spec;
import cn.order.ordereasy.bean.SpecBean;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.Config;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.utils.MyLog;
import cn.order.ordereasy.utils.ProgressUtil;
import cn.order.ordereasy.utils.SystemfieldUtils;
import cn.order.ordereasy.utils.ToastUtil;
import cn.order.ordereasy.view.OrderEasyView;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2017/9/4.
 * <p>
 * 上货Activity
 */

public class ShangHuoActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, OrderEasyView, ShangHuoAdapter.OnItemClickLitener {

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;

    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;


    private static final int REQUEST_CODE_SELECTTYPE_PREVIEW = 1001;
    private static final int REQUEST_CODE_SELECTSPEC_PREVIEW = 1002;
    private static final int REQUEST_CODE_DESCRIPTION_PREVIEW = 1003;
    private static final int REQUEST_CODE_PRICE_PREVIEW = 1004;

    private Goods good = new Goods();
    private OrderEasyPresenter orderEasyPresenter;
    private boolean isBaocun = false;
    String flag = "shanghuo";

    AlertDialog alertDialog;
    ShangHuoAdapter shangHuoAdapter;
    private int imagNumber = 0;
    List<String> datas = new ArrayList<>();
    List<String> uploadData = new ArrayList<>();
    SpecBean specBean;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.shanghuo);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        shangHuoAdapter = new ShangHuoAdapter(this);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        shanghuo_recyclerview.setLayoutManager(linearLayoutManager);
        shanghuo_recyclerview.setAdapter(shangHuoAdapter);
        shangHuoAdapter.setOnItemClickLitener(this);
        specBean = new SpecBean();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            eidtData(bundle);
        }
        getSharedPreferences("price", 0).edit().clear().commit();
    }

    private void eidtData(Bundle bundle) {
        good = (Goods) bundle.getSerializable("data");
        flag = bundle.getString("flag");
        shanghuo_no.setText(good.getGoods_no());
        shanghuo_no.setTextColor(getResources().getColor(R.color.darkgray));
        shanghuo_name.setText(good.getTitle());
        shanghuo_fenlei.setText(good.getCategory_name());
        if (good.getIs_enable_stock_warn() == 1) {
            warning_togbtn.setChecked(true);
            warning_low.setText(good.getMin_stock_warn_num() + "");
            warning_high.setText(good.getMax_stock_warn_num() + "");
        } else {
            warning_togbtn.setChecked(false);
        }
        if (good.getIs_hidden_price() == 1) {
            mTogBtn.setChecked(true);
        }
        if (good.getIs_hidden_store() == 1) {
            stTogBtn.setChecked(true);
        }
        if (good.getIs_hidden_sales_num() == 1) {
            svTogBtn.setChecked(true);
        }
        List<Spec> specs = good.getSpec();

        StringBuilder sb = new StringBuilder();
        if (specs.size() > 0) {
            good.setSpec(specs);
            for (Spec s : specs) {
                List<String> values = s.getValues();
                if (values == null) values = new ArrayList<>();
                if (s.getSpec_id() == 0) {
                    return;
                }
                sb.append(s.getName() + "：");
                for (int i = 0; i < values.size(); i++) {
//                        sb.append(values.get(i));
//                        if (i != values.size()) {
//                            sb.append("/");
//                        }
                    if (i != 0) {
                        sb.append("/");
                    }
                    sb.append(values.get(i));
                }
                if (specs.size() > 1) {
                    sb.append("\n");
                }
            }
        }

        if (TextUtils.isEmpty(sb.toString())) {
            shanghuo_spec.setText("无规格");
        } else {
            shanghuo_spec.setText(sb.toString());
        }
        String desc = good.getDescription();
        if (!TextUtils.isEmpty(desc)) {
            shanghuo_desc.setText("已填写");
        }
        good.setDescription(desc);
        shanghuo_price.setText("已填写");
        List<Map<String, Object>> images = (List<Map<String, Object>>) good.getImages();

        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            strings.add(Config.URL_HTTP + "/" + images.get(i).get("url"));
        }
        uploadData = strings;
        shangHuoAdapter.setData(strings, false);
        shangHuoAdapter.setImages(images);
        shanghuo_no.setFocusable(false);
        specBean.setSpec(good.getSpec());
        delete_btn.setVisibility(View.VISIBLE);
    }

    //返回按钮
    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.shang_huo_scroll)
    ScrollView shang_huo_scroll;
    @InjectView(R.id.shop_type)
    TextView shop_type;
    @InjectView(R.id.shanghuo_fenlei)
    TextView shanghuo_fenlei;
    @InjectView(R.id.shanghuo_desc)
    TextView shanghuo_desc;
    @InjectView(R.id.shanghuo_spec)
    TextView shanghuo_spec;
    @InjectView(R.id.shanghuo_price)
    TextView shanghuo_price;

    //添加图片
    @InjectView(R.id.shanghuo_add_imageview)
    ImageView shanghuo_add_imageview;
    //RecyclerView
    @InjectView(R.id.shanghuo_recyclerview)
    RecyclerView shanghuo_recyclerview;
    //保存
    @InjectView(R.id.baocun)
    TextView baocun;
    //上传图片点击控件
    @InjectView(R.id.shangchuan_image)
    LinearLayout shangchuan_image;
    @InjectView(R.id.saoyisao)
    ImageView saoyisao;

    //商品规格点击选择控件
    @InjectView(R.id.guige)
    LinearLayout guige;
    //商品价格点击选择控件
    @InjectView(R.id.jiage)
    LinearLayout jiage;
    //商品分类选择控件
    @InjectView(R.id.shangping_type)
    LinearLayout shangping_type;


    //商品描述选择控件
    @InjectView(R.id.shangping_miaoshu)
    LinearLayout shangping_miaoshu;

    //商品描述选择控件
    @InjectView(R.id.mTogBtn)
    ToggleButton mTogBtn;

    @InjectView(R.id.warning_togbtn)
    ToggleButton warning_togbtn;
    @InjectView(R.id.stTogBtn)
    ToggleButton stTogBtn;
    @InjectView(R.id.svTogBtn)
    ToggleButton svTogBtn;
    @InjectView(R.id.shanghuo_no)
    EditText shanghuo_no;

    @InjectView(R.id.shanghuo_name)
    EditText shanghuo_name;
    @InjectView(R.id.warning_low)
    EditText warning_low;
    @InjectView(R.id.warning_high)
    EditText warning_high;
    @InjectView(R.id.delete_btn)
    Button delete_btn;

    //添加图片点击事件
    @OnClick(R.id.shanghuo_add_imageview)
    void shanghuo_add_imageview() {
        // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
        File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");
        startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 8 - shangHuoAdapter.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
//        startActivityForResult(BGAPhotoPickerActivity.newIntent(this, null, 16 - shangHuoAdapter.getItemCount(), null, false), REQUEST_CODE_CHOOSE_PHOTO);
    }

    //需要的点击事件
    //返回按钮
    @OnClick(R.id.return_click)
    void return_click() {
        showdialogs(0);
    }


    //扫一扫点击事件
    @OnClick(R.id.saoyisao)
    void saoyisao() {
        if (SystemfieldUtils.isCameraUseable()) {
            Intent intent = new Intent(ShangHuoActivity.this, ScanActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            Bundle bundle = new Bundle();
            bundle.putInt("code", 9001);
            intent.putExtras(bundle);
            startActivityForResult(intent, 9001);
        } else {
            ToastUtil.show(getString(R.string.open_permissions));
        }
    }

    //规格管理点击事件
    @OnClick(R.id.guige)
    void guige() {
        if (flag.equals("detail")) {
            List<Spec> specs = good.getSpec();
            if (specs != null) {
                if (specs.size() > 0) {
                    if (TextUtils.isEmpty(specs.get(0).getName())
                            || specs.get(0).getName().equals("无")
                            || specs.get(0).getName().equals("无规格")) {
                        showdialogs(2);
                        return;
                    }
                } else {
                    showdialogs(2);
                    return;
                }
            } else {
                showdialogs(2);
                return;
            }

        }
        Intent intent = new Intent(ShangHuoActivity.this, GuigeGuanliActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", flag);
        bundle.putSerializable("data", good);
        bundle.putSerializable("spec", specBean);
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_SELECTSPEC_PREVIEW);
    }

    //价格管理点击事件
    @OnClick(R.id.jiage)
    void jiage() {
        Intent intent = new Intent(ShangHuoActivity.this, PriceSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("flag", flag);
        bundle.putSerializable("good", good);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_PRICE_PREVIEW);
    }

    //商品分类点击事件
    @OnClick(R.id.shangping_type)
    void shangping_type() {
        Intent intent = new Intent(ShangHuoActivity.this, FenleiGuanliActivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "shanghuo");
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_SELECTTYPE_PREVIEW);
        //startActivity(intent);
    }

    //开关点击事件
    @OnClick(R.id.mTogBtn)
    void mTogBtn() {

    }


    @OnClick(R.id.shangping_miaoshu)
    void miaoshu() {
//        setFocusable(false);
        Intent intent = new Intent(ShangHuoActivity.this, ShangpinDescribeAvtivity.class);
        //利用bundle来存取数据
        Bundle bundle = new Bundle();
        bundle.putString("flag", "shanghuo");
        bundle.putString("data", good.getDescription());
        //再把bundle中的数据传给intent，以传输过去
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_CODE_DESCRIPTION_PREVIEW);
    }

    @OnClick(R.id.baocun)
    void baocun() {
        Log.e("ShangHuoActivity", "图片：" + datas.size());
        datas.removeAll(uploadData);
        if (datas != null && datas.size() > 0) {
            ProgressUtil.showDialog(this);
            File file = new File(datas.get(0));
            Log.e("ShangHuo", "file长度：" + file.length());
            uploadData.add(datas.get(0));
            orderEasyPresenter.uploadGoodImg(datas.get(0));
        } else {
            uploadData();
        }
    }

    @OnClick(R.id.delete_btn)
    void delete_btn() {
        showdialogs(1);
    }

    private void uploadData() {
        //遍历找出最大和最小金额
        List<Product> products = good.getProduct_list();
        List<Map<String, Object>> images = (List<Map<String, Object>>) good.getImages();
        if (images != null) {
            if (images.size() > 0) {
                good.setCover(images.get(0).get("url").toString());//封面
            }
        }
        String no = shanghuo_no.getText().toString();
        if (TextUtils.isEmpty(no)) {
            showToast("请输入商品编号");
            return;
        }
        String name = shanghuo_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            showToast("请输入商品名称");
            return;
        }
        if (products == null) products = new ArrayList<>();
        double min = 0, max = 0;
        boolean isFind = false;
        for (Product product : products) {
            if (product.getSell_price() > 0) {
                isFind = true;
                break;
            }
        }
        if (products.size() < 1) {
            showToast("请选择价格！");
            return;
        }
        if (!isFind) {
            showToast("请重新设置价格！");
            return;
        }
        if (flag.equals("detail")) {
            if (!equalList(specBean.getSpec(), good.getSpec()) && !isBaocun) {
                showToast("请重新设置价格！");
                return;
            }
        }
        if (warning_togbtn.isChecked()) {
            good.setIs_enable_stock_warn(1);
            if (warning_high.getText().toString().equals("")) {
                ToastUtil.show("请输入最高库存预警数量");
                return;
            }
            if (warning_low.getText().toString().equals("")) {
                ToastUtil.show("请输入最低库存预警数量");
                return;
            }
            if (Integer.parseInt(warning_high.getText().toString()) > Integer.parseInt(warning_low.getText().toString())) {
                good.setMax_stock_warn_num(Integer.parseInt(warning_high.getText().toString()));
                good.setMin_stock_warn_num(Integer.parseInt(warning_low.getText().toString()));
            } else {
                ToastUtil.show("最低库存预警不能大于最高库存预警");
                return;
            }
        } else {
            good.setIs_enable_stock_warn(0);
        }

        good.setMin_price(min);
        good.setMax_price(max);
        good.setGoods_num(products.size());

        good.setCreate_time(System.currentTimeMillis() + "");

        good.setGoods_no(no);
        if (mTogBtn.isChecked()) {
            good.setIs_hidden_price(1);
        } else {
            good.setIs_hidden_price(0);
        }
        if (svTogBtn.isChecked()) {
            good.setIs_hidden_sales_num(1);
        } else {
            good.setIs_hidden_sales_num(0);
        }
        if (stTogBtn.isChecked()) {
            good.setIs_hidden_store(1);
        } else {
            good.setIs_hidden_store(0);
        }

        if (warning_togbtn.isChecked()) {
            good.setIs_enable_stock_warn(1);
            good.setMax_stock_warn_num(Integer.parseInt(warning_high.getText().toString()));
            good.setMin_stock_warn_num(Integer.parseInt(warning_low.getText().toString()));
        } else {
            good.setIs_enable_stock_warn(0);
        }
        good.setTitle(name);
        if (flag.equals("detail")) {
            ProgressUtil.showDialog(this);
            orderEasyPresenter.updateGood(good);
        } else {
            ProgressUtil.showDialog(this);
            orderEasyPresenter.addGoods(good);
        }
    }

    //弹出框
    private void showdialogs(final int type) {
        alertDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.tanchuang_view, null);
        alertDialog.setView(view);

        alertDialog.show();
        Window window = alertDialog.getWindow();
        //window.setContentView(view);
        window.setContentView(R.layout.tanchuang_view_textview);
        //标题
        TextView title_name = (TextView) window.findViewById(R.id.title_name);
        title_name.setText("温馨提示");
        TextView text_conten = (TextView) window.findViewById(R.id.text_conten);
        View view1 = window.findViewById(R.id.view1);
        //按钮1点击事件
        TextView quxiao = (TextView) window.findViewById(R.id.quxiao);
        if (type == 0) {
            text_conten.setText("您确认要退出货品编辑吗？");
            quxiao.setText("继续编辑");
        } else if (type == 1) {
            text_conten.setText("您确认要删除货品吗？");
            quxiao.setText("继续编辑");
        } else {
            text_conten.setText("为了确保数据准确，无规格货品不能再次修改规格");
            quxiao.setVisibility(View.GONE);
            view1.setVisibility(View.GONE);

        }


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
                if (type == 0) {
                    ShangHuoActivity.this.finish();
                } else if (type == 1) {
                    orderEasyPresenter.goodsDel(good.getGoods_id());
                }
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(this, shangHuoAdapter.getItemCount(), shangHuoAdapter.getmData(), shangHuoAdapter.getmData(), position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (requestCode == REQUEST_CODE_PERMISSION_PHOTO_PICKER) {
            Toast.makeText(this, "您拒绝了「订货无忧」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            int size = shangHuoAdapter.getmData().size() + BGAPhotoPickerActivity.getSelectedImages(data).size();
            if (size < 9) {
                shangHuoAdapter.setData(BGAPhotoPickerActivity.getSelectedImages(data), true);
                //一张一张上传图片
                datas = shangHuoAdapter.getmData();
            } else {
                ToastUtil.show("上传图片已达上限");
            }

        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            //预览图片
//            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));

        } else if (resultCode == REQUEST_CODE_SELECTTYPE_PREVIEW) {
            //获取数据
            Bundle bundle = data.getExtras();
            String type = bundle.getString("type");
            String id = bundle.getString("id");
            good.setCategory_id(Integer.parseInt(id));
            good.setCategory_name(type);
            shanghuo_fenlei.setText(type);
        } else if (resultCode == REQUEST_CODE_SELECTSPEC_PREVIEW) {
            //获取规格数据
            Bundle bundle = data.getExtras();
            Goods goods = (Goods) bundle.getSerializable("good");
            List<Spec> specs = goods.getSpec();
            StringBuilder sb = new StringBuilder();
            if (specs.size() > 0) {
                good.setSpec(specs);
                for (Spec s : specs) {
                    List<String> values = s.getValues();
                    if (values == null) values = new ArrayList<>();
                    sb.append(s.getName() + "：");
                    for (int i = 0; i < values.size(); i++) {

                        if (i != 0) {
                            sb.append("/");
                        }
                        sb.append(values.get(i));

                    }
                    if (specs.size() > 1) {
                        sb.append("\n");
                    }
                }
            }
            if (TextUtils.isEmpty(sb.toString())) {
                shanghuo_spec.setText("无");
            }
            shanghuo_spec.setText(sb.toString());
            shanghuo_spec.setTextSize(13);
        } else if (resultCode == REQUEST_CODE_DESCRIPTION_PREVIEW) {
            Bundle bundle = data.getExtras();
            String desc = bundle.getString("desc");
            if (!TextUtils.isEmpty(desc)) {
                shanghuo_desc.setText("已填写");
            }
            Log.e("ShanghuoActivity", "setFocusable");
//            setFocusable(true);
            good.setDescription(desc);
        } else if (resultCode == REQUEST_CODE_PRICE_PREVIEW) {
            shanghuo_price.setText("已填写");
            SharedPreferences sp = getSharedPreferences("price", 0);
            List<Spec> specs = good.getSpec();
            if (specs == null) specs = new ArrayList<>();
            List<Product> products = new ArrayList<>();
            String nonePrice = sp.getString("无", "");
            double noneCb = 0;
            double noneXs = 0;
            if (!TextUtils.isEmpty(nonePrice)) {
                String[] prices = nonePrice.split(",");
                if (prices.length > 0) {
                    noneCb = Double.parseDouble(prices[0]);
                    noneXs = Double.parseDouble(prices[1]);
                }
            }
            if (specs.size() > 0) {
                //如果已经选择了规格
                if (specs.size() == 1) {
                    List<String> values = specs.get(0).getValues();
                    if (values == null) values = new ArrayList<>();
                    if (values.size() > 0) {
                        for (String value : values) {
                            double cb = 0, xs = 0;
                            Product product = new Product();
                            List<String> spec_data = new ArrayList<>();
                            spec_data.add(value);
                            product.setSpec_data(spec_data);
                            if (flag.equals("detail")) {
                                setProductId(product);
                            }
                            //遍历属性价格
                            String price = sp.getString(value, "");
                            Log.e("ShangHuoActivity", "price:" + price);
                            if (!TextUtils.isEmpty(price)) {
                                String[] prices = price.split(",");
                                if (prices.length > 0) {
                                    double CB = Double.parseDouble(prices[0]);
                                    double XS = Double.parseDouble(prices[1]);
                                    if (CB > cb) cb = CB;
                                    if (XS > xs) xs = XS;

                                }
                            }
                            product.setSell_price(xs);
                            product.setCost_price(cb);
                            products.add(product);
                        }

                    } else {
                        Product product = new Product();
                        product.setSpec_data(Arrays.asList(new String[]{"无"}));
                        product.setSell_price(noneXs);
                        product.setCost_price(noneCb);
                        products.add(product);
                    }

                } else {
                    List<String> values = specs.get(0).getValues();
                    List<String> values1 = specs.get(1).getValues();
                    if (values1.size() == 0) {
                        for (String value : values) {
                            double cb = 0, xs = 0;
                            Product product = new Product();
                            List<String> spec_data = new ArrayList<>();
                            spec_data.add(value);
                            product.setSpec_data(spec_data);
                            if (flag.equals("detail")) {
                                setProductId(product);
                            }
                            //遍历属性价格
                            String price = sp.getString(value, "");
                            Log.e("ShangHuoActivity", "price:" + price);
                            if (!TextUtils.isEmpty(price)) {
                                String[] prices = price.split(",");
                                if (prices.length > 0) {
                                    double CB = Double.parseDouble(prices[0]);
                                    double XS = Double.parseDouble(prices[1]);
                                    if (CB > cb) cb = CB;
                                    if (XS > xs) xs = XS;

                                }
                            }
                            product.setSell_price(xs);
                            product.setCost_price(cb);
                            products.add(product);
                        }
                    }
                    if (values.size() == 0) {
                        for (String value : values1) {
                            double cb = 0, xs = 0;
                            Product product = new Product();
                            List<String> spec_data = new ArrayList<>();
                            spec_data.add(value);
                            product.setSpec_data(spec_data);
                            if (flag.equals("detail")) {
                                setProductId(product);
                            }
                            //遍历属性价格
                            String price = sp.getString(value, "");
                            Log.e("ShangHuoActivity", "price:" + price);
                            if (!TextUtils.isEmpty(price)) {
                                String[] prices = price.split(",");
                                if (prices.length > 0) {
                                    double CB = Double.parseDouble(prices[0]);
                                    double XS = Double.parseDouble(prices[1]);
                                    if (CB > cb) cb = CB;
                                    if (XS > xs) xs = XS;

                                }
                            }
                            product.setSell_price(xs);
                            product.setCost_price(cb);
                            products.add(product);
                        }
                    }
                    if (values.size() != 0 && values1.size() != 0) {
                        for (int i = 0; i < values.size(); i++) {
                            for (int j = 0; j < values1.size(); j++) {
                                double cb = 0, xs = 0;
                                Product product = new Product();
                                List<String> spec_data = new ArrayList<>();
                                spec_data.add(values.get(i));
                                spec_data.add(values1.get(j));
                                product.setSpec_data(spec_data);
                                if (flag.equals("detail")) {
                                    setProductId(product);
                                }
                                //遍历属性价格
                                String suxing = values.get(i) + "/" + values1.get(j);
                                String price = sp.getString(suxing, "");
                                Log.e("ShangHuoActivity", "price:" + price);
                                if (!TextUtils.isEmpty(price)) {
                                    String[] prices = price.split(",");
                                    Log.e("ShangHuoActivity", "成本：" + prices[0] + "销售：" + prices[1]);
                                    if (prices.length > 0) {
                                        double CB = Double.parseDouble(prices[0]);
                                        double XS = Double.parseDouble(prices[1]);
                                        if (CB > cb) cb = CB;
                                        if (XS > xs) xs = XS;

                                    }
                                }
                                Log.e("ShangHuoActivity", "cb：" + cb + "xs：" + xs);
                                product.setSell_price(xs);
                                product.setCost_price(cb);
                                products.add(product);
                            }
                        }
                    }

                }

            } else {
                //如果没有选择规格
                Product product = new Product();
                product.setSpec_data(Arrays.asList(new String[]{"无"}));
                product.setSell_price(noneXs);
                product.setCost_price(noneCb);
                products.add(product);
            }
            good.setProduct_list(products);
        } else if (resultCode == 9001) {
            Bundle bundle = data.getExtras();
            String res = bundle.getString("data");
            if (TextUtils.isEmpty(res)) {
                showToast("未扫描到任何结果");
            } else {

                shanghuo_no.setText(res);
            }
            MyLog.e("扫一扫返回数据", res);
        }
    }

    @Override
    public void showProgress(int type) {
    }

    @Override
    public void hideProgress(int type) {
        ProgressUtil.dissDialog();
        if (type == 0 && datas.size() > 0) {
            imagNumber++;
            if (imagNumber < datas.size()) {
                uploadData.add(datas.get(imagNumber));
                orderEasyPresenter.uploadGoodImg(datas.get(imagNumber));
            } else {
                ProgressUtil.dissDialog();
                uploadData();
            }
        }
    }

    @Override
    public void loadData(JsonObject data, int type) {
        Message message = new Message();
        switch (type) {
            case 0:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1001;

                }
                break;
            case 1:
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1002;

                }
                break;
            case 2:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1003;
                }
                break;
            case 3:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1004;

                }
                break;
            case 4:
                message = new Message();
                if (data == null) {
                    message.what = 1007;
                } else {
                    message.what = 1005;

                }


                break;
            default:
                break;
        }
        message.obj = data;
        handler.sendMessage(message);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1001:
                    JsonObject result = (JsonObject) msg.obj;
                    Log.e("ShangHuoActivity", result.toString());
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            showToast("删除成功！");
                            ProgressUtil.dissDialog();
                            Intent intent = new Intent();
                            intent.putExtra("flag", "delete");
                            setResult(1001, intent);
                            finish();
                        }
                    }

                    break;
                case 1002:
                    result = (JsonObject) msg.obj;
                    Log.e("ShangHuoActivity", result.toString());
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            showToast("添加成功！");
                            SharedPreferences sp = getSharedPreferences("price", 0);
                            Map<String, ?> key_Value = (Map<String, ?>) sp.getAll();
                            for (Map.Entry<String, ?> entry : key_Value.entrySet()) {
                                sp.edit().remove(entry.getKey()).commit();
                            }
                            if (!flag.equals("detail")) {
                                DataStorageUtils.getInstance().setShanghuo(true);
                            }
                            ProgressUtil.dissDialog();
                            setResult(1001);
                            finish();
                        }
                    }
                    ProgressUtil.dissDialog();
                    Log.e("添加商品信息", result.toString());
                    break;
                case 1004:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            imagNumber++;
                            //成功
                            JsonObject res = result.get("result").getAsJsonObject();
                            List<Map<String, Object>> images = (List<Map<String, Object>>) good.getImages();
                            if (images == null) images = new ArrayList<>();
                            Map<String, Object> image = new HashMap();
                            image.put("title", res.get("title").getAsString());
                            image.put("url", res.get("file").getAsString());
                            images.add(image);
                            good.setImages(images);
                            Log.e("ShangHuo", "imagNumber：" + imagNumber);
                            Log.e("ShangHuo", " datas.size()：" + datas.size());
                            if (imagNumber < datas.size()) {
                                uploadData.add(datas.get(imagNumber));
                                orderEasyPresenter.uploadGoodImg(datas.get(imagNumber));
                            }
                            if (imagNumber == datas.size()) {
                                showToast("上传图片成功！");
                                ProgressUtil.dissDialog();
                                uploadData();
                            }
                        }
                    }
                    Log.e("图片信息", result.toString());
                    break;
                case 1005:
                    result = (JsonObject) msg.obj;
                    if (result != null) {
                        int status = result.get("code").getAsInt();
                        if (status == 1) {
                            //成功
                            showToast("修改成功！");
                            ProgressUtil.dissDialog();
                            Intent intent = new Intent();
                            intent.putExtra("flag", "detail");
                            setResult(1001, intent);
                            finish();
                        }
                    }
                    Log.e("修改信息", result.toString());
                    break;
            }
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            showdialogs(0);
        }
        return super.onKeyDown(keyCode, event);

    }

    private void setProductId(Product product) {
        isBaocun = true;
        for (int i = 0; i < good.getProduct_list().size(); i++) {
            if (equalList(product.getSpec_data(), good.getProduct_list().get(i).getSpec_data())) {
                product.setProduct_id(good.getProduct_list().get(i).getProduct_id());
            }
        }
    }

    public boolean equalList(List list1, List list2) {
        return (list1.size() == list2.size()) && list1.containsAll(list2);
    }
}
