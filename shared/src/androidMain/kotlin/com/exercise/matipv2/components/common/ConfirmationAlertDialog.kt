package com.exercise.matipv2.components.common

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.cancel
import androidx.compose.ui.text.AnnotatedString

@Composable
fun ConfirmationAlertDialog(
    title: String,
    message: AnnotatedString?,
    icon: DrawableResource,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    ) {
    AlertDialog(
        icon = { Icon(painter = painterResource(icon), contentDescription = null) },
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
                Text(stringResource(Res.string.cancel))
            }
        }

    )
}