package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.data.repo.TimeRepo
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val timeRepo: TimeRepo
) :
    BaseViewModel<DashboardViewState, DashboardInteractor, DashboardViewAction>(
        defaultViewState = DashboardViewState.Idle
        // defaultViewState = DashboardViewState.Active((System.currentTimeMillis() / 1000) + (2 * 60)) // TODO : This should be idle
    ) {

    init {
        val targetSeconds = timeRepo.getTargetSeconds()
        if (targetSeconds != null) {
            emitViewState(DashboardViewState.Active(targetSeconds))
        }// otherwise it's idle
    }

    override fun onInteraction(interactor: DashboardInteractor) {
        when (interactor) {
            DashboardInteractor.AddToBlockListClicked -> {
                TODO()
            }
            DashboardInteractor.EditBlockListClicked -> {
                TODO()
            }
            DashboardInteractor.ExtendBlockTimerClicked -> {
                TODO()
            }
            is DashboardInteractor.StartClicked -> {
                emitViewAction(DashboardViewAction.ShowDurationPicker)
            }
            DashboardInteractor.TimerFinished -> {
                TODO()
            }
            is DashboardInteractor.TimePicked -> {
                onTimePicked(interactor)
            }
        }.exhaustive()
    }

    @OptIn(ExperimentalTime::class)
    private fun onTimePicked(interactor: DashboardInteractor.TimePicked) {
        viewModelScope.launch {
            emitViewState(DashboardViewState.Loading(R.string.dashboard_calculating_time))

            val hoursInSeconds = Duration.hours(interactor.hour).inWholeSeconds
            val minutesInSeconds = Duration.minutes(interactor.minute).inWholeSeconds
            try {
                val targetSeconds =
                    timeRepo.getCurrentTimeInSeconds() + hoursInSeconds + minutesInSeconds
                timeRepo.saveTargetSeconds(targetSeconds)

                emitViewState(DashboardViewState.Active(targetSeconds))
            } catch (e: IOException) {
                e.printStackTrace()
                TODO("throw time error")
            }
        }
    }
}