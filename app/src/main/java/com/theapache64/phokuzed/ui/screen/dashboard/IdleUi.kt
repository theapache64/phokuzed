package com.theapache64.phokuzed.ui.screen.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.theapache64.phokuzed.R

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
