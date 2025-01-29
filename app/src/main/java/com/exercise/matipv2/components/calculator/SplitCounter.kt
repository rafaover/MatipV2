package com.exercise.matipv2.components.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.R

@Composable
fun SplitCounter(
    splitUiState: Int,
    onclickPositive: () -> Unit,
    onclickNegative: () -> Unit
) {
    Row(
        modifier = Modifier
            .semantics { contentDescription = "Bill Split" }
            .padding(bottom = dimensionResource(R.dimen.padding_mid))
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.split),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.padding(20.dp))
        Button(
            onClick = onclickNegative,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors()
        ) { Text(text = "-") }
        AnimatedContent(
            targetState = splitUiState,
            transitionSpec = {
                if (targetState > initialState) {
                    slideInVertically { -it } togetherWith slideOutVertically { it }
                } else {
                    slideInVertically { it } togetherWith slideOutVertically { -it }
                }
            }, label = "Animated Counter"
        ) { count ->
            Text(
                modifier = Modifier.testTag("SplitCounter"),
                text = "$count",
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center,
            )
        }
        Button(
            onClick = onclickPositive,
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors()
        ) { Text(text = "+") }
    }
}