package com.exercise.matipv2.ui.settings

import com.exercise.matipv2.generated.resources.ic_arrow_back
import com.exercise.matipv2.generated.resources.ic_cloud_upload
import com.exercise.matipv2.generated.resources.ic_delete_forever
import com.exercise.matipv2.generated.resources.ic_description
import com.exercise.matipv2.generated.resources.ic_feedback
import com.exercise.matipv2.generated.resources.ic_restore
import com.exercise.matipv2.generated.resources.ic_star
import com.exercise.matipv2.generated.resources.ic_warning
import org.jetbrains.compose.resources.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.account_deleted
import com.exercise.matipv2.generated.resources.back
import com.exercise.matipv2.generated.resources.backup_success
import com.exercise.matipv2.generated.resources.cloud_backup
import com.exercise.matipv2.generated.resources.delete
import com.exercise.matipv2.generated.resources.delete_account
import com.exercise.matipv2.generated.resources.delete_account_confirm_message
import com.exercise.matipv2.generated.resources.delete_account_confirm_title
import com.exercise.matipv2.generated.resources.feedback
import com.exercise.matipv2.generated.resources.feedback_subject
import com.exercise.matipv2.generated.resources.last_backup
import com.exercise.matipv2.generated.resources.no_backup_found
import com.exercise.matipv2.generated.resources.no_email_app_found
import com.exercise.matipv2.generated.resources.privacy_policy
import com.exercise.matipv2.generated.resources.restore_backup
import com.exercise.matipv2.generated.resources.restore_success
import com.exercise.matipv2.generated.resources.review_app
import com.exercise.matipv2.generated.resources.settings
import com.exercise.matipv2.generated.resources.support_legal
import com.exercise.matipv2.components.common.ConfirmationAlertDialog
import com.exercise.matipv2.platform.PlatformActions
import com.exercise.matipv2.ui.auth.AuthViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.compose.koinInject
import androidx.compose.ui.text.AnnotatedString

private const val FEEDBACK_EMAIL = "feeltheboardgame@gmail.com"
private const val PRIVACY_POLICY_URL = "https://sites.google.com/view/matip-privacy-police/home"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onShowMessage: (String) -> Unit,
    platformActions: PlatformActions = koinInject(),
) {
    val currentUser by authViewModel.currentUser.collectAsStateWithLifecycle()
    val lastBackupDate by authViewModel.lastBackupDate.collectAsStateWithLifecycle()
    val backupSuccessMessage = stringResource(Res.string.backup_success)
    val restoreSuccessMessage = stringResource(Res.string.restore_success)
    val accountDeletedMessage = stringResource(Res.string.account_deleted)
    val feedbackSubject = stringResource(Res.string.feedback_subject)
    val noEmailAppMessage = stringResource(Res.string.no_email_app_found)

    if (authViewModel.showDeleteAccountDialog) {
        ConfirmationAlertDialog(
            title = stringResource(Res.string.delete_account_confirm_title),
            message = AnnotatedString(stringResource(Res.string.delete_account_confirm_message)),
            icon = Res.drawable.ic_warning,
            confirmButtonText = stringResource(Res.string.delete),
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
                title = { Text(stringResource(Res.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painterResource(Res.drawable.ic_arrow_back), contentDescription = stringResource(Res.string.back))
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
                    text = stringResource(Res.string.cloud_backup),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp)
                )

                ListItem(
                    headlineContent = { Text(stringResource(Res.string.cloud_backup)) },
                    leadingContent = { Icon(painterResource(Res.drawable.ic_cloud_upload), contentDescription = null) },
                    modifier = Modifier.clickable {
                        authViewModel.backupData(
                            onSuccess = { onShowMessage(backupSuccessMessage) },
                            onError = onShowMessage
                        )
                    }
                )

                ListItem(
                    headlineContent = { Text(stringResource(Res.string.restore_backup)) },
                    supportingContent = {
                        Text(
                            text = if (lastBackupDate != null)
                                stringResource(Res.string.last_backup, lastBackupDate!!)
                            else
                                stringResource(Res.string.no_backup_found)
                        )
                    },
                    leadingContent = { Icon(painterResource(Res.drawable.ic_restore), contentDescription = null) },
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
                text = stringResource(Res.string.support_legal),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text(stringResource(Res.string.review_app)) },
                leadingContent = { Icon(painterResource(Res.drawable.ic_star), contentDescription = null) },
                modifier = Modifier.clickable {
                    platformActions.openStoreListing()
                }
            )

            ListItem(
                headlineContent = { Text(stringResource(Res.string.feedback)) },
                leadingContent = { Icon(painterResource(Res.drawable.ic_feedback), contentDescription = null) },
                modifier = Modifier.clickable {
                    if (!platformActions.sendEmail(FEEDBACK_EMAIL, feedbackSubject)) {
                        onShowMessage(noEmailAppMessage)
                    }
                }
            )

            ListItem(
                headlineContent = { Text(stringResource(Res.string.privacy_policy)) },
                leadingContent = { Icon(painterResource(Res.drawable.ic_description), contentDescription = null) },
                modifier = Modifier.clickable {
                    platformActions.openUrl(PRIVACY_POLICY_URL)
                }
            )

            if (currentUser != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                ListItem(
                    headlineContent = { 
                        Text(
                            text = stringResource(Res.string.delete_account),
                            color = MaterialTheme.colorScheme.error
                        ) 
                    },
                    leadingContent = { 
                        Icon(
                            painter = painterResource(Res.drawable.ic_delete_forever), 
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
