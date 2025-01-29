package com.six.sense.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.six.sense.data.local.room.entities.ThreadsEntity
import com.six.sense.data.local.room.entities.UserEntity

/**
 * The Room database for local data persistence.
 */
@Database(entities = [UserEntity::class, ThreadsEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase: RoomDatabase() {
    /**
     * Provides access to the [UserDao].
     * @return The [UserDao] instance.
     */
    abstract fun userDao(): UserDao

    abstract fun threadDao(): ThreadDao
}