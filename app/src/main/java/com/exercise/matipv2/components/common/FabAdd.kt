package com.exercise.matipv2.components.common

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FabAdd(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Icon(
            imageVector = Icons.Rounded.Add,
            contentDescription = contentDescription,
            modifier = Modifier.size(30.dp)
        )
    }
}