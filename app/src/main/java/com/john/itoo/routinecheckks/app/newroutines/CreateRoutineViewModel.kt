package com.john.itoo.routinecheckks.app.newroutines

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.john.itoo.routinecheckks.base.BaseViewModel
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.app.models.asDbRoutine
import com.john.itoo.routinecheckks.scheduling.AlarmFanny
import com.john.itoo.routinecheckks.utils.PrefsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class CreateRoutineViewModel @Inject constructor(
    private val exampleRepository: ExampleRepository,
    private val alarmPack: AlarmFanny,
    private val prefsUtils: PrefsUtils
) :
    BaseViewModel() {

    fun insert(routine: Routine, context: Context, isEdit: Boolean, oldRoutine: Routine) =
        viewModelScope.launch(Dispatchers.IO) {
            if (isEdit) {
                routine.id = oldRoutine.id
                exampleRepository.update(routine.asDbRoutine())
                alarmPack.updateAlarm(oldRoutine, context, routine)

            } else {
               val id =  exampleRepository.insert(routine.asDbRoutine())
                routine.id = id.toInt()
                Timber.d(routine.toString())
                alarmPack.schedule(routine, context)
            }
        }

    suspend fun delete(routine: Routine, context: Context) {
        exampleRepository.delete(routine.asDbRoutine())
        alarmPack.cancelAlarm(routine, context)
    }


}