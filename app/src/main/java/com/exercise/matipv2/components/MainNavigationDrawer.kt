package com.exercise.matipv2.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exercise.matipv2.R
import com.exercise.matipv2.data.model.AuthUser

@Composable
fun MainNavigationDrawerContent(
    currentUser: AuthUser?,
    onSignInGoogle: () -> Unit,
    onSignInEmail: () -> Unit,
    onSignOut: () -> Unit,
    onFeedbackClick: () -> Unit,
    onTermsClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        if (currentUser == null) {
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                label = { Text(text = stringResource(R.string.sign_in_google)) },
                selected = false,
                onClick = onSignInGoogle
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.Default.AccountCircle, contentDescription = null) },
                label = { Text(text = stringResource(R.string.sign_in_email)) },
                selected = false,
                onClick = onSignInEmail
            )
        } else {
            NavigationDrawerItem(
                icon = {
                    if (currentUser.photoUrl != null) {
                        AsyncImage(
                            model = currentUser.photoUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                    }
                },
                label = {
                    Column {
                        Text(text = currentUser.name ?: currentUser.email ?: stringResource(R.string.sign_in))
                        if (currentUser.name != null) {
                            Text(
                                text = currentUser.email ?: "",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                },
                selected = false,
                onClick = { /* User Profile logic could go here */ }
            )
            NavigationDrawerItem(
                icon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                label = { Text(text = stringResource(R.string.sign_out)) },
                selected = false,
                onClick = onSignOut
            )
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Feedback, contentDescription = null) },
            label = { Text(text = stringResource(R.string.feedback)) },
            selected = false,
            onClick = onFeedbackClick
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Description, contentDescription = null) },
            label = { Text(text = stringResource(R.string.terms_and_conditions)) },
            selected = false,
            onClick = onTermsClick
        )
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text(text = stringResource(R.string.settings)) },
            selected = false,
            onClick = onSettingsClick
        )
    }
}
