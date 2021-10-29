package com.theapache64.phokuzed.ui.composable.timer

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.data.remote.worldtime.WorldTimeApi
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountDownTimerViewModel @Inject constructor(
    private val worldTimeApi: WorldTimeApi
) : BaseViewModel<TimerViewState, TimerInteractor, TimerViewAction>(
    defaultViewState = TimerViewState.Loading
) {

    override fun onInteraction(interactor: TimerInteractor) {
        when (interactor) {
            is TimerInteractor.Init -> {
                ignite(interactor)
            }
        }
    }


    private fun ignite(
        interactor: TimerInteractor.Init
    ) {
        viewModelScope.launch {
            worldTimeApi.getTime().collect {
                when (it) {
                    is Resource.Loading -> {
                        emitViewState(TimerViewState.Loading)
                    }
                    is Resource.Success -> {
                        alignTime(
                            _remoteSeconds = it.data.unixtime,
                            targetSeconds = interactor.targetSeconds,
                            onFinished = interactor.onFinished
                        )
                    }
                    is Resource.Error -> {
                        emitViewState(TimerViewState.Error(it.errorData))
                    }
                    is Resource.Idle -> {
                        // do nothing
                    }
                }
            }
        }
    }

    private fun alignTime(
        _remoteSeconds: Long,
        targetSeconds: Long,
        onFinished: () -> Unit
    ) {
        var remoteSeconds = _remoteSeconds
        viewModelScope.launch {
            while (true) {
                val diff = targetSeconds - remoteSeconds
                if (diff > 0) {
                    val hours = diff / (60 * 60 * 1000) % 24
                    val minutes = diff / 60 % 60
                    val seconds = diff % 60

                    val hoursString = String.format("%02d", hours)
                    val minutesString = String.format("%02d", minutes)
                    val secondsString = String.format("%02d", seconds)
                    emitViewState(TimerViewState.Success("$hoursString:$minutesString:$secondsString"))
                } else {
                    onFinished.invoke()
                }

                delay(1000)
                remoteSeconds++
            }
        }
    }


}