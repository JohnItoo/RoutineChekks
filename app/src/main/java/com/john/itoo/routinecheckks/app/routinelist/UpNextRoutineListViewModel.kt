package com.john.itoo.routinecheckks.app.routinelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.john.itoo.routinecheckks.base.BaseViewModel
import com.john.itoo.routinecheckks.app.ExampleRepository
import com.john.itoo.routinecheckks.app.models.*
import com.john.itoo.routinecheckks.extensions.nextUpCapDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class UpNextRoutineListViewModel @Inject constructor(private val exampleRepository: ExampleRepository, private val db: RoutineDatabase) :
    BaseViewModel() {


    var overdueRoutines =  Transformations.map(db.routineDao.getNextUpRoutines(Date().nextUpCapDate(), Date())) {
                       it.asDomainModel()  }

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

    fun setAsDone(routine: Routine) : Boolean {
        if(routine.canUpdate != 1) return false
        viewModelScope.launch(Dispatchers.IO) {
            if(routine.canUpdate == 1) {
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
}