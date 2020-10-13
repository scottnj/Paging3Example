package com.example.paging3example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

private val TAG = MainActivity::class.java.simpleName

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModel()
    private val myItemAdapter: MyItemAdapter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvData.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(false)
            adapter = myItemAdapter
        }

        lifecycleScope.launchWhenCreated {
            viewModel.flow.collectLatest { pagingData ->
                myItemAdapter.submitData(pagingData)
            }
        }
    }
}