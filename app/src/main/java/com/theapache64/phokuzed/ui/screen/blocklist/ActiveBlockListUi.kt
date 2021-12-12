package com.theapache64.phokuzed.ui.screen.blocklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.theapache64.phokuzed.R

@Composable
fun ActiveBlockListUi(
    activeState: BlockListViewState.Active,
    onRemoveDomainClicked: (domain: String) -> Unit
) {
    LazyColumn {
        activeState.blockList.forEach { domain ->
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 10.dp),
                    verticalAlignment = CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = domain
                    )

                    if (activeState.mode == Mode.ADD_AND_REMOVE) {
                        // If mode == alter, we'll show the delete domain button
                        IconButton(
                            onClick = { onRemoveDomainClicked.invoke(domain) },
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Close,
                                contentDescription = stringResource(id = R.string.block_list_cd_remove_domain)
                            )
                        }
                    }
                }
            }
        }
    }
}
