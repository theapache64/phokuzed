package com.theapache64.phokuzed.ui.composable.timer

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.data.remote.worldtime.WorldTimeApi
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RemoteTimerViewModel @Inject constructor(
    private val worldTimeApi: WorldTimeApi
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
            worldTimeApi.getTime().collect {
                when (it) {
                    is Resource.Loading -> {
                        emitViewState(RemoteTimerViewState.Loading)
                    }
                    is Resource.Success -> {
                        startPeriodicUpdate(
                            _remoteSeconds = it.data.unixtime,
                            targetSeconds = interactorRemote.targetSeconds,
                            onFinished = interactorRemote.onFinished
                        )
                    }
                    is Resource.Error -> {
                        emitViewState(RemoteTimerViewState.Error(it.errorData))
                    }
                    is Resource.Idle -> {
                        // do nothing
                    }
                }
            }
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
                if (diff > 0) {
                    val hours = diff / (60 * 60 * 1000) % 24
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