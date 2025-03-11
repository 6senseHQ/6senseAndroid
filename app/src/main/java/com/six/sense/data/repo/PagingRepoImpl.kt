package com.six.sense.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.six.sense.data.paging.DummyPagingSource
import com.six.sense.domain.model.DummyPaging
import com.six.sense.domain.repo.PagingRepo
import kotlinx.coroutines.flow.Flow

/**
 * Implementation of the [PagingRepo] interface.
 *
 * This class is responsible for providing a [Flow] of [PagingData] containing [DummyPaging] objects.
 * It uses the Android Paging library to handle the pagination of data from a [DummyPagingSource].
 */
class PagingRepoImpl : PagingRepo {
    override fun getPagingData(): Flow<PagingData<DummyPaging>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { DummyPagingSource() }
        ).flow
    }
}