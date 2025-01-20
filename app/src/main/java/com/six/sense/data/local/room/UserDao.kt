package com.six.sense.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.six.sense.data.local.room.entities.UserEntity

/**
 * Data Access Object for interacting with the user table in the local database.
 */
@Dao
interface UserDao {
    /**
     * Inserts a user entity into the database.
     * @param dummyEntity The user entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(dummyEntity: UserEntity)
}