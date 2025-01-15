package com.six.sense.presentation.screen.auth

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import com.facebook.login.widget.LoginButton
import com.six.sense.domain.repo.AuthRepo
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authRepo: AuthRepo
) : BaseViewModel() {

    fun facebookSignIn(button: LoginButton, onSuccess: () -> Unit) {
        launch(showLoading = false){
            authRepo.facebookSignIn(button)
            onSuccess()
        }
    }
    fun googleSignIn(activity: Activity, onSuccess: () -> Unit) {
        launch {
            authRepo.googleSignIn(activity)
            onSuccess()
        }

    }

}