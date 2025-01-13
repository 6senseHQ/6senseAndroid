package com.six.sense.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/**
 * Qualifier annotation for injecting a coroutine dispatcher for IO operations.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IoDispatcher

/**
 * Qualifier annotation for injecting the default coroutine dispatcher.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultDispatcher

/**
 * Dagger Hilt module for providing coroutine dispatchers.
 */
@Module
@InstallIn(SingletonComponent::class)
object CoroutinesModule {

    /**
     * Provides a coroutine dispatcher for IO operations.
     * @return A [CoroutineDispatcher] for IO operations.
     */
    @Provides
    @IoDispatcher
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    /**
     * Provides the default coroutine dispatcher.
     * @return The default [CoroutineDispatcher].
     */
    @Provides
    @DefaultDispatcher
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}