package cn.order.ordereasy.view.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.order.ordereasy.R;
import cn.order.ordereasy.adapter.BluetoothActivityAdapter;
import cn.order.ordereasy.utils.ProgressUtil;

import static cn.order.ordereasy.view.activity.BaseActivity.setColor;

public class BluetoothActivity extends Activity implements AdapterView.OnItemClickListener {
    private BluetoothAdapter adapter;
    private BluetoothActivityAdapter blueAdapter;
    private final int PERMISSION_REQUEST_COARSE_LOCATION = 0xb01;
    private List<HashMap<String, String>> list = new ArrayList<>();
    private UUID uuid = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    private BluetoothDevice device = null;
    private BluetoothSocket clientSocket = null;
    private OutputStream outputStream = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth_activity);
        setColor(this, this.getResources().getColor(R.color.lanse));
        ButterKnife.inject(this);
        intData();
    }

    private void intData() {
        listview.setOnItemClickListener(this);
        // 检查设备是否支持蓝牙
        adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null) return;
        // 打开蓝牙
        if (!adapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            // 设置蓝牙可见性，最多300秒
            intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(intent);
        }
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        if (devices != null && devices.size() > 0) {

            for (int i = 0; i < devices.size(); i++) {
                BluetoothDevice device = (BluetoothDevice) devices.iterator().next();
                Log.e("BluetoothActivity", "名字：" + device.getName() + "地址：" + device.getAddress());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", device.getName());
                map.put("address", device.getAddress());
                list.add(map);
            }
            blueAdapter = new BluetoothActivityAdapter(this, list);
            listview.setAdapter(blueAdapter);
            receiver = null;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_COARSE_LOCATION);
                }
            }
            sendSearch();
        }
    }

    private void sendSearch() {
        // 设置广播信息过滤
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 注册广播接收器，接收并处理搜索结果
        this.registerReceiver(receiver, intentFilter);
        // 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
        adapter.startDiscovery();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.e("BluetoothActivity", "action:" + action);
            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                ProgressUtil.showDialog(BluetoothActivity.this);
            }
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("BluetoothActivity", "名字：" + device.getName() + "地址：" + device.getAddress());
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", device.getName());
                map.put("address", device.getAddress());
                list.add(map);
            }
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.e("BluetoothActivity", "扫描结束.");
                ProgressUtil.dissDialog();
                blueAdapter = new BluetoothActivityAdapter(BluetoothActivity.this, list);
                listview.setAdapter(blueAdapter);
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @InjectView(R.id.bluetooth_list_view)
    ListView listview;

    //返回按钮
    @OnClick(R.id.back_click)
    void back_click() {
        finish();
    }

    @OnClick(R.id.dayin_click)
    void dayin_click() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HashMap<String, String> map = blueAdapter.getItem(position);
        String address = map.get("address");
        Log.e("BluetoothActivity", "address:" + address);
        //主动连接蓝牙服务端
        try {
            //判断当前是否正在搜索
            if (adapter.isDiscovering()) {
                adapter.cancelDiscovery();
            }
            try {
                if (device == null) {
                    //获得远程设备
                    device = adapter.getRemoteDevice(address);
                }
                if (clientSocket == null) {
                    //创建客户端蓝牙Socket
                    clientSocket = device.createRfcommSocketToServiceRecord(uuid);
                    // 连接建立之前的先配对
                    if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                        Method creMethod = BluetoothDevice.class
                                .getMethod("createBond");
                        creMethod.invoke(device);
                    }
                    //开始连接蓝牙，如果没有配对则弹出对话框提示我们进行配对
                    clientSocket.connect();
                    //获得输出流（客户端指向服务端输出文本）
                    outputStream = clientSocket.getOutputStream();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (outputStream != null) {
                //往服务端写信息
                outputStream.write("蓝牙信息来了".getBytes("utf-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}