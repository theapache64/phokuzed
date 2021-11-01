package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.composable.CenterBox
import com.theapache64.phokuzed.ui.screen.dashboard.ActiveDashboardUi
import com.theapache64.phokuzed.util.exhaustive

@Suppress("UnnecessaryVariable")
@Composable
fun BlockListScreen(
    onBackPressed: () -> Unit,
    viewModel: BlockListViewModel = hiltViewModel()
) {
    val dynViewState by viewModel.viewState.collectAsState()
    val viewState = dynViewState

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.block_list_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed()
                    }) {
                        Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "back button")
                    }
                }
            )
        }
    ) {
        when (viewState) {
            BlockListViewState.Loading -> {
                LoadingUi()
            }
            is BlockListViewState.Active -> {
                ActiveBlockListUi(
                    activeState = viewState,
                    onRemoveDomainClicked = { domain ->
                        viewModel.onInteraction(
                            BlockListInteractor.RemoveDomainClick(domain)
                        )
                    }
                )
            }
            BlockListViewState.BlockListEmpty -> TODO()
        }.exhaustive()
    }
}



@Composable
fun LoadingUi() {
    CenterBox {
        Column {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.block_list_loading))
        }
    }
}
