package com.theapache64.phokuzed.ui.composable.timer

// ViewState (ViewModel to UI - to render UI)
sealed class RemoteTimerViewState {
    object Loading : RemoteTimerViewState()
    data class NewTime(val remainingTime: String) : RemoteTimerViewState()
    data class Error(val reason: String) : RemoteTimerViewState()
}

// Interactors (UI to ViewModel)
sealed class RemoteTimerInteractor {
    // no external interaction
    data class StartTimer(
        val targetSeconds: Long,
        val onFinished: () -> Unit
    ) : RemoteTimerInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class RemoteTimerViewAction {
    // no one time actions
}
