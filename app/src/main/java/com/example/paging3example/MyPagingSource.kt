package com.example.paging3example

import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

private val TAG = MyPagingSource::class.java.simpleName

class MyPagingSource(
    private val pagingSourceCount: Int,
    private val totalItems: Int,
) : PagingSource<Int, MyItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyItem> =
        withContext(Dispatchers.IO) {
            return@withContext try {
                val offset = params.key ?: 0
                val limit = params.loadSize
                val response = query(
                    limit = limit,
                    offset = offset,
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
            } catch (e: RuntimeException) {
                Log.e(TAG, "load: caught: ${e.localizedMessage}", e)
                LoadResult.Error(e)
            }
        }

    // This function simply generates a list of items with useful debugging information.
    // The repeat loop could very easily be replaced with a content provider query or call to
    // some other data source.
    private suspend fun query(
        limit: Int,
        offset: Int,
        pagingSourceCount: Int
    ): List<MyItem> = withContext(Dispatchers.IO) {

//TODO: Added delay to see status of Loading
        delay(500)

        val list = mutableListOf<MyItem>()

        repeat(limit) { index ->
            val id = offset + index

//TODO: Throwing exception to see status of Error
            if (id >= 10) throw RuntimeException("Fake Runtime Exception")

            if (id >= totalItems) return@withContext list

            val item = MyItem(
                id = id,
                pagingSourceCount = pagingSourceCount,
                limit = limit,
                offset = offset,
                index = index
            )
            list.add(item)
        }

        Log.v(
            TAG, """
                query:
                    limit = $limit
                    offset = $offset
                    pagingSourceCount = $pagingSourceCount
                    count = ${list.size}
                """.trimIndent()
        )

        return@withContext list
    }
}