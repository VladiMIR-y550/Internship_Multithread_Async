package com.mironenko.internship_multithread_async

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class DataListViewModel : ViewModel() {

    val numbersStateFlow = flow {
        (0..10).map {
            delay(1000)
            emit(Random.nextInt(100))
        }
    }

    val numbersObservable: Observable<Int> = Observable.interval(1, TimeUnit.SECONDS)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .map {
        Random.nextInt(100 + it.toInt())
    }

    fun numbersLiveData(): LiveData<Int> {
        val numbers = MutableLiveData<Int>()
        val runnable = Runnable {
            for (num in 0..10) {
                Thread.sleep(1000)
                numbers.postValue(Random.nextInt(100))
            }
        }
        val thread = Thread(runnable)
        thread.start()
        return numbers
    }
}