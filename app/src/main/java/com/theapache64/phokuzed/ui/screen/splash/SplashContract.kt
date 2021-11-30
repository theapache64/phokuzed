package com.theapache64.phokuzed.ui.screen.splash

import androidx.annotation.StringRes

// ViewState (ViewModel to UI - to render UI)
sealed class SplashViewState {
    data class Loading(
        @StringRes val message: Int
    ) : SplashViewState()
    object Success : SplashViewState()
    data class Error(val message: String) : SplashViewState()
    object NoRootAccess : SplashViewState()
}

// Interactors (UI to ViewModel)
sealed class SplashInteractor {
    object UpdateClick : SplashInteractor()
    object RetryRootCheckClick : SplashInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class SplashViewAction {
    object GoToMain : SplashViewAction()
    object ShowUpdateDialog : SplashViewAction()
    data class OpenUrl(val url: String) : SplashViewAction()
}
