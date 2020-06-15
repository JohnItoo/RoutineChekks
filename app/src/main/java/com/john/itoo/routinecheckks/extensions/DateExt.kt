package com.john.itoo.routinecheckks.extensions

import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.scheduling.AlarmFanny
import timber.log.Timber
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

private val expireTime get() = Date().time + 1 * 60 * 1000
private val hourInMillis get() = 60 * 60 * 1000
private val priorBuffer get() = 5 * 60 * 1000
fun Date.scheduleNext(routine: Routine): Date {
    var triggerTime = routine.date.time - 5 * 60 * 1000
    when (routine.frequency) {
        1 -> triggerTime = Date().time + hourInMillis - priorBuffer
        2 -> triggerTime = Date().time + 24 * hourInMillis - priorBuffer
        3 -> triggerTime = Date().time + 30 * 24 * hourInMillis - priorBuffer
    }
    return Date(triggerTime)
}

fun Date.nextUpCapDate(): Date {
   return Date(this.time + 12 * hourInMillis)
}

fun Date.canUpdate(): Date {
    return Date(this.time + priorBuffer)
}

fun Date.readableString(): String {
    val formatter = SimpleDateFormat("E, MMM dd, yyyy HH:mm aaa")
    return formatter.format(this.time)
}
