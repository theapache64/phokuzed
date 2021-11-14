package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.composable.timer.CountDownTimer

@Composable
fun ActiveDashboardUi(
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
                Text(text = stringResource(id = R.string.action_add_to_blocklist))
            }

            Spacer(modifier = Modifier.height(10.dp))

            /*
            // TODO: v2
            Button(
                onClick = {
                    onExtendBlockTimerClicked()
                },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.action_extend_timer))
            }*/
        }
    }
}
