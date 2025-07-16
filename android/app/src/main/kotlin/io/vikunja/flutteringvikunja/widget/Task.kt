package io.vikunja.flutteringvikunja.widget
import java.time.LocalDateTime
import java.util.Date

class Task (
    var id: String,
    var title: String,
    var dueDate: Date,
    var today: Boolean,
    var overdue: Boolean,
    )
