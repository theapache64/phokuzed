package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.core.HostManager
import com.theapache64.phokuzed.data.repo.BlockListRepo
import com.theapache64.phokuzed.data.repo.HostRepo
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
    private val timeRepo: TimeRepo,
    private val hostRepo: HostRepo,
    private val blockListRepo: BlockListRepo
) : BaseViewModel<DashboardViewState, DashboardInteractor, DashboardViewAction>(
    defaultViewState = DashboardViewState.Idle
    // defaultViewState = DashboardViewState.Active((System.currentTimeMillis() / 1000) + (2 * 60)) // TODO : This should be idle
) {
    companion object {
        const val MIN_DURATION_IN_MINUTES = 20
    }

    init {
        val targetSeconds = timeRepo.getTargetSeconds()
        if (targetSeconds != null) {
            emitViewState(DashboardViewState.Active(targetSeconds))
        }
    }

    override fun onInteraction(interactor: DashboardInteractor) {
        when (interactor) {
            DashboardInteractor.AddToBlockListClicked -> {
                onAddToBlockListClicked()
            }
            DashboardInteractor.EditBlockListClicked -> {
                onEditBlockListClicked()
            }
            DashboardInteractor.ExtendBlockTimerClicked -> {
                TODO()
            }
            is DashboardInteractor.StartClicked -> {
                emitViewAction(DashboardViewAction.ShowDurationPicker)
            }
            DashboardInteractor.TimerFinished -> {
                onTimerFinished()
            }
            is DashboardInteractor.TimePicked -> {
                onTimePicked(interactor)
            }
        }.exhaustive()
    }

    private fun onTimerFinished() {
        viewModelScope.launch {
            // modify the host file
            val currentHostFileContent = hostRepo.getHostFileContent()
            val newHostFileContent = HostManager(currentHostFileContent).clearRules()
            hostRepo.updateHostFileContent(newHostFileContent)
            timeRepo.clearTargetSeconds()

            // change the state to idle
            emitViewState(DashboardViewState.Idle)
        }
    }

    private fun onAddToBlockListClicked() {
        emitViewAction(DashboardViewAction.GoToBlockList(shouldEnableRemove = false))
    }

    private fun onEditBlockListClicked() {
        emitViewAction(DashboardViewAction.GoToBlockList(shouldEnableRemove = true))
    }

    @OptIn(ExperimentalTime::class)
    private fun onTimePicked(interactor: DashboardInteractor.TimePicked) {
        viewModelScope.launch {
            // validation
            if (false && interactor.hour == 0 && interactor.minute <= MIN_DURATION_IN_MINUTES) {
                emitViewAction(DashboardViewAction.ErrorMinTime)
                return@launch
            }

            emitViewState(DashboardViewState.Loading(R.string.dashboard_calculating_time))

            val hoursInSeconds = Duration.hours(interactor.hour).inWholeSeconds
            val minutesInSeconds = Duration.minutes(interactor.minute).inWholeSeconds

            try {
                val targetSeconds =
                    timeRepo.getCurrentTimeInSeconds() + hoursInSeconds + minutesInSeconds
                timeRepo.saveTargetSeconds(targetSeconds)

                emitViewState(DashboardViewState.Loading(R.string.dashboard_writing_rules))
                // get block list - list of domains
                val blockList = blockListRepo.getBlockList()

                // get host file content - list of domains and ips
                val hostFileContent = hostRepo.getHostFileContent()

                // install the blocklist inside the host file content
                val newHostFileContent = HostManager(hostFileContent).applyBlockList(blockList)

                // update the host file with new content
                val isUpdated = hostRepo.updateHostFileContent(newHostFileContent)

                if (isUpdated) {
                    emitViewState(DashboardViewState.Active(targetSeconds))
                } else {
                    emitViewState(DashboardViewState.Error("Something went wrong"))
                }
            } catch (e: IOException) {
                e.printStackTrace()
                emitViewState(DashboardViewState.Error(e.message ?: "Something went wrong"))
            }
        }
    }
}
