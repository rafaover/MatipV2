package com.exercise.matipv2.ui.tipcalculator

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.exercise.matipv2.R
import com.exercise.matipv2.components.calculator.AddTipToListDialogBox
import com.exercise.matipv2.components.calculator.SplitCounter
import com.exercise.matipv2.components.calculator.TotalTipAmount
import com.exercise.matipv2.components.common.ButtonToOpenDialog
import com.exercise.matipv2.components.common.EditTextForm
import com.exercise.matipv2.components.common.RoundTheTipSwitch
import com.exercise.matipv2.ui.MainScreenViewModel
import kotlinx.coroutines.runBlocking

@Composable
fun TipCalculatorScreen(
    viewModel: MainScreenViewModel,
    uiState: TipCalculatorScreenUiState,
    snackbarHostState: SnackbarHostState
) {
    val focusManager = LocalFocusManager.current
    val tipAmountInput = uiState.tipAmount
    val tipPercentInput = uiState.tipPercent

    Column(
        modifier = Modifier
            .semantics { contentDescription = "Tip Calculator Screen" }
            .fillMaxSize()
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {

        /* Title */

        Text(
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_mid))
                .align(alignment = Alignment.Start),
            text = stringResource(R.string.calculate_tip),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
        )

        /* EditTextForm for Total Bill Amount */

        EditTextForm(
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_mid))
                .align(alignment = Alignment.Start),
            label = R.string.bill_amount,
            value = tipAmountInput,
            onValueChange = { viewModel.updateTipAmount(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            leadingIcon = R.drawable.attach_money
        )

        /** EditTextForm for Tip percentage **/

        EditTextForm(
            modifier = Modifier
                .padding(bottom = 40.dp)
                .align(alignment = Alignment.Start),
            label = R.string.tip_percentage,
            value = tipPercentInput,
            onValueChange = { viewModel.updateTipPercent(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = R.drawable.percent
        )

        SplitCounter(
            splitUiState = uiState.splitShare,
            onclickPositive = { viewModel.increaseCounter() },
            onclickNegative = { viewModel.decreaseCounter() }
        )

        /** Total Tip Amount calculated after [SplitCounter], [RoundTheTipSwitch] and
         * [EditTextForm] elements.
         **/
        TotalTipAmount(viewModel.updateFinalTip())

        RoundTheTipSwitch(
            modifier = Modifier
                .padding(bottom = dimensionResource(R.dimen.padding_mid))
                .align(alignment = Alignment.Start),
            roundUp = uiState.roundUp,
            onRoundUpChange = { viewModel.updateRoundUp(it) }
        )

        /** Button to Open [AddTipToListDialogBox] to add tip to list
         * after [EditTextForm] elements are filled.
         */

        ButtonToOpenDialog(
            dataIsPresent = tipAmountInput.isNotEmpty() && tipPercentInput.isNotEmpty(),
            updateShowDialog = { viewModel.updateShowAddListDialog(true) },
            buttonText = stringResource(R.string.add_tip_to_list)
        )

        /** Conditional attached to [ButtonToOpenDialog] above, opening dialog */

        if(viewModel.showAddListDialog) {
            AddTipToListDialogBox(
                viewModel = viewModel,
                allLists = viewModel.getAllLists(),
                onListSelected = {
                    runBlocking {
                        viewModel.insertTip()
                    }
                    focusManager.clearFocus()
                    viewModel.updateShowSnackBar(true)
                    viewModel.resetCalculateTipScreen()
                }
            )
        }

        /** Snackbar to show confirmation from [AddTipToListDialogBox],
         * that tip was added to list */

        if (viewModel.showSnackBar) {
            LaunchedEffect(snackbarHostState) {
                snackbarHostState.showSnackbar(
                    message = "Tip added to list",
                    duration = SnackbarDuration.Short
                )
            }
        }
    }
}