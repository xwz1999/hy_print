#import "HyPrinterPlugin.h"

@implementation HyPrinterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"hy_printer"
            binaryMessenger:[registrar messenger]];
  HyPrinterPlugin* instance = [[HyPrinterPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
  if ([@"connect" isEqualToString:call.method]) {
      int result;
  } else {
    result(FlutterMethodNotImplemented);
  }
}


@end
