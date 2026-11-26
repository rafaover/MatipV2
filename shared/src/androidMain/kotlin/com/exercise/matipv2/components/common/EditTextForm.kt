package com.exercise.matipv2.components.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun EditTextForm(
    modifier: Modifier = Modifier,
    label: StringResource,
    leadingIcon: DrawableResource? = null,
    onValueChange: (String) -> Unit = {},
    value: String,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    painter = painterResource(leadingIcon),
                    contentDescription = null
                )
            }
        },
        keyboardOptions = keyboardOptions,
        label = { Text(stringResource(label)) },
        value = value,
        onValueChange = onValueChange,
    )
}