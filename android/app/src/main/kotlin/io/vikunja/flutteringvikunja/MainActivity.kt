package io.vikunja.flutteringvikunja

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import java.io.File

/**
 * App not open:
 *  - After click on tile or share onCreate is called
 *  - Then configureFlutterEngine is called.
 *    This the the launch method for later and registers a method channel
 *  - After that "isQuickTile" is called from flutter code to check
 *    if the launch method was set and if parameter were passed
 *  - If so the add task dialog is shown
 *
 * App open:
 *  - When the flutter application start a method channel is registered
 *  - After click on tile or share onCreate is called
 *  - Then onNewIntent is called.
 *  - This register a method channel and direclty calles flutter code
 *    to show the add taks dialog
 *
 */
class MainActivity : FlutterActivity() {
    private var launchMethod: String? = null
    private val CHANNEL = "vikunja"
    override fun onNewIntent(intent: Intent) {

        super.onNewIntent(intent)
        loggy("Running on new intent fool")
        setIntent(intent)
        flutterEngine?.let {
            callFlutterCode(intent, it)
        }

    }

    fun loggy(logline: String) {
        val path = context.getFilesDir()
        // /data/data/io.vikunja.app.unsigned/files/LET
        Log.d("DEBUG", path.toString())
        val letDirectory = File(path, "LET")
        letDirectory.mkdirs()
        val file = File(letDirectory, "test.txt")
        file.appendText(logline + "\n")
    }

    private fun callFlutterCode(intent: Intent, flutterEngine: FlutterEngine) {
        loggy("Calling flutter code")
        val channel = MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
        val action: String? = intent.action
        val type: String? = intent.type

        loggy("ACTION: $action")
        loggy("Type: $type")

        when (action) {
            Intent.ACTION_INSERT -> {
                if (INTENT_TYPE_ADD_TASK == type) {
                    channel.invokeMethod("open_add_task", "")
                }
            }

            Intent.ACTION_SEND if "text/plain" == type -> {
                channel.invokeMethod("open_add_task", intent.getStringExtra(Intent.EXTRA_TEXT))
            }

            else -> {
            }
        }
    }


    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        loggy("Configuring the engine")
        super.configureFlutterEngine(flutterEngine)

        setLaunchMethod(intent)

        registerMethodChannel(flutterEngine)
    }

    private fun setLaunchMethod(intent: Intent) {
        loggy("Setting the launch method")
        val action: String? = intent.action
        val type: String? = intent.type

        when (action) {
            Intent.ACTION_INSERT -> {
                if (INTENT_TYPE_ADD_TASK == type) {
                    launchMethod = "open_add_task"
                }
            }

            Intent.ACTION_SEND if "text/plain" == type -> {
                launchMethod = "open_add_task"
            }

            else -> {
            }
        }
    }

    private fun registerMethodChannel(flutterEngine: FlutterEngine) {
        loggy("Registering method channel")
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger, CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method?.contentEquals("isQuickTile") == true) {
                if (launchMethod == "open_add_task") {
                    result.success(intent.getStringExtra(Intent.EXTRA_TEXT))
                } else {
                    result.error("1", null, null)
                }

                launchMethod = null
            }
        }
    }
}

// THe below fixes the bug but the add task popup is launched EVERY time
//class MainActivity : FlutterActivity() {
//
//    private val CHANNEL = "vikunja"
//
//    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
//        Log.d("DEBUG", "Configuring FLutter Engine")
//        super.configureFlutterEngine(flutterEngine)
//
//
//        val channel = MethodChannel(
//            flutterEngine.dartExecutor.binaryMessenger,
//            CHANNEL
//        )
//        Log.d("DEBUG", "Set up Method Channel")
//
//        intent?.action?.let { action ->
//            Log.d("DEBUG", "PROCESSING INTENT")
//            channel.invokeMethod("launch_action", action)
//        }
//    }
//
//    override fun onNewIntent(intent: Intent) {
//        super.onNewIntent(intent)
//
//        flutterEngine?.let {
//            MethodChannel(
//                it.dartExecutor.binaryMessenger,
//                CHANNEL
//            ).invokeMethod("launch_action", intent.action)
//        }
//    }
//}

