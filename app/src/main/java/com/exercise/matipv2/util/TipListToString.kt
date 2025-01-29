package com.exercise.matipv2.util

import com.exercise.matipv2.data.local.model.Tip

/** Converts a list of [Tip] objects into a single string.
 * This function is being used to export the list of tips to a file.
 * */
fun tipListToString(
    stringList: List<Tip>
): String {
    return stringList.joinToString("\n") {tip ->
        tip.tipAmount
    }
}