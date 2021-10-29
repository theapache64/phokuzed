package com.theapache64.phokuzed.ui.screen.dashboard

import com.theapache64.phokuzed.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor() :
    BaseViewModel<DashboardViewState, DashboardInteractor, DashboardViewAction>(
        defaultViewState = DashboardViewState.Active
    ) {
    override fun onInteraction(interactor: DashboardInteractor) {
        when (interactor) {
            DashboardInteractor.AddToBlockListClicked -> TODO()
            DashboardInteractor.EditBlockListClicked -> TODO()
            DashboardInteractor.ExtendBlockTimerClicked -> TODO()
            DashboardInteractor.StartClicked -> TODO()
        }
    }
}