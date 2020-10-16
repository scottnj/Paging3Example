package com.example.paging3example

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.paging3example.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var myItemAdapter: MyItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvData.apply {
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