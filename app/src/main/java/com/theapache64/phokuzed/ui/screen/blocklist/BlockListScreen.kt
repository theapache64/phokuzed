package com.theapache64.phokuzed.ui.screen.blocklist

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.theapache64.phokuzed.R
import com.theapache64.phokuzed.ui.composable.CenterBox
import com.theapache64.phokuzed.ui.composable.CenterColumn
import com.theapache64.phokuzed.ui.theme.Colors
import com.theapache64.phokuzed.util.exhaustive
import kotlinx.coroutines.delay

@Suppress("UnnecessaryVariable")
@Composable
fun BlockListScreen(
    onBackPressed: () -> Unit,
    viewModel: BlockListViewModel = hiltViewModel()
) {
    val dynViewState by viewModel.viewState.collectAsState()
    val viewState = dynViewState

    val dynViewAction by viewModel.viewAction.collectAsState(initial = null)
    val viewAction = dynViewAction?.action

    var isShowAddDialog by remember { mutableStateOf(false) }
    var newDomain by remember { mutableStateOf("") } // TODO: move to viewModel?

    val context = LocalContext.current

    LaunchedEffect(dynViewAction) {
        when (viewAction) {
            BlockListViewAction.ShowAddDialog -> {
                isShowAddDialog = true
            }
            // TODO: To dismiss the dialog
            BlockListViewAction.DismissAddDialog -> {
                isShowAddDialog = false
            }

            BlockListViewAction.InvalidDomain -> {
                Toast.makeText(context, R.string.block_list_invalid_domain, Toast.LENGTH_SHORT)
                    .show()
            }

            null -> {
                // do nothing
            }
        }.exhaustive()
    }

    if (isShowAddDialog) {
        AlertDialog(
            onDismissRequest = {
                isShowAddDialog = false
            },
            text = {
                Column {
                    val focusRequester = remember { FocusRequester() }
                    Text(text = stringResource(id = R.string.block_list_add_dialog_title))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newDomain,
                        onValueChange = {
                            newDomain = it
                        },
                        modifier = Modifier.focusRequester(focusRequester),
                        singleLine = true,
                        placeholder = {
                            Text(text = stringResource(id = R.string.block_list_hint_enter_domain))
                        },
                    )
                    LaunchedEffect(Unit) {
                        delay(200) // Workaround for http://shorturl.at/lrIY1
                        focusRequester.requestFocus()
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.onInteraction(BlockListInteractor.AddDomainClick(newDomain))
                }) {
                    Text(text = stringResource(id = R.string.action_add))
                }
            },
            backgroundColor = Colors.Daintree_200,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.block_list_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = "back button"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        viewModel.onInteraction(BlockListInteractor.AddClick)
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(id = R.string.cd_add_to_blocklist)
                        )
                    }
                }
            )
        }
    ) {
        when (viewState) {
            BlockListViewState.Loading -> {
                LoadingUi()
            }
            is BlockListViewState.Active -> {
                ActiveBlockListUi(
                    activeState = viewState,
                    onRemoveDomainClicked = { domain ->
                        viewModel.onInteraction(
                            BlockListInteractor.RemoveDomainClick(domain)
                        )
                    }
                )
            }
            is BlockListViewState.BlockListEmpty -> {
                EmptyUi()
            }
        }.exhaustive()
    }
}

@Composable
fun EmptyUi() {
    CenterBox {
        CenterColumn {
            Text(text = "📭", fontSize = 50.sp)
            Text(text = "You have no domains!")
        }
    }
}

@Composable
fun LoadingUi() {
    CenterBox {
        Column {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = stringResource(id = R.string.block_list_loading))
        }
    }
}
