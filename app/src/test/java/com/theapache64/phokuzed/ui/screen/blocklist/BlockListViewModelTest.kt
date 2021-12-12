package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.github.theapache64.expekt.should
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class BlockListViewModelTest {

    @Test
    fun `Delete removes data and updates UI`() = runBlockingTest {
        val fakeDomains = setOf(
            "facebook.com",
            "instagram.com"
        )
        val viewModel = BlockListViewModel(
            blockListRepo = mock {
                onBlocking { getBlockList() } doReturn fakeDomains
            },
            savedStateHandle = SavedStateHandle(
                mapOf(
                    BlockListViewModel.KEY_ARG_MODE to true
                )
            ),
            hostRepo = mock() // FIXME:
        )

        viewModel.viewState.test {
            awaitItem().let { firstState ->
                firstState.should.be.instanceof(BlockListViewState.Active::class.java)
                firstState as BlockListViewState.Active
                firstState.blockList.should.equal(fakeDomains)

                // now remove facebook.com
                viewModel.onInteraction(BlockListInteractor.RemoveDomainClick("facebook.com"))

                awaitItem().let { secondState ->
                    secondState.should.be.instanceof(BlockListViewState.Active::class.java)
                    secondState as BlockListViewState.Active
                    secondState.blockList.should.equal(setOf("instagram.com"))
                }
            }
        }
    }
}
