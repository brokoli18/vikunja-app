package io.vikunja.flutteringvikunja

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Intent
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RemoteViews
import es.antonborri.home_widget.HomeWidgetPlugin

/**
 * Implementation of App Widget functionality.
 */
class VikunjaWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            val views = RemoteViews(context.packageName, R.layout.vikunja_widget)
            val intent = Intent(context, WidgetRemoteViewsService::class.java)
            views.setRemoteAdapter(R.id.list_id, intent)

            val clickIntent = Intent(context, VikunjaWidgetProvider::class.java).apply {
                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
            }
            val pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            views.setPendingIntentTemplate(R.id.list_id, pendingIntent)

//            // get data from flutter app
//            val widgetData = HomeWidgetPlugin.getData(context)
//
//
//            val views = RemoteViews(context.packageName, R.layout.vikunja_widget).apply {
//                val vikunjaTasks = widgetData.getStringSet("Tasks", null)
//                val converted = ArrayList(vikunjaTasks)
//                val listView: ListView = findViewById(R.id.list_id)
//                val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, converted)
//                listView.adapter = adapter
//            }
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val widgetText = context.getString(R.string.appwidget_text)
    // Construct the RemoteViews object
//    val views = RemoteViews(context.packageName, R.layout.vikunja_widget)
//    views.setTextViewText(R.id.text_id, widgetText)

    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
