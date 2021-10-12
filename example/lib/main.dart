import 'dart:async';

import 'package:flutter/material.dart';
import 'package:hy_printer/hy_printer.dart';
import 'package:hy_printer_example/scan_page.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    if (!mounted) return;
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return const MaterialApp(
      home: Home(),
    );
  }
}

class Home extends StatefulWidget {
  const Home({Key? key}) : super(key: key);

  @override
  _HomeState createState() => _HomeState();
}

class _HomeState extends State<Home> {
  int status = -1;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
          child: Column(
        children: [
          TextButton(
              onPressed: () {
                Navigator.push(context, MaterialPageRoute(builder: (context) {
                  return const ScanPage();
                }));
              },
              child: const Text('扫描设备')),
          TextButton(
              onPressed: () async {
                await HyPrinter.disConnect();
              },
              child: const Text('断开连接')),
          TextButton(
              onPressed: () async {
                status = await HyPrinter.getStatus();
                setState(() {});
              },
              child: Text('$status')),
          TextButton(
              onPressed: () async {
                await HyPrinter.printAsOrder(
                    'ASSZ202112120001', '', "", '', "2/10");
                setState(() {});
              },
              child: const Text('打印非FBA面单')),
          TextButton(
              onPressed: () async {
                await HyPrinter.printAsOrder('ASSZ202112120001',
                    'FBA15RY33MN8U00001', "欧洲特快", '法国', "2/10");
                setState(() {});
              },
              child: const Text('打印FBA面单')),
          TextButton(
              onPressed: () async {
                await HyPrinter.printBarCode(0, 0, "2", "1", "130", "45", "26",
                    true, "8", "14", "12345678");
                setState(() {});
              },
              child: const Text('打印条码')),
        ],
      )),
    );
  }
}
