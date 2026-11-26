package com.exercise.matipv2.ui

import com.exercise.matipv2.data.local.model.TipList
import com.exercise.matipv2.data.model.AuthUser
import com.exercise.matipv2.fakes.FakeAuthRepository
import com.exercise.matipv2.fakes.FakeLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    private val localRepository = FakeLocalRepository()
    private val signedInUser = AuthUser(id = "user-1", name = "Rafael", email = null)

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(user: AuthUser? = null) =
        MainScreenViewModel(localRepository, FakeAuthRepository(user))

    @Test
    fun insertTip_savesValuesFromBeforeTheScreenIsReset() {
        val viewModel = createViewModel()
        viewModel.updateTipPercent("15")
        viewModel.updateListId(3)

        viewModel.insertTip()
        viewModel.resetCalculateTipScreen()

        val savedTip = localRepository.tips.value.single()
        assertEquals("15", savedTip.tipPercent)
        assertEquals(3, savedTip.listId)
    }

    @Test
    fun insertTip_belongsToTheSignedInUser() {
        val viewModel = createViewModel(user = signedInUser)

        viewModel.insertTip()

        assertEquals("user-1", localRepository.tips.value.single().userId)
    }

    @Test
    fun insertTip_isGuestDataWhenNobodyIsSignedIn() {
        val viewModel = createViewModel()

        viewModel.insertTip()

        assertNull(localRepository.tips.value.single().userId)
    }

    @Test
    fun decreaseCounter_neverGoesBelowZero() {
        val viewModel = createViewModel()

        repeat(times = 5) { viewModel.decreaseCounter() }

        assertEquals(0, viewModel.uiState.value.splitShare)
    }

    @Test
    fun resetCalculateTipScreen_clearsWhatWasTyped() {
        val viewModel = createViewModel()
        viewModel.updateTipAmount("45")
        viewModel.updateTipPercent("20")
        viewModel.increaseCounter()

        viewModel.resetCalculateTipScreen()

        val state = viewModel.uiState.value
        assertEquals("", state.tipAmount)
        assertEquals("", state.tipPercent)
        assertEquals(1, state.splitShare)
    }

    @Test
    fun userLists_areTheSignedInUsersListsAndFollowSignIn() = runTest {
        localRepository.lists.value = listOf(
            TipList(id = 1, name = "Guest list"),
            TipList(id = 2, name = "Mine", userId = "user-1"),
        )
        val authRepository = FakeAuthRepository()
        val viewModel = MainScreenViewModel(localRepository, authRepository)
        // The lists are only collected while someone is looking at them, like a screen does.
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.userLists.collect {} }

        assertEquals(listOf("Guest list"), viewModel.userLists.value.map { it.name })

        authRepository.setUser(signedInUser)

        assertEquals(listOf("Mine"), viewModel.userLists.value.map { it.name })
    }
}
