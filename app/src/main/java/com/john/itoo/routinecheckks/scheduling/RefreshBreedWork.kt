package com.john.itoo.routinecheckks.scheduling

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.john.itoo.routinecheckks.App
import com.john.itoo.routinecheckks.app.ExampleRepository
import retrofit2.HttpException
import javax.inject.Inject

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    @Inject
    lateinit var repository: ExampleRepository

    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

    init {
        (appContext as App).component.inject(this)
    }

    override suspend fun doWork(): Payload {
        return try {
            Payload(Result.SUCCESS)
        } catch (exception: HttpException) {
            Payload(Result.RETRY)
        }
    }
}