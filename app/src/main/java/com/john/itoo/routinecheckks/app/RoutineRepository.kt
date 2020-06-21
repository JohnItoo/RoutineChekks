package com.john.itoo.routinecheckks.app

import androidx.annotation.WorkerThread
import androidx.lifecycle.Transformations
import com.john.itoo.routinecheckks.app.models.*
import com.john.itoo.routinecheckks.utils.TimeUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class RoutineRepository @Inject constructor(
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
    suspend fun getSingleRoutine(routine: Routine): DatabaseRoutine {
        return routineDatabase.routineDao.getRoutine(routine.date)
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