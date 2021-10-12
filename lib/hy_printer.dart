import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_blue/flutter_blue.dart';
import 'package:hy_printer/device.dart';

class HyPrinter {
  static const MethodChannel _channel = MethodChannel('hy_printer');
  static FlutterBlue flutterBlue = FlutterBlue.instance;

  static void init() {}

  static Future<List<Device>> getDeiveces() async {
    var devices = <Device>[];
    await flutterBlue.startScan(timeout: const Duration(seconds: 4));
    flutterBlue.scanResults.listen((results) {
      for (ScanResult r in results) {
        //过滤名称为空的蓝牙
        if (r.device.name.isNotEmpty) {
          devices.add(Device(name: r.device.name, address: r.device.id.id));
        }
      }
    });
    await flutterBlue.stopScan();
    return devices;
  }

  ///0：连接成功
  ///-1：连接超时
  ///-2：地址格式错误
  ///-3：打印机与sdk不匹配
  ///-4：连接失败
  static Future<int> connect(String address) async {
    int result = await _channel.invokeMethod('connect', {'address': address});
    return result;
  }

  static Future<bool> disConnect() async {
    bool result = await _channel.invokeMethod('disConnect');
    return result;
  }

  ///0:打印机正常
  ///-1：发送失败
  ///2：缺纸
  ///3：开盖
  static Future<int> getStatus() async {
    int result = await _channel.invokeMethod(
      'getStatus',
    );
    return result;
  }

  static Future<int> printBarCode(
    int direction,
    int type,
    String width,
    String ratio,
    String height,
    String x,
    String y,
    bool undertext,
    String size,
    String offset,
    String data,
  ) async {
    int result = await _channel.invokeMethod('printBarCode', {
      'direction': direction,
      'type': type,
      'width': width,
      'ratio': ratio,
      'height': height,
      'x': x,
      'y': y,
      'undertext': undertext,
      'size': size,
      'offset': offset,
      'data': data,
    });
    return result;
  }

  static Future<int> printLine(
    String x0,
    String y0,
    String x1,
    String y1,
  ) async {
    int result = await _channel
        .invokeMethod('printLine', {'x0': x0, 'x1': x1, 'y0': y0, 'y1': y1});
    return result;
  }

  static Future<int> align(String align) async {
    int result = await _channel.invokeMethod('align', {'align': align});
    return result;
  }

  static Future<int> setBold(String bold) async {
    int result = await _channel.invokeMethod('setBold', {'bold': bold});
    return result;
  }

  static Future<int> prefeed(String prefeed) async {
    int result = await _channel.invokeMethod('setBold', {'prefeed': prefeed});
    return result;
  }

  static Future<int> printAsOrder(String code, String fbaCode, String channel,
      String country, String count) async {
    int result = await _channel.invokeMethod('printAsOrder', {
      'code': code,
      'fbaCode': fbaCode,
      'channel': channel,
      'country': country,
      'count': count,
    });
    return result;
  }
}
