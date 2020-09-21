package com.example.paging3example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

private val TAG = MainViewModel::class.java.simpleName

class MainViewModel(private val myDao: MyDao) : ViewModel() {
    private val pageSize = 1
    private val initialLoadSize = 1 //pageSize * 3
    private val totalItems = 10

    val flow: Flow<PagingData<MyItemAdapter.Model>>
        get() = myDao.flow(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
            totalItems = totalItems
        ).map {
            it.map { myItem -> MyItemAdapter.Model.Item(myItem = myItem) as MyItemAdapter.Model }
        }.flowOn(Dispatchers.IO).cachedIn(viewModelScope)
}