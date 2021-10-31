
package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.composable.CenterBox
import com.theapache64.phokuzed.ui.composable.timer.CountDownTimer
import com.theapache64.phokuzed.util.exhaustive

private val bottomPadding = 30.dp

@Suppress("UnnecessaryVariable")
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val dynViewState by viewModel.viewState.collectAsState()
    val viewState = dynViewState

    val dynViewAction by viewModel.viewAction.collectAsState(null)
    val viewAction = dynViewAction

    when (viewAction) {
        is DashboardViewAction.ShowDurationPicker -> {
            launchTimePicker(
                activity = context as FragmentActivity,
                onTimePicked = { timePicker ->
                    viewModel.onInteraction(
                        DashboardInteractor.TimePicked(
                            timePicker.hour,
                            timePicker.minute
                        )
                    )
                }
            )
        }
        null -> {
            // do nothing
        }
    }.exhaustive()


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
            is DashboardViewState.Loading -> {
                CalculatingUi(viewState.message)
            }
            is DashboardViewState.Active -> {
                ActiveUi(
                    targetSeconds = viewState.targetSeconds,
                    onAddToBlocklistClicked = {
                        viewModel.onInteraction(DashboardInteractor.AddToBlockListClicked)
                    },
                    onExtendBlockTimerClicked = {
                        viewModel.onInteraction(DashboardInteractor.ExtendBlockTimerClicked)
                    },
                    onTimerFinished = {
                        viewModel.onInteraction(DashboardInteractor.TimerFinished)
                    }
                )
            }
        }.exhaustive()
    }
}

fun launchTimePicker(
    activity: FragmentActivity,
    onTimePicked: (MaterialTimePicker) -> Unit
) {
    val timePicker = MaterialTimePicker.Builder()
        .setTimeFormat(CLOCK_24H)
        .build()

    timePicker.addOnPositiveButtonClickListener {
        onTimePicked(timePicker)
    }

    timePicker.show(activity.supportFragmentManager, null)
}

@Composable
fun CalculatingUi(
    @StringRes message: Int
) {
    CenterBox {
        Text(text = stringResource(id = message))
    }
}

@Composable
fun ActiveUi(
    targetSeconds: Long,
    onAddToBlocklistClicked: () -> Unit,
    onExtendBlockTimerClicked: () -> Unit,
    onTimerFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {

        CountDownTimer(
            targetSeconds = targetSeconds,
            modifier = Modifier.align(Alignment.Center)
        ) {
            onTimerFinished()
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IdleUi(
    onStartClicked: () -> Unit,
    onEditBlockListClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {


        Column(
            modifier = Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            /*Action : Start*/
            Button(
                onClick = {
                    onStartClicked()
                },
            ) {
                Text(text = stringResource(id = R.string.action_start))
            }
        }


        /*Action : Edit Blocklist*/
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