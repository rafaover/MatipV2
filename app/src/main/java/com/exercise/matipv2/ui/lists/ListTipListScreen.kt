package com.exercise.matipv2.ui.lists

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.R
import com.exercise.matipv2.components.common.FabAdd
import com.exercise.matipv2.components.common.ListItemComponent
import com.exercise.matipv2.components.common.shareTextWithApps
import com.exercise.matipv2.components.lists.AddTipValueToListDialog
import com.exercise.matipv2.ui.MainScreenViewModel
import com.exercise.matipv2.util.tipListToString
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTipListScreen(
    viewModel: MainScreenViewModel,
    onDismissRequest: () -> Unit,
    listId: Int,
    onBackClick: () -> Unit,
) {
    /** Get List by ID from database */
    val list = viewModel
        .getListById(listId)
        .collectAsState(null)

    /** Get the list of tips for the List collected on variable "list" above */
    val listTipList by viewModel
        .getAllTipsFromList(listId)
        .collectAsState(emptyList())

    /** Share the list of tips using [shareTextWithApps] */
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceTint.copy(alpha = 0.1f),
                    ),
                    title = { list.value?.let {
                        Text(text = it.name) }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClick
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    }
                )
            }
        ) {
            Box(
                modifier = Modifier.padding(it)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(dimensionResource(R.dimen.padding_mid))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.tip_list_title),
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(dimensionResource(R.dimen.padding_sml))
                        )

                        /** Button to [shareTextWithApps].
                         * Sharing list of Tips from List
                         * */
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    shareTextWithApps(
                                        title = list.value?.name!!,
                                        content = tipListToString(listTipList),
                                        context = context
                                    )
                                },
                            imageVector = Icons.Default.Share,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = stringResource(R.string.share_tip_list)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(bottom = dimensionResource(R.dimen.padding_mid))
                    )
                    LazyColumn(
                        userScrollEnabled = true,
                        modifier = Modifier.padding(bottom = 65.dp)
                    ) {
                        /**
                         * Display rows of tips from specific List(List).
                         */
                        itemsIndexed(listTipList) { _, tip ->
                            ListItemComponent(
                                overLineContent = { Text(text = tip.dateCreated) },
                                item = tip,
                                getName = { tip.tipAmount },
                                mainTrailItemInfo = { },
                                listItemTrailingIcon = Icons.Default.DeleteForever,
                                trailingIconContentDescription = stringResource(R.string.delete),
                                onClickTrailingIcon = { viewModel.deleteTip(tip) },
                                onClickLabel = context.getString(R.string.tip_will_be_deleted),
                                modifier = Modifier.height(70.dp)
                            )
                        }
                    }
                }

                /** FAB to add a final tip value without calculation, to the current List **/
                FabAdd(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensionResource(R.dimen.padding_mid)),
                    onClick = { viewModel.updateShowAddTipValueToListDialog(true) },
                    contentDescription = stringResource(R.string.add_tip_to_list)
                )

                /** Conditional attached to [FabAdd] composable above to
                 * show dialog when clicked */

                if(viewModel.showAddTipValueToListDialog) {
                    AddTipValueToListDialog(
                        viewModel = viewModel,
                        onSaveRequest = {
                            viewModel.viewModelScope.launch {
                                viewModel.insertTipValueToList(listId)
                                viewModel.updateShowAddTipValueToListDialog(false)
                            }
                        }
                    )
                }
            }
        }
    }
}