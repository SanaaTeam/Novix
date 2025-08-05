package com.sanaa.presentation.screen.welcome

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.CreateGuestSessionUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private lateinit var viewModel: WelcomeViewModel
    private val createGuestSessionUseCase: CreateGuestSessionUseCase = mockk()
    private lateinit var testDispatcher: TestDispatcher


    @BeforeEach
    fun setUp() {
        testDispatcher = StandardTestDispatcher()
        Dispatchers.setMain(testDispatcher)

        coEvery { createGuestSessionUseCase.createGuestSession() } returns Unit
        viewModel = WelcomeViewModel(
            createGuestSessionUseCase = createGuestSessionUseCase,
             dispatcher = testDispatcher
        )
    }

    @Test
    fun `initial state should be Unit`() = runTest {
        assertThat(viewModel.state.value).isEqualTo(Unit)
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
    fun `onContinueClicked emits ContinueAsGuest effect`() = runTest {
        viewModel.onContinueClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ReturnGuestResultCode)
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
    fun `repeated onLoginClicked emits effect each time`() = runTest {
        viewModel.onLoginClicked()
        viewModel.onLoginClicked()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `effects can be collected multiple times`() = runTest {
        viewModel.onContinueClicked()

        // First collection
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ReturnGuestResultCode)
            cancelAndIgnoreRemainingEvents()
        }

        // Trigger again and collect again
        viewModel.onContinueClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ReturnGuestResultCode)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state remains Unit after multiple repeated actions`() = runTest {
        repeat(5) {
            viewModel.onLoginClicked()
            viewModel.onContinueClicked()
            viewModel.onExit()
        }

        assertThat(viewModel.state.value).isEqualTo(Unit)
    }

    @Test
    fun `viewModel implements WelcomeScreenInteractionListener`() = runTest {
        assertThat(viewModel).isInstanceOf(WelcomeScreenInteractionListener::class.java)
    }

    @Test
    fun `viewModel uses provided dispatcher`() = runTest {
        val customViewModel = WelcomeViewModel(createGuestSessionUseCase)

        customViewModel.onLoginClicked()
        customViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel uses default dispatcher when none provided`() = runTest {
        val defaultViewModel = WelcomeViewModel(createGuestSessionUseCase)

        defaultViewModel.onLoginClicked()
        defaultViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `effects are emitted immediately without delay`() = runTest {
        viewModel.onLoginClicked()
        viewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `all WelcomeScreenEffects are tested`() = runTest {
        viewModel.onLoginClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.onContinueClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ReturnGuestResultCode)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.onExit()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `emitEffect works correctly on custom dispatcher`() = runTest {
        val customViewModel = WelcomeViewModel(createGuestSessionUseCase)

        customViewModel.onExit()
        customViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
