package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.data.repo.BlockListRepo
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val blockListRepo: BlockListRepo
) : BaseViewModel<BlockListViewState, BlockListInteractor, BlockListViewAction>(
    defaultViewState = BlockListViewState.Loading
) {
    companion object {
        const val ARG_SHOULD_ENABLE_REMOVE = "should_enable_remove"
    }

    init {
        val shouldEnableRemove = savedStateHandle.get<Boolean>(ARG_SHOULD_ENABLE_REMOVE)!!

        viewModelScope.launch {
            val blockList = blockListRepo.getBlockList()
            onBlockListUpdated(blockList, shouldEnableRemove)
        }
    }

    private fun onBlockListUpdated(blockList: Set<String>, shouldEnableRemove: Boolean) {
        if (blockList.isEmpty()) {
            emitViewState(BlockListViewState.BlockListEmpty)
        } else {
            emitViewState(
                BlockListViewState.Active(
                    blockList,
                    shouldEnableRemove = shouldEnableRemove
                )
            )
        }
    }

    override fun onInteraction(interactor: BlockListInteractor) {
        when (interactor) {
            is BlockListInteractor.RemoveDomainClick -> {
                onRemoveDomainClicked(interactor.domain)
            }
            BlockListInteractor.AddClick -> {
                onAddClicked()
            }
            is BlockListInteractor.AddDomainClick -> {
                onAddDomainClick(interactor.domain)
            }
        }.exhaustive()
    }

    private fun onAddDomainClick(domain: String) {
        // TODO: Input validation

        emitViewAction(BlockListViewAction.DismissAddDialog)
    }

    private fun onAddClicked() {
        emitViewAction(BlockListViewAction.ShowAddDialog)
    }

    private fun onRemoveDomainClicked(domain: String) {
        viewModelScope.launch {
            blockListRepo.getBlockList().toMutableSet().apply {
                remove(domain)
            }.let { newBlockList ->
                blockListRepo.saveBlockList(newBlockList)
                val currentState = viewState.value as BlockListViewState.Active
                onBlockListUpdated(newBlockList, currentState.shouldEnableRemove)
            }
        }
    }
}