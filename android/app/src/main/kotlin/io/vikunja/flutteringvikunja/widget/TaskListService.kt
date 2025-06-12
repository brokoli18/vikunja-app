package io.vikunja.flutteringvikunja

import io.vikunja.flutteringvikunja.TaskListFactory
import android.content.Intent
import android.widget.RemoteViewsService

class ListViewWidgetService : RemoteViewsService(){
    override fun onGetViewFactory(intent: Intent?): RemoteViewsService.RemoteViewsFactory {
        return TaskListFactory(this.applicationContext, intent)
    }
}
