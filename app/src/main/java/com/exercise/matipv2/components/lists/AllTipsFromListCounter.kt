package com.exercise.matipv2.components.lists

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.ui.MainScreenViewModel

@Composable
fun AllTipsFromListCounter(
    viewModel: MainScreenViewModel,
    list: List
) {
    val tipsFromList by viewModel
        .getAllTipsFromList(list.id).collectAsState(initial = emptyList())

    Row {
        Text("${tipsFromList.size} ")
        if (tipsFromList.size == 1) Text("tip") else Text("tips")
    }
}