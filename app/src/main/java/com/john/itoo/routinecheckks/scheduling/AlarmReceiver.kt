package com.john.itoo.routinecheckks.scheduling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.app.models.Routine
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper
    @Inject
    lateinit var exampleRepository: ExampleRepository

    override fun onReceive(context: Context, intent: Intent) {

        (context.applicationContext as App).component.inject(this)

        val routine =
            intent.getBundleExtra(AlarmFanny.INTENT_TAG).getParcelable(AlarmFanny.INTENT_TAG) as Routine


        Timber.d("received routine" + routine)
        if (intent.hasExtra(AlarmFanny.EXPIRE_ROUTINES)) {
            Timber.d("Expire routine")
            GlobalScope.launch {
                exampleRepository.expireRoutine(routine)
            }

        } else if (intent.hasExtra(AlarmFanny.FUTURE_UPDATE_ROUTINES)) {
            GlobalScope.launch {
                exampleRepository.updateRoutineCanUpdateAndTag(routine)
            }
        } else {
            GlobalScope.launch {
                exampleRepository.updateRoutineDate(routine)
            }
            notificationHelper.showNotification(routine, context)
        }
    }
}