package com.example.paging3example

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class MainViewModel : ViewModel() {
    companion object {
        private const val PAGE_SIZE = 10
        private const val PREFETCH_DISTANCE = PAGE_SIZE                       // default = pageSize
        private const val ENABLE_PLACEHOLDERS = true                          // default = true
        private const val INITIAL_LOAD_SIZE = PAGE_SIZE                       // default = pageSize * 3
        private const val MAX_SIZE = 2 * PREFETCH_DISTANCE + PAGE_SIZE        // default = Int.MAX_VALUE
        private const val JUMP_THRESHOLD = Int.MIN_VALUE                      // default = Int.MIN_VALUE
        private const val JUMPING_SUPPORTED = JUMP_THRESHOLD != Int.MIN_VALUE // default = false
    }

    val flow: Flow<PagingData<MyItem>> = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            enablePlaceholders = ENABLE_PLACEHOLDERS,
            initialLoadSize = INITIAL_LOAD_SIZE,
            maxSize = MAX_SIZE,
            jumpThreshold = JUMP_THRESHOLD
        ),
        pagingSourceFactory = {
            MyPagingSource(jumpingSupported = JUMPING_SUPPORTED)
        },
    ).flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope)
}