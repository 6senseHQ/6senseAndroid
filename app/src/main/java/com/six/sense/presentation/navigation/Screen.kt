package com.six.sense.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material.icons.rounded.Person3
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Defines the different screens in the application.
 */
@Serializable
object Screens {
    /**
     * Represents the login screen.
     */
    @Serializable object Login
    /**
     * Represents the home screen and its sub-screens.
     */
    @Serializable object Home{
        /**
         * Represents the chat sub-screen within the home screen.
         */
        @Serializable object Chat
        /**
         * Represents the profile sub-screen within the home screen.
         */
        @Serializable object Profile
    }
}

/**
 * Extension property to get the route of a KSerializer.
 */
val <T> KSerializer<T>.route
    get() = descriptor.serialName

/**
 * Represents the main screen types used in the bottom navigation bar.
 *
 * @property icon The icon for the screen.
 * @property route The navigation route for the screen.
 */
enum class MainScreenType(val icon: ImageVector, val route: Any) {
    /**
     * Represents the chat screen.
     */
    Chat(icon = Icons.Rounded.Home, route = Screens.Home.Chat),
    /**
     * Represents the profile screen.
     */
    Profile(icon = Icons.Rounded.Person3, route = Screens.Home.Profile)
}