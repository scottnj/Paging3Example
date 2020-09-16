package com.example.paging3example

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

enum class UpdaterType {
    NONE, ADAPTER, PAGING_SOURCE
}

class MainViewModel(private val myDao: MyDao) : ViewModel() {
    private val pageSize = 15
    private val initialLoadSize = pageSize * 4
    private val totalItems = 1000
    private val updateInterval: Long = 10
    private var updaterJob: Job? = null
    private val _adapterUpdater = MutableLiveData<Unit>()
    val adapterUpdater: LiveData<Unit> get() = _adapterUpdater
    var adapterUpdatedCount = 0

    val flow
        get() = myDao.flow(
            pageSize = pageSize,
            initialLoadSize = initialLoadSize,
            totalItems = totalItems
        ).flowOn(Dispatchers.IO).cachedIn(viewModelScope)

    fun runUpdater(updaterType: UpdaterType) {
        updaterJob?.cancel()

        updaterJob = viewModelScope.launch(Dispatchers.IO) {
            while (updaterType != UpdaterType.NONE) {
                when (updaterType) {
                    UpdaterType.NONE -> updaterJob?.cancel()
                    UpdaterType.ADAPTER -> _adapterUpdater.postValue(Unit)
                    UpdaterType.PAGING_SOURCE -> myDao.invalidatePagingSource()
                }
                delay(updateInterval)
            }
        }
    }

    fun invalidatePagingSource() {
        viewModelScope.launch(Dispatchers.IO) {
            myDao.invalidatePagingSource()
        }
    }
}