package com.theapache64.phokuzed.ui.base

import androidx.lifecycle.ViewModel
import com.theapache64.phokuzed.util.flow.mutableEventFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ViewAction<VA>(
    val action: VA
)

abstract class BaseViewModel<VS, I, VA>(
    defaultViewState: VS
) : ViewModel() {

    private val _viewState = MutableStateFlow(defaultViewState)
    val viewState = _viewState.asStateFlow()

    private val _viewAction = mutableEventFlow<ViewAction<VA>>()
    val viewAction = _viewAction.asSharedFlow()

    abstract fun onInteraction(interactor: I)

    fun emitViewState(viewState: VS) {
        _viewState.value = viewState
    }

    fun emitViewAction(action: VA) {
        val newAction = ViewAction(action)
        _viewAction.tryEmit(newAction)
    }


    inline fun <reified T : VS> getCurrentState() : T? {
        return viewState.value as? T
    }
}