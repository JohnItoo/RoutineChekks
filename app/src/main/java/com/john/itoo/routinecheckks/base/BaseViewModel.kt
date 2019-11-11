package com.john.itoo.routinecheckks.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.john.itoo.routinecheckks.networkutils.LoadingStatus


abstract class BaseViewModel() : ViewModel() {

    protected val _loadingStatus = MutableLiveData<LoadingStatus>()

    val loadingStatus: LiveData<LoadingStatus>
        get() = _loadingStatus



    override fun onCleared() {
        super.onCleared()
    }
}
