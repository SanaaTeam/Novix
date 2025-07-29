package com.sanaa.presentation.screen.login_base

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
class BaseViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var testViewModel: TestBaseViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        testViewModel = TestBaseViewModel(testDispatcher)
    }

    @Test
    fun `initial state is set correctly`() = runTest {
        // Then
        assertThat(testViewModel.state.value).isEqualTo("initial")
    }

    @Test
    fun `updateState updates state correctly`() = runTest {
        // When
        testViewModel.updateTestState("updated")

        // Then
        assertThat(testViewModel.state.value).isEqualTo("updated")
    }

    @Test
    fun `emitEffect emits effect correctly`() = runTest {
        // When
        testViewModel.emitTestEffect("test_effect")

        // Then
        testViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo("test_effect")
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `multiple effects are emitted in sequence`() = runTest {
        // When
        testViewModel.emitTestEffect("effect1")
        testViewModel.emitTestEffect("effect2")
        testViewModel.emitTestEffect("effect3")

        // Then
        testViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo("effect1")
            assertThat(awaitItem()).isEqualTo("effect2")
            assertThat(awaitItem()).isEqualTo("effect3")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `state updates are applied correctly`() = runTest {
        // When
        testViewModel.updateTestState("first")
        testViewModel.updateTestState("second")
        testViewModel.updateTestState("third")

        // Then
        assertThat(testViewModel.state.value).isEqualTo("third")
    }

    @Test
    fun `viewModel uses default dispatcher when none provided`() = runTest {
        // Given
        val defaultViewModel = TestBaseViewModel()

        // When
        defaultViewModel.emitTestEffect("test")

        // Then
        defaultViewModel.effect.test {
            assertThat(awaitItem()).isEqualTo("test")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `tryToExecute without callbacks works correctly`() = runTest {
        // When & Then - Should not throw
        testViewModel.executeTestWithoutCallbacks { "success" }
    }


    // Test implementation of BaseViewModel
    private class TestBaseViewModel(
        dispatcher: kotlinx.coroutines.CoroutineDispatcher = Dispatchers.IO
    ) : BaseViewModel<String, String>("initial", dispatcher) {

        fun updateTestState(newState: String) {
            updateState { newState }
        }

        fun emitTestEffect(effect: String) {
            emitEffect(effect)
        }

        fun executeTest(
            callee: suspend () -> String,
            onSuccess: (String) -> Unit = {},
            onError: (Throwable) -> Unit = {}
        ) {
            tryToExecute(callee, onSuccess, onError)
        }

        fun executeTestWithCustomDispatcher(
            callee: suspend () -> String,
            onSuccess: (String) -> Unit = {},
            dispatcher: kotlinx.coroutines.CoroutineDispatcher
        ) {
            tryToExecute(callee, onSuccess, dispatcher = dispatcher)
        }

        fun executeTestWithoutCallbacks(callee: suspend () -> String) {
            tryToExecute(callee)
        }

        fun executeTestWithOnlySuccess(
            callee: suspend () -> String,
            onSuccess: (String) -> Unit
        ) {
            tryToExecute(callee, onSuccess)
        }

        fun executeTestWithOnlyError(
            callee: suspend () -> String,
            onError: (Throwable) -> Unit
        ) {
            tryToExecute(callee, onError = onError)
        }
    }
}
