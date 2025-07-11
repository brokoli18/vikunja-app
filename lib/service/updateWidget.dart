import 'package:vikunja_app/models/task.dart';
// import 'package:vikunja_app/service/services.dart';
import 'package:home_widget/home_widget.dart';
import 'package:intl/intl.dart';
import 'dart:convert';

// I expect a list of tasks here, when I get it I get:
// Filter tasks and get only the ones due today
// Extract the time from the datetime
// Take the title of the task
// Update shared preferences using the home_widget package
// First item needs to be the number of tasks that need to be saved

void completeTask() async {
  var num = await HomeWidget.getWidgetData("completeTask", defaultValue: "null");
  if (num == "null") {
    print("Its empty");
    return;
  }
  print(num);
}

List<Task> filterForTodayTasks(List<Task> tasks) {
  var todayTasks = <Task>[];

  for (var task in tasks) {
    final now = DateTime.now();
    final today = DateTime(now.year, now.month, now.day);
    if (task.dueDate!.day == today.day) {
      todayTasks.add(task);
    }
  }
  return todayTasks;
}

void updateWidgetTasks(List<Task>? tasklist) async {
  // print('Running UpdateWidget');
  var todayTasks = filterForTodayTasks(tasklist!);

  // Set the number of tasks
  HomeWidget.saveWidgetData('numTasks', todayTasks.length);
  // var completedTask = await HomeWidget.getWidgetData('completeTask');
  DateFormat timeFormat = DateFormat("HH:mm");
  var widgetTaskIDs = [];
  for (var task in todayTasks) {
    widgetTaskIDs.add(task.id);
    var widgetTask = [timeFormat.format(task.dueDate!), task.title, task.id];
    final jsonString = jsonEncode(widgetTask);
    // print(jsonString);
    HomeWidget.saveWidgetData(task.id.toString(), jsonString);
    // print(await HomeWidget.getWidgetData(task.id.toString()));
  }

  HomeWidget.saveWidgetData("widgetTaskIDs", widgetTaskIDs.toString());

  // Update the widget
  HomeWidget.updateWidget(
    name: 'AppWidget',
    // androidName: '.widget.MyAppWidgetReceiver',
    qualifiedAndroidName: 'io.vikunja.flutteringvikunja.AppWidgetReciever',
  );
}
