package com.exercise.matipv2.components.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.exercise.matipv2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar() {
    CenterAlignedTopAppBar(
        modifier = Modifier.semantics { contentDescription = "Top Bar" },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.1f),
        ),
        title = {
            Image(
                modifier = Modifier.size(dimensionResource(R.dimen.logo_topbar_size)),
                painter = if (isSystemInDarkTheme()) {
                    painterResource(R.drawable.matip_logo_sml_dark)
                } else {
                    painterResource(R.drawable.matip_logo_sml)
                },
                contentDescription = stringResource(R.string.matip_logo),
            )
        },
        navigationIcon = {
            IconButton(
                // TODO("Menu to be implemented")
                onClick = { },
                enabled = false
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = dimensionResource(R.dimen.padding_mid)),
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.options_menu)
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}