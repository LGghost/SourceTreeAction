package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.TelListAdapter;
import cn.order.ordereasy.bean.ContactInfo;
import cn.order.ordereasy.bean.Customer;
import cn.order.ordereasy.presenter.OrderEasyPresenter;
import cn.order.ordereasy.presenter.OrderEasyPresenterImp;
import cn.order.ordereasy.utils.DataStorageUtils;
import cn.order.ordereasy.view.OrderEasyView;
import cn.order.ordereasy.widget.CharacterParser;
import cn.order.ordereasy.widget.PinyinComparator;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by Mr.Pan on 2017/10/13.
 */

public class TelListActivity extends BaseActivity implements OrderEasyView, EasyPermissions.PermissionCallbacks {

    @InjectView(R.id.return_click)
    ImageView return_click;
    @InjectView(R.id.baocun)
    TextView baocun;
    @InjectView(R.id.tel_listview)
    ListView tel_listview;

    private ContentResolver cr;

    private TelListAdapter telListAdapter;
    private List<ContactInfo> mp = new ArrayList<>();
    OrderEasyPresenter orderEasyPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tel);
        setColor(this, this.getResources().getColor(R.color.lanse));
        //拿到内容访问者
        cr = getContentResolver();
        orderEasyPresenter = new OrderEasyPresenterImp(this);
        ButterKnife.inject(this);
        //拿到内容访问者
        //得到listView
        telListAdapter = new TelListAdapter(this);
        tel_listview.setAdapter(telListAdapter);
        choiceContact();
        tel_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactInfo data = telListAdapter.getData().get(position);
                int isChecked = data.getIsCheck();
                if (isChecked == 1) {
                    telListAdapter.getData().get(position).setIsCheck(0);
                } else {
                    telListAdapter.getData().get(position).setIsCheck(1);
                }
                telListAdapter.notifyDataSetChanged();
            }
        });
    }


    public void getContacts() {
        Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Cursor cs = cr.query(uri, null, null, null, null, null);
        while (cs.moveToNext()) {
            //拿到联系人id 跟name
            int id = cs.getInt(cs.getColumnIndex("_id"));
            String name = cs.getString(cs.getColumnIndex("display_name"));
            //得到这个id的所有数据（data表）
            Uri uri1 = Uri.parse("content://com.android.contacts/raw_contacts/" + id + "/data");
            Cursor cs2 = cr.query(uri1, null, null, null, null, null);
            ContactInfo maps = new ContactInfo();//实例化一个map
            while (cs2.moveToNext()) {
                //得到data这一列 ，包括很多字段
                String data1 = cs2.getString(cs2.getColumnIndex("data1"));
                //得到data中的类型
                String type = cs2.getString(cs2.getColumnIndex("mimetype"));
                String str = type.substring(type.indexOf("/") + 1, type.length());//截取得到最后的类型
                if ("name".equals(str)) {//匹配是否为联系人名字
                    maps.setName(data1);
                }
                if ("phone_v2".equals(str)) {//匹配是否为电话
                    maps.setNumber(data1);
                }
                Log.i("test", data1 + "       " + type);
            }
            mp.add(maps);//将map加入list集合中
        }
        //筛选联系人
        sortData();
        telListAdapter.setData(mp);
        telListAdapter.notifyDataSetChanged();//通知适配器发生数据改变
    }

    void sortData() {
        PinyinComparator pinyinComparator = new PinyinComparator();
        CharacterParser characterParser = CharacterParser.getInstance();
        for (ContactInfo indexModel : mp) {
            String name = "";
            if (TextUtils.isEmpty(indexModel.getName())) {
                name = "-";
            } else {
                name = indexModel.getName();
            }
            String char_name = characterParser.getSelling(name).substring(0, 1).toUpperCase();
            Pattern pattern = Pattern.compile("[a-zA-Z]");
            Matcher matcher = pattern.matcher(char_name);
            //当条件满足时，将返回true，否则返回false
            if (matcher.matches()) {
                indexModel.setTopic(characterParser.getSelling(name).substring(0, 1).toUpperCase());
            } else {
                indexModel.setTopic("#");
            }
            indexModel.setTopic(characterParser.getSelling(name).substring(0, 1).toUpperCase());
        }
        Collections.sort(mp, pinyinComparator);
    }

    @Override
    public void showProgress(int type) {

    }

    @Override
    public void hideProgress(int type) {

    }

    @Override
    public void loadData(JsonObject data, int type) {
        Log.e("TelListActivity", "data:" + data.toString());
        if (data != null) {
            int status = data.get("code").getAsInt();
            if (status == 1) {
                showToast("添加成功！");
                DataStorageUtils.getInstance().setCustomer(true);
                finish();
            }
        }
    }

    private static final int REQUEST_CODE_PERMISSION_TEL = 1;

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_TEL)
    private void choiceContact() {
        String[] perms = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            getContacts();
        } else {
            EasyPermissions.requestPermissions(this, "订货无忧需要以下权限:\n\n1.拨打电话、\n\n2.读取联系人", REQUEST_CODE_PERMISSION_TEL, perms);
        }
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
        if (requestCode == REQUEST_CODE_PERMISSION_TEL) {
            Toast.makeText(this, "您拒绝了「订货无忧」所需要的相关权限!", Toast.LENGTH_SHORT).show();
        }
    }

    public class PinyinComparator implements Comparator<ContactInfo> {

        public int compare(ContactInfo o1, ContactInfo o2) {
            if (o1.getTopic().equals("@") || o2.getTopic().equals("#")) {
                return -1;
            } else if (o1.getTopic().equals("#") || o2.getTopic().equals("@")) {
                return 1;
            } else {
                return o1.getTopic().compareTo(o2.getTopic());
            }
        }

    }

    @OnClick(R.id.return_click)
    void return_click() {
        finish();
    }

    @OnClick(R.id.baocun)
    void baocun() {
        List<Customer> datas = new ArrayList<>();
        for (ContactInfo data : mp) {
            if (data.getIsCheck() == 1) {
                Customer customer = new Customer();
                customer.setName(data.getName());
                customer.setTelephone(data.getNumber());
                datas.add(customer);
            }
        }
        if (datas.size() < 1) {
            showToast("请至少选择一条数据！");
            return;
        }
        orderEasyPresenter.importCustomers(datas);
    }
}
