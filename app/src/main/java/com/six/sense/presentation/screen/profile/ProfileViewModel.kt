package com.six.sense.presentation.screen.profile

import androidx.lifecycle.SavedStateHandle
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel()