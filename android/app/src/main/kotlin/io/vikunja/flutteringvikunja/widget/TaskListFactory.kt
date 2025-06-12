package io.vikunja.flutteringvikunja

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService

class TaskListFactory(private val context: Context, private val intent: Intent?) : RemoteViewsService.RemoteViewsFactory{
    override fun onCreate() {
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int {
        return 10
    }

    override fun getViewAt(position: Int): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.task)
        views.setTextViewText(R.id.task_time, "$position")
        Log.d("WidgetDebug", "Setting time to $position")
        views.setTextViewText(R.id.title, "THIS IS A BLOODY TASK")
        Log.d("WidgetDebug", "THIS IS THE BLOODY TASK")
        return views
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return  1
    }

    override fun getItemId(position: Int): Long {
        return  position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }
}
