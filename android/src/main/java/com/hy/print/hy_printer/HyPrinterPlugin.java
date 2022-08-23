package com.hy.print.hy_printer;

import android.content.Context;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * HyPrinterPlugin
 */
public class HyPrinterPlugin implements FlutterPlugin, MethodCallHandler {

//    private MethodChannel channel;
//    private Context context;

    private MethodChannel channel;
    private EventChannel discoveryChannel;
    private EventChannel connectChannel;
    private Context context;
    private static final String DISCOVERYDEVICE = "com.discovery.devices";
    private static final String CONNECT = "com.connect";
    PrintAsOrder printAsOrder;
    Bluetooth bluetooth = new Bluetooth();

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "hy_printer");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
        discoveryChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), DISCOVERYDEVICE);

        connectChannel = new EventChannel(flutterPluginBinding.getBinaryMessenger(), CONNECT);
        discoveryChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                bluetooth.createDiscoveryBroadcast(context, events);
            }

            @Override
            public void onCancel(Object arguments) {
                bluetooth.cancelDiscoveryResult(context);
            }
        });
        connectChannel.setStreamHandler(new EventChannel.StreamHandler() {
            @Override
            public void onListen(Object arguments, EventChannel.EventSink events) {
                bluetooth.createConnectBroadcast(context, events);
            }

            @Override
            public void onCancel(Object arguments) {
                bluetooth.ExcuteDisconnect(context);
            }
        });
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result)  {
        if (call.method.equals("startSearch")) {
            bluetooth.SearchingBTDevice();
        } else if (call.method.equals("connect")) {
            int code = -4;
            String arg = call.argument("address");
            System.out.println(arg);
            try {
                code = bluetooth.btConn(arg,context);
                System.out.println(code);
                //PrinterHelper.portOpenBT(context,"04:7F:0E:95:FD:A1");
                result.success(code);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call.method.equals("init")) {
            System.out.println("打印机初始化");
            bluetooth.init();
            printAsOrder = new PrintAsOrder();

            result.success(true);
        } else if (call.method.equals("newPrint")) {
            System.out.println("newPrint");
            String code = call.argument("code");
            String barCode = call.argument("barCode");
            String channel = call.argument("channel");
            String country = call.argument("country");
            String num = call.argument("num");
            String sum = call.argument("sum");
            int offset = call.argument("offset");
            boolean hasPlan = call.argument("hasPlan");

            printAsOrder.printAsCodeNew(code, barCode, channel, country, num, sum, offset, hasPlan);
            result.success(true);
        } else if (call.method.equals("getStatus")) {
            int sta = 0;
            try {
                sta = printAsOrder.getStatus();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.success(sta);
        } else if (call.method.equals("disposeDiscovery")) {
            bluetooth.cancelDiscoveryResult(context);
            result.success(true);
        } else if (call.method.equals("disposeConnect")) {
            bluetooth.ExcuteDisconnect(context);
            result.success(true);
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
