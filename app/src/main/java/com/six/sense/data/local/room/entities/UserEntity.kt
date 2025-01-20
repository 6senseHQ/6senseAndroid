package com.six.sense.data.local.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a user entity in the local database.
 * @property id The primary key for the user entity.
 */
@Entity(tableName = "user_table")
data class UserEntity(
    /**
     * The primary key for the user entity.
     */
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)