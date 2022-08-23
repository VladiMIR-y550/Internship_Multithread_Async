package com.mironenko.internship_multithread_async.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mironenko.internship_multithread_async.DataListViewModel
import com.mironenko.internship_multithread_async.data.DataRandomService
import com.mironenko.internship_multithread_async.data.DataService

class MainFactory : ViewModelProvider.Factory {
    private val dataService: DataService =
        DataRandomService.getInstance()!!

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DataListViewModel(dataService = dataService) as T
    }
}