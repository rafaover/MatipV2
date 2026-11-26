package com.exercise.matipv2.components.lists

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.ui.MainScreenViewModel
import androidx.compose.ui.res.pluralStringResource
import com.exercise.matipv2.R

@Composable
fun AllTipsFromListCounter(
    viewModel: MainScreenViewModel,
    list: TipList
) {
    val tipsFromList by viewModel
        .getAllTipsFromList(list.id).collectAsState(initial = emptyList())

    Text(pluralStringResource(R.plurals.tips_count, tipsFromList.size, tipsFromList.size))
}