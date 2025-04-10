package com.six.sense.data.local.appsearch

import androidx.appsearch.annotation.Document
import androidx.appsearch.app.AppSearchSchema

@Document
data class Note(
    @Document.Namespace
    val namespace: String = "user",
    @Document.Id
    val id: String,

    @Document.StringProperty(indexingType = AppSearchSchema.StringPropertyConfig.INDEXING_TYPE_PREFIXES)
    val title: String,

    val content: String,
    )
