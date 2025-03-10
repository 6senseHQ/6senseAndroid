package com.six.sense.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.six.sense.domain.model.DummyPaging

/**
 * A dummy implementation of [PagingSource] used for testing or demonstration purposes.
 *
 * This class provides a minimal implementation of [PagingSource] that always returns a single
 * page with a single [DummyPaging] item. It is not intended for use in production code where
 * actual data pagination is required.
 *
 * The `load` method always returns a page containing a single `DummyPaging` object.
 * Both `prevKey` and `nextKey` are set to null, indicating that there are no previous or next pages.
 *
 * The `getRefreshKey` method provides a basic implementation to calculate a refresh key based on the anchor position.
 *
 * @see PagingSource
 * @see PagingState
 * @see LoadResult
 * @see LoadParams
 * @see DummyPaging
 */
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