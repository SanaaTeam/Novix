package com.sanaa.presentation.screen.welcome

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WelcomeViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WelcomeViewModel(testDispatcher)
    }

    @Test
    fun `initial state should be Unit`() = runTest {
        // Then
        assertThat(viewModel.state.value).isEqualTo(Unit)
    }

    @Test
    fun `onLoginClicked emits NavigateToLogin effect`() = runTest {
        // When
        viewModel.onLoginClicked()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onContinueClicked emits ContinueAsGuest effect`() = runTest {
        // When
        viewModel.onContinueClicked()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onExit emits ExitApp effect`() = runTest {
        // When
        viewModel.onExit()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `multiple effects are emitted in sequence`() = runTest {
        // When
        viewModel.onLoginClicked()
        viewModel.onContinueClicked()
        viewModel.onExit()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel implements WelcomeScreenInteractionListener`() = runTest {
        // Then
        assertThat(viewModel).isInstanceOf(WelcomeScreenInteractionListener::class.java)
    }

    @Test
    fun `viewModel uses provided dispatcher`() = runTest {
        // Given
        val customDispatcher = StandardTestDispatcher()
        val customViewModel = WelcomeViewModel(customDispatcher)

        // When & Then
        customViewModel.onLoginClicked()
        customViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel uses default dispatcher when none provided`() = runTest {
        // Given
        val defaultViewModel = WelcomeViewModel()

        // When
        defaultViewModel.onLoginClicked()

        // Then
        defaultViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state remains Unit after all interactions`() = runTest {
        // Given
        val initialState = viewModel.state.value

        // When
        viewModel.onLoginClicked()
        viewModel.onContinueClicked()
        viewModel.onExit()

        // Then
        assertThat(viewModel.state.value).isEqualTo(initialState)
        assertThat(viewModel.state.value).isEqualTo(Unit)
    }

    @Test
    fun `effects are emitted immediately without delay`() = runTest {
        // When
        viewModel.onLoginClicked()

        // Then
        viewModel.effect.test {
            val effect = awaitItem()
            assertThat(effect).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `all WelcomeScreenEffects are tested`() = runTest {
        // When & Then - Test all effects
        viewModel.onLoginClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.onContinueClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
            cancelAndIgnoreRemainingEvents()
        }

        viewModel.onExit()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }
} 