package com.exercise.matipv2.components.calculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.exercise.matipv2.R
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.ui.MainScreenViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun AddTipToListDialogBox(
    viewModel: MainScreenViewModel,
    allLists: Flow<kotlin.collections.List<List>>,
    onListSelected: (List) -> Unit
) {
    val allListsFlow by allLists.collectAsState(initial = emptyList())
    val (selectedOption, onOptionSelected) =
        remember { mutableStateOf<List?>(null) }

    Dialog(
        onDismissRequest = { viewModel.updateShowAddListDialog(false) }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 280.dp, max = 380.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_dialog)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_mid))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.select_list),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )

                    /* Button to Save the selected list */

                    TextButton(
                        enabled = selectedOption != null,
                        onClick = {
                            selectedOption?.let { list ->
                                onListSelected(list)
                            }
                            viewModel.updateShowAddListDialog(false)
                        },
                        modifier = Modifier
                            .padding(dimensionResource(R.dimen.padding_button))
                    ) {
                        Text(
                            text = stringResource(R.string.confirm),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
                if (allListsFlow.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        userScrollEnabled = true
                    ) {
                        itemsIndexed(allListsFlow) { _, list ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (list == selectedOption),
                                        onClick = {
                                            onOptionSelected(list)
                                            viewModel.updateListId(list.id)
                                        },
                                        role = Role.RadioButton
                                    ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (list == selectedOption),
                                    onClick = null
                                )
                                Text(
                                    text = list.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier
                                        .padding(start = dimensionResource(R.dimen.padding_mid))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}