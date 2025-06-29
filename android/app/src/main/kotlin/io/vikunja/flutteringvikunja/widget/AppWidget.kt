package io.vikunja.flutteringvikunja

import HomeWidgetGlanceState
import HomeWidgetGlanceStateDefinition
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.LocalSize
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
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider


class AppWidget : GlanceAppWidget() {
    override val sizeMode = SizeMode.Single

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = HomeWidgetGlanceStateDefinition()

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceContent(context, currentState())
        }
    }

    @Composable
    private fun GlanceContent(context: Context, currentState: HomeWidgetGlanceState) {
        val prefs = currentState.preferences
        val things = arrayListOf("11:22", "12:24", "13:13", "14:14", "14:16", "15:16")
        val counter = prefs.getString("counter", "Whatever")
        val size = LocalSize.current
        print(size)
        Column {
            MyTopBar()
            LazyColumn(modifier = GlanceModifier.background(Color.White)) {
                items(things) { num ->
                    RenderRow(num)
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
    private fun RenderRow(number: String) {
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
                    text = number,
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
            Box(
                modifier = GlanceModifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "This would be a task description.",
                    style = TextStyle(
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}
