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
import androidx.compose.ui.graphics.Color
import com.exercise.matipv2.ui.theme.MatipDimens
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.exercise.matipv2.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    onNavigationClick: () -> Unit
) {
    val topBarDescription = stringResource(R.string.top_bar_description)
    CenterAlignedTopAppBar(
        modifier = Modifier.semantics { contentDescription = topBarDescription },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.1f),
            scrolledContainerColor = Color.Unspecified,
            navigationIconContentColor = Color.Unspecified,
            titleContentColor = Color.Unspecified,
            actionIconContentColor = Color.Unspecified
        ),
        title = {
            Image(
                modifier = Modifier.size(MatipDimens.topBarLogoSize),
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
                onClick = onNavigationClick,
                enabled = true
            ) {
                Icon(
                    modifier = Modifier
                        .padding(start = MatipDimens.paddingMedium),
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.options_menu)
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}