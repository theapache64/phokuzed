package com.theapache64.phokuzed.ui.composable.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.ui.theme.PhokuzedTheme
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
@Preview
@Composable
fun CountDownTimerPreview() {
    PhokuzedTheme {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CountDownTimer(
                targetSeconds = System.currentTimeMillis() + Duration.seconds(10).inWholeMilliseconds,
                modifier = Modifier.align(Alignment.Center),
                onFinished = {
                    println("Finished")
                }
            )
        }
    }
}


//1. time from remote
//2. increment it every 1 second
@Composable
fun CountDownTimer(
    targetSeconds: Long,
    modifier: Modifier = Modifier,
    viewModel: CountDownTimerViewModel = hiltViewModel(),
    textColor: Color = MaterialTheme.colors.onSurface,
    fontSize: TextUnit = 30.sp,
    onFinished: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()
    var displayTime by remember { mutableStateOf("") }

    when(viewState){
        TimerViewState.Loading -> {
            displayTime = "LO:AD:ING"
        }
        is TimerViewState.Error -> {
            displayTime = (viewState as TimerViewState.Error).reason
        }
        is TimerViewState.Success -> {
            val diff = targetSeconds - (viewState as TimerViewState.Success).currentSeconds
            if (diff > 0) {
                val hours = diff / (60 * 60 * 1000) % 24
                val minutes = diff / 60 % 60
                val seconds = diff % 60

                val hoursString = String.format("%02d", hours)
                val minutesString = String.format("%02d", minutes)
                val secondsString = String.format("%02d", seconds)
                displayTime = "$hoursString:$minutesString:$secondsString"
            } else {
                onFinished()
            }
        }
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