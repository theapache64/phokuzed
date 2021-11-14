package com.theapache64.phokuzed.ui.composable.timer

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.data.repo.TimeRepo
import com.theapache64.phokuzed.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RemoteTimerViewModel @Inject constructor(
    private val timeRepo: TimeRepo
) : BaseViewModel<RemoteTimerViewState, RemoteTimerInteractor, RemoteTimerViewAction>(
    defaultViewState = RemoteTimerViewState.Loading
) {
    init {
        Timber.d("New viewModel: created ")
    }

    override fun onInteraction(interactor: RemoteTimerInteractor) {
        when (interactor) {
            is RemoteTimerInteractor.StartTimer -> {
                init(interactor)
            }
        }
    }

    private var isRunning = false
    private fun init(
        interactorRemote: RemoteTimerInteractor.StartTimer
    ) {
        if (isRunning) {
            return
        }

        viewModelScope.launch {
            emitViewState(RemoteTimerViewState.Loading)
            val currentTimeInSeconds = timeRepo.getCurrentTimeInSeconds()

            startPeriodicUpdate(
                _remoteSeconds = currentTimeInSeconds,
                targetSeconds = interactorRemote.targetSeconds,
                onFinished = interactorRemote.onFinished
            )
        }
    }

    private fun startPeriodicUpdate(
        _remoteSeconds: Long,
        targetSeconds: Long,
        onFinished: () -> Unit
    ) {
        var remoteSeconds = _remoteSeconds
        viewModelScope.launch {
            isRunning = true
            while (true) {
                remoteSeconds++
                val diff = targetSeconds - remoteSeconds
                if (diff >= 0) {
                    val hours = diff / (60 * 60) % 24
                    val minutes = diff / 60 % 60
                    val seconds = diff % 60

                    val hoursString = String.format("%02d", hours)
                    val minutesString = String.format("%02d", minutes)
                    val secondsString = String.format("%02d", seconds)
                    emitViewState(RemoteTimerViewState.NewTime("$hoursString:$minutesString:$secondsString"))
                } else {
                    onFinished.invoke()
                    break
                }

                delay(1000)
            }
        }
    }
}
