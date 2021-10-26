package com.theapache64.phokuzed.ui.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.util.LogCompositions
import com.theapache64.phokuzed.util.exhaustive

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit
) {
    val viewState by viewModel.viewState.collectAsState()
    val viewAction by viewModel.viewAction.collectAsState(null)

    // TODO: Handle update : show update dialog
    if (viewAction is SplashViewAction.GoToMain) {
        onSplashFinished()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        // TODO: Get a logo man
        LogCompositions(tag = "SplashScreen", msg = "rendering...")

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.cd_app_logo),
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.Center),
        )

        when (viewState) {
            SplashViewState.ConfigLoading -> {
                Loading()
            }

            is SplashViewState.ConfigError -> {
                Text(text = (viewState as SplashViewState.ConfigError).message)
            }

            SplashViewState.ConfigLoaded -> {
                // do nothing
            }
        }.exhaustive
    }
}

@Preview
@Composable
private fun BoxScope.Loading(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 30.dp)
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = SplashViewModel.versionName)
    }
}