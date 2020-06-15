package com.john.itoo.routinecheckks.scheduling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.utils.PrefsUtils
import timber.log.Timber
import javax.inject.Inject


class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var preferences: PrefsUtils

    override fun onReceive(context: Context?, intent: Intent?) {

        if(intent?.action == "android.intent.action.BOOT_COMPLETED") {
            (context!!.applicationContext as App).component.inject(this)

            Timber.d("Boot completed")
            RestartAlarmsService.enqueueWork(context, Intent())
        }

    }
}