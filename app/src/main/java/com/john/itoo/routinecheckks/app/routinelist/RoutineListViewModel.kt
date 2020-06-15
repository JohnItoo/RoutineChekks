package com.john.itoo.routinecheckks.app.routinelist

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.john.itoo.routinecheckks.base.BaseViewModel
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.app.models.*
import com.john.itoo.routinecheckks.extensions.nextUpCapDate
import com.john.itoo.routinecheckks.scheduling.AlarmFanny
import com.john.itoo.routinecheckks.utils.PrefsUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class RoutineListViewModel @Inject constructor(
    private val exampleRepository: ExampleRepository,
    private val alarmPack: AlarmFanny,
    private val prefsUtils: PrefsUtils
) :
    BaseViewModel() {

    var routines = exampleRepository.routines

    var switchNextUp = false


    private val _navigateToSelectedRoutine = MutableLiveData<Routine>()

    val navigateToSelectedRoutine: LiveData<Routine>
        get() = _navigateToSelectedRoutine


    init {
//        viewModelScope.launch {
//        }
    }

    fun insert(routine: Routine) = viewModelScope.launch(Dispatchers.IO) {
        exampleRepository.insert(routine.asDbRoutine())
    }

    fun setAsDone(routine: Routine): Boolean {
        if (routine.canUpdate != 1) return false
        viewModelScope.launch(Dispatchers.IO) {
            if (routine.canUpdate == 1) {
                routine.done += 1
                exampleRepository.update(routine.asDbRoutine())
            }
        }
        return true
    }

    fun displaySelectedRoutineDetails(routine: Routine) {
        _navigateToSelectedRoutine.value = routine
    }

    fun displaySelectedRoutineDetailsComplete() {
        _navigateToSelectedRoutine.value = null
    }

    fun fetchDefaultRoutine(): Routine {
        return Routine(-1, "a", "a", -1, -1, -1, Date(), Date(), Date(), -1, -1, 0, "a")
    }

    fun handleBoot(context: Context) {
//        if (prefsUtils.doesContain("JustBoot")) {
        Timber.d("Handle boot")

        alarmPack.cancelAll(exampleRepository.getAllRoutinesAsync(), context)
        exampleRepository.updateFireNext(exampleRepository.getAllRoutinesAsync())
        alarmPack.rescheduleAll(exampleRepository.getAllRoutinesAsync(), context)
        prefsUtils.remove("JustBoot")
//        }
    }
}