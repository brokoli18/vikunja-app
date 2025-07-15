import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:timezone/data/latest.dart' as tz;
import 'package:vikunja_app/api/task_implementation.dart';
import 'package:vikunja_app/models/task.dart';
// import 'package:vikunja_app/service/services.dart';
import 'package:home_widget/home_widget.dart';
import 'package:intl/intl.dart';
import 'dart:convert';
import 'package:vikunja_app/api/client.dart';
import 'package:vikunja_app/service/services.dart';

// I expect a list of tasks here, when I get it I get:
// Filter tasks and get only the ones due today
// Extract the time from the datetime
// Take the title of the task
// Update shared preferences using the home_widget package
// First item needs to be the number of tasks that need to be saved

void completeTask() async {
  Task? task;
  var taskID =
      await HomeWidget.getWidgetData("completeTask", defaultValue: "null");
  if (taskID == "null") {
    return;
  }

  // Get my token and auth shit from local storage. Then use that to update the task

  // Need to do the whole setup of the client to etch tasks

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

void updateWidgetTasks(TaskService taskService) async {

  var todayTasks = await taskService.getByFilterString("done = false && due_date < now/d+1d");
  if (todayTasks == null) {
    return;
  }

  // Set the number of tasks
  HomeWidget.saveWidgetData('numTasks', todayTasks.length);
  // var completedTask = await HomeWidget.getWidgetData('completeTask');
  DateFormat timeFormat = DateFormat("HH:mm");
  var widgetTaskIDs = [];
  for (var task in todayTasks) {
    widgetTaskIDs.add(task.id);
    var widgetTask = [timeFormat.format(task.dueDate!), task.title, task.id];
    final jsonString = jsonEncode(widgetTask);
    HomeWidget.saveWidgetData(task.id.toString(), jsonString);
  }

  HomeWidget.saveWidgetData("widgetTaskIDs", widgetTaskIDs.toString());
  reRenderWidget();
}

void reRenderWidget() {
  HomeWidget.updateWidget(
    name: 'AppWidget',
    qualifiedAndroidName: 'io.vikunja.flutteringvikunja.AppWidgetReciever',
  );

}
