package com.exercise.matipv2.ui

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.data.local.model.List
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.repository.MatipRepository
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreenUiState
import com.exercise.matipv2.util.calculateTip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainScreenViewModel (
    private val matipRepository: MatipRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TipCalculatorScreenUiState())
    val uiState = _uiState.asStateFlow()

    var showAddListDialog by mutableStateOf(false)
    var showDeleteListDialog by mutableStateOf(false)
    var showSnackBar by mutableStateOf(false)
    var newListName by mutableStateOf("")

    init {
        resetCalculateTipScreen()
    }

    /*
    * Update Functions
    */

    private fun updateState(update: (TipCalculatorScreenUiState) -> TipCalculatorScreenUiState) {
        _uiState.value = update(_uiState.value)
    }

    fun updateTipAmount(amount: String) {
        updateState { it.copy(tipAmount = amount) }
    }

    fun updateTipPercent(tipPercent: String) {
        updateState { it.copy(tipPercent = tipPercent)}
    }

    fun updateRoundUp(roundUp: Boolean) {
        updateState { it.copy(roundUp = roundUp) }
    }

    fun updateList(list: List) {
        viewModelScope.launch(Dispatchers.IO) {
            matipRepository.updateList(list)
        }
    }

    fun updateListId(listId: Int) {
        updateState { it.copy(listId = listId) }
    }

    fun updateNewListName(listName: String) {
        newListName = listName
    }

    fun updateShowAddListDialog(showDialog: Boolean) {
        showAddListDialog = showDialog
    }

    fun updateShowDeleteListDialog(showDialog: Boolean) {
        showDeleteListDialog = showDialog
    }

    fun increaseCounter() {
        updateState { it.copy(splitShare = uiState.value.splitShare + 1)}
    }

    fun decreaseCounter() {
        if (uiState.value.splitShare > 0) {
            updateState { it.copy(splitShare = uiState.value.splitShare - 1)}
        }
    }

    @SuppressLint("VisibleForTests")
    fun updateFinalTip(): String {
        val calculatedTip = calculateTip(
            amount = uiState.value.tipAmount,
            tipPercent = uiState.value.tipPercent,
            roundUp = uiState.value.roundUp,
            tipSplit = uiState.value.splitShare
        )
        updateState { it.copy(finalTip = calculatedTip)}
        return calculatedTip
    }

    fun resetCalculateTipScreen() {
        _uiState.value = TipCalculatorScreenUiState()
    }

    fun updateShowSnackBar (snackBar: Boolean) {
        showSnackBar = snackBar
    }

    /*
    * Insert Functions
    */

    fun insertTip() {
        viewModelScope.launch(Dispatchers.IO) {
            val tip = Tip(
                tipAmount = uiState.value.finalTip,
                tipPercent = uiState.value.tipPercent,
                listId = uiState.value.listId
            )
            matipRepository.insertTip(tip)
        }
    }

    fun insertList(list: List) {
        viewModelScope.launch(Dispatchers.IO) {
            matipRepository.insertList(list)
        }
        updateNewListName("")
    }

//    suspend fun addTipToList(tip: Tip, listId: Int) {
//            tip.listId = listId
//            matipRepository.updateTip(tip)
//    }

    /*
    * Delete Functions
    */

    fun deleteList(list: List?) {
        viewModelScope.launch(Dispatchers.IO) {
            if (list != null) {
                matipRepository.deleteList(list)
            }
        }
    }

    fun deleteTip(tip: Tip) {
        viewModelScope.launch(Dispatchers.IO) {
            matipRepository.deleteTip(tip)
        }
    }

    fun deleteTipsFromList(listId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            getAllTipsFromList(listId).collect { tips ->
                tips.forEach { tip ->
                    deleteTip(tip)
                }
            }
        }
    }

    /*
    * Get Functions
    */

//    suspend fun getLastTipSaved(): Tip {
//        return matipRepository.getLastTipSaved()
//    }

    fun getAllLists(): Flow<kotlin.collections.List<List>> {
        return matipRepository.getAllLists()
    }

    fun getListById(listId: Int): Flow<List> {
        return matipRepository.getListById(listId)
    }

    fun getAllTipsFromList(eventId: Int): Flow<kotlin.collections.List<Tip>> {
        return matipRepository.getAllTipsFromList(eventId)
    }

    /*
    * Extension Functions
    */

//    private fun TipCalculatorScreenUiState.toTip(): Tip = Tip(
//        tipAmount = finalTip,
//        tipPercent = tipPercent
//    )
}

