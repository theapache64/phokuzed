package com.theapache64.phokuzed.ui.base

import androidx.lifecycle.ViewModel
import com.theapache64.phokuzed.util.flow.mutableEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<VS, I, VA>(
    defaultViewState: VS
) : ViewModel() {

    private val _viewState = MutableStateFlow<VS>(defaultViewState)
    val viewState = _viewState.asStateFlow()

    private val _viewAction = mutableEventFlow<VA>()
    val viewAction = _viewAction.asSharedFlow()

    abstract fun onInteraction(interactor: I)

    fun emitViewState(viewState: VS) {
        _viewState.value = viewState
    }

    fun emitViewAction(viewAction: VA) {
        _viewAction.tryEmit(viewAction)
    }
}