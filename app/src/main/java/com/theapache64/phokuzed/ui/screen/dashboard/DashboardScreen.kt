
package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat.CLOCK_24H
import com.theapache64.phokuzed.ui.composable.CenterBox
import com.theapache64.phokuzed.util.exhaustive

val bottomPadding = 30.dp

@Suppress("UnnecessaryVariable")
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val dynViewState by viewModel.viewState.collectAsState()
    val viewState = dynViewState
    WatchViewAction(viewModel)

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
                ActiveDashboardUi(
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
            is DashboardViewState.Error -> {
                // TODO: Improve the UI
                CenterBox {
                    Text(text = viewState.reason)
                }
            }
        }.exhaustive()
    }
}

@Suppress("UnnecessaryVariable")
@Composable
private fun WatchViewAction(
    viewModel: DashboardViewModel
) {
    val context = LocalContext.current
    val dynViewAction by viewModel.viewAction.collectAsState(null)
    val viewAction = dynViewAction

    when (viewAction?.action) {
        is DashboardViewAction.ShowDurationPicker -> {
            LaunchedEffect(key1 = viewAction) {
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
        }
        null -> {
            // do nothing
        }
    }.exhaustive()
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