package com.six.sense.presentation.screen.profile

import androidx.lifecycle.SavedStateHandle
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
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel()