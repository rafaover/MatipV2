package com.exercise.matipv2.components.calculator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.tip_amount
import com.exercise.matipv2.generated.resources.total_tip_amount_description
import androidx.compose.ui.unit.IntOffset

@Composable
fun TotalTipAmount(finalTip: String) {
    val totalTipDescription = stringResource(Res.string.total_tip_amount_description)
    val motionScheme = MaterialTheme.motionScheme
    Row(
        modifier = Modifier
            .semantics { contentDescription = totalTipDescription }
            .padding(bottom = MatipDimens.paddingSmall)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(Res.string.tip_amount),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )
        
        AnimatedContent(
            targetState = finalTip,
            transitionSpec = {
                val slideSpec = motionScheme.defaultSpatialSpec<IntOffset>()
                val fadeSpec = motionScheme.defaultEffectsSpec<Float>()
                if (targetState > initialState) {
                    (slideInVertically(slideSpec) { height -> height } + fadeIn(fadeSpec)) togetherWith
                            (slideOutVertically(slideSpec) { height -> -height } + fadeOut(fadeSpec))
                } else {
                    (slideInVertically(slideSpec) { height -> -height } + fadeIn(fadeSpec)) togetherWith
                            (slideOutVertically(slideSpec) { height -> height } + fadeOut(fadeSpec))
                }.using(
                    // Disable clipping since the letters may bounce outside the bounds of the container
                    androidx.compose.animation.SizeTransform(clip = false)
                )
            },
            label = "TipAmountAnimation"
        ) { targetTip ->
            Text(
                modifier = Modifier.testTag("TipAmount"),
                text = targetTip,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        }
    }
}
