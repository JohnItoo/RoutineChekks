package com.john.itoo.routinecheckks.scheduling

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.john.itoo.routinecheckks.R
import com.john.itoo.routinecheckks.app.models.Routine
import timber.log.Timber
import javax.inject.Inject

class NotificationHelper @Inject constructor(
    private val notificationManager: NotificationManager,
    private val builder: NotificationCompat.Builder,
    private val alarmPack: AlarmFanny
) {
    companion object {

        private const val CHANNEL_TODOS = "todos"

        private const val REQUEST_CONTENT = 1
        private const val REQUEST_BUBBLE = 2
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setUpNotificationChannels() {
        if (notificationManager.getNotificationChannel(CHANNEL_TODOS) == null) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    CHANNEL_TODOS,
                    "Notification Name",
                    // The importance must be IMPORTANCE_HIGH to show Bubbles.
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "This describes this channel"
                }
            )
        }
    }

    @WorkerThread
    fun showNotification(routine: Routine, context: Context) {
        Timber.d("Alarm Fanny SHow")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setUpNotificationChannels()
        }

        builder
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(routine.description)
            .setContentTitle(routine.title)
            .setContentIntent(getLaunchAppPendingIntent(routine, context.applicationContext))
            .setSubText(context.getString(R.string.app_name))
            .setVibrate(longArrayOf(0, 1000, 1000, 1000, 1000))
            .setDefaults(Notification.DEFAULT_SOUND)
            .setAutoCancel(true)

        try {
            Timber.d("Should show this ish")
            notificationManager.notify(0, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getLaunchAppPendingIntent(routine: Routine, context: Context): PendingIntent {
        val args = Bundle()
        args.putParcelable("Routine", routine)
        return NavDeepLinkBuilder(context)
            .setGraph(R.navigation.nav_graph)
            .setDestination(R.id.routinesListFragment)
            .setArguments(args)
            .createPendingIntent()
    }
}