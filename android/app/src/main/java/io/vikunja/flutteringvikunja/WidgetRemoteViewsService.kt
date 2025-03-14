package io.vikunja.flutteringvikunja

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.util.*

class WidgetRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return WidgetRemoteViewsFactory(applicationContext)
    }

    class WidgetRemoteViewsFactory(private val context: Context) : RemoteViewsService.RemoteViewsFactory {
        private val vikunjaTasks: Set<String> = setOf("Task 1", "Task 2", "Task 3") // Sample data

        override fun onCreate() {
            // Initialize the data here (e.g., load tasks from shared preferences or database)
        }

        override fun onDataSetChanged() {
            // Update data if needed
        }

        override fun onDestroy() {
            // Clean up if needed
        }

        override fun getCount(): Int {
            return vikunjaTasks.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val task = vikunjaTasks.toList()[position]
            val views = RemoteViews(context.packageName, android.R.layout.simple_list_item_1)
            views.setTextViewText(android.R.id.text1, task)
            return views
        }

        override fun getLoadingView(): RemoteViews? {
            return null // Optionally provide a loading view
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
            return true
        }
    }
}
