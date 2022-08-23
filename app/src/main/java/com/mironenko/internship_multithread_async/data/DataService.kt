package com.mironenko.internship_multithread_async.data

interface DataService {
    fun randomNumbersByTimer(onSuccess: (number: Int) -> Unit)
}