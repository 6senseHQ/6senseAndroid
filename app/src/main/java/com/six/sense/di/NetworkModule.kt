package com.six.sense.di

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.openai.client.OpenAIClient
import com.openai.client.okhttp.OpenAIOkHttpClient
import com.six.sense.data.local.datastore.DataStoreManager
import com.six.sense.data.remote.StripePaymentManager
import com.six.sense.data.repo.AuthRepoImpl
import com.six.sense.domain.ConnectivityObserver
import com.six.sense.domain.repo.AuthRepo
import com.six.sense.presentation.screen.profile.GooglePlayBillingManager
import com.six.sense.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.serialization.json.Json
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
        chuckerInterceptor: ChuckerInterceptor,
    ): HttpClient {
        val okHttpEngine = OkHttp.create { addInterceptor(chuckerInterceptor) }
        return HttpClient(okHttpEngine) {
            install(Logging) { level = LogLevel.BODY }
            defaultRequest {
                url(Constants.BASE_URL)
                contentType(ContentType.Application.Json)
//                header(HttpHeaders.Authorization, Constants.TOKEN)
//                header(
//                    HttpHeaders.ContentType,
//                    ContentType.Application.Json
//                )
                //not needed , but for understanding
                //alternate way
                /**     headers {
                append(HttpHeaders.Authorization, Constants.TOKEN)
                append(HttpHeaders.ContentType, ContentType.Application.Json) //not needed , but for understanding
                }*/
                headers { append(HttpHeaders.Authorization, "Bearer ${"TOKEN"}") }
            }
            install(ContentNegotiation) {
                //for kotlinx and gson serialization
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    explicitNulls = false
                    encodeDefaults = true
                })
            }
            install(Auth) {
                bearer {
                    loadTokens {
                        BearerTokens("access", "refresh")
                    }
                    refreshTokens {
                        val token = client.post("jwt/refresh/") {
                            markAsRefreshTokenRequest()
                            setBody(BearerTokens("access", "refresh"))
                        }.body<BearerTokens>()
                        //Save the token to local
                        BearerTokens("access", "refresh")
                    }
                }
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
        connectivity: ConnectivityObserver,
        dataStoreManager: DataStoreManager,
        firebaseAnalytics: FirebaseAnalytics
    ): AuthRepo =
        AuthRepoImpl(
            context = context,
            dispatcher = dispatcher,
            connectivity = connectivity,
            dataStoreManager = dataStoreManager,
            firebaseAnalytics = firebaseAnalytics
        )

    /**
     * Provides a singleton instance of [ConnectivityObserver].
     * @param context The application context.
     * @return A [ConnectivityObserver] instance.
     */
    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver =
        ConnectivityObserver(context)

    /**
     * Provides a singleton instance of the OpenAI client.
     *
     * @return An instance of the [OpenAIClient].
     */
    @Provides
    @Singleton
    fun provideOpenAIClient(): OpenAIClient =
        OpenAIOkHttpClient.builder()
            .apiKey(Constants.OPENAI_API_KEY)
            .organization(Constants.OPENAI_ORG_ID)
            .project(Constants.OPENAI_PROJECT_ID)
            .build()


    /**
     * Provides a singleton instance of [StripePaymentManager].
     *
     * This function is responsible for creating and providing a [StripePaymentManager] instance.
     * It uses the application [Context] and an [HttpClient] for network requests.
     *
     * @param context The application context.
     * @param ktorClient The Ktor HTTP client for network requests.
     * @return A singleton instance of [StripePaymentManager].
     */
    @Provides
    @Singleton
    fun provideStripePaymentManager(
        @ApplicationContext context: Context,
        ktorClient: HttpClient
    ): StripePaymentManager = StripePaymentManager(context, ktorClient)

    @Provides
    @Singleton
    fun provideSGooglePlayBillingManager(
        @ApplicationContext context: Context,
    ): GooglePlayBillingManager = GooglePlayBillingManager(context)

    @Provides
    @Singleton
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics
}