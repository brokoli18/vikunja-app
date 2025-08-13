package io.vikunja.flutteringvikunja.widget

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.work.ListenableWorker.Result
import androidx.work.Worker
import androidx.work.WorkerParameters
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class FlutterWorker(private val context: Context, params: WorkerParameters) :
    Worker(context, params) {


    override fun doWork(): Result {
        // Create a latch to wait for main thread completion
        var engine: FlutterEngine? = null
        var engineError: Exception? = null

        // Post to main thread using Handler
        Handler(Looper.getMainLooper()).post {
            val flutterLoader = FlutterInjector.instance().flutterLoader()
            flutterLoader.startInitialization(context)
            flutterLoader.ensureInitializationComplete(context, null)
            // Allocate memory for the flutter engine
            engine = FlutterEngine(applicationContext)
            // Actually start the flutter engine and execute main() from flutter
            engine?.dartExecutor?.executeDartEntrypoint(
                DartExecutor.DartEntrypoint.createDefault()
            )
            engine?.dartExecutor?.binaryMessenger?.let {
                MethodChannel(it, "com.vikunja.widget/actions")
                    .invokeMethod("completeTask", null)
            }
        }
        return Result.success()
    }
}
