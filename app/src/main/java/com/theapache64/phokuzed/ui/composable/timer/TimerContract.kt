package com.theapache64.phokuzed.ui.composable.timer

// ViewState (ViewModel to UI - to render UI)
sealed class TimerViewState {
    object Loading : TimerViewState()
    data class Success(val remainingTime: String) : TimerViewState()
    data class Error(val reason: String) : TimerViewState()
}

// Interactors (UI to ViewModel)
sealed class TimerInteractor {
    // no external interaction
    data class Init(
        val targetSeconds: Long,
        val onFinished: () -> Unit
    ) : TimerInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class TimerViewAction {
    // no one time actions
}
