package com.sanaa.presentation.bottomsheets.deletebottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlist.SnackData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class DeleteListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk()

    private lateinit var viewModel: DeleteListViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val failureMessage = "Failed to delete list."

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { stringProvider.deleteListFailed } returns failureMessage
        initViewModel()
    }

    @Test
    fun `onDeleteConfirmed on success deletes list and emits Dismiss effect`() = runTest {
        val listId = 123L
        coEvery { manageSavedListsUseCase.deleteSavedList(listId.toInt()) } returns Unit

        viewModel.effect.test {
            viewModel.onDeleteConfirmed(listId)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(DeleteListEffect.Dismiss)

            coVerify(exactly = 1) { manageSavedListsUseCase.deleteSavedList(listId.toInt()) }
            val state = viewModel.state.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.snackBarData).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteConfirmed on failure updates state with error snackbar and emits no effect`() =
        runTest {
            val listId = 456L
            val error = RuntimeException("API error")
            coEvery { manageSavedListsUseCase.deleteSavedList(listId.toInt()) } throws error

            viewModel.effect.test {
                viewModel.onDeleteConfirmed(listId)
                advanceUntilIdle()

                expectNoEvents()

                val state = viewModel.state.value
                assertThat(state.isLoading).isFalse()
                assertThat(state.snackBarData).isNotNull()
                assertThat(state.snackBarData?.message).isEqualTo(failureMessage)
                assertThat(state.snackBarData?.isError).isTrue()
            }
        }

    @Test
    fun `onSnackBarDismiss clears snackbar data from state`() = runTest {
        val listId = 789L
        coEvery { manageSavedListsUseCase.deleteSavedList(listId.toInt()) } throws RuntimeException()
        viewModel.onDeleteConfirmed(listId)
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNotNull()

        viewModel.onSnackBarDismiss()
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNull()
    }

    private fun initViewModel() {
        viewModel = DeleteListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }
}