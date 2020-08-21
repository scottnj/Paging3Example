package com.example.paging3example

import androidx.paging.PagingSource

private val TAG = MyPagingSource::class.java.simpleName

class MyPagingSource(
    private val invalidateCalledCount: Int,
    private val pagingSourceCount: Int,
    private val totalItems: Int
) : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {

        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize
            val response = query(
                limit = limit,
                offset = offset,
                invalidateCalledCount = invalidateCalledCount,
                pagingSourceCount = pagingSourceCount
            )
            val nextKey = if (response.size < limit) null else offset + limit

            return LoadResult.Page(
                data = response,
                prevKey = null,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            // TODO: Investigate Paging 3 error handling
            LoadResult.Error(e)
        }
    }

    // This function simply generates a list of items with useful debugging information.
    // The repeat loop could very easily be replaced with a content provider query or call to
    // some other data source.
    private fun query(
        limit: Int,
        offset: Int,
        invalidateCalledCount: Int,
        pagingSourceCount: Int
    ): List<String> {
        val list = mutableListOf<String>()

        repeat(limit) { index ->
            val id = offset + index
            if (id >= totalItems) return list
            val message =
                "$id, invalidateCalledCount:$invalidateCalledCount, pagingSourceCount:$pagingSourceCount, limit:$limit, offset:$offset, index:$index"
            list.add(message)
        }

        return list
    }
}