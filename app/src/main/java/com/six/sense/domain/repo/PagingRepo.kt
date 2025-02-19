package com.six.sense.domain.repo

import androidx.paging.PagingData
import com.six.sense.domain.model.DummyPaging
import kotlinx.coroutines.flow.Flow

interface PagingRepo {
    fun getPagingData(): Flow<PagingData<DummyPaging>>
}