package com.john.itoo.routinecheckks.scheduling

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.app.RoutineRepository
import timber.log.Timber
import javax.inject.Inject


class RestartAlarmsService : JobIntentService() {

    @Inject
    lateinit var repository: RoutineRepository
    @Inject
    lateinit var alarmFanny: AlarmFanny
    lateinit var context: Context

    companion object {
        fun enqueueWork(context: Context?, work: Intent?) {
            val JOB_ID = 0x01

            enqueueWork(
                context!!, RestartAlarmsService::
                class.java, JOB_ID, work!!
            )
        }
    }

    override fun onHandleWork(intent: Intent) {
        Timber.d("HandleWork")
        (this.applicationContext as App).component.inject(this)

        alarmFanny.cancelAll(repository.getAllRoutinesAsync(), this)
        repository.updateFireNext(repository.getAllRoutinesAsync())
        alarmFanny.rescheduleAll(repository.getAllRoutinesAsync(), this)
    }
    // Handler that receives messages from the thread
}