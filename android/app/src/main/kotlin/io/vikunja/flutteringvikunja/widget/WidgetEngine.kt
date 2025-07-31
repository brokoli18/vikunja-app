


class WidgetEngine : Application() {
  lateinit var flutterEngine: FlutterEngine

  override fun onCreate() {
    super.onCreate()
    flutterEngine = FlutterEngine(this)
    flutterEngine.dartExecutor.executeDartEntrypoint(
      DartExecutor.DartEntryPoint.createDefault()
    )
  }
}
