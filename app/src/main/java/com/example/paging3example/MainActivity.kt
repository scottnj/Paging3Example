package com.example.paging3example

import android.os.Bundle
import android.util.Log
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

            //TODO: To show the header/footer, Comment/Uncomment code here
            adapter = myItemAdapter.withLoadStateHeaderAndFooter(
                header = MyLoadStateAdapter(),
                footer = MyLoadStateAdapter(),
            )

//            adapter = myItemAdapter.withLoadStateHeader(
//                header = MyLoadStateAdapter(),
//            )

//            adapter = myItemAdapter.withLoadStateFooter(
//                footer = MyLoadStateAdapter(),
//            )
        }

        lifecycleScope.launchWhenCreated {
            viewModel.flow.collectLatest { pagingData ->
                Log.v(TAG, "onCreate: collecting: $pagingData")
                myItemAdapter.submitData(pagingData)
            }
        }
    }
}