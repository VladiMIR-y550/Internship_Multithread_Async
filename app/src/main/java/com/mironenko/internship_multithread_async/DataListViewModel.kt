package com.mironenko.internship_multithread_async

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.TimeUnit
import kotlin.random.Random

const val GENERATED_COUNT: Int = 10

class DataListViewModel : ViewModel() {
    private var threadForLiveData: Thread? = null

    fun numbersStateFlow(): Flow<Int> {
        return flow {
            (0 until GENERATED_COUNT).map {
                delay(1000)
                emit(Random.nextInt(100))
            }
        }
    }

    fun numbersObservable(): Observable<Int> {
        return Observable.interval(1, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .take(GENERATED_COUNT.toLong())
            .map {
                Random.nextInt(100 + it.toInt())
            }
    }

    fun numbersLiveData(): LiveData<Int> {
        val numbers = MutableLiveData<Int>()
        val runnable = Runnable {

            for (num in 0 until GENERATED_COUNT) {
                if (!Thread.currentThread().isInterrupted) {
                    try {
                        Thread.sleep(1000)
                        numbers.postValue(Random.nextInt(100))
                    } catch (e: InterruptedException) {
                        Thread.currentThread().interrupt()
                    }
                }
            }
            Thread.currentThread().interrupt()
        }

        threadForLiveData = Thread(runnable)
        threadForLiveData!!.start()
        return numbers
    }

    fun stopLiveDataThread() {
        threadForLiveData?.interrupt()
    }
}