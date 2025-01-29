package com.six.sense.presentation.navigation.graph

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.window.core.layout.WindowWidthSizeClass
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.six.sense.R
import com.six.sense.presentation.MainViewModel
import com.six.sense.presentation.navigation.MainScreenType
import com.six.sense.presentation.navigation.Screens
import com.six.sense.presentation.navigation.route
import com.six.sense.presentation.navigation.route.navDrawerRoute
import ir.kaaveh.sdpcompose.sdp
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch

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
    val context = LocalContext.current
    val mainViewModel = hiltViewModel<MainViewModel>(context as ViewModelStoreOwner)
    val currentRoute by navControllerBottomBar.currentBackStackEntryFlow.mapNotNull { it.destination.route }
        .collectAsStateWithLifecycle("")
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isUiLightMode by mainViewModel.isUiLightMode.collectAsStateWithLifecycle()
    var showModelDialog by remember { mutableStateOf(false) }

    val mainContent: @Composable () -> Unit = {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = currentDestination) },
                    navigationIcon = {
                        if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.EXPANDED) {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }) {
                                Icon(Icons.Default.Menu, contentDescription = "Menu")
                            }
                        }
                    },

                    actions = {
                        if (currentRoute == Screens.Home.Profile.serializer().route) IconButton(
                            onClick = {
                                mainViewModel.switchUiMode(!isUiLightMode)
                            }) {
                            Icon(
                                if (isUiLightMode) Icons.Outlined.DarkMode else Icons.Outlined.LightMode,
                                contentDescription = "Menu"
                            )
                        }
                        if (currentRoute == Screens.Home.Chat.serializer().route) IconButton(onClick = { showModelDialog = !showModelDialog }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.page_info_24px),
                                contentDescription = "Automate"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            NavHost(
                startDestination = startDestination,
                navController = navControllerBottomBar
            ) {
                navDrawerRoute(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    onLogoutClicked = {
                        Firebase.auth.signOut()
                        navController.navigate(Screens.Login) {
                            popUpTo(Screens.Home) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
            // BackHandler to handle drawer state
            BackHandler(enabled = drawerState.isOpen) {
                scope.launch { drawerState.close() }
            }
        }
    }
    val drawerContent: @Composable () -> Unit = {
        Text(
            stringResource(R.string.app_name),
            modifier = Modifier.padding(16.sdp),
            style = MaterialTheme.typography.headlineSmall
        )
        HorizontalDivider(modifier = Modifier.padding(5.sdp))
        MainScreenType.entries.forEach { destination ->
            NavigationDrawerItem(
                icon = { Icon(destination.icon, contentDescription = destination.name) },
                label = { Text(destination.name) },
                selected = currentDestination == destination.name,
                onClick = {
                    onDestinationChanged(destination)
                    scope.launch {
                        drawerState.close()
                    }
                },
                modifier = Modifier.padding(6.sdp)
            )
        }
    }

// Dynamic drawer based on window size class
    when (windowSizeClass.windowWidthSizeClass) {
        WindowWidthSizeClass.COMPACT, WindowWidthSizeClass.MEDIUM -> {
            // Use ModalNavigationDrawer for compact and medium screens
            ModalNavigationDrawer(
                drawerContent = {
                    ModalDrawerSheet { drawerContent() }
                },
                drawerState = drawerState,
                gesturesEnabled = true, // Allow swipe gestures to open/close the drawer
                content = mainContent
            )
        }

        WindowWidthSizeClass.EXPANDED -> {
            // Use PermanentNavigationDrawer for expanded screens (e.g., tablets in landscape mode)
            PermanentNavigationDrawer(
                drawerContent = {
                    PermanentDrawerSheet { drawerContent() }
                },
                content = mainContent
            )
        }
    }
}