package com.theapache64.phokuzed.ui.screen.blocklist

// ViewState (ViewModel to UI - to render UI)
sealed class BlockListViewState {
    object Loading : BlockListViewState()
    data class Active(
        val blockList: Set<String>,
        val shouldEnableRemove : Boolean
    ) : BlockListViewState()

    object BlockListEmpty : BlockListViewState()
}

// Interactors (UI to ViewModel)
sealed class BlockListInteractor {
    object AddClick : BlockListInteractor()
    data class RemoveDomainClick(val domain: String) : BlockListInteractor()
    data class AddDomainClick(val domain: String) : BlockListInteractor()
}

// ViewAction (ViewModel to UI - to notify one-time events)
sealed class BlockListViewAction {
    object ShowAddDialog : BlockListViewAction()
    object DismissAddDialog : BlockListViewAction()
}
