package com.exercise.matipv2.components.common

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.R

@Composable
fun RoundTheTipSwitch(
    roundUp: Boolean,
    onRoundUpChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
)
{
    Row(
        modifier = modifier
            .semantics { contentDescription = "Round The Tip Switch"}
            .fillMaxWidth()
            .size(50.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(text = stringResource(R.string.round_up_tip))
        Switch(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
                .testTag("RoundTheTipSwitch")
                .semantics {
                    stateDescription = if (roundUp) {
                        "Round Up On"
                    } else {
                        "Round Up Off"
                    }
                },
            checked = roundUp,
            onCheckedChange = onRoundUpChange
        )
    }
}