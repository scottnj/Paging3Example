package com.example.paging3example

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

private val TAG = MyDao::class.java.simpleName

class MyDao {
    private var pagingSource: PagingSource<Int, String>? = null
    private var pagingSourceCount = 0 // number of pagingSources created
    private var invalidateCalledCount = 0 // number of times invalidatePagingSource() was called

    fun flow(
        pageSize: Int,
        initialLoadSize: Int,
        totalItems: Int
    ): Flow<PagingData<String>> {
        val config = PagingConfig(pageSize = pageSize, initialLoadSize = initialLoadSize)
        val pagingSourceFactory: () -> PagingSource<Int, String> = {
            getNewPagingSource(totalItems)
        }
        return Pager(config = config, pagingSourceFactory = pagingSourceFactory).flow
    }

    private fun getNewPagingSource(totalItems: Int): PagingSource<Int, String> {
        pagingSourceCount++
        Log.d(TAG, "getNewPagingSource: $pagingSourceCount")
        val pagingSource = MyPagingSource(invalidateCalledCount, pagingSourceCount, totalItems)
        this.pagingSource = pagingSource
        return pagingSource
    }

    fun invalidatePagingSource() {
        invalidateCalledCount++
        Log.d(TAG, "invalidatePagingSource: $invalidateCalledCount")
        pagingSource?.invalidate()
        //pagingSource = null
    }
}