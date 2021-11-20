package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.annotation.StringRes

// ViewState (ViewModel to UI - to render UI)
sealed class DashboardViewState {
    object Idle : DashboardViewState()
    data class Loading(@StringRes val message: Int) : DashboardViewState()
    data class Active(val targetSeconds: Long) : DashboardViewState()
    data class Error(val reason: String) : DashboardViewState()
}

// Interactors (UI to ViewModel)
sealed class DashboardInteractor {
    object StartClicked : DashboardInteractor()
    object EditBlockListClicked : DashboardInteractor()
    object AddToBlockListClicked : DashboardInteractor()
    object ExtendBlockTimerClicked : DashboardInteractor()
    object TimerFinished : DashboardInteractor()

    data class TimePicked(val hour: Int, val minute: Int) : DashboardInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class DashboardViewAction {
    object ShowDurationPicker : DashboardViewAction()
    object ErrorMinTime : DashboardViewAction()
    data class GoToBlockList(val shouldEnableRemove: Boolean) : DashboardViewAction()
}
