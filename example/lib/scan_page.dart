import 'dart:convert';

import 'package:bot_toast/bot_toast.dart';
import 'package:flutter/material.dart';
import 'package:hy_printer/device.dart';
import 'package:hy_printer/dj_printer.dart';
import 'package:hy_printer/hy_printer.dart';

class ScanPage extends StatefulWidget {
  const ScanPage({Key? key}) : super(key: key);

  @override
  _ScanPageState createState() => _ScanPageState();
}

class _ScanPageState extends State<ScanPage> {
  List<Device> devices = [];
  var cancel;
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
      cancel();
    });
    DjPrinter().addConnectListen(onConnect: () {
      print("connected");
    }, onDisconnect: () {
      print('disconnected');
    });
    print('jieshu');
  }



  @override
  Widget build(BuildContext context) {

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: const Text('设备列表',style: TextStyle(color: Colors.white),),
      ),
      body: Center(
        child: Column(
          children: [
            TextButton(
                onPressed: () {
                  devices.clear();
                  setState(() {});
                  cancel =
                      BotToast.showLoading(wrapToastAnimation: (controller, func, child) {
                        return discoveryLoadingWidget();
                      });
                  DjPrinter().startSearch;
                },
                child: const Text('扫描设备')),
            // TextButton(onPressed: () {}, child: const Text('打印')),
            const SizedBox(
              height: 20,
            ),
            ...devices
                .map((e) => TextButton(
                onPressed: ()async {

                 await DjPrinter().connect(e.address);


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
                  DjPrinter().printNewAScode(
                    code: 'ASSZ000000002',
                    barCode: 'ASSZ0000000020001',
                    channel: '加拿大温哥华海派快线-卡派 / UPS派送',
                    country: '美国啊啊',
                    num: '2',
                    sum:'99',
                    offset: 0,
                    hasPlan: true, );
                },
                child: const Text('nocode打印')),
            TextButton(
                onPressed: () {

                  DjPrinter().printNewAScode(
                    code: 'ASSZ000000002',
                    barCode: 'ASSZ0000000020001',
                    channel: '加拿大温哥华海派快线-卡派 / UPS派送',
                    country: '美国',
                    num: '2',
                    sum:'999',
                    offset: 0,
                    hasPlan: false, );
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
    );
  }


  Container discoveryLoadingWidget() {
    return Container(
      padding: const EdgeInsets.all(15),
      decoration: const BoxDecoration(
          color: Colors.black54,
          borderRadius: BorderRadius.all(Radius.circular(8))),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          CircularProgressIndicator(
            backgroundColor: Colors.white,
          ),

          Text(
            '扫描中……请稍作等待',
            style: TextStyle(
              color: Colors.white,
              fontSize: 18,
              fontWeight: FontWeight.bold,
            ),
          )
        ],
      ),
    );
  }
}
