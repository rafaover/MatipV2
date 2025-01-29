package com.exercise.matipv2.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.exercise.matipv2.R

@Composable
fun ConfirmationAlertDialog(
    title: String,
    message: String?,
    icon: ImageVector,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    ) {
    AlertDialog(
        icon = { Icon(imageVector = icon, contentDescription = null) },
        title = { Text(text = title) },
        text = {
            if (message != null) {
                Text(text = message)
            }
        },
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { onConfirm() }
            ) {
                Text(confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss() }
            ) {
                Text(stringResource(R.string.cancel))
            }
        }

    )
}