import 'dart:convert';

import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:timezone/data/latest.dart' as tz;
import 'package:vikunja_app/api/task_implementation.dart';
import 'package:vikunja_app/models/task.dart';
import 'package:home_widget/home_widget.dart';
import 'package:vikunja_app/api/client.dart';
import 'package:vikunja_app/models/widgetTask.dart';
import 'package:vikunja_app/service/services.dart';

// I expect a list of tasks here, when I get it I get:
// Filter tasks and get only the ones due today
// Extract the time from the datetime
// Take the title of the task
// Update shared preferences using the home_widget package
// First item needs to be the number of tasks that need to be saved

void completeTask() async {
  print('COMPLETING TASK');
  Task? task;
  var taskID =
      await HomeWidget.getWidgetData("completeTask", defaultValue: "null");
  if (taskID == "null") {
    return;
  }

  final FlutterSecureStorage _storage = new FlutterSecureStorage();
  var currentUser = await _storage.read(key: 'currentUser');
  if (currentUser == null) {
    return;
  }
  var token = await _storage.read(key: currentUser);

  var base = await _storage.read(key: '${currentUser}_base');
  if (token == null || base == null) {
    return Future.value(true);
  }
  Client client = Client(null);
  client.configure(token: token, base: base, authenticated: true);

  tz.initializeTimeZones();
  TaskAPIService taskService = TaskAPIService(client);

  // Get task , update it locally and then send back to the server. Then update the widget
  if (taskID != null) {
    task = await taskService.getTask(int.tryParse(taskID)!);
    if ( task != null ){
      taskService.update(task.copyWith(done: true));
    }
  }
  // Get all the tasks again and update widget
  updateWidgetTasks(taskService);
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

WidgetTask convertTask(Task task) {
  // Check if task is for today
  final now = DateTime.now();
  final today = DateTime(now.year, now.month, now.day);

  bool wgToday = task.dueDate!.day == today.day ? true : false;
  bool overdue = task.dueDate!.isBefore(now) ? true : false;

  // CHeck if task is overdue
  
  WidgetTask wgTask = WidgetTask(
    id: task.id.toString(),
    title: task.title,
    dueDate: task.dueDate,
    today: wgToday,
    overdue: overdue
  );
  return wgTask;
} 

void updateWidgetTasks(TaskService taskService) async {
  var todayTasks = await taskService.getByFilterString("done = false && due_date < now/d+1d");
  if (todayTasks == null) {
    return;
  }

  // Set the number of tasks
  var widgetTaskIDs = [];
  for (var task in todayTasks) {
    widgetTaskIDs.add(task.id);
    var wgTask = convertTask(task);
    await HomeWidget.saveWidgetData(task.id.toString(), jsonEncode(wgTask.toJSON()));
  }

  HomeWidget.saveWidgetData("widgetTaskIDs", widgetTaskIDs.toString());
  reRenderWidget();
}

void reRenderWidget() {
  HomeWidget.updateWidget(
    name: 'AppWidget',
    // androidName: '.widget.AppWidgetReciever',
    qualifiedAndroidName: 'io.vikunja.flutteringvikunja.widget.AppWidgetReciever',
  );

}
