package com.mironenko.internship_multithread_async

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mironenko.internship_multithread_async.databinding.ActivityMainBinding
import com.mironenko.internship_multithread_async.util.MainFactory
import io.reactivex.rxjava3.kotlin.subscribeBy
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!
    private val dataAdapter = DataListAdapter()
    private val viewModel: DataListViewModel by lazy {
        ViewModelProvider(
            this,
            MainFactory()
        )[DataListViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        showNumbersFromLiveData()

//        showNumbersFromFlow()

//        showNumbersFromObservable()

        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    private fun showNumbersFromLiveData() {
        viewModel.numbersLiveData.observe(this) {
            updateListInAdapter(it)
        }
    }

    private fun showNumbersFromObservable() {
        viewModel.observable.subscribeBy {
            Log.d("TAG", "Observer $it")
            updateListInAdapter(it)
        }
    }

    private fun showNumbersFromFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.numbersStateFlow
                .onEach {
                    updateListInAdapter(it)
                }
                .collect()
        }
    }

    private fun updateListInAdapter(newInt: Int) {
        val list = ArrayList(dataAdapter.currentList).toMutableList()
        list.add(newInt)
        dataAdapter.submitList(list)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}