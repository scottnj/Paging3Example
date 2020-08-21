package com.example.paging3example

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val myStringAdapter: MyStringAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvData.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = myStringAdapter
        }

        lifecycleScope.launch {
            viewModel.flow.collectLatest { pagingData ->
                Log.v(TAG, "onCreate: collecting: $pagingData")
                myStringAdapter.submitData(pagingData)
            }
        }

        rbtnNone.setOnClickListener {
            Log.d(TAG, "onCreate: rbtnNone clicked")
            viewModel.runUpdater(UpdaterType.NONE)
        }

        rbtnAdapter.setOnClickListener {
            Log.d(TAG, "onCreate: rbtnAdapter clicked")
            viewModel.runUpdater(UpdaterType.ADAPTER)
        }

        rbtnPagingSource.setOnClickListener {
            Log.d(TAG, "onCreate: rbtnPagingSource clicked")
            viewModel.runUpdater(UpdaterType.PAGING_SOURCE)
        }

        btnAdapterRefresh.setOnClickListener {
            Log.d(TAG, "onCreate: btnAdapterRefresh clicked")
            refreshAdapter()
        }

        btnPagingSourceInvalidate.setOnClickListener {
            Log.d(TAG, "onCreate: btnPagingSourceInvalidate clicked")
            viewModel.invalidatePagingSource()
        }

        viewModel.adapterUpdater.observe(this, {
            Log.v(TAG, "onCreate: adapterUpdater observed")
            refreshAdapter()
        })
    }

    private fun refreshAdapter() {
        viewModel.adapterUpdatedCount++
        Log.d(TAG, "refreshAdapter: called ${viewModel.adapterUpdatedCount}")
        myStringAdapter.refresh()
    }
}