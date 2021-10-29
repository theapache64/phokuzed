package com.theapache64.phokuzed.ui.composable.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun CountDownTimer(
    targetSeconds: Long,
    modifier: Modifier = Modifier,
    viewModel: RemoteTimerViewModel = hiltViewModel(),
    textColor: Color = MaterialTheme.colors.onSurface,
    fontSize: TextUnit = 30.sp,
    onFinished: () -> Unit
) {

    var displayTime by remember { mutableStateOf("") }
    val viewState by viewModel.viewState.collectAsState()

    displayTime = when (viewState) {
        is RemoteTimerViewState.Error -> {
            (viewState as RemoteTimerViewState.Error).reason
        }
        RemoteTimerViewState.Loading -> {
            "LO:AD:ING"
        }
        is RemoteTimerViewState.NewTime -> {
            (viewState as RemoteTimerViewState.NewTime).remainingTime
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onInteraction(RemoteTimerInteractor.StartTimer(targetSeconds, onFinished))
    }

    Text(
        text = displayTime,
        modifier = modifier
            .background(
                MaterialTheme.colors.secondary,
                CircleShape
            )
            .padding(10.dp),
        color = textColor,
        fontSize = fontSize
    )
}