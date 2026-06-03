package com.exercise.matipv2.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Restore
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.R
import com.exercise.matipv2.ui.auth.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    authViewModel: AuthViewModel = koinViewModel(),
    onBackClick: () -> Unit,
    onShowMessage: (String) -> Unit,
) {
    val currentUser by authViewModel.currentUser.collectAsState()
    val lastBackupDate by authViewModel.lastBackupDate.collectAsState()
    val backupSuccessMessage = stringResource(R.string.backup_success)
    val restoreSuccessMessage = stringResource(R.string.restore_success)

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

//            Text(
//                text = "Support & Legal",
//                style = MaterialTheme.typography.labelLarge,
//                color = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.padding(16.dp)
//            )
//
//            ListItem(
//                headlineContent = { Text(stringResource(R.string.feedback)) },
//                leadingContent = { Icon(Icons.Default.Feedback, contentDescription = null) },
//                modifier = Modifier.clickable { /* TODO */ }
//            )
//
//            ListItem(
//                headlineContent = { Text(stringResource(R.string.terms_and_conditions)) },
//                leadingContent = { Icon(Icons.Default.Description, contentDescription = null) },
//                modifier = Modifier.clickable { /* TODO */ }
//            )
        }
    }
}
