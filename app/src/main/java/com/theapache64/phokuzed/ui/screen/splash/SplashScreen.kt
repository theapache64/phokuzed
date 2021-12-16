package com.theapache64.phokuzed.ui.screen.splash

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.base.ViewAction
import com.theapache64.phokuzed.util.exhaustive

@Suppress("UnnecessaryVariable")
@Composable
fun SplashScreen(
    viewModel: SplashViewModel = hiltViewModel(),
    onSplashFinished: () -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    LaunchedEffect(Unit) {
        viewModel.init()
        activity.lifecycle.addObserver(viewModel)
    }

    viewModel.viewAction.collectAsState(null).value?.let { viewAction ->
        HandleViewAction(
            viewAction = viewAction,
            onSplashFinished = onSplashFinished,
            onUpdateClicked = {
                viewModel.onInteraction(SplashInteractor.UpdateClick)
            }
        )
    }

    viewModel.viewState.collectAsState().value.let { viewState ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            if (viewState !is SplashViewState.NoRootAccess) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(id = R.string.cd_app_logo),
                    modifier = Modifier
                        .size(100.dp)
                        .align(Alignment.Center),
                )
            }

            when (viewState) {
                is SplashViewState.Loading -> {
                    Loading(viewState.message)
                }

                is SplashViewState.Error -> {
                    Text(text = (viewState as SplashViewState.Error).message)
                }

                SplashViewState.Success -> {
                    // do nothing
                }
                SplashViewState.NoRootAccess -> {
                    NoRootAccessUi(
                        onRetryClicked = {
                            viewModel.onInteraction(SplashInteractor.RetryRootCheckClick)
                        }
                    )
                }
            }.exhaustive()
        }
    }
}

@Composable
private fun HandleViewAction(
    viewAction: ViewAction<SplashViewAction>,
    onSplashFinished: () -> Unit,
    onUpdateClicked: () -> Unit,
) {
    val context = LocalContext.current as Activity
    when (viewAction.action) {
        is SplashViewAction.GoToMain -> {
            onSplashFinished()
        }
        SplashViewAction.ShowUpdateDialog -> {
            UpdateDialog(onUpdateClicked)
        }
        is SplashViewAction.OpenUrl -> {
            val urlToOpen = viewAction.action.url
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlToOpen)))
        }
    }.exhaustive()
}

@Composable
private fun BoxScope.NoRootAccessUi(
    onRetryClicked: () -> Unit
) {
    Column(
        modifier = Modifier.align(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Text
        Text(text = stringResource(id = R.string.splash_no_root))

        Spacer(modifier = Modifier.height(10.dp))

        // Retry button
        Button(onClick = onRetryClicked) {
            Text(text = stringResource(id = R.string.action_retry))
        }
    }
}

@Composable
private fun UpdateDialog(
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

@Composable
private fun BoxScope.Loading(
    @StringRes message: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .align(Alignment.BottomCenter)
            .padding(bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = message),
            fontSize = 14.sp,
            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f)
        )
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = SplashViewModel.versionName)
    }
}
