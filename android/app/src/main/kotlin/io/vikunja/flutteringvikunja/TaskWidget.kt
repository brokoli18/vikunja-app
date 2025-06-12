package io.vikunja.flutteringvikunja

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews

class TaskWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (id in appWidgetIds) {
          // Create intent for ListViewWidgetService
          val intent = Intent(
              context,
              TaskListService::class.java
           )
            Log.d("WidgetDebug", "POO")
          // Add App widget id to the intent
          intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id)
          // Instantiate a RemoteViews object with the listview xml layout
          val views = RemoteViews(context.packageName, R.layout.task_list_widget)
          views.setRemoteAdapter(R.id.task_list, intent)
          // update the app widget to reflect the data
          appWidgetManager.updateAppWidget(id, views)
            Log.d("WidgetDebug", "END OF WIDGET")
        }
    }
}
