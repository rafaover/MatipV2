package com.exercise.matipv2.ui.settings

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.exercise.matipv2.R
import com.exercise.matipv2.components.common.ConfirmationAlertDialog
import com.exercise.matipv2.ui.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val context = LocalContext.current
    val currentUser by authViewModel.currentUser.collectAsState()
    val lastBackupDate by authViewModel.lastBackupDate.collectAsState()
    val backupSuccessMessage = stringResource(R.string.backup_success)
    val restoreSuccessMessage = stringResource(R.string.restore_success)
    val accountDeletedMessage = stringResource(R.string.account_deleted)

    if (authViewModel.showDeleteAccountDialog) {
        ConfirmationAlertDialog(
            title = stringResource(R.string.delete_account_confirm_title),
            message = stringResource(R.string.delete_account_confirm_message),
            icon = Icons.Default.Warning,
            confirmButtonText = stringResource(R.string.delete),
            onConfirm = {
                authViewModel.deleteAccount(
                    onSuccess = {
                        onShowMessage(accountDeletedMessage)
                        authViewModel.updateShowDeleteAccountDialog(false)
                        onBackClick()
                    },
                    onError = { onShowMessage(it) }
                )
            },
            onDismiss = { authViewModel.updateShowDeleteAccountDialog(false) }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (currentUser != null) {
                Text(
                    text = stringResource(R.string.cloud_backup),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )

                ListItem(
                    headlineContent = { Text(stringResource(R.string.cloud_backup)) },
                    leadingContent = { Icon(Icons.Default.CloudUpload, contentDescription = null) },
                    modifier = Modifier.clickable {
                        authViewModel.backupData(
                            onSuccess = { onShowMessage(backupSuccessMessage) },
                            onError = onShowMessage
                        )
                    }
                )

                ListItem(
                    headlineContent = { Text(stringResource(R.string.restore_backup)) },
                    supportingContent = {
                        Text(
                            text = if (lastBackupDate != null)
                                stringResource(R.string.last_backup, lastBackupDate!!)
                            else
                                stringResource(R.string.no_backup_found)
                        )
                    },
                    leadingContent = { Icon(Icons.Default.Restore, contentDescription = null) },
                    modifier = Modifier.clickable {
                        authViewModel.restoreData(
                            onSuccess = { onShowMessage(restoreSuccessMessage) },
                            onError = onShowMessage
                        )
                    }
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            }

            Text(
                text = stringResource(R.string.support_legal),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text(stringResource(R.string.review_app)) },
                leadingContent = { Icon(Icons.Default.Star, contentDescription = null) },
                modifier = Modifier.clickable {
                    openPlayStore(context)
                }
            )

            ListItem(
                headlineContent = { Text(stringResource(R.string.feedback)) },
                leadingContent = { Icon(Icons.Default.Feedback, contentDescription = null) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = "mailto:feeltheboardgame@gmail.com".toUri()
                        putExtra(Intent.EXTRA_SUBJECT, "Feedback for MaTip App")
                    }
                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        onShowMessage("No email app found")
                    }
                }
            )

            ListItem(
                headlineContent = { Text(stringResource(R.string.privacy_policy)) },
                leadingContent = { Icon(Icons.Default.Description, contentDescription = null) },
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_VIEW,
                        "https://sites.google.com/view/matip-privacy-police/home".toUri())
                    context.startActivity(intent)
                }
            )

            if (currentUser != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ListItem(
                    headlineContent = { 
                        Text(
                            text = stringResource(R.string.delete_account),
                            color = MaterialTheme.colorScheme.error
                        ) 
                    },
                    leadingContent = { 
                        Icon(
                            imageVector = Icons.Default.DeleteForever, 
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        ) 
                    },
                    modifier = Modifier.clickable {
                        authViewModel.updateShowDeleteAccountDialog(true)
                    }
                )
            }
        }
    }
}

private fun openPlayStore(context: Context) {
    val packageName = context.packageName
    val marketUri = "market://details?id=$packageName".toUri()
    val intent = Intent(Intent.ACTION_VIEW, marketUri).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    try {
        context.startActivity(intent)
    } catch (e: Exception) {
        val webUri = "https://play.google.com/store/apps/details?id=$packageName".toUri()
        val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(webIntent)
    }
}
