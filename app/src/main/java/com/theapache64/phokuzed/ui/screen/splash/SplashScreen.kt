package com.theapache64.phokuzed.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit
) {

    val viewState by viewModel.viewState.collectAsState()
    val viewAction by viewModel.viewAction.collectAsState(null)

    if (viewAction is SplashViewAction.GoToMain) {
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // Logo
        Image(
            painter = painterResource(id = R.drawable.ic_compose_logo),
            contentDescription = stringResource(id = R.string.cd_app_logo),
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.Center),
        )

        when (viewState) {
            is SplashViewState.Loaded -> {
                // Version number
                Text(
                    text = (viewState as SplashViewState.Loaded).versionName,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 20.dp)
                )
            }
        }
    }
}