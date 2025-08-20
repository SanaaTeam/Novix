package com.sanaa.presentation.screen.welcome

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import exceptions.NoInternetException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.IdentityStringProvider
import usecase.CreateGuestSessionUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {
    @MockK
    private lateinit var createGuestSessionUseCase: CreateGuestSessionUseCase

    @MockK
    private lateinit var stringProvider: IdentityStringProvider
    private lateinit var viewModel: WelcomeViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)

        coEvery { stringProvider.noInternetConnectionError } returns "No internet connection error"
        coEvery { stringProvider.somethingWentWrongError } returns "Something went wrong"

        viewModel = WelcomeViewModel(
            createGuestSessionUseCase = createGuestSessionUseCase,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `onLoginClicked emits NavigateToLogin effect`() = runTest {
        viewModel.onLoginClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onExit emits ExitApp effect`() = runTest {
        viewModel.onExit()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onContinueClicked calls createGuestSession use case`() = runTest {
        // Given
        coEvery { createGuestSessionUseCase.createGuestSession() } returns Unit

        // When
        viewModel.onContinueClicked()
        advanceUntilIdle()

        // Then
        coVerify(exactly = 1) { createGuestSessionUseCase.createGuestSession() }
    }

    @Test
    fun `onContinueClickedFailed handles NoNetworkException correctly`() = runTest {
        // Given
        val noInternetError = "No internet connection error"
        coEvery { createGuestSessionUseCase.createGuestSession() } throws NoInternetException()
        coEvery { stringProvider.noInternetConnectionError } returns noInternetError

        // When
        viewModel.onContinueClicked()
        advanceUntilIdle()

        // Then
        assertThat(viewModel.state.value.snackBarData).isNotNull()
        assertThat(viewModel.state.value.snackBarData?.message).isEqualTo(noInternetError)
        assertThat(viewModel.state.value.snackBarData?.isError).isTrue()
    }
}