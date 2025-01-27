package com.six.sense.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.six.sense.presentation.MainViewModel
import com.six.sense.presentation.base.BaseViewModel
import com.six.sense.presentation.navigation.Screens
import kotlinx.coroutines.launch

/**
 * An extension function for [NavGraphBuilder] that creates a composable destination with a ViewModel.
 *
 * This function simplifies the process of creating composable destinations that require a ViewModel.
 * It automatically handles the creation of the ViewModel, loading state, error handling, and success messages.
 *
 * @receiver NavGraphBuilder
 * @param T The type of the destination.
 * @param VM The type of the ViewModel associated with the destination.
 * @param navController The NavController for navigation.
 * @param content The composable content to be displayed for this destination.
 *   It receives a [BaseContent] object containing the ViewModel and other necessary components.
 */
inline fun <reified T : Any, reified VM : BaseViewModel> NavGraphBuilder.composableWithVM(
    navController: NavController,
    crossinline content: @Composable (BaseContent<VM>.(NavBackStackEntry) -> Unit)
) {
    composable<T> { navBackStackEntry ->
        val context = LocalContext.current
        val mainViewModel = hiltViewModel<MainViewModel>(context as ViewModelStoreOwner)
        val viewModel = hiltViewModel<VM>()

        val isLoading by viewModel.loadingFlow.collectAsStateWithLifecycle()
        val snackBarHostState = remember { SnackbarHostState() }
        val coroutineScope = rememberCoroutineScope()

        LaunchedEffect(Unit) {
            mainViewModel.firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
                val name = navBackStackEntry.destination.route.toString().split('.').lastOrNull()
                    ?.split('/')?.firstOrNull() ?: ""
                name.log("ScreenLog")
                param(FirebaseAnalytics.Param.SCREEN_NAME, name)
                param(FirebaseAnalytics.Param.SCREEN_CLASS, name)
            }
            coroutineScope.launch {
                viewModel.errorFlow.collect { error ->
                    navController.navigate(Screens.ErrorDialog(error))
                }
            }
            coroutineScope.launch {
                viewModel.successFlow.collect { success ->
                    snackBarHostState.showSnackbar(success)
                }
            }
        }

        content(BaseContent(
            animatedContentScope = this,
            viewModel = viewModel,
            mainViewModel = mainViewModel
        ), navBackStackEntry)

        isLoading.log("isLoading")
        if(isLoading)
            LoadingLayout()
    }
}

/**
 * A composable function that displays a loading layout.
 *
 * This layout shows a circular progress indicator on top of a semi-transparent background.
 *
 * @param modifier Modifier for the layout.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoadingLayout(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(.6F))
    ){
        LoadingIndicator(
            modifier = Modifier.size(60.dp),
        )
    }
}

/**
 * Data class that holds the base content for a composable destination.
 *
 * @property animatedContentScope The AnimatedContentScope for animations.
 * @property viewModel The ViewModel associated with the destination.
 * @property mainViewModel The MainViewModel for the application.
 */
data class BaseContent<VM : BaseViewModel>(
    /**
     * The AnimatedContentScope for animations.
     */
    val animatedContentScope: AnimatedContentScope,
    /**
     * The ViewModel associated with the destination.
     */
    val viewModel: VM,
    /**
     * The MainViewModel for the application.
     */
    val mainViewModel: MainViewModel,
)