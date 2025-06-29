package io.vikunja.flutteringvikunja


import HomeWidgetGlanceWidgetReceiver

class AppWidgetReciever : HomeWidgetGlanceWidgetReceiver<AppWidget>() {
    override val glanceAppWidget = AppWidget() // pass created widget here
}
