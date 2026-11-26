package com.exercise.matipv2.ui

import com.exercise.matipv2.data.local.model.Tip
import com.exercise.matipv2.data.repository.AuthRepository
import com.exercise.matipv2.data.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainScreenViewModelTest {

    private val localRepository: LocalRepository = mock()
    private val authRepository: AuthRepository = mock {
        on { currentUser } doReturn MutableStateFlow(null)
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun insertTip_savesValuesFromBeforeTheScreenIsReset() = runTest {
        val viewModel = MainScreenViewModel(localRepository, authRepository)
        viewModel.updateTipPercent("15")
        viewModel.updateListId(3)

        viewModel.insertTip()
        viewModel.resetCalculateTipScreen()

        val savedTip = argumentCaptor<Tip>()
        verify(localRepository).insertTip(savedTip.capture())
        assertEquals("15", savedTip.firstValue.tipPercent)
        assertEquals(3, savedTip.firstValue.listId)
    }
}
