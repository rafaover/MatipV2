package com.exercise.matipv2.ui

import com.exercise.matipv2.fakes.FakeAuthRepository
import com.exercise.matipv2.fakes.FakeBackupRepository
import com.exercise.matipv2.fakes.FakeLocalRepository
import com.exercise.matipv2.ui.auth.AuthViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val authRepository = FakeAuthRepository()
    private val errorMessage = "The email address is already in use by another account."

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() =
        AuthViewModel(authRepository, FakeLocalRepository(), FakeBackupRepository())

    @Test
    fun signUpWithEmail_failure_exposesTheErrorForTheDialog() {
        authRepository.emailResult = Result.failure(Exception(errorMessage))
        val viewModel = createViewModel()

        viewModel.signUpWithEmail("a@b.com", "password123")

        assertEquals(errorMessage, viewModel.authErrorMessage)
    }

    @Test
    fun signInWithEmail_failure_exposesTheErrorForTheDialog() {
        authRepository.emailResult = Result.failure(Exception(errorMessage))
        val viewModel = createViewModel()

        viewModel.signInWithEmail("a@b.com", "password123")

        assertEquals(errorMessage, viewModel.authErrorMessage)
    }

    @Test
    fun newAttempt_clearsThePreviousError() {
        authRepository.emailResult = Result.failure(Exception(errorMessage))
        val viewModel = createViewModel()
        viewModel.signUpWithEmail("a@b.com", "password123")

        authRepository.emailResult = Result.success(Unit)
        viewModel.signUpWithEmail("a@b.com", "password123")

        assertNull(viewModel.authErrorMessage)
    }

    @Test
    fun closingTheDialog_clearsTheError() {
        authRepository.emailResult = Result.failure(Exception(errorMessage))
        val viewModel = createViewModel()
        viewModel.signUpWithEmail("a@b.com", "password123")

        viewModel.updateShowAuthDialog(false)

        assertNull(viewModel.authErrorMessage)
    }

    @Test
    fun clearAuthError_removesTheError() {
        authRepository.emailResult = Result.failure(Exception(errorMessage))
        val viewModel = createViewModel()
        viewModel.signInWithEmail("a@b.com", "password123")

        viewModel.clearAuthError()

        assertNull(viewModel.authErrorMessage)
    }
}
