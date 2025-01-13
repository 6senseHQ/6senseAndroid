package com.six.sense.presentation.navigation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.six.sense.presentation.navigation.MainScreenType

/**
 * A composable function that renders a bottom navigation bar.
 *
 * @param currentDestination The name of the currently selected destination.
 * @param onDestinationChanged A callback function that is invoked when the user selects a new destination.
 */
@Composable
fun BottomBar(
    currentDestination: String,
    onDestinationChanged: (MainScreenType) -> Unit
) {
    NavigationBar {
        MainScreenType.entries.forEach{  destination ->
            NavigationBarItem(
                icon = { Icon(destination.icon, contentDescription = destination.name) },
                label = { Text(destination.name) },
                selected = currentDestination == destination.name,
                onClick = {
                    onDestinationChanged(destination)
                },
            )
        }
    }
}