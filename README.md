# hy_printer

汉印打印机插件
仅支持安卓
###
在build.gradle中 需要把minSdkVersion 改为19
权限配置中加入
```dart

    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
```
##扫描设备
```dart
HyPrinter.getDevices();
```
##连接设备
```dart
HyPrinter.connect();
```
##打印安速面单(模版固定，暂不支持更改)
```dart
HyPrinter.printAsOrder();
```
##打印机状态
```dart
HyPrinter.getStatus();
```
