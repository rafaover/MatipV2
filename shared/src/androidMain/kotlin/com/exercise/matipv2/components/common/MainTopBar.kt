package com.exercise.matipv2.components.common

import com.exercise.matipv2.generated.resources.ic_menu
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.matip_logo
import com.exercise.matipv2.generated.resources.matip_logo_sml
import com.exercise.matipv2.generated.resources.matip_logo_sml_dark
import com.exercise.matipv2.generated.resources.options_menu
import com.exercise.matipv2.generated.resources.top_bar_description

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    onNavigationClick: () -> Unit
) {
    val topBarDescription = stringResource(Res.string.top_bar_description)
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
                    painterResource(Res.drawable.matip_logo_sml_dark)
                } else {
                    painterResource(Res.drawable.matip_logo_sml)
                },
                contentDescription = stringResource(Res.string.matip_logo),
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
                    painter = painterResource(Res.drawable.ic_menu),
                    contentDescription = stringResource(Res.string.options_menu)
                )
            }
        },
        scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    )
}