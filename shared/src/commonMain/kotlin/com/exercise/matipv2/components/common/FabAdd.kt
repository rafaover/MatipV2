package com.exercise.matipv2.components.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

@Composable
fun FabAdd(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null,
    icon: DrawableResource? = null,
    contentDescription: String
) {
    FloatingActionButton(
        modifier = modifier.semantics { this.contentDescription = contentDescription },
        onClick = { onClick() }
    ) {
        if (icon != null) {
            Icon(
                painter = painterResource(icon),
                contentDescription = contentDescription,
                modifier = Modifier.size(36.dp)
            )
        } else if (text != null) {
            Text(
                text = text,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}