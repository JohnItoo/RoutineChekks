package com.john.itoo.routinecheckks.extensions

import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.scheduling.AlarmFanny
import timber.log.Timber
import java.util.*

private val expireTime get() = Date().time + 1 * 60 * 1000
private val hourInMillis get() = 60 * 60 * 1000
private val priorBuffer get() = 5 * 60 * 1000
fun Date.scheduleNext(routine: Routine): Date {
    var triggerTime = routine.date.time - 5 * 60 * 1000
    when (routine.frequency) {
        "Hourly" -> triggerTime = Date().time + hourInMillis - priorBuffer
        "daily" -> triggerTime = Date().time + 24 * hourInMillis - priorBuffer
        "monthly" -> triggerTime = Date().time + 30 * 24 * hourInMillis - priorBuffer
    }
    return Date(triggerTime)
}

fun Date.nextUpCapDate(): Date {
   return Date(this.time + 12 * hourInMillis)
}

fun Date.canUpdate(): Date {
    return Date(this.time + priorBuffer)
}
