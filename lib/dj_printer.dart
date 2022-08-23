import 'dart:async';
import 'dart:convert';
import 'dart:typed_data';
import 'dart:ui';


import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:hy_printer/status_enum.dart';

class DjPrinter {
  static late final DjPrinter _instance = DjPrinter._();

  DjPrinter._();

  factory DjPrinter() => _instance;
  static const MethodChannel _channel = MethodChannel('hy_printer');
  static const EventChannel _deviceChannel =
  EventChannel("com.discovery.devices");
  StreamSubscription? _discoveryStream;

  StreamSubscription addDiscoveryListen(
      {required void Function(dynamic data) onReceive,
        void Function()? onStart,
        void Function()? onFinish}) {
    if (_discoveryStream == null) {
      return _deviceChannel.receiveBroadcastStream().listen((data) {
        if (data == "start" && onStart != null) {
          onStart();
        } else if (data == "finish" && onFinish != null) {
          onFinish();
        } else {
          onReceive(data);
        }
      });
    } else {
      return _discoveryStream!;
    }
  }

  void cancelDiscovery() {
    _discoveryStream?.cancel();
    _discoveryStream = null;
    print('结束搜索');
    disposeDiscovery();
  }

  static const EventChannel _connectChannel = EventChannel("com.connect");
  StreamSubscription? _connectStream;

  StreamSubscription addConnectListen({required void Function() onConnect,
    required void Function() onDisconnect}) {
    if (_connectStream == null) {
      return _connectChannel.receiveBroadcastStream().listen((data) {
        if (data == 'connected') {
          onConnect();
        } else if (data == 'disconnected') {
          onDisconnect();
        }
      });
    } else {
      return _connectStream!;
    }
  }

  void cancelConnect() {
    if (_connectStream != null) {
      _connectStream!.cancel();
      _connectStream = null;
    }
  }

  void get startSearch {
    final res = _channel.invokeMethod('startSearch');
  }

  bool _hasInit = false;

  bool get hasInit => _hasInit;

  Future<bool?> connect(String address) async {

    await Future.delayed(const Duration(milliseconds: 2000), () async {
      var res = await _channel.invokeMethod('connect', {'address': address});
      print('connect');
      print(res);
    });
    return true;
  }

  void disposeDiscovery() {
    print('disposeDiscovery');
    final res = _channel.invokeMethod('disposeDiscovery');
  }

  Future<bool?> disposeConnect() async {
    final res = await _channel.invokeMethod('disposeConnect');
    return res;
  }

  Future<bool?> init() async {
    final res = await _channel.invokeMethod('init');
    _hasInit = true;
    return res;
  }

  //0 normal
  //1 busy
  //2 paper empty
  //4 cover open
  //8 battery low
  // Future<PRINT_STATUS?> getStatus() async {
  //   final res = await _channel.invokeMethod('getStatus');
  //   switch (res) {
  //     case 0:
  //       return PRINT_STATUS.normal;
  //     case 1:
  //       return PRINT_STATUS.busy;
  //     case 2:
  //       return PRINT_STATUS.paperEmpty;
  //     case 4:
  //       return PRINT_STATUS.coverOpen;
  //     case 8:
  //       return PRINT_STATUS.batteryLow;
  //     default:
  //       return null;
  //   }
  // }



  //0 normal
  //-1 发送失败
  //2 paper empty
  //6 cover open

  Future<PRINT_STATUS?> getStatus() async {
    final res = await _channel.invokeMethod('getStatus');
    switch (res) {
      case 0:
        return PRINT_STATUS.normal;
      case -1:
        return PRINT_STATUS.fail;
      case 2:
        return PRINT_STATUS.paperEmpty;
      case 6:
        return PRINT_STATUS.coverOpen;
      default:
        return null;
    }
  }

  Future<bool?> printNewAScode({required String code,
    required String barCode,
    required String channel,
    required String country,
    required String num,
    required String sum,
    required int offset,
    required bool hasPlan}) async {
    final res = await _channel.invokeMethod('newPrint', {
      'code': code,
      'barCode':barCode,
      'channel': channel,
      'country': country,
      'num': num,
      'sum': sum,
      'offset': offset,
      'hasPlan': hasPlan,
    });
    return res;
  }




  Future<bool?> printAScode({required String code,
    required String channel,
    required String country,
    required String countStr,
    required int offset,
    required bool hasPlan}) async {
    final res = await _channel.invokeMethod('print', {
      'code': code,
      'channel': channel,
      'country': country,
      'countStr': countStr,
      'offset': offset,
      'hasPlan': hasPlan,
    });
    return res;
  }





  }

