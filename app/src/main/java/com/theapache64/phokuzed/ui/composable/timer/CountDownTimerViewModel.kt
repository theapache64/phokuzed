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

    init {
        viewModelScope.launch {
            worldTimeApi.getTime().collect {
                when (it) {
                    is Resource.Loading -> {
                        emitViewState(TimerViewState.Loading)
                    }
                    is Resource.Success -> {
                        emitViewState(TimerViewState.Success(it.data.unixtime))
                        alignTime()
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

    private fun alignTime() {
        viewModelScope.launch {
            while (true) {
                val currentState = viewState.value
                if (currentState is TimerViewState.Success) {
                    emitViewState(TimerViewState.Success(currentState.currentSeconds + 1))
                }
                delay(1000)
            }
        }
    }

    override fun onInteraction(interactor: TimerInteractor) {
        error("No interaction handled")
    }
}