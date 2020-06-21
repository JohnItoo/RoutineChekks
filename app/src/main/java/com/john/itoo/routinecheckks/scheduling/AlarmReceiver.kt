package com.john.itoo.routinecheckks.scheduling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.app.RoutineRepository
import com.john.itoo.routinecheckks.app.models.Routine
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper
    @Inject
    lateinit var exampleRepository: RoutineRepository
    @Inject
    lateinit var alarmFanny: AlarmFanny


    override fun onReceive(context: Context, intent: Intent) {

        (context.applicationContext as App).component.inject(this)

        val routine =
            intent.getBundleExtra(AlarmFanny.INTENT_TAG).getParcelable(AlarmFanny.INTENT_TAG) as Routine

        exampleRepository.updateRoutineNextTime(routine)
        alarmFanny.schedule(routine, context)
        notificationHelper.showNotification(routine, context)
    }
}