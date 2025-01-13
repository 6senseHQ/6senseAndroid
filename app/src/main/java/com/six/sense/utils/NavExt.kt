package com.six.sense.utils

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
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

inline fun <reified T : Any, reified VM : BaseViewModel> NavGraphBuilder.composableWithVM(
    navController: NavController,
    crossinline content: @Composable (BaseContent<VM>.(NavBackStackEntry) -> Unit)
) {
    composable<T> { navBackStackEntry ->
        val context = LocalContext.current
        val mainViewModel = hiltViewModel<MainViewModel>(context as ViewModelStoreOwner)
        val viewModel = hiltViewModel<VM>()

        val error = viewModel.errorFlow.collectAsStateWithLifecycle(null)
        val success = viewModel.successFlow.collectAsStateWithLifecycle(null)
        val loading = viewModel.loadingFlow.collectAsStateWithLifecycle(false)

        error.value?.let {
            navController.navigate(Screens.ErrorDialog(it))
        }
//        success.value?.let {
//            navController.navigate(Screens.SuccessDialog(it))
//        }
//        if(loading.value)
//            LoadingDialog()
        content(
            BaseContent(
                animatedContentScope = this,
                viewModel = viewModel,
                mainViewModel = mainViewModel
            ), navBackStackEntry
        )
    }
}

data class BaseContent<VM : BaseViewModel>(
    val animatedContentScope: AnimatedContentScope,
    val viewModel: VM,
    val mainViewModel: MainViewModel,
)

