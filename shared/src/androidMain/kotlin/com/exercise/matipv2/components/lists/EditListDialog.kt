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
import org.jetbrains.compose.resources.stringResource
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.cancel
import com.exercise.matipv2.generated.resources.edit_list_dialog_title
import com.exercise.matipv2.generated.resources.edit_list_name_prompt
import com.exercise.matipv2.generated.resources.save
import com.exercise.matipv2.components.common.EditTextForm
import com.exercise.matipv2.data.local.model.TipList

@Composable
fun EditListDialog(
    listToEdit: TipList,
    onSave: (newName: String) -> Unit,
    onDismiss: () -> Unit
) {
    var newNameState by remember(listToEdit.name) { mutableStateOf(listToEdit.name) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(Res.string.edit_list_dialog_title)) },
        text = {
            Column {
                EditTextForm(
                    value = newNameState,
                    label = Res.string.edit_list_name_prompt,
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
                Text(stringResource(Res.string.save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        }
    )
}
