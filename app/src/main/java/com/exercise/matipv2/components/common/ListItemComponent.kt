package com.exercise.matipv2.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

/**
 * Component that represents a list item with a trailing icon.
 * [item] is the item to be displayed.
 * [getName] is a function that returns the name of the item to be used as title.
 * [mainTrailItemInfo] is a composable that displays the main information of TrailItem.
 */


@Composable
fun <T> ListItemComponent(
    item: T,
    getName: (T) -> String,
    overLineContent: @Composable () -> Unit = {},
    mainTrailItemInfo: @Composable (T) -> Unit,
    listItemTrailingIcon: ImageVector,
    trailingIconContentDescription: String = "",
    onClickTrailingIcon: () -> Unit = {},
    onClickLabel: String = "",
    modifier: Modifier
    ) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.background(MaterialTheme.colorScheme.surface)

    ) {
        Spacer(
            modifier = Modifier
                .width(4.dp)
                .height(40.dp)
                .background(MaterialTheme.colorScheme.primary)
        )
        ListItem(
            modifier = modifier,
            overlineContent = overLineContent,
            headlineContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = getName(item),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            },
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    mainTrailItemInfo(item)
                    Spacer(Modifier.width(16.dp))
                    Icon(
                        imageVector = listItemTrailingIcon,
                        contentDescription = trailingIconContentDescription,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .clickable(onClickLabel = onClickLabel) {
                                onClickTrailingIcon()
                            }
                            .size(24.dp)
                    )
                }
            }
        )
    }
}