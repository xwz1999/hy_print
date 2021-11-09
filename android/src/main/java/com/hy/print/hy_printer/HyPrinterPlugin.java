package com.hy.print.hy_printer;

import android.content.Context;

import androidx.annotation.NonNull;

import cpcl.PrinterHelper;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * HyPrinterPlugin
 */
public class HyPrinterPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Context context;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "hy_printer");
        channel.setMethodCallHandler(this);
        context = flutterPluginBinding.getApplicationContext();
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("connect")) {
            int code = -4;
            try {
                code = PrinterHelper.portOpenBT(context, (String) call.argument("address"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.success(code);
        } else if (call.method.equals("disConnect")) {
            boolean status = false;
            try {
                status = PrinterHelper.portClose();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.success(status);
        } else if (call.method.equals("getStatus")) {
            int status = -1;
            try {
                status = PrinterHelper.getstatus();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.success(status);
        } else if (call.method.equals("printAsOrder")) {
            int re = PrintAsOrder.print(call.argument("code"), call.argument("fbaCode"), call.argument("country"), call.argument("channel"), call.argument("count"), call.argument("hasPlan"));
            result.success(re);
        } else if (call.method.equals("printBarCode")) {
            int re = -1;
            try {
                PrinterHelper.printAreaSize("0", "200", "200", "500", "1");
                re = PrinterHelper.Barcode(PrinterHelper.BARCODE, PrinterHelper.code128, call.argument("width"), call.argument("ratio"), call.argument("height"), call.argument("x"), call.argument("y"), call.argument("undertext"), "7", call.argument("size"), call.argument("offset"), call.argument("data"));
                PrinterHelper.Form();
                PrinterHelper.Print();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.success(re);
        } else {
            result.notImplemented();
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
