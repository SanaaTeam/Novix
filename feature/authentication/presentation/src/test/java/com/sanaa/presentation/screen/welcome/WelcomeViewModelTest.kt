package com.sanaa.presentation.screen.welcome

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private lateinit var viewModel: WelcomeViewModel

    @BeforeEach
    fun setUp() {
        viewModel = WelcomeViewModel()
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
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
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
    fun `multiple effects are emitted in sequence`() = runTest {
        viewModel.onLoginClicked()
        viewModel.onContinueClicked()
        viewModel.onExit()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
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
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
            cancelAndIgnoreRemainingEvents()
        }

        // Trigger again and collect again
        viewModel.onContinueClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
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
        val customViewModel = WelcomeViewModel()

        customViewModel.onLoginClicked()
        customViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `viewModel uses default dispatcher when none provided`() = runTest {
        val defaultViewModel = WelcomeViewModel()

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
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ContinueAsGuest)
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
        val customViewModel = WelcomeViewModel()

        customViewModel.onExit()
        customViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WelcomeScreenEffects.ExitApp)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
