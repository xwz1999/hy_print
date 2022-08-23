import 'dart:async';
import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:hy_printer/device.dart';
import 'package:hy_printer/dj_printer.dart';
import 'package:hy_printer/hy_printer.dart';
import 'package:hy_printer_example/scan_page.dart';
import 'package:permission_handler/permission_handler.dart';

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
    var per = await Permission.bluetooth.isGranted;
    if (!per) {
      Permission.bluetooth.request();
    }
    var pers = await Permission.locationWhenInUse.isGranted;
    if (!pers) {
      Permission.locationWhenInUse.request();
    }
    var per1 = await Permission.bluetoothScan.isGranted;
    if (!per1) {
      Permission.bluetoothScan.request();
    }
    var per2 = await Permission.bluetoothConnect.isGranted;
    if (!per2) {
      Permission.bluetoothConnect.request();
    }

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
    home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: Column(
          children: [
            TextButton(
                onPressed: () {
                  devices.clear();
                  setState(() {});
                  DjPrinter().startSearch;
                },
                child: const Text('扫描设备')),
            // TextButton(onPressed: () {}, child: const Text('打印')),
            const SizedBox(
              height: 20,
            ),
            ...devices
                .map((e) => TextButton(
                onPressed: () {
                  [Permission.bluetoothConnect,].request().then((value) async {
                    print("===============${value[Permission.bluetoothConnect]}");
                    if(value[Permission.bluetoothConnect]!=PermissionStatus.denied){
                      await DjPrinter().connect(e.address);
                    }
                  });
                //

                },
                child: Column(
                  children: [
                    Text(e.name),
                    Text(e.address),
                  ],
                )))
                .toList(),
            const SizedBox(
              height: 20,
            ),
            TextButton(
                onPressed: () {

                  // DjPrinter().printNewAScode(
                  //   code: 'ASSZ000000002',
                  //   barCode: 'ASSZ0000000020001',
                  //   channel: '加拿大温哥华海派快线-',
                  //   country: '美国',
                  //   num: '2',
                  //   sum:'9',
                  //   offset: 0,
                  //   hasPlan: true, );

                  DjPrinter().printNewAScode(
                    code: 'ASSZ000000002',
                    barCode: 'ASSZ0000000020001',
                    channel: '加拿大温哥华海派快线-卡派 / UPS派送',
                    country: '美国啊啊',
                    num: '2',
                    sum:'999',
                    offset: 0,
                    hasPlan: true, );
                  // DjPrinter().printNewAScode(
                  //   code: 'ASSZ000000002',
                  //   barCode: 'ASSZ0000000020001',
                  //   channel: '加拿大温哥华海派快线-卡派 / UPS派送',
                  //   country: '捷克斯洛伐克',
                  //   num: '2',
                  //   sum:'99',
                  //   offset: 0,
                  //   hasPlan: true, );
                },
                child: const Text('打印')),
            TextButton(
                onPressed: () {

                  // DjPrinter().printNewAScode(
                  //   code: 'ASSZ000000002',
                  //   barCode: 'ASSZ0000000020001',
                  //   channel: '加拿大温哥华海派快线-',
                  //   country: '美国',
                  //   num: '2',
                  //   sum:'9',
                  //   offset: 0,
                  //   hasPlan: true, );

                  DjPrinter().printNewAScode(
                    code: 'ASSZ000000002',
                    barCode: 'ASSZ0000000020001',
                    channel: '加拿大温哥华海派快线-卡派 / UPS派送',
                    country: '美国啊啊',
                    num: '2',
                    sum:'999',
                    offset: 0,
                    hasPlan: false, );
                  // DjPrinter().printNewAScode(
                  //   code: 'ASSZ000000002',
                  //   barCode: 'ASSZ0000000020001',
                  //   channel: '加拿大温哥华海派快线-卡派 / UPS派送',
                  //   country: '捷克斯洛伐克',
                  //   num: '2',
                  //   sum:'99',
                  //   offset: 0,
                  //   hasPlan: true, );
                },
                child: const Text('noplan打印')),
            const SizedBox(
              height: 20,
            ),
            TextButton(
                onPressed: () {
                  DjPrinter().disposeConnect();
                },
                child: const Text('取消链接')),

          ],
        ),
      ),
    ),
  );
  }
}


