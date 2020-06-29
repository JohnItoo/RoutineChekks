package com.john.itoo.routinecheckks.scheduling

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.extensions.readableString
import timber.log.Timber
import javax.inject.Inject

class AlarmFanny @Inject constructor(
    private val alarmManager: AlarmManager
) {

    companion object {
        const val INTENT_TAG = "intent_tag"
    }

    fun schedule(routine: Routine, context: Context) {

        setAlarm(
            routine,
            context.applicationContext,
            routine.date.time
        )
    }

    fun cancelAlarm(
        routine: Routine,
        context: Context
    ) {
        val pendingIntent = fetchPendingIntent(routine, context.applicationContext)

        alarmManager.cancel(pendingIntent)
    }

    fun updateAlarm(
        routine: Routine,
        context: Context,
        newRoutine: Routine
    ) {
        cancelAlarm(routine, context.applicationContext)
        schedule(newRoutine, context.applicationContext)
    }

    private fun setAlarm(
        routine: Routine,
        context: Context,
        alarmTime: Long
    ) {
        val pendingIntent = fetchPendingIntent(routine, context.applicationContext)
        Timber.d("Set Alarm now.")
        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(alarmTime, pendingIntent),
            pendingIntent
        )
        Timber.d("Just set alarm for : %s", routine.date.readableString())
    }

    fun rescheduleAll(routineList: List<Routine>, context: Context) {
        for (routine in routineList) {
            schedule(routine, context)
        }
        Timber.d("Rescheduled All")

    }

    fun cancelAll(routineList: List<Routine>, context: Context) {
        for (routine in routineList) {
            cancelAlarm(routine, context)
        }
        Timber.d("Cancelled All")
    }


    private fun fetchPendingIntent(
        routine: Routine,
        context: Context
    ): PendingIntent {

        val bundle = Bundle()
        bundle.putParcelable(INTENT_TAG, routine)
        val thisIntent = Intent(context.applicationContext, AlarmReceiver::class.java)

        thisIntent.putExtra(INTENT_TAG, bundle)

        thisIntent.data = Uri.parse(routine.id.toString())

        return PendingIntent.getBroadcast(
            context,
            routine.createdAt.time.toInt(),
            thisIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}