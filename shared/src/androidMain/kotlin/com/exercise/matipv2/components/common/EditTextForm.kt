package com.exercise.matipv2.components.common

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

@Composable
fun EditTextForm(
    modifier: Modifier = Modifier,
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int? = null,
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