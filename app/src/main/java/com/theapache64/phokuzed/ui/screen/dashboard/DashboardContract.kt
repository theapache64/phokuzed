package com.theapache64.phokuzed.ui.screen.dashboard

// ViewState (ViewModel to UI - to render UI)
sealed class DashboardViewState{
    object Idle : DashboardViewState()
    object Active : DashboardViewState()
}

// Interactors (UI to ViewModel)
sealed class DashboardInteractor{
   object StartClicked : DashboardInteractor()
   object EditBlockListClicked : DashboardInteractor()
   object AddToBlockListClicked : DashboardInteractor()
   object ExtendBlockTimerClicked : DashboardInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class DashboardViewAction {

}
