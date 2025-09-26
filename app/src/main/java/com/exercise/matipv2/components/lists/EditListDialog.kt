package com.exercise.matipv2.components.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.exercise.matipv2.R
import com.exercise.matipv2.components.common.EditTextForm
import com.exercise.matipv2.data.local.model.List

@Composable
fun EditListDialog(
    listToEdit: List,
    onSave: (newName: String) -> Unit,
    onDismiss: () -> Unit
) {
    var newNameState by remember(listToEdit.name) { mutableStateOf(listToEdit.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.edit_list_dialog_title)) },
        text = {
            Column {
                EditTextForm(
                    value = newNameState,
                    label = R.string.edit_list_name_prompt,
                    onValueChange = { newNameState = it }
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newNameState.isNotBlank()) {
                        onSave(newNameState)
                    }
                }
            ) {
                Text(stringResource(R.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
