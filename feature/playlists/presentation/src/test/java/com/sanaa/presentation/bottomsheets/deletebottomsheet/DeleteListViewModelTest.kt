package com.sanaa.presentation.bottomsheets.deletebottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.custom_list.ManageSavedListsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)

    private lateinit var viewModel: DeleteListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        initViewModel()
    }

    @Test
    fun `onDeleteConfirmed on success deletes list and emits success effect`() = runTest {
        val listId = 123L
        coEvery { manageSavedListsUseCase.deleteSavedList(listId.toInt()) } returns Unit

        viewModel.effect.test {
            viewModel.onDeleteConfirmed(listId)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(DeleteListEffect.DeleteSuccess)

            coVerify(exactly = 1) { manageSavedListsUseCase.deleteSavedList(listId.toInt()) }
            assertThat(viewModel.state.value.isLoading).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteConfirmed on failure updates state with error and emits failure effect`() =
        runTest {
            val listId = 456L
            val error = RuntimeException("API error")
            coEvery { manageSavedListsUseCase.deleteSavedList(listId.toInt()) } throws error

            viewModel.effect.test {
                viewModel.onDeleteConfirmed(listId)
                advanceUntilIdle()

                assertThat(awaitItem()).isEqualTo(DeleteListEffect.DeleteFailure)

                val state = viewModel.state.value
                assertThat(state.isLoading).isFalse()
                assertThat(state.errorMessage).isEqualTo("Failed to delete list. Please try again.")

                cancelAndIgnoreRemainingEvents()
            }
        }

    private fun initViewModel() {
        viewModel = DeleteListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            dispatcher = testDispatcher
        )
    }
}