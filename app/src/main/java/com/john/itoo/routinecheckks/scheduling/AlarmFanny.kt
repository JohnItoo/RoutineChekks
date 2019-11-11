package com.john.itoo.routinecheckks.scheduling

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.KITKAT
import android.os.Bundle
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.extensions.canUpdate
import com.john.itoo.routinecheckks.extensions.scheduleNext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class AlarmFanny @Inject constructor(
    private val alarmManager: AlarmManager,
    private val intent: Intent
) {

    companion object {
        const val INTENT_TAG = "intent_tag"
        const val UPDATE_ALARM = "update_alarm"
        const val SCHEDULE_TYPE_EXPIRE = 1
        const val RESCHEDULE = 1
        const val EXPIRE_ROUTINES = "expire_routines"
        const val FUTURE_UPDATE_ROUTINES = "future_update_routines"
        const val SCHEDULE_TYPE_DEFAULT = 0
        const val SCHEDULE_TYPE_UPDATE = 3
    }

    fun schedule(routine: Routine, context: Context, isReschedule: Int) {

        if (isReschedule == RESCHEDULE) {
            setAlarm(
                routine,
                context,
                Date().scheduleNext(routine).time,
                SCHEDULE_TYPE_DEFAULT
            )
            setAlarm(routine, context, Date().canUpdate().time, SCHEDULE_TYPE_UPDATE)
        } else {
            setAlarm(
                routine,
                context,
                routine.date.time,
                SCHEDULE_TYPE_DEFAULT
            )
            setAlarm(routine, context, expireTime, SCHEDULE_TYPE_EXPIRE)
        }
    }

    private fun setAlarm(
        routine: Routine,
        context: Context,
        alarmTime: Long,
        scheduleType: Int
    ) {
        val pendingIntent = fetchPendingIntent(routine, context, scheduleType)
        when {

            SDK_INT >= Build.VERSION_CODES.M -> alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
            SDK_INT >= KITKAT -> alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
            else -> alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent)
        }
    }

    private val expireTime get() = Date().time + 15 * 60 * 1000
    private val hourInMillis get() = 60 * 60 * 1000
    private val priorBuffer get() = 5 * 60 * 1000
    private fun getTriggerTime(routine: Routine, isReschedule: Int): Long {
        var triggerTime = routine.date.time - 5 * 60 * 1000
        if (isReschedule == RESCHEDULE && routine.expired == 0) {
            /*
            This is where decision to reschedule ideally should be made. We will rarely get here for this version because all our routines
            expire after 15 minutes
             */
            Timber.d(" AlarmFanny In When clause")

            when (routine.frequency) {
                "Hourly" -> triggerTime = Date().time + hourInMillis - priorBuffer
                "daily" -> triggerTime = Date().time + 24 * hourInMillis - priorBuffer
                "monthly" -> triggerTime = Date().time + 30 * 24 * hourInMillis - priorBuffer
            }
        }
        return triggerTime
    }

    private fun fetchPendingIntent(
        routine: Routine,
        context: Context,
        scheduleType: Int
    ): PendingIntent {
        Timber.d(scheduleType.toString())

        val bundle = Bundle()
        bundle.putParcelable(INTENT_TAG, routine)
        intent.putExtra(INTENT_TAG, bundle)

        if (scheduleType == SCHEDULE_TYPE_EXPIRE) {
            intent.putExtra(EXPIRE_ROUTINES, EXPIRE_ROUTINES)
            return PendingIntent.getBroadcast(
                context,
                routine.date.time.toInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else if (scheduleType == SCHEDULE_TYPE_UPDATE) {
            intent.putExtra(FUTURE_UPDATE_ROUTINES, FUTURE_UPDATE_ROUTINES)
            return PendingIntent.getBroadcast(
                context,
                Random().nextInt(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        intent.data = Uri.parse(routine.id.toString())

        return PendingIntent.getBroadcast(
            context,
            routine.createdAt.time.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    }
}