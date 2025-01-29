package com.six.sense.di

import android.content.Context
import androidx.room.Room
import com.six.sense.data.local.datastore.DataStoreManager
import com.six.sense.data.local.room.LocalDatabase
import com.six.sense.data.local.room.ThreadDao
import com.six.sense.data.local.room.UserDao
import com.six.sense.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    /**
     * Provides a singleton instance of [LocalDatabase].
     * @param context The application context.
     * @return A [LocalDatabase] instance.
     */
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): LocalDatabase = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        Constants.DATABASE_NAME
    ).build()

    /**
     * Provides a singleton instance of [UserDao].
     * @param database The [LocalDatabase] instance.
     * @return A [UserDao] instance.
     */
    @Singleton
    @Provides
    fun provideLocalDao(database: LocalDatabase): UserDao = database.userDao()


    /**
     * Provides a singleton instance of [ThreadDao].
     * @param database The [LocalDatabase] instance.
     * @return A [ThreadDao] instance.
     */
    @Singleton
    @Provides
    fun provideThreadDao(database: LocalDatabase): ThreadDao = database.threadDao()

    /**
     * Provide data store manager
     *
     * @param context The application context.
     * @return
     */
    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context): DataStoreManager = DataStoreManager(context)

}