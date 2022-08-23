package com.mironenko.internship_multithread_async

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mironenko.internship_multithread_async.data.DataService
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class DataListViewModel(val dataService: DataService) : ViewModel() {

    private val _numberLiveData = MutableLiveData<Int>()
    val numbersLiveData: LiveData<Int> = _numberLiveData

    private val _numbersStateFlow =
        MutableSharedFlow<Int>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val numbersStateFlow: SharedFlow<Int> get() = _numbersStateFlow

    val observable: Observable<Int> = Observable.create {
            subscriber ->
        dataService.randomNumbersByTimer { subscriber.onNext(it) }
    }

    init {
        updateLiveDataByTimer()
        updateStateFlowByTimer()
    }

    private fun updateLiveDataByTimer() {
        dataService.randomNumbersByTimer {
            _numberLiveData.postValue(it)
        }
    }

    private fun updateStateFlowByTimer() {
        dataService.randomNumbersByTimer {
            _numbersStateFlow.tryEmit(it)
        }
    }
}