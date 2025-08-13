import 'package:flutter/services.dart';

void setupMethodChannel() {
  const MethodChannel _channel = MethodChannel('com.vikunja.widget/actions');
  _channel.setMethodCallHandler((MethodCall call) async {
    if (call.method == 'completeTask') {
      print("Background task triggered from Android!");
      return "Success";
    }
    return null;
  });
}
