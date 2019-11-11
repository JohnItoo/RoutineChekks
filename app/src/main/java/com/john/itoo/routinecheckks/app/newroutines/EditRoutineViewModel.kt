package com.john.itoo.routinecheckks.app.newroutines

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.john.itoo.routinecheckks.base.BaseViewModel
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.app.models.Routine
import com.john.itoo.routinecheckks.app.models.asDbRoutine
import com.john.itoo.routinecheckks.scheduling.AlarmFanny
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditRoutineViewModel @Inject constructor(private val exampleRepository: ExampleRepository, private val alarmPack: AlarmFanny) :
    BaseViewModel() {

    fun insert(routine: Routine, context: Context) = viewModelScope.launch(Dispatchers.IO) {
        exampleRepository.insert(routine.asDbRoutine())
        alarmPack.schedule(routine, context, AlarmFanny.SCHEDULE_TYPE_DEFAULT)
    }
}