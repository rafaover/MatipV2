package com.exercise.matipv2.components.lists

import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.ic_delete_outlined
import com.exercise.matipv2.generated.resources.ic_edit_outlined
import org.jetbrains.compose.resources.painterResource
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
/* Swipe box to Delete or Edit */
fun <T> SwipeBox(
    item: T,
    modifier: Modifier = Modifier,
    onDelete: (T) -> Unit,
    onEdit: (T) -> Unit,
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()

    lateinit var icon: DrawableResource
    lateinit var alignment: Alignment
    val color: Color

    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            icon = Res.drawable.ic_delete_outlined
            alignment = Alignment.CenterEnd
            color = MaterialTheme.colorScheme.errorContainer
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            icon = Res.drawable.ic_edit_outlined
            alignment = Alignment.CenterStart
            color = Color.Green.copy(alpha = 0.3f)
        }

        SwipeToDismissBoxValue.Settled -> {
            icon = Res.drawable.ic_delete_outlined // Default or fallback icon
            alignment = Alignment.CenterEnd // Default or fallback alignment
            color = MaterialTheme.colorScheme.errorContainer // Default or fallback color
        }
    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }
        }
    ) {
        content()
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            LaunchedEffect(swipeState) {
                onDelete(item)
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                onEdit(item)
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> { }
    }
}