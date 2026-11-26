package com.exercise.matipv2.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.exercise.matipv2.ui.theme.MatipDimens

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ButtonToOpenDialog(
    updateShowDialog: () -> Unit,
    buttonText: String,
    dataIsPresent: Boolean = false
) {
    Row {
        Button(
            enabled = dataIsPresent,
            shapes = ButtonDefaults.shapes(),
            modifier = Modifier.padding(MatipDimens.paddingSmall),
            // Update to "true" to show the dialog box
            onClick = { updateShowDialog() }
        ) {
            Text(
                text = buttonText,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
