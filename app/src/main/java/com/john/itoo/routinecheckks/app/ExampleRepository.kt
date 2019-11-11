package com.john.itoo.routinecheckks.app

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.john.itoo.routinecheckks.app.models.*
import com.john.itoo.routinecheckks.extensions.scheduleNext
import timber.log.Timber
import javax.inject.Inject

class ExampleRepository @Inject constructor(
   private val routineDatabase: RoutineDatabase
) {

    val routines: LiveData<List<DatabaseRoutine>> = routineDatabase.routineDao.getAllRoutines()

    @WorkerThread
    suspend fun insert(routine: DatabaseRoutine){
        Timber.d("this is the db routine " + routine.id)
        routineDatabase.routineDao.insert(routine)
    }

    @WorkerThread
    suspend fun update(routine: DatabaseRoutine){
       routineDatabase.routineDao.update(routine)
    }

    @WorkerThread
    suspend fun expireRoutine(routine: Routine) {
        val exactRoutine = getSingleRoutine(routine).asRoutine()
        Timber.d("gotten new  routine to expire " + routine)
        exactRoutine.expired = 1
        insert(exactRoutine.asDbRoutine())
    }

    @WorkerThread
    suspend fun getSingleRoutine(routine: Routine) : DatabaseRoutine {
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
        if (exactRoutine.withinMinute == 0)  exactRoutine.tagProgress = "Missed"  else exactRoutine.tagProgress = "Done"
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
}