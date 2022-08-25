package com.mironenko.internship_multithread_async

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mironenko.internship_multithread_async.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val mBinding get() = _binding!!
    private val dataAdapter = DataListAdapter()
    private val viewModel: DataListViewModel by lazy {
        ViewModelProvider(this)[DataListViewModel::class.java]
    }
    private val composite = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(mBinding.root)

        showNumbersFromLiveData()

        showNumbersFromFlow()

        showNumbersFromObservable()

        mBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dataAdapter
        }
    }

    private fun showNumbersFromLiveData() {
        viewModel.numbersLiveData().observe(this) {
            updateListInAdapter(it)
        }
    }

    private fun showNumbersFromObservable() {
        composite.add(viewModel.numbersObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    updateListInAdapter(it)
                },
                {

                }, {

                }
            ))
    }

    private fun showNumbersFromFlow() {
        lifecycleScope.launchWhenStarted {
            viewModel.numbersStateFlow()
                .collect {
                    updateListInAdapter(it)
                }
        }
    }

    private fun updateListInAdapter(newInt: Int) {
        val list = ArrayList(dataAdapter.currentList).toMutableList()
        list.add(newInt)
        dataAdapter.submitList(list)
    }

    override fun onDestroy() {
        _binding = null
        composite.dispose()
        viewModel.stopLiveDataThread()
        super.onDestroy()
    }
}