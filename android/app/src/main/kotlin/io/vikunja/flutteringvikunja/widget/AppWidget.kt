package io.vikunja.flutteringvikunja

import HomeWidgetGlanceState
import HomeWidgetGlanceStateDefinition
import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import es.antonborri.home_widget.HomeWidgetPlugin
import androidx.glance.appwidget.CheckBox
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.google.gson.Gson


class AppWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Single

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = HomeWidgetGlanceStateDefinition()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceContent(context, currentState())
        }
    }

//    @Composable
//    private fun constructArray() {
//        val prefs = currentState.preferences
//        val numTasks = prefs.GetInt("numTasks", 0)
//
//    }

    @Composable
    private fun GlanceContent(context: Context, currentState: HomeWidgetGlanceState) {
        val prefs = currentState.preferences
        val tasks: MutableList<String> = ArrayList()
//        val things = arrayListOf("11:22", "12:24", "13:13", "14:14", "14:16", "15:16")
        // First work out how many tasks we gotta pull down
        val numTasks = prefs.getInt("numTasks", 0)

        // Extract all the tasks and put them into that array
        for (i in 1..numTasks) {
            prefs.getString(i.toString(), null)?.let { tasks.add(it) }
        }

//        // Gonna get data and see what it is
//        val hello = prefs.getString("1", null)
//        val gson = Gson()
//        val task = gson.fromJson(hello, ArrayList::class.java) as ArrayList<String>
//        Log.d("widget", task[0])
//        Log.d("widget", task[1])

//        if (hello != null) {
//            hello::class.simpleName?.let { Log.d("widget", it) }
//            Log.d("widget", "Logging")
//            Log.d("widget", hello)
//        } else {
//            Log.d("widget", "Its EMPTY")
//        }

        Column {
            MyTopBar()
            LazyColumn(modifier = GlanceModifier.background(Color.White)) {
                items(tasks) { task ->
                    RenderRow(task)
                }
            }
        }
    }

    @Composable
    private fun MyTopBar() {
        Box(
            modifier = GlanceModifier
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Blue),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "Today",
                style = TextStyle(fontSize = 20.sp, color = ColorProvider(Color.White))
            )
        }
    }

    @Composable
    private fun RenderRow(taskJson: String) {
        val gson = Gson()
        val task = gson.fromJson(taskJson, ArrayList::class.java) as ArrayList<String>
        Row(modifier = GlanceModifier.fillMaxWidth().padding(8.dp)) {
            CheckBox(
                checked = false,
                onCheckedChange = {},
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            )
            Box(
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = task[0],
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
            Box(
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = task[1],
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}
