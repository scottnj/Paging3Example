package com.example.paging3example

import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private val TAG = MyPagingSource::class.java.simpleName

class MyPagingSource(
    private val invalidateCalledCount: Int,
    private val pagingSourceCount: Int,
    private val totalItems: Int
) : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val offset = params.key ?: 0
                val limit = params.loadSize
                val response = query(
                    limit = limit,
                    offset = offset,
                    invalidateCalledCount = invalidateCalledCount,
                    pagingSourceCount = pagingSourceCount,
                )
                val nextKey = if (response.size < limit) null else offset + limit
                val prevKey = if (offset == 0) null else offset - limit
                val itemsAfter = totalItems - (response.size + offset)

                return@withContext LoadResult.Page(
                    data = response,
                    prevKey = prevKey,
                    nextKey = nextKey,
                    itemsBefore = offset,
                    itemsAfter = itemsAfter,
                )
            } catch (e: Exception) {
                Log.e(TAG, "load: ${e.localizedMessage}", e)
                LoadResult.Error(e)
            }
        }

    // This function simply generates a list of items with useful debugging information.
    // The repeat loop could very easily be replaced with a content provider query or call to
    // some other data source.
    private suspend fun query(
        limit: Int,
        offset: Int,
        invalidateCalledCount: Int,
        pagingSourceCount: Int
    ): List<String> = withContext(Dispatchers.IO) {
        delay(1000)

        val list = mutableListOf<String>()

        repeat(limit) { index ->
            val id = offset + index
            if (id >= totalItems) return@withContext list
            val message =
                "$id, invalidateCalledCount:$invalidateCalledCount, pagingSourceCount:$pagingSourceCount, limit:$limit, offset:$offset, index:$index"
            list.add(message)
        }

        Log.v(
            TAG, """
                query:
                    limit = $limit
                    offset = $offset
                    invalidateCalledCount = $invalidateCalledCount
                    pagingSourceCount = $pagingSourceCount
                    count = ${list.size}
                """.trimIndent()
        )

        return@withContext list
    }
}