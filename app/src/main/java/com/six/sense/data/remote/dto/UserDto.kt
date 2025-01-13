package com.six.sense.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data Transfer Object (DTO) representing user data from the remote server.
 * @property email The user's email address.
 * @property name The user's name.
 * @property phone The user's phone number.
 * @property photoUrl The URL of the user's profile photo.
 */
@Serializable
data class UserDto(
    /**
     * The user's email address.
     */
    @SerialName("email")
    val email: String? = null,
    /**
     * The user's name.
     */
    @SerialName("name")
    val name: String? = null,
    /**
     * The user's phone number.
     */
    @SerialName("phone")
    val phone: String? = null,
    /**
     * The URL of the user's profile photo.
     */
    @SerialName("photo_url")
    val photoUrl: String? = null
)