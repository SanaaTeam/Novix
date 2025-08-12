package com.sanaa.presentation


import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.OnboardingViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import app.cash.turbine.test


@OptIn(ExperimentalCoroutinesApi::class)
class OnboardingViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: OnboardingViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = OnboardingViewModel(dispatcher = testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should load onboarding pages and set currentPageIndex to 0`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.pageList).isNotEmpty()
            assertThat(initialState.pageList.size).isEqualTo(3)
            assertThat(initialState.currentPageIndex).isEqualTo(0)
            assertThat(initialState.isSkipAble).isFalse()
        }
    }

    @Test
    fun `onNextPageClick should increment currentPageIndex when not on the last page`() = runTest {
        viewModel.state.test {
            awaitItem()
            viewModel.onNextPageClick()
            val updatedState = awaitItem()
            assertThat(updatedState.currentPageIndex).isEqualTo(1)
        }
    }

    @Test
    fun `onNextPageClick on last page should call onSkipClick and set isSkipable to true`() = runTest {
        viewModel.state.test {
            awaitItem()
            viewModel.setCurrentPage(2)
            awaitItem()
            viewModel.onNextPageClick()
            val updatedState = awaitItem()
            assertThat(updatedState.isSkipAble).isTrue()
        }
    }

    @Test
    fun `onBackClick should decrement currentPageIndex when not on the first page`() = runTest {
        viewModel.state.test {
            awaitItem()

            viewModel.setCurrentPage(1)
            awaitItem()
            viewModel.onBackClick()
            val updatedState = awaitItem()
            assertThat(updatedState.currentPageIndex).isEqualTo(0)
        }
    }

    @Test
    fun `onBackClick on first page should not change currentPageIndex`() = runTest {
        viewModel.state.test {
            val initialState = awaitItem()
            val initialIndex = initialState.currentPageIndex
            viewModel.onBackClick()
            val currentState = viewModel.state.value
            assertThat(currentState.currentPageIndex).isEqualTo(initialIndex)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSkipClick should set isSkipable to true`() = runTest {
        viewModel.state.test {
            awaitItem()
            viewModel.onSkipClick()
            val updatedState = awaitItem()
            assertThat(updatedState.isSkipAble).isTrue()
        }
    }

    @Test
    fun `setCurrentPage should update currentPageIndex to the provided value`() = runTest {
        viewModel.state.test {
            awaitItem()
            viewModel.setCurrentPage(2)
            val updatedState = awaitItem()
            assertThat(updatedState.currentPageIndex).isEqualTo(2)
        }
    }
}