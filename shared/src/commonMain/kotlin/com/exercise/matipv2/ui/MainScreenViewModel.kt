package com.exercise.matipv2.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.repository.AuthRepository
import com.exercise.matipv2.data.repository.LocalRepository
import com.exercise.matipv2.ui.search.SearchFilterState
import com.exercise.matipv2.ui.tipcalculator.TipCalculatorScreenUiState
import com.exercise.matipv2.util.calculateTip
import com.exercise.matipv2.util.formatCurrency
import com.exercise.matipv2.util.stringAmountToDouble
import com.exercise.matipv2.util.localDateTimeFormated
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/** Keeps the lists loaded for a moment after the last screen stops looking, so a rotation does not reload them. */
private const val SUBSCRIPTION_TIMEOUT_MILLIS = 5_000L

class MainScreenViewModel (
    private val localRepository: LocalRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val currentUserId: String?
        get() = authRepository.currentUser.value?.id

    private val _uiState = MutableStateFlow(TipCalculatorScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchFilterState = MutableStateFlow(SearchFilterState())
    val searchFilterState = _searchFilterState.asStateFlow()

    /** The lists of whoever is signed in (or the guest). Follows sign in and sign out. */
    @OptIn(ExperimentalCoroutinesApi::class)
    val userLists: StateFlow<List<TipList>> = authRepository.currentUser
        .flatMapLatest { user -> localRepository.getAllLists(user?.id) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MILLIS),
            initialValue = emptyList()
        )

    var showAddListDialog by mutableStateOf(value = false)
    var showAddTipValueToListDialog by mutableStateOf(value = false)
    var showDeleteListDialog by mutableStateOf(value = false)
    var showSnackBar by mutableStateOf(value = false)
    var snackBarMessage by mutableStateOf("")
    var newListName by mutableStateOf("")

    init {
        resetCalculateTipScreen()
        updateDateCreated()
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

    private fun updateDateCreated() {
        updateState { it.copy(dateCreated = localDateTimeFormated()) }
    }

    fun updateList(list: TipList) {
        viewModelScope.launch {
            localRepository.updateList(list)
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

    fun updateShowAddTipValueToListDialog(showDialog: Boolean) {
        showAddTipValueToListDialog = showDialog
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
        updateDateCreated()
    }

    fun updateShowSnackBar (snackBar: Boolean, message: String = "") {
        showSnackBar = snackBar
        snackBarMessage = message
    }

    /*
    * Insert Functions
    */

    fun insertTip() {
        // Built before launching: the screen resets the state right after this call.
        val tip = Tip(
            tipAmount = uiState.value.finalTip,
            tipPercent = uiState.value.tipPercent,
            listId = uiState.value.listId,
            dateCreated = uiState.value.dateCreated,
            userId = currentUserId
        )
        viewModelScope.launch {
            localRepository.insertTip(tip)
        }
    }

    fun insertTipValueToList(listId: Int) {
        val tipAmountConverted = formatCurrency(stringAmountToDouble(uiState.value.tipAmount))

        viewModelScope.launch {
            val tip = Tip(
                tipAmount = tipAmountConverted,
                tipPercent = "0",
                listId = listId,
                dateCreated = uiState.value.dateCreated,
                userId = currentUserId
            )
            localRepository.insertTip(tip)
            resetCalculateTipScreen()
        }
    }

    fun insertList(list: TipList) {
        viewModelScope.launch {
            val listWithUser = list.copy(userId = currentUserId)
            localRepository.insertList(listWithUser)
        }
        updateNewListName("")
    }

    /*
    * Delete Functions
    */

    fun deleteList(list: TipList?) {
        viewModelScope.launch {
            if (list != null) {
                localRepository.deleteList(list)
            }
        }
    }

    fun deleteTip(tip: Tip) {
        viewModelScope.launch {
            localRepository.deleteTip(tip)
        }
    }

    fun deleteTipsFromList(listId: Int) {
        viewModelScope.launch {
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


    fun getAllTipsFromList(eventId: Int): Flow<List<Tip>> {
        return localRepository.getAllTipsFromList(eventId, currentUserId)
    }


    /**
     * Update search query and active state
     */
    fun updateSearchQuery(query: String) {
        _searchFilterState.value = _searchFilterState.value.copy(
            searchQuery = query,
            isSearchActive = query.isNotEmpty()
        )

    }

    fun clearSearch() {
        _searchFilterState.value = SearchFilterState()
    }


    /**
     * Get filtered tips from a specific list based on search query
     */
    fun updateFilteredTipsList(listId: Int): Flow<List<Tip>> {
        val state = _searchFilterState.value

       return if (state.searchQuery.isNotEmpty()) {
           localRepository.searchTipsInList(listId, state.searchQuery, currentUserId)
        } else {
           localRepository.getAllTipsFromList(listId, currentUserId)
        }
    }

    /**
     * Get filtered lists based on search query
     */
    fun updateFilteredLists(): Flow<List<TipList>> {
        val state = _searchFilterState.value

        return if (state.searchQuery.isNotEmpty()) {
            localRepository.searchLists(state.searchQuery, currentUserId)
        } else {
            localRepository.getAllLists(currentUserId)
        }
    }
}
