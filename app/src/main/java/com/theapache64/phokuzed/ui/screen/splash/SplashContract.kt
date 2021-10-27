package com.theapache64.phokuzed.ui.screen.splash

// ViewState (ViewModel to UI - to render UI)
sealed class SplashViewState {
    object ConfigLoading : SplashViewState()
    object ConfigLoaded : SplashViewState()
    data class ConfigError(val message: String) : SplashViewState()

}

// Interactors (UI to ViewModel)
sealed class SplashInteractor {
    object UpdateClick : SplashInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class SplashViewAction {
    object GoToMain : SplashViewAction()
    object NeedUpdate : SplashViewAction()
    data class OpenUrl(val url: String) : SplashViewAction()
}
