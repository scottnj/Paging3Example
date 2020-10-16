package com.example.paging3example

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var myItemAdapter: MyItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvData.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = myItemAdapter
        }

        lifecycleScope.launchWhenCreated {
            viewModel.flow.collectLatest { pagingData ->
                myItemAdapter.submitData(pagingData)
            }
        }
    }
}