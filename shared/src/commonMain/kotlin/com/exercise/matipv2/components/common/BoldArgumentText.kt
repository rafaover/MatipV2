package com.exercise.matipv2.components.common

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/** Stands in for the argument while the string is formatted, so we know where to split it. */
private const val ARGUMENT_MARKER = "￼"

/** Loads the string with a single `%1$s` placeholder and shows [argument] in bold there. */
@Composable
fun stringResourceWithBoldArgument(id: StringResource, argument: String): AnnotatedString {
    val (textBefore, textAfter) = stringResource(id, ARGUMENT_MARKER).split(ARGUMENT_MARKER, limit = 2)
    return buildAnnotatedString {
        append(textBefore)
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(argument) }
        append(textAfter)
    }
}
