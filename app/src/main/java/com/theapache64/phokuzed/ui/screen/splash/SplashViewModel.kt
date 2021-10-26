package com.theapache64.phokuzed.ui.screen.splash

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.BuildConfig
import com.theapache64.phokuzed.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() :
    BaseViewModel<SplashViewState, SplashInteractor, SplashViewAction>() {

    companion object {
        private const val SPLASH_DURATION_IN_MILLIS = 1500L
    }

    init {
        emitViewState(SplashViewState.Loaded("v${BuildConfig.VERSION_NAME}"))

        viewModelScope.launch {
            delay(SPLASH_DURATION_IN_MILLIS)
            emitViewAction(SplashViewAction.GoToMain)
        }
    }

    override fun onInteraction(interactor: SplashInteractor) {
        // no external interaction
    }
}