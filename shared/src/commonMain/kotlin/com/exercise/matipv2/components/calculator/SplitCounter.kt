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
import com.exercise.matipv2.ui.theme.MatipDimens
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.bill_split_description
import com.exercise.matipv2.generated.resources.split

@Composable
fun SplitCounter(
    splitUiState: Int,
    onclickPositive: () -> Unit,
    onclickNegative: () -> Unit
) {
    val billSplitDescription = stringResource(Res.string.bill_split_description)
    Row(
        modifier = Modifier
            .semantics { contentDescription = billSplitDescription }
            .padding(bottom = MatipDimens.paddingMedium)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(Res.string.split),
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