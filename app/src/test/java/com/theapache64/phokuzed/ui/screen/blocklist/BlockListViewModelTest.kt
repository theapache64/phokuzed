package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.github.theapache64.expekt.should
import com.theapache64.phokuzed.test.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@OptIn(ExperimentalCoroutinesApi::class)
class BlockListViewModelTest {

    @get:Rule
    val rule = MainCoroutineRule()

    @Test
    fun `Delete removes data and updates UI`() = runTest {
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
                    BlockListViewModel.KEY_ARG_MODE to Mode.ADD_AND_REMOVE
                )
            ),
            hostRepo = mock() // FIXME:
        )

        viewModel.viewState.test {
            awaitItem().let { firstState ->
                firstState.should.be.instanceof(BlockListViewState.Loading::class.java)
            }

            awaitItem().let { secondState ->
                secondState.should.be.instanceof(BlockListViewState.Active::class.java)
                secondState as BlockListViewState.Active
                secondState.blockList.should.equal(fakeDomains)
            }

            // now remove facebook.com
            viewModel.onInteraction(BlockListInteractor.RemoveDomainClick("facebook.com"))

            awaitItem().let { thirdState ->
                thirdState.should.be.instanceof(BlockListViewState.Loading::class.java)
            }

            awaitItem().let { fourthState ->
                fourthState.should.be.instanceof(BlockListViewState.Active::class.java)
                fourthState as BlockListViewState.Active
                fourthState.blockList.should.equal(setOf("instagram.com"))
            }
        }
    }
}
