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
  var cancel;
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {

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
  Widget build(BuildContext context) {return MaterialApp(
    title: '安速货运',
    builder: (context, widget) {
      // ScreenUtil.setContext(context);
      return MediaQuery(
          data: MediaQuery.of(context).copyWith(textScaleFactor: 1.0),
          child: BotToastInit().call(context, widget));
    },
    navigatorObservers: [BotToastNavigatorObserver()],
    home:  Scaffold(
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
                BotToast.showLoading(wrapToastAnimation: (controller, func, child) {
                  return discoveryLoadingWidget();
                });


                await DjPrinter().connect(e.address).then((value) {
                  print("main value result");
                  print(value);
                  if(value!=null&&value){
                    BotToast.closeAllLoading();
                  }
                });
                // Future.delayed(Duration(seconds: 3), () async {
                //   if(value==0){
                //     BotToast.closeAllLoading();
                //   }
                // });


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
  )

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


