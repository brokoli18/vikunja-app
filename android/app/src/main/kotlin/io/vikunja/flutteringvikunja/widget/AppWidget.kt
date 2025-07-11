package io.vikunja.flutteringvikunja

import HomeWidgetGlanceState
import HomeWidgetGlanceStateDefinition
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.*
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.gson.Gson
import es.antonborri.home_widget.HomeWidgetBackgroundIntent
import android.net.Uri
import android.util.Log
import androidx.core.content.edit


class AppWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Single

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = HomeWidgetGlanceStateDefinition()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceContent(context, currentState())
        }
    }

    private fun doneTask(context: Context, prefs: SharedPreferences, taskID: String) {
        Log.d("WIDGET", taskID)
        prefs.edit {
            putString("completeTask", taskID)
            commit()
        }
        val backgroundIntent = HomeWidgetBackgroundIntent.getBroadcast(
            context,
            Uri.parse("appWidget://completeTask")
        )
        backgroundIntent.send()
    }

    @Composable
    private fun GlanceContent(context: Context, currentState: HomeWidgetGlanceState) {
        val prefs = currentState.preferences
        val tasks: MutableList<String> = ArrayList()

        // Get an array of the widget tasks we gotta display
        val taskIDChars = prefs.getString("widgetTaskIDs", null)
        var taskIDs: List<String>  = emptyList()
        if (taskIDChars != null) {
            val noBrackets = taskIDChars.substring(1, taskIDChars.length - 1)
            taskIDs = noBrackets.split(",")

        } else {
            Log.d("Widget", "There was a problem getting the widget ids")
        }

        // Extract all the tasks and put them into that array
        if (taskIDs.isNotEmpty()) {
            for (taskId in taskIDs) {
                val task = prefs.getString(taskId.trim(), null)
                task?.let { tasks.add(it) }
            }
        }


        Column {
            MyTopBar()
            if (tasks.isNotEmpty()) {
//                Log.d("Widget", tasks.toString())
                LazyColumn(modifier = GlanceModifier.background(Color.White)) {
                    items(tasks) { task ->
                        RenderRow(context, task, prefs)
                    }
                }
            } else {
                Box(modifier = GlanceModifier.background(Color.White)) {
                    Text(
                        text = "There are no tasks due today"
                    )
                }
            }

        }
    }

    @Composable
    private fun MyTopBar() {
        Box(
            modifier = GlanceModifier.fillMaxWidth().height(50.dp).background(Color.Blue),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Today",
                style = TextStyle(fontSize = 20.sp, color = ColorProvider(Color.White))
            )
        }
    }

    @Composable
    private fun RenderRow(context: Context, taskJson: String, prefs : SharedPreferences) {
        val gson = Gson()
        val task = gson.fromJson(taskJson, ArrayList::class.java) as ArrayList<*>

        // The task ID will be a double here even though it isnt immidiately obvious. This will have a decimal that needs to be removed
        val dubID : Double = task[2] as Double // Unsafe cast
        val taskID = dubID.toInt()

        Row(modifier = GlanceModifier.fillMaxWidth().padding(8.dp)) {
            CheckBox(
                checked = false,
                onCheckedChange = { doneTask(context, prefs, taskID.toString())},
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            )
            Box(
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = task[0].toString(), style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
            Box(
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = task[1].toString(), style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}