package com.example.paging3example

import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

private val TAG = MyPagingSource::class.java.simpleName

class MyPagingSource(override val jumpingSupported: Boolean) : PagingSource<Int, MyItem>() {

    companion object {
        val sampleData: List<MyItem> = mutableListOf<MyItem>().apply {
            repeat(125) { index -> add(MyItem(index)) }
        }
    }

    private fun getCount(): Int = sampleData.size

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MyItem> {
        return withContext(Dispatchers.IO) {
            return@withContext try {
                val offset = params.key ?: 0
                val limit = params.loadSize

                val response = query(limit, offset)

                val itemsAfter = getCount() - (response.size + offset)
                val nextKey = if (itemsAfter == 0) null else min(offset + limit, getCount())
                val prevKey = if (offset == 0) null else max(offset - limit, 0)

                LoadResult.Page(
                    data = response,
                    prevKey = prevKey,
                    nextKey = nextKey,
                    itemsBefore = offset,
                    itemsAfter = itemsAfter,
                )
            } catch (e: ExpectedException) {
                Log.e(TAG, "load: caught: ${e.localizedMessage}", e)
                LoadResult.Error(e)
            }
        }
    }

    private suspend fun query(
        limit: Int,
        offset: Int,
    ): List<MyItem> = withContext(Dispatchers.IO) {
        Log.d(TAG, "query: limit: $limit, offset: $offset")

        delay(500)
        //if (offset > 100) throw ExpectedException()

        val minIndex = max(offset, 0)
        val maxIndex = min(limit + offset, getCount())
        return@withContext sampleData.subList(minIndex, maxIndex)
    }

    class ExpectedException(message: String = "Fake exception") : RuntimeException(message)
}