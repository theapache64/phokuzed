package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.theapache64.phokuzed.core.HostManager
import com.theapache64.phokuzed.data.repo.BlockListRepo
import com.theapache64.phokuzed.data.repo.HostRepo
import com.theapache64.phokuzed.ui.base.BaseViewModel
import com.theapache64.phokuzed.util.exhaustive
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlockListViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val blockListRepo: BlockListRepo,
    private val hostRepo: HostRepo
) : BaseViewModel<BlockListViewState, BlockListInteractor, BlockListViewAction>(
    defaultViewState = BlockListViewState.Loading
) {
    companion object {
        const val KEY_ARG_MODE = "mode"
        val domainRegex by lazy {
            """^[a-zA-Z0-9][a-zA-Z0-9-]{1,61}[a-zA-Z0-9](?:\.[a-zA-Z]{2,})+${'$'}""".toRegex()
        }
    }

    private val mode = (savedStateHandle.get<Mode>(KEY_ARG_MODE) ?: error("No mode passed"))

    init {

        viewModelScope.launch {
            val blockList = blockListRepo.getBlockList()
            if (blockList.isEmpty()) {
                emitViewState(BlockListViewState.BlockListEmpty(mode))
            } else {
                emitViewState(BlockListViewState.Active(blockList, mode))
            }
        }
    }

    private fun onBlockListUpdated(blockList: Set<String>, mode: Mode) {
        viewModelScope.launch {
            emitViewState(BlockListViewState.Loading)

            val isSuccess = when (mode) {
                Mode.ADD -> {
                    // First add update the prefs
                    blockListRepo.saveBlockList(blockList)

                    // Then update the hosts file
                    val currentHostFileContent = hostRepo.getHostFileContent()
                    val newHostFileContent =
                        HostManager(currentHostFileContent).applyBlockList(blockList)
                    hostRepo.updateHostFileContent(newHostFileContent)
                }
                Mode.ADD_AND_REMOVE -> {
                    blockListRepo.saveBlockList(blockList)
                    true
                }
            }

            if (isSuccess) {
                if (blockList.isEmpty()) {
                    emitViewState(BlockListViewState.BlockListEmpty(mode))
                } else {
                    emitViewState(BlockListViewState.Active(blockList, mode))
                }
            }
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

    private fun onAddDomainClick(_domain: String) {
        val domain = _domain.trim()

        if (isValid(domain)) {
            viewModelScope.launch {
                val newBlockList = blockListRepo.getBlockList().toMutableSet().apply {
                    add(domain)
                }
                blockListRepo.saveBlockList(newBlockList)
                val mode =
                    getCurrentState<BlockListViewState.Active>()?.mode
                        ?: getCurrentState<BlockListViewState.BlockListEmpty>()?.mode
                        ?: error("TSH: The state should be either Active or BlockListEmpty")

                onBlockListUpdated(newBlockList, mode)

                emitViewAction(BlockListViewAction.DismissAddDialog)
            }
        } else {
            emitViewAction(BlockListViewAction.InvalidDomain)
        }
    }

    private fun isValid(domain: String) = domainRegex.matches(domain)

    private fun onAddClicked() {
        emitViewAction(BlockListViewAction.ShowAddDialog)
    }

    private fun onRemoveDomainClicked(domain: String) {
        viewModelScope.launch {
            blockListRepo.getBlockList().toMutableSet().apply {
                remove(domain)
            }.let { newBlockList ->
                blockListRepo.saveBlockList(newBlockList)
                val currentState: BlockListViewState.Active =
                    getCurrentState() ?: error("Current state can't be null")
                onBlockListUpdated(newBlockList, currentState.mode)
            }
        }
    }
}
