package com.six.sense.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.six.sense.data.repo.AuthRepoImpl
import com.six.sense.domain.ConnectivityObserver
import com.six.sense.domain.repo.AuthRepo
import com.six.sense.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing network-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides a singleton instance of [ChuckerInterceptor].
     * @param context The application context.
     * @return A [ChuckerInterceptor] instance.
     */
    @Provides
    @Singleton
    fun provideChuckerInterceptor(@ApplicationContext context: Context): ChuckerInterceptor {
        return ChuckerInterceptor(context = context)
    }

    /**
     * Provides a singleton instance of [HttpClient] configured with Ktor.
     * @param chuckerInterceptor The Chucker interceptor.
     * @return A configured [HttpClient] instance.
     */
    @Provides
    @Singleton
    fun provideKtorClient(
        chuckerInterceptor: ChuckerInterceptor
    ): HttpClient {
        val okHttpEngine = OkHttp.create {
            addInterceptor(chuckerInterceptor)
        }
        return HttpClient(okHttpEngine) {
            install(Logging) {
                level = LogLevel.BODY
            }
            defaultRequest {
                url(Constants.BASE_URL)
//                header(HttpHeaders.Authorization, Constants.TOKEN)
                header(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json
                )//not needed , but for understanding
                //alternate way
                /**     headers {
                append(HttpHeaders.Authorization, Constants.TOKEN)
                append(HttpHeaders.ContentType, ContentType.Application.Json) //not needed , but for understanding
                }*/
            }
            install(ContentNegotiation) {
                //for kotlinx and gson serialization
                /**             json(Json {
                explicitNulls = false
                encodeDefaults = true
                })
                gson{
                serializeNulls()
                }*/
                //for jackson serialization-recommended
                /*jackson {
                    setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.SKIP))
                }*/
            }
        }
    }

    /**
     * Provides a singleton instance of [AuthRepo].
     * @param context The application context.
     * @param dispatcher The coroutine dispatcher for IO operations.
     * @param connectivity The connectivity observer.
     * @return An [AuthRepo] instance.
     */
    @Provides
    @Singleton
    fun provideAuthRepo(
        @ApplicationContext context: Context,
        @IoDispatcher dispatcher: CoroutineDispatcher,
        connectivity: ConnectivityObserver
    ): AuthRepo =
        AuthRepoImpl(context = context, dispatcher = dispatcher, connectivity = connectivity)

    /**
     * Provides a singleton instance of [ConnectivityObserver].
     * @param context The application context.
     * @return A [ConnectivityObserver] instance.
     */
    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
        ConnectivityObserver(context)

}