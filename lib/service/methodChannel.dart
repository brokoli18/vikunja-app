import 'package:flutter/services.dart';
import 'package:vikunja_app/service/updateWidget.dart';

void setupMethodChannel() {
  const MethodChannel _channel = MethodChannel('com.vikunja.widget/actions');
  _channel.setMethodCallHandler((MethodCall call) async {
    print('Running');
    if (call.method == 'completeTask') {
      completeTask(); 
    }
  });
}
