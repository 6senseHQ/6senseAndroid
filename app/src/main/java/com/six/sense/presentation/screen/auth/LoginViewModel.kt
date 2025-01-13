package com.six.sense.presentation.screen.auth

import androidx.lifecycle.SavedStateHandle
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

}