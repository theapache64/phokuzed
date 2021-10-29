package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.composable.timer.CountDownTimer

private val bottomPadding = 30.dp

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsState()
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        when (viewState) {
            DashboardViewState.Idle -> {
                IdleUi(
                    onStartClicked = {
                        viewModel.onInteraction(DashboardInteractor.StartClicked)
                    },
                    onEditBlockListClicked = {
                        viewModel.onInteraction(DashboardInteractor.EditBlockListClicked)
                    }
                )
            }
            DashboardViewState.Active -> {
                ActiveUi(
                    onAddToBlocklistClicked = {
                        viewModel.onInteraction(DashboardInteractor.AddToBlockListClicked)
                    },
                    onExtendBlockTimerClicked = {
                        viewModel.onInteraction(DashboardInteractor.ExtendBlockTimerClicked)
                    }
                )
            }
        }
    }
}

@Composable
fun ActiveUi(
    onAddToBlocklistClicked: () -> Unit,
    onExtendBlockTimerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        CountDownTimer(targetSeconds = (System.currentTimeMillis() / 1000) + (10 * 60)) {

        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomPadding)
        ) {
            Button(
                onClick = {
                    onAddToBlocklistClicked()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.action_start))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Button(
                onClick = {
                    onExtendBlockTimerClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.action_edit_block))
            }
        }

    }
}

@Composable
fun IdleUi(
    onStartClicked: () -> Unit,
    onEditBlockListClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        /*Start*/
        Button(
            onClick = {
                onStartClicked()
            },
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Text(text = stringResource(id = R.string.action_start))
        }


        /*Edit Blocklist*/
        Button(
            onClick = {
                onEditBlockListClicked()
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = bottomPadding)
        ) {
            Text(text = stringResource(id = R.string.action_edit_block))
        }

    }
}
