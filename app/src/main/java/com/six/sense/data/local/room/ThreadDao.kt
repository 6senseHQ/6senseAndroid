package com.six.sense.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.six.sense.data.local.room.entities.ThreadsEntity

/**
 * Data Access Object for interacting with the user table in the local database.
 */
@Dao
interface ThreadDao {
    /**
     * Inserts a Threads entity into the database.
     * @param threads The Threads entity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertData(threads: ThreadsEntity)

    @Query("SELECT * FROM threads_table")
    suspend fun getAllThreads(): List<ThreadsEntity>

    @Query("DELETE FROM threads_table WHERE threadId = :threadId")
    suspend fun deleteThread(threadId: String)

}