package com.six.sense.data.local.appsearch

import android.content.Context
import androidx.appsearch.app.SetSchemaRequest
import androidx.appsearch.localstorage.LocalStorage
import com.google.common.util.concurrent.Futures
import com.six.sense.data.local.appsearch.AppSearchDummyData.addDummyNotes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class AppSearchManager @Inject constructor(
    @ApplicationContext private val context: Context,
    val mExecutor: ExecutorService,
) {
    private val isInitialized: MutableStateFlow<Boolean> = MutableStateFlow(false)
    lateinit var appSearchSession: androidx.appsearch.app.AppSearchSession


    private val sessionFuture = LocalStorage.createSearchSessionAsync(
        LocalStorage.SearchContext.Builder(context, "notes_app")
            .build()
    )

    private val setSchemaRequest = SetSchemaRequest.Builder().addDocumentClasses(Note::class.java)
        .build()

    private val setSchemaFuture = Futures.transformAsync(sessionFuture, { session ->
        session?.setSchemaAsync(setSchemaRequest)
    }, mExecutor)


    init {
        setSchemaFuture.addListener({
            isInitialized.value = true
            addDummyNotes()
        }, mExecutor)
        Futures.addCallback(
            sessionFuture,
            object :
                com.google.common.util.concurrent.FutureCallback<androidx.appsearch.app.AppSearchSession> {
                override fun onSuccess(session: androidx.appsearch.app.AppSearchSession?) {
                    appSearchSession = session!!
                }

                override fun onFailure(t: Throwable) {
                    //todo
                }
            },
            mExecutor
        )
    }

    fun isInitialized() = isInitialized
}
