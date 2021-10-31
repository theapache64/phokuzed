package com.theapache64.phokuzed.ui.screen.splash

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.util.exhaustive

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit
) {

    val context = LocalContext.current
    val viewState by viewModel.viewState.collectAsState()
    val viewAction by viewModel.viewAction.collectAsState(null)


    LaunchedEffect(Unit) {
        viewModel.init()

        if (context is ComponentActivity) {
            context.lifecycle.addObserver(viewModel)
        }
    }

    // TODO: Handle update : show update dialog
    when (viewAction) {
        is SplashViewAction.GoToMain -> {
            onSplashFinished()
        }
        SplashViewAction.ShowUpdateDialog -> {
            UpdateDialog(
                onUpdateClicked = {
                    viewModel.onInteraction(SplashInteractor.UpdateClick)
                }
            )
        }
        is SplashViewAction.OpenUrl -> {
            // TODO: IDK if launching activity from here is a good thing
            // TODO: Refactor
            if (context is Activity) {
                val urlToOpen = (viewAction as SplashViewAction.OpenUrl).url
                context.startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen))
                )
            }
            Unit
        }
        null -> {
            // do nothing
        }
    }.exhaustive()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

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
        }.exhaustive()
    }
}

// BRB
@Composable
fun UpdateDialog(
    onUpdateClicked: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {
            // do nothing. this won't get called ever.
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        title = {
            Text(text = stringResource(id = R.string.splash_update_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.splash_update_dialog_content))
        },
        confirmButton = {
            TextButton(onClick = {
                onUpdateClicked()
            }) {
                Text(
                    text = stringResource(id = R.string.splash_action_update),
                    color = MaterialTheme.colors.onPrimary
                )
            }
        }
    )
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