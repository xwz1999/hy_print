import 'dart:async';
import 'dart:convert';
import 'package:bot_toast/bot_toast.dart';
import 'package:flutter/material.dart';
import 'package:hy_printer/device.dart';
import 'package:hy_printer/dj_printer.dart';
import 'package:hy_printer/hy_printer.dart';
import 'package:hy_printer_example/scan_page.dart';
// import 'package:permission_handler/permission_handler.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  List<Device> devices = [];
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Future<void> initPlatformState() async {
  //   if (!mounted) return;
  //   setState(() {});
  // }
  Future<void> initPlatformState() async {
    // var per = await Permission.bluetooth.isGranted;
    // if (!per) {
    //   Permission.bluetooth.request();
    // }
    // var pers = await Permission.locationWhenInUse.isGranted;
    // if (!pers) {
    //   Permission.locationWhenInUse.request();
    // }
    // var per1 = await Permission.bluetoothScan.isGranted;
    // if (!per1) {
    //   Permission.bluetoothScan.request();
    // }
    // var per2 = await Permission.bluetoothConnect.isGranted;
    // if (!per2) {
    //   Permission.bluetoothConnect.request();
    // }

    DjPrinter().init();
    DjPrinter().addDiscoveryListen(onReceive: (data) {
      var js = json.decode(data.toString());
      if ((js['name'] as String).startsWith('HM')) {
        devices.add(Device(
            name: js['name'],
            address: js['address'],
            isPaired: js['isPaired']));
      }



      setState(() {});
    }, onStart: () {
      print("————————————————————————");
    }, onFinish: () {
      print('——————————————————————————————');
      DjPrinter().cancelDiscovery();
    });
    DjPrinter().addConnectListen(onConnect: () {
      print("connected");
    }, onDisconnect: () {
      print('disconnected');
    });
  }



  @override
  Widget build(BuildContext context) {return MaterialApp(
    title: '安速货运',
    builder: (context, widget) {
      // ScreenUtil.setContext(context);
      return MediaQuery(
          data: MediaQuery.of(context).copyWith(textScaleFactor: 1.0),
          child: BotToastInit().call(context, widget));
    },
    navigatorObservers: [BotToastNavigatorObserver()],
    home: const ScanPage()

  );
  }
}


