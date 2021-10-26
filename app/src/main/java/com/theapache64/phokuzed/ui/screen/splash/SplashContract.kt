package com.theapache64.phokuzed.ui.screen.splash

// ViewState (ViewModel to UI - to render UI)
sealed class SplashViewState {
    data class Loaded(
        val versionName: String
    ) : SplashViewState()
}

// Interactors (UI to ViewModel)
sealed class SplashInteractor {

}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class SplashViewAction {
    object GoToMain : SplashViewAction()
}
