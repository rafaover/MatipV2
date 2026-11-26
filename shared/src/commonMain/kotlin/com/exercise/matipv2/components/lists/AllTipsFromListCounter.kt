package com.exercise.matipv2.components.lists

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.ui.MainScreenViewModel
import org.jetbrains.compose.resources.pluralStringResource
import com.exercise.matipv2.generated.resources.Res
import com.exercise.matipv2.generated.resources.tips_count

@Composable
fun AllTipsFromListCounter(
    viewModel: MainScreenViewModel,
    list: TipList
) {
    val tipsFlow = remember(list.id) { viewModel.getAllTipsFromList(list.id) }
    val tipsFromList by tipsFlow.collectAsStateWithLifecycle(initialValue = emptyList())

    Text(pluralStringResource(Res.plurals.tips_count, tipsFromList.size, tipsFromList.size))
}