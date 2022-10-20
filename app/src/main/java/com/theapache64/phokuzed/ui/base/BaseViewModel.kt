package com.theapache64.phokuzed.ui.base

import androidx.lifecycle.ViewModel
import com.theapache64.phokuzed.util.flow.mutableEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class ViewAction<VA>(
    val action: VA,
)

abstract class BaseViewModel<VS, I, VA>(
    defaultViewState: VS,
) : ViewModel() {

    private val _viewState = MutableStateFlow(defaultViewState)
    val viewState: StateFlow<VS> = _viewState

    private val _viewAction = mutableEventFlow<ViewAction<VA>>()
    val viewAction: SharedFlow<ViewAction<VA>> = _viewAction

    abstract fun onInteraction(interactor: I)

    fun emitViewState(viewState: VS) {
        _viewState.value = viewState
    }

    fun emitViewAction(action: VA) {
        val newAction = ViewAction(action)
        _viewAction.tryEmit(newAction)
    }

    inline fun <reified T : VS> getCurrentState(): T? {
        return viewState.value as? T
    }
}
