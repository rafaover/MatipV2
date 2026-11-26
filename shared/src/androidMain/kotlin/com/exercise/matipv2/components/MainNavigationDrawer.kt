package com.exercise.matipv2.components

import com.exercise.matipv2.generated.resources.ic_account_circle
import com.exercise.matipv2.generated.resources.ic_list
import com.exercise.matipv2.generated.resources.ic_logout
import com.exercise.matipv2.generated.resources.ic_settings
import com.exercise.matipv2.generated.resources.ic_star
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.list_tab
import com.exercise.matipv2.generated.resources.more_lists
import com.exercise.matipv2.generated.resources.review_app
import com.exercise.matipv2.generated.resources.settings
import com.exercise.matipv2.generated.resources.sign_in
import com.exercise.matipv2.generated.resources.sign_in_email
import com.exercise.matipv2.generated.resources.sign_in_google
import com.exercise.matipv2.generated.resources.sign_out
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.model.AuthUser

@Composable
fun MainNavigationDrawerContent(
    currentUser: AuthUser?,
    userLists: List<TipList> = emptyList(),
    onSignInGoogle: () -> Unit,
    onSignInEmail: () -> Unit,
    onSignOut: () -> Unit,
    onListClick: (TipList) -> Unit,
    onSettingsClick: () -> Unit,
    onReviewClick: () -> Unit,
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(Modifier.height(12.dp))
            if (currentUser == null) {
                NavigationDrawerItem(
                    icon = { Icon(painterResource(Res.drawable.ic_account_circle), contentDescription = null) },
                    label = { Text(text = stringResource(Res.string.sign_in_google)) },
                    selected = false,
                    onClick = onSignInGoogle
                )
                NavigationDrawerItem(
                    icon = { Icon(painterResource(Res.drawable.ic_account_circle), contentDescription = null) },
                    label = { Text(text = stringResource(Res.string.sign_in_email)) },
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
                            Icon(painterResource(Res.drawable.ic_account_circle), contentDescription = null)
                        }
                    },
                    label = {
                        Column {
                            Text(text = currentUser.name ?: currentUser.email ?: stringResource(Res.string.sign_in))
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
                    icon = { Icon(painterResource(Res.drawable.ic_logout), contentDescription = null) },
                    label = { Text(text = stringResource(Res.string.sign_out)) },
                    selected = false,
                    onClick = onSignOut
                )
            }

            if (userLists.isNotEmpty()) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    text = stringResource(Res.string.list_tab),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp)
                )
                userLists.take(5).forEach { list ->
                    NavigationDrawerItem(
                        icon = { Icon(painterResource(Res.drawable.ic_list), contentDescription = null) },
                        label = { Text(text = list.name) },
                        selected = false,
                        onClick = { onListClick(list) }
                    )
                }
                if (userLists.size > 5) {
                    TextButton(
                        onClick = { /* Navigate to All Lists logic handled by caller */ },
                        modifier = Modifier.padding(horizontal = 28.dp)
                    ) {
                        Text(text = stringResource(Res.string.more_lists))
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            NavigationDrawerItem(
                icon = { Icon(painterResource(Res.drawable.ic_settings), contentDescription = null) },
                label = { Text(text = stringResource(Res.string.settings)) },
                selected = false,
                onClick = onSettingsClick
            )

            NavigationDrawerItem(
                icon = { Icon(painterResource(Res.drawable.ic_star), contentDescription = null) },
                label = { Text(text = stringResource(Res.string.review_app)) },
                selected = false,
                onClick = onReviewClick
            )
        }
    }
}
