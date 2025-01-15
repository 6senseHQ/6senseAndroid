package com.six.sense.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.six.sense.presentation.MainViewModel
import com.six.sense.presentation.base.BaseViewModel
import com.six.sense.presentation.navigation.Screens
import kotlinx.coroutines.launch

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

@Composable
fun LoadingLayout(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(.6F))
    ){
        CircularProgressIndicator()
    }
}

data class BaseContent<VM : BaseViewModel>(
    val animatedContentScope: AnimatedContentScope,
    val viewModel: VM,
    val mainViewModel: MainViewModel,
)

