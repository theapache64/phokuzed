package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.data.repo.BlockListRepo
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockListViewModel @Inject constructor(
    private val blockListRepo: BlockListRepo
) : BaseViewModel<BlockListViewState, BlockListInteractor, BlockListViewAction>(
    defaultViewState = BlockListViewState.Loading
) {
    init {
        viewModelScope.launch {
            val blockList = blockListRepo.getBlockList()
            if (blockList.isEmpty()) {
                emitViewState(BlockListViewState.BlockListEmpty)
            } else {
                emitViewState(BlockListViewState.Active(blockList))
            }
        }
    }

    override fun onInteraction(interactor: BlockListInteractor) {
        when (interactor) {
            is BlockListInteractor.RemoveDomainClick -> {
                onRemoveDomainClicked(interactor.domain)
            }
        }.exhaustive()
    }

    private fun onRemoveDomainClicked(domain: String) {
        viewModelScope.launch {
            blockListRepo.getBlockList().toMutableSet().apply {
                remove(domain)
            }.let { newBlockList ->
                blockListRepo.saveBlockList(newBlockList)

                // emit the updated state
                emitViewState(BlockListViewState.Active(newBlockList))
            }
        }
    }
}