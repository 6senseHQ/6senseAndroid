package com.six.sense.presentation.screen.onboarding

import com.six.sense.data.local.datastore.DataStoreManager
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(private val dataStore: DataStoreManager) :
    BaseViewModel() {
    fun saveOnboarding(onBoard: Boolean) {
        launch {
            dataStore.save("onboarding", onBoard)
        }
    }
}