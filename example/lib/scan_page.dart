import 'package:flutter/material.dart';
import 'package:hy_printer/device.dart';
import 'package:hy_printer/hy_printer.dart';

class ScanPage extends StatefulWidget {
  const ScanPage({Key? key}) : super(key: key);

  @override
  _ScanPageState createState() => _ScanPageState();
}

class _ScanPageState extends State<ScanPage> {
  List<Device> _devices = [];

  @override
  void initState() {
    init();
    super.initState();
  }

  Future init() async {
    _devices = await HyPrinter.getDeiveces();
    setState(() {});
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('设备列表'),
      ),
      body: ListView(
          padding: const EdgeInsets.all(10),
          children: _devices
              .map((e) => TextButton(
                  onPressed: () async {
                    var result = await HyPrinter.connect(e.address);
                    if (result == 0) {
                      Navigator.pop(context);
                    }
                  },
                  child: Column(
                    children: [
                      Text(e.name),
                      Text(e.address),
                    ],
                  )))
              .toList()),
    );
  }
}
