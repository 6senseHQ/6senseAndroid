package com.six.sense.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.six.sense.domain.model.DummyPaging

class DummyPagingSource : PagingSource<Int, DummyPaging>() {
    override fun getRefreshKey(state: PagingState<Int, DummyPaging>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DummyPaging> {
        val data = listOf(DummyPaging())
        return LoadResult.Page(
            data = data,
            prevKey = null,
            nextKey = null
        )
    }
}