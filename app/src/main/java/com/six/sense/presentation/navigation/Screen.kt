package com.six.sense.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.MarkChatUnread
import androidx.compose.material.icons.rounded.PersonOutline
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable

/**
 * Defines the different screens in the application.
 */
@Serializable
object Screens {
    @Serializable object Onboarding
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
         * Represents the MaterialComponents sub-screen within the home screen.
         */
        @Serializable object Components
        /**
         * Represents the profile sub-screen within the home screen.
         */
        @Serializable object Profile
    }

    /**
     * Represents the error dialog screen.
     */
    @Serializable
    data class ErrorDialog(val error: String)

    @Serializable
    object ImageViewer
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
    Chat(icon = Icons.Outlined.MarkChatUnread, route = Screens.Home.Chat),
    /**
     * Represents the MaterialComponents screen.
     */
    Components(icon = Icons.Outlined.Category, route = Screens.Home.Components),
    /**
     * Represents the profile screen.
     */
    Profile(icon = Icons.Rounded.PersonOutline, route = Screens.Home.Profile)
}