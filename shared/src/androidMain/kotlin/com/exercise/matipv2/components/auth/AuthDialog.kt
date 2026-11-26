package com.exercise.matipv2.components.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.already_have_account
import com.exercise.matipv2.generated.resources.cancel
import com.exercise.matipv2.generated.resources.confirm
import com.exercise.matipv2.generated.resources.dont_have_account
import com.exercise.matipv2.generated.resources.email
import com.exercise.matipv2.generated.resources.forgot_password
import com.exercise.matipv2.generated.resources.password
import com.exercise.matipv2.generated.resources.register
import com.exercise.matipv2.generated.resources.sign_in

@Composable
fun AuthDialog(
    isLoading: Boolean,
    onDismissRequest: () -> Unit,
    onSignIn: (String, String) -> Unit,
    onSignUp: (String, String) -> Unit,
    onForgotPassword: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(text = if (isRegistering) stringResource(Res.string.register) else stringResource(Res.string.sign_in))
        },
        text = {
            Column {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(Res.string.email)) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text(stringResource(Res.string.password)) },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextButton(
                    onClick = { isRegistering = !isRegistering },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isRegistering) stringResource(Res.string.already_have_account)
                        else stringResource(Res.string.dont_have_account),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (!isRegistering) {
                    TextButton(
                        onClick = { onForgotPassword(email) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading && email.isNotBlank()
                    ) {
                        Text(
                            text = stringResource(Res.string.forgot_password),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (isRegistering) onSignUp(email, password)
                    else onSignIn(email, password)
                },
                enabled = email.isNotBlank() && password.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = if (isRegistering) stringResource(Res.string.register) else stringResource(Res.string.confirm))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest, enabled = !isLoading) {
                Text(text = stringResource(Res.string.cancel))
            }
        }
    )
}
