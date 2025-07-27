package com.sanaa.presentation.screen.login_base

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.advanceUntilIdle
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
    fun `initial state is set correctly`() = runTest(testDispatcher) {
        val initialState = TestState(value = "initial")
        val viewModel = TestBaseViewModel(testDispatcher, initialState)
        assertThat(viewModel.state.value).isEqualTo(initialState)
    }

    @Test
    fun `updateState updates the state correctly`() = runTest(testDispatcher) {
        val newValue = "updated"
        testViewModel.updateTestState { it.copy(value = newValue) }
        advanceUntilIdle()
        assertThat(testViewModel.state.value.value).isEqualTo(newValue)
    }

    @Test
    fun `tryToExecute with success calls onSuccess callback`() = runTest(testDispatcher) {
        var successCalled = false
        var errorCalled = false
        val expectedResult = "success"

        testViewModel.executeTest(
            callee = { expectedResult },
            onSuccess = { result ->
                successCalled = true
                assertThat(result).isEqualTo(expectedResult)
            },
            onError = { errorCalled = true }
        )
        advanceUntilIdle()

        assertThat(successCalled).isTrue()
        assertThat(errorCalled).isFalse()
    }

    @Test
    fun `tryToExecute with exception calls onError callback`() = runTest(testDispatcher) {
        var successCalled = false
        var errorCalled = false
        val testException = RuntimeException("Test error")

        testViewModel.executeTest(
            callee = { throw testException },
            onSuccess = { successCalled = true },
            onError = { exception ->
                errorCalled = true
                assertThat(exception).isEqualTo(testException)
            }
        )
        advanceUntilIdle()

        assertThat(successCalled).isFalse()
        assertThat(errorCalled).isTrue()
    }

    @Test
    fun `tryToExecute without callbacks handles success gracefully`() = runTest(testDispatcher) {
        val expectedResult = "success"
        testViewModel.executeTest(callee = { expectedResult })
        advanceUntilIdle()
    }

    @Test
    fun `tryToExecute without callbacks handles error gracefully`() = runTest(testDispatcher) {
        val testException = RuntimeException("Test error")
        testViewModel.executeTest(callee = { throw testException })
        advanceUntilIdle()
    }

    @Test
    fun `emitEffect emits effect correctly`() = runTest(testDispatcher) {
        val testEffect = TestEffect.ShowMessage("test message")
        testViewModel.emitTestEffect(testEffect)

        testViewModel.effect.test {
            val emittedEffect = awaitItem()
            assertThat(emittedEffect).isEqualTo(testEffect)
        }
    }

    @Test
    fun `multiple state updates work correctly`() = runTest(testDispatcher) {
        val updates = listOf("update1", "update2", "update3")

        updates.forEach { update ->
            testViewModel.updateTestState { it.copy(value = update) }
        }
        advanceUntilIdle()

        assertThat(testViewModel.state.value.value).isEqualTo(updates.last())
    }

    @Test
    fun `multiple effects are emitted correctly`() = runTest(testDispatcher) {
        val effects = listOf(
            TestEffect.ShowMessage("message1"),
            TestEffect.ShowMessage("message2"),
            TestEffect.ShowMessage("message3")
        )

        effects.forEach { effect ->
            testViewModel.emitTestEffect(effect)
        }

        testViewModel.effect.test {
            effects.forEach { expectedEffect ->
                val emittedEffect = awaitItem()
                assertThat(emittedEffect).isEqualTo(expectedEffect)
            }
        }
    }

    // ------- Helper classes -------

    data class TestState(val value: String = "default")

    sealed class TestEffect {
        data class ShowMessage(val message: String) : TestEffect()
    }

    private class TestBaseViewModel(
        dispatcher: CoroutineDispatcher,
        initialState: TestState = TestState()
    ) : BaseViewModel<TestState, TestEffect>(initialState, dispatcher) {

        fun updateTestState(updater: (TestState) -> TestState) {
            updateState(updater)
        }

        fun <T> executeTest(
            callee: suspend () -> T,
            onSuccess: (T) -> Unit = {},
            onError: (exception: Throwable) -> Unit = {},
            dispatcher: CoroutineDispatcher = defaultDispatcher,
        ) {
            tryToExecute(callee, onSuccess, onError, dispatcher)
        }

        fun emitTestEffect(effect: TestEffect) {
            emitEffect(effect)
        }
    }
}
