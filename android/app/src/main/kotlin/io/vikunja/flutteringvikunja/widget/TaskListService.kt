package io.vikunja.flutteringvikunja

import io.vikunja.flutteringvikunja.TaskListFactory
import android.content.Intent
import android.util.Log
import android.widget.RemoteViewsService

class TaskListService : RemoteViewsService(){
    override fun onGetViewFactory(intent: Intent?): RemoteViewsService.RemoteViewsFactory {
        Log.d("WidgetDebug", "THIS IS THE SERVICE")
        return TaskListFactory(this.applicationContext, intent)
    }
}
