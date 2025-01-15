package com.six.sense.presentation.navigation.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.core.layout.WindowWidthSizeClass
import com.six.sense.presentation.navigation.MainScreenType
import com.six.sense.presentation.navigation.route.navDrawerRoute

/**
 * A composable function that sets up the bottom navigation graph.
 *
 * @param startDestination The starting destination for the navigation graph.
 * @param currentDestination The name of the currently selected destination.
 * @param onDestinationChanged A callback function that is invoked when the user selects a new destination.
 * @param navController The NavHostController for the main navigation graph.
 * @param navControllerBottomBar The NavHostController for the bottom navigation bar.
 * @param modifier Modifier for the layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupSideNavGraph(
    startDestination: Any,
    currentDestination: String,
    onDestinationChanged: (MainScreenType) -> Unit,
    navController: NavHostController,
    navControllerBottomBar: NavHostController,
    modifier: Modifier,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    var isDrawerOpen by remember { mutableStateOf(false) }

    val mainContent: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("App Title") },
                    navigationIcon = {
                        IconButton(onClick = { isDrawerOpen = true }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                NavHost(
                    startDestination = startDestination,
                    navController = navControllerBottomBar
                ) {
                    navDrawerRoute(
                        navController = navController
                    )
                }
            }
        }
    }
    val drawerContent: @Composable () -> Unit = {
        Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.headlineSmall)
        VerticalDivider()
        MainScreenType.entries.forEach{  destination ->
            NavigationDrawerItem(
                icon = { Icon(destination.icon, contentDescription = destination.name) },
                label = { Text(destination.name) },
                selected = currentDestination == destination.name,
                onClick = {
                    onDestinationChanged(destination)
                },
            )
        }
    }

// Dynamic drawer based on window size class
    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT, WindowWidthSizeClass.MEDIUM -> {
            // Use ModalNavigationDrawer for compact and medium screens
            ModalNavigationDrawer(
                drawerContent = drawerContent,
                drawerState = rememberDrawerState(initialValue = if (isDrawerOpen) DrawerValue.Open else DrawerValue.Closed),
                modifier = Modifier.fillMaxSize(),
                gesturesEnabled = true, // Allow swipe gestures to open/close the drawer
                content = mainContent
            )
        }

        WindowWidthSizeClass.EXPANDED -> {
            // Use PermanentNavigationDrawer for expanded screens (e.g., tablets in landscape mode)
            PermanentNavigationDrawer(
                drawerContent = drawerContent,
                modifier = Modifier.fillMaxSize(),
                content = mainContent
            )
        }
    }
}