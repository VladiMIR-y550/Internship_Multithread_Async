package com.mironenko.internship_multithread_async.data

import android.os.CountDownTimer
import kotlin.random.Random

const val RANGE_NUMBERS: Int = 100

class DataRandomService : DataService {

    override fun randomNumbersByTimer(onSuccess: (number: Int) -> Unit) {
        object : CountDownTimer(20000, 1000) {
            override fun onFinish() {

            }
            override fun onTick(millisUntilFinished: Long) {
                onSuccess(Random.nextInt(RANGE_NUMBERS))
            }
        }.start()
    }


    companion object {
        private var instance: DataRandomService? = null

        fun getInstance(): DataRandomService? {
            if (instance == null) {
                synchronized(DataRandomService::class.java) {
                    if (instance == null) {
                        instance = DataRandomService()
                    }
                }
            }
            return instance
        }
    }
}