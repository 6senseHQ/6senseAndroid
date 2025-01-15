package com.six.sense.presentation.screen.auth

import android.app.Activity
import androidx.lifecycle.SavedStateHandle
import com.facebook.login.widget.LoginButton
import com.six.sense.domain.repo.AuthRepo
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the login screen.
 *
 * This ViewModel handles the logic for Facebook and Google sign-in.
 *
 * @property savedStateHandle SavedStateHandle for managing state across configuration changes.
 * @property authRepo The authentication repository for handling sign-in operations.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val authRepo: AuthRepo
) : BaseViewModel() {

    /**
     * Initiates the Facebook sign-in process.
     *
     * @param button The Facebook login button.
     * @param onSuccess A callback function that is invoked when the sign-in is successful.
     */
    fun facebookSignIn(button: LoginButton, onSuccess: () -> Unit) {
        launch(showLoading = false){
            val result= authRepo.facebookSignIn(button)
            authRepo.firebaseSignIn(result.accessToken)
            onSuccess()
        }
    }

    /**
     * Initiates the Google sign-in process.
     *
     * @param activity The activity context.
     * @param onSuccess A callback function that is invoked when the sign-in is successful.
     */
    fun googleSignIn(activity: Activity, onSuccess: () -> Unit) {
        launch {
            val result= authRepo.googleSignIn(activity)
            authRepo.firebaseSignIn(result)
            onSuccess()
        }

    }

}