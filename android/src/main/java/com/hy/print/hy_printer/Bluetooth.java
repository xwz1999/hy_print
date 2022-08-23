package com.hy.print.hy_printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;

import java.io.IOException;

import cpcl.PrinterHelper;
import io.flutter.plugin.common.EventChannel;

//import com.sewoo.port.android.BluetoothPort;
//import com.sewoo.request.android.RequestHandler;


public class Bluetooth {
    private BluetoothAdapter mBluetoothAdapter;
    //private BluetoothPort bluetoothPort;

    private BroadcastReceiver connectDevice;
    private BroadcastReceiver discoveryResult;
    private BroadcastReceiver searchFinish;
    private BroadcastReceiver searchStart;

    private Thread btThread;

    private boolean searchflags;
    private boolean disconnectflags;

    public void init() {

//        bluetoothPort = BluetoothPort.getInstance();
//        bluetoothPort.SetMacFilter(false);
        bluetoothSetup();
        searchflags = false;
        disconnectflags = false;
        System.out.println("打印机蓝牙初始化完成");
    }

    //初始化连接监听
    public void createConnectBroadcast(Context context, EventChannel.EventSink eventSink) {

        connectDevice = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                    eventSink.success("connected");
                } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                    try {
                        PrinterHelper.portClose();
//                        if (bluetoothPort.isConnected()) {
//                            System.out.println("1111s");
//                            bluetoothPort.disconnect();
//                        }
                        if ((btThread != null) && (btThread.isAlive())) {
                            cancelThread();
                        }
                        System.out.println("disconnected");
                        eventSink.success("disconnected");

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };

        context.registerReceiver(connectDevice, new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED));
        context.registerReceiver(connectDevice, new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED));
        System.out.println("注册连接广播");
    }

    public boolean isValidAddress(String address) {
        String[] btDevAddr = new String[]{"00:12:6f", "00:13:7b", "00:07:80", "88:6b:0f", "40:19:20", "00:01:90", "ec:1b:bd", "58:8e:81", "14:b4:57", "90:fd:9f", "00:0b:57", "68:0a:e2", "84:2e:14", "08:6b:d7", "60:a4:23", "cc:cc:cc", "bc:33:ac", "74:f0:7d", "84:fd:27", "84:71:27", "80:4b:50", "5c:02:72", "cc:86:ec", "2c:11:65", "b4:e3:f9", "0c:43:14", "50:32:5f", "04:cd:15", "8c:f6:81", "90:35:ea"};
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            return false;
        } else {
            for(int i = 0; i < btDevAddr.length; ++i) {
                if (btDevAddr[i].equalsIgnoreCase(address.substring(0, 8))) {
                    return true;
                }
            }

            return false;
        }
    }

    //初始化搜索设备监听
    public void createDiscoveryBroadcast(Context context, EventChannel.EventSink eventSink) {
        discoveryResult = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String key = "";
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String name = remoteDevice.getName();
                System.out.println(name);
                System.out.println(remoteDevice.getAddress());
                System.out.println(remoteDevice.getBluetoothClass().getMajorDeviceClass());
                if (remoteDevice != null && name != null&&remoteDevice.getBluetoothClass().getMajorDeviceClass()==1536) {
                    if (remoteDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                        key = "{" + "\"name\":" + "\"" + name + "\"" + ","
                                + "\"address\":" + "\"" + remoteDevice.getAddress() + "\"" + ","
                                + "\"isPaired\":" + false
                                + "}";

                    } else {

                        key = "{" + "\"name\":" + "\"" + name + "\"" + ","
                                + "\"address\":" + "\"" + remoteDevice.getAddress() + "\"" + ","
                                + "\"isPaired\":" + true
                                + "}";
                    }
                    System.out.println(key);
                    eventSink.success(key);
                    System.out.println(key);
//                    if (isValidAddress(remoteDevice.getAddress())) {
//                        eventSink.success(key);
//                        System.out.println(key);
//                    }
                }

            }
        };
        context.registerReceiver(discoveryResult, new IntentFilter(BluetoothDevice.ACTION_FOUND));
        searchStart = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                eventSink.success("start");
                System.out.println("start");
            }
        };
        context.registerReceiver(searchStart, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_STARTED));

        searchFinish = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                eventSink.success("finish");
                searchflags = true;
                System.out.println("finish");
            }
        };
        context.registerReceiver(searchFinish, new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        System.out.println("注册搜索广播");
    }

    //蓝牙设置
    private void bluetoothSetup() {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            System.out.println("不支持蓝牙");
        }
    }


    //搜索设备
    public void SearchingBTDevice() {
        new CheckTypesTask().execute();
        System.out.println("开始搜索");

    }


    public int btConn(final String address, Context context) throws Exception {
//        bluetoothPort.connect(address);
        final int[] portOpen = {-4};
        btThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    System.out.println(address);
                    portOpen[0] = PrinterHelper.portOpenBT(context,address);
                    PrinterHelper.logcat("portOpen:"+ portOpen[0]);
                    System.out.println(portOpen[0]);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        btThread.start();
        //final int result = PrinterHelper.portOpenBT(context,address);
        //new connBT().execute(address);

        return portOpen[0];
    }

    //断开连接
    public void DisconnectDevice(Context context) {
        try {
           // bluetoothPort.disconnect();
            PrinterHelper.portClose();
            if ((btThread != null) && (btThread.isAlive()))
                btThread.interrupt();

            disconnectflags = true;
            context.unregisterReceiver(connectDevice);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //取消搜索
    public void cancelDiscoveryResult(Context context) {
        System.out.println("取消搜索");
        mBluetoothAdapter.cancelDiscovery();
        context.unregisterReceiver(discoveryResult);
        context.unregisterReceiver(searchStart);
        context.unregisterReceiver(searchFinish);
    }


    public void cancelThread() {
        btThread.interrupt();
        btThread = null;
    }

    private class CheckTypesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            mBluetoothAdapter.startDiscovery();
            super.onPreExecute();
        }

        ;

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                while (true) {
                    if (searchflags)
                        break;

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            searchflags = false;
            super.onPostExecute(result);
        }

        ;
    }



    class connBT extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            Integer retVal = null;

            //bluetoothPort.connect(strings[0]);

            retVal = Integer.valueOf(0);

            return retVal;
        }

        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result.intValue() == 0)    // Connection success.
            {
//                RequestHandler rh = new RequestHandler();
//                btThread = new Thread(rh);
//                btThread.start();
            } else    // Connection failed.
            {

            }
            super.onPostExecute(result);
        }
    }

    public void ExcuteDisconnect(Context context) {
        new ExcuteDisconnectBT().execute(context);
    }

    private class ExcuteDisconnectBT extends AsyncTask<Context, Void, Void> {


        @Override
        protected Void doInBackground(Context... contexts) {
            try {
                DisconnectDevice(contexts[0]);

                while (true) {
                    if (disconnectflags)
                        break;

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        ;


        @Override
        protected void onPostExecute(Void result) {
            disconnectflags = false;
            super.onPostExecute(result);
        }

        ;
    }

}
