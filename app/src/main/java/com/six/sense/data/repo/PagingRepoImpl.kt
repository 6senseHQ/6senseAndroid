package com.six.sense.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.six.sense.data.paging.DummyPagingSource
import com.six.sense.domain.model.DummyPaging
import com.six.sense.domain.repo.PagingRepo
import kotlinx.coroutines.flow.Flow

class PagingRepoImpl : PagingRepo {
    override fun getPagingData(): Flow<PagingData<DummyPaging>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { DummyPagingSource() }
        ).flow
    }
}