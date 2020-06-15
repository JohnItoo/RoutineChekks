package com.john.itoo.routinecheckks.app

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.john.itoo.routinecheckks.app.models.*
import com.john.itoo.routinecheckks.extensions.scheduleNext
import com.john.itoo.routinecheckks.utils.TimeUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ExampleRepository @Inject constructor(
    private val routineDatabase: RoutineDatabase,
    private val timeUtils: TimeUtils
) {

    var routines = Transformations.map(routineDatabase.routineDao.getAllRoutines()) {
        it.asDomainModel()
    }

    @WorkerThread
    suspend fun insert(routine: DatabaseRoutine): Long {
        Timber.d("this is the db routine " + routine.id)
        return routineDatabase.routineDao.insert(routine)
    }

    @WorkerThread
    suspend fun update(routine: DatabaseRoutine) {
        routineDatabase.routineDao.update(routine)
    }

    @WorkerThread
    suspend fun delete(routine: DatabaseRoutine) {
        routineDatabase.routineDao.delete(routine)
    }

    @WorkerThread
    suspend fun expireRoutine(routine: Routine) {
        val exactRoutine = getSingleRoutine(routine).asRoutine()
        Timber.d("gotten new  routine to expire " + routine)
        exactRoutine.expired = 1
        insert(exactRoutine.asDbRoutine())
    }

    @WorkerThread
    suspend fun getSingleRoutine(routine: Routine): DatabaseRoutine {
        return routineDatabase.routineDao.getRoutine(routine.date)
    }

    @WorkerThread
    suspend fun updateRoutineDate(routine: Routine) {
        val exactRoutine = getSingleRoutine(routine).asRoutine()
        exactRoutine.date = exactRoutine.date.scheduleNext(routine)
        exactRoutine.total += 1
        exactRoutine.canUpdate = 0
        Timber.d("gotten new  routine to expire " + exactRoutine)

        insert(exactRoutine.asDbRoutine())
    }

    @WorkerThread
    suspend fun updateRoutineCanUpdateAndTag(routine: Routine) {
        val exactRoutine = getSingleRoutine(routine).asRoutine()
        if (exactRoutine.withinMinute == 0) exactRoutine.tagProgress =
            "Missed" else exactRoutine.tagProgress = "Done"
        exactRoutine.withinMinute = 0
        exactRoutine.canUpdate = 1
        insert(exactRoutine.asDbRoutine())
    }

    @WorkerThread
    suspend fun updateRoutineToMarkAsDone(routine: Routine) {
        val exactRoutine = getSingleRoutine(routine).asRoutine()
        exactRoutine.done += 1
        exactRoutine.canUpdate = 0
        exactRoutine.withinMinute = 1
        insert(exactRoutine.asDbRoutine())
    }

    fun updateFireNext(routineList: List<Routine>) {
        Timber.d("Updating fire next")
        for (routine in routineList) {
            var newCurrent = Date()
            newCurrent.time = routine.date.time

            if (routine.date > Date()) {
                val predictedDate = timeUtils.predictNextAlarm(newCurrent, routine.frequency)
                routine.date = predictedDate
            }
            GlobalScope.launch {
                update(routine.asDbRoutine())
            }
        }
        Timber.d("Updated fire next")
    }

    fun updateFireNextLegacy(routineList: List<Routine>) {
        Timber.d("Updating fire next")
        for (routine in routineList) {
            var newCurrent = Date()
            newCurrent.time = routine.nextTime.time
            var newNext = Date()
            newNext.time = routine.nextTime.time
            if (routine.date < Date() && routine.nextTime > Date()) {
                routine.date = newCurrent
                routine.nextTime = timeUtils.getTimeToUpdate(newNext, routine.frequency)
            } else if (routine.date < Date() && routine.nextTime < Date()) {
                val predictedDate = timeUtils.predictNextAlarm(newCurrent, routine.frequency)
                routine.date = predictedDate
                routine.nextTime = timeUtils.getTimeToUpdate(predictedDate, routine.frequency)
            }
            GlobalScope.launch {
                update(routine.asDbRoutine())
            }
        }
        Timber.d("Updated fire next")
    }

    fun updateRoutineNextTime(routine: Routine) {
        var startDate = Date()
        startDate.time = routine.date.time
        val upNextDate = timeUtils.getTimeToUpdate(startDate, routine.frequency)
        routine.date = upNextDate

        GlobalScope.launch {
            update(routine.asDbRoutine())
        }
    }

    fun updateRoutineNextTimeLegacy(routine: Routine) {
        var startDate = Date()
        var nextDate = Date()
        startDate.time = routine.nextTime.time
        nextDate.time = routine.nextTime.time
        routine.date = startDate
        routine.nextTime = timeUtils.getTimeToUpdate(nextDate, routine.frequency)
        Timber.d("next date" + routine.date.toString())
        Timber.d("next next" + routine.nextTime.toString())

        GlobalScope.launch {
            update(routine.asDbRoutine())
        }
    }

    fun getAllRoutinesAsync(): List<Routine> {
        var result = routineDatabase.routineDao.getAllRoutinesAsync().map {
            it.asRoutine()
        }
        return result
    }

}