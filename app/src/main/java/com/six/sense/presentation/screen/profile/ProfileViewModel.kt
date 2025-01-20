package com.six.sense.presentation.screen.profile

import androidx.lifecycle.SavedStateHandle
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.six.sense.data.local.datastore.DataStoreManager
import com.six.sense.data.local.datastore.StoreKeys
import com.six.sense.domain.model.UserInfo
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel for the profile screen.
 *
 * This ViewModel manages the state and logic for the profile screen.
 *
 * @property savedStateHandle SavedStateHandle for managing state across configuration changes.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val dataStoreManager: DataStoreManager
) : BaseViewModel(){

    val userInfo = savedStateHandle.getStateFlow(StoreKeys.USER_INFO, UserInfo())

    init {
        launch(showLoading = false){
            savedStateHandle[StoreKeys.USER_INFO] = dataStoreManager.read(StoreKeys.USER_INFO, UserInfo())
        }
    }

    fun signOut() {
        Firebase.auth.signOut()
    }

}