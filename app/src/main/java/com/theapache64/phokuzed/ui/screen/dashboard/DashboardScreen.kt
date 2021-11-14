package com.theapache64.phokuzed.ui.screen.dashboard

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
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
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.composable.CenterBox
import com.theapache64.phokuzed.util.exhaustive

val bottomPadding = 30.dp

@Suppress("UnnecessaryVariable")
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onEditBlockListClicked: (shouldEnableRemove: Boolean) -> Unit
) {
    val dynViewState by viewModel.viewState.collectAsState()
    val viewState = dynViewState
    WatchViewAction(
        viewModel = viewModel,
        onEditBlockListClicked = onEditBlockListClicked
    )

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
    viewModel: DashboardViewModel,
    onEditBlockListClicked: (shouldEnableRemove: Boolean) -> Unit
) {
    val context = LocalContext.current
    val dynViewAction by viewModel.viewAction.collectAsState(null)
    LaunchedEffect(dynViewAction) {
        val viewAction = dynViewAction?.action
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
            is DashboardViewAction.GoToBlockList -> {
                val shouldEnableRemove = viewAction.shouldEnableRemove
                onEditBlockListClicked.invoke(shouldEnableRemove)
            }
            DashboardViewAction.MinTime -> {
                showMinTimeError(context)
            }
            null -> {
                // do nothing
            }
        }.exhaustive()
    }
}

private fun showMinTimeError(context: Context) {
    // TODO: Convert this to a SnackBar with RETRY launching time picker
    val msg = context.getString(
        R.string.dashboard_min_time_error,
        DashboardViewModel.MIN_DURATION_IN_MINUTES
    )
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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