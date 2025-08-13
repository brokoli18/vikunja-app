package io.vikunja.flutteringvikunja.widget

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel

class FlutterWorker(private val context: Context, params: WorkerParameters) : Worker(context, params) {
  @SuppressLint("WrongThread")
  override fun doWork(): Result {
    val flutterEngine = FlutterEngine(context) // Initialize Flutter in background
    flutterEngine.dartExecutor.executeDartEntrypoint(
      DartExecutor.DartEntrypoint.createDefault()
    )

    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.vikunja.widget/actions")
      .invokeMethod("completeTask", null)

    return Result.success()
  }
}
