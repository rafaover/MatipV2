package com.exercise.matipv2.ui.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.R
import com.exercise.matipv2.components.common.ConfirmationAlertDialog
import com.exercise.matipv2.components.common.FabAdd
import com.exercise.matipv2.components.common.ListItemComponent
import com.exercise.matipv2.components.lists.AddAnListDialog
import com.exercise.matipv2.components.lists.AllTipsFromListCounter
import com.exercise.matipv2.components.lists.EditListDialog
import com.exercise.matipv2.components.lists.SwipeBox
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.ui.MainScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Composable
fun ListsScreen(
    viewModel: MainScreenViewModel,
    allLists: Flow<kotlin.collections.List<List>>,
    navigateTo: (List) -> Unit
) {
    val allListsFlow by allLists.collectAsState(initial = emptyList())
    var selectedList by remember { mutableStateOf<List?>(null) }
    var showEditListDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .semantics { contentDescription = "Lists Screen. ${allListsFlow.count()} lists" }
            .padding(16.dp)
            .fillMaxSize()
    ) {
        if (allListsFlow.isNotEmpty()) {
            LazyColumn(
                userScrollEnabled = true
            ) {
                itemsIndexed(allListsFlow) { _, list ->
                    SwipeBox(
                        item = list,
                        onDelete = { listToDelete ->
                            selectedList = listToDelete
                            viewModel.updateShowDeleteListDialog(true)
                        },
                        onEdit = { listToEdit ->
                            selectedList = listToEdit
                            showEditListDialog = true
                        }
                    ) {
                        ListItemComponent(
                            modifier = Modifier
                                .clickable(onClickLabel = list.name) {
                                    navigateTo(list)
                                }
                                .height(60.dp),
                            item = list,
                            getName = { list.name },
                            mainTrailItemInfo = {
                                AllTipsFromListCounter(viewModel, list)
                            },
                            listItemTrailingIcon = Icons.Filled.ChevronRight
                        )
                    }
                }
            }
        }

        FabAdd(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(dimensionResource(R.dimen.padding_mid)),
            onClick = { viewModel.updateShowAddListDialog(true) },
            text = "New List",
            contentDescription = stringResource(R.string.add_a_list)
        )

        if(viewModel.showAddListDialog) {
            AddAnListDialog(
                viewModel = viewModel,
                onSaveRequest = {
                    viewModel.viewModelScope.launch {
                        viewModel.insertList(List(name = viewModel.newListName))
                        viewModel.updateShowAddListDialog(false)
                    }
                }
            )
        }

        if (viewModel.showDeleteListDialog && selectedList != null) {
            val listName = selectedList?.name ?: "List"
            ConfirmationAlertDialog(
                title = stringResource(R.string.dialog_title),
                message = stringResource(
                    id = R.string.dialog_delete_list_text,
                    listName
                ),
                icon = Icons.Filled.Info,
                confirmButtonText = stringResource(R.string.delete),
                onConfirm = {
                    selectedList?.let { listToDelete ->
                        viewModel.deleteList(listToDelete)
                        viewModel.deleteTipsFromList(listToDelete.id)
                    }
                    viewModel.updateShowDeleteListDialog(false)
                    selectedList = null
                },
                onDismiss = {
                    viewModel.updateShowDeleteListDialog(false)
                    selectedList = null
                }
            )
        }

        // Dialog for editing the list name
        if (showEditListDialog && selectedList != null) {
            EditListDialog(
                listToEdit = selectedList!!,
                onSave = { newName ->
                    viewModel.viewModelScope.launch {
                        val listToUpdate = selectedList!!.copy(name = newName)
                        viewModel.updateList(listToUpdate)
                        showEditListDialog = false
                        selectedList = null
                    }
                },
                onDismiss = {
                    showEditListDialog = false
                    selectedList = null
                }
            )
        }
    }
}