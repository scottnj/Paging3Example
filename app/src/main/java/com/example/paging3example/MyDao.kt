package com.example.paging3example

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

private val TAG = MyDao::class.java.simpleName

class MyDao {
    private var pagingSourceCount = 0 // number of pagingSources created

    fun flow(
        pageSize: Int,
        initialLoadSize: Int,
        totalItems: Int
    ): Flow<PagingData<MyItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = initialLoadSize,
                enablePlaceholders = true,
            ),
            pagingSourceFactory = {
                pagingSourceCount++
                Log.d(TAG, "getNewPagingSource: $pagingSourceCount")
                MyPagingSource(
                    pagingSourceCount = pagingSourceCount,
                    totalItems = totalItems,
                )
            },
        ).flow
    }
}