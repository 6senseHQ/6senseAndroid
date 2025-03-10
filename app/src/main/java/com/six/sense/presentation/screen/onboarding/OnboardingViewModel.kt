package com.six.sense.presentation.screen.onboarding

import com.six.sense.data.local.datastore.DataStoreManager
import com.six.sense.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * [OnboardingViewModel]
 *
 * This ViewModel is responsible for managing the state and logic related to the onboarding process.
 * It handles the saving of the onboarding completion status to the data store.
 *
 * @property dataStore An instance of [DataStoreManager] for persistent data storage.
 * @constructor Injects the [DataStoreManager] dependency.
 */
@HiltViewModel
class OnboardingViewModel @Inject constructor(private val dataStore: DataStoreManager) :
    BaseViewModel() {
    fun saveOnboarding(onBoard: Boolean) {
        launch {
            dataStore.save("onboarding", onBoard)
        }
    }
}