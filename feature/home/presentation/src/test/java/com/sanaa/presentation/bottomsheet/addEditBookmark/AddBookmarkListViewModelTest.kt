package com.sanaa.presentation.bottomsheet.addEditBookmark

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
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
class AddBookmarkListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk()

    private lateinit var viewModel: AddBookmarkListViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val successMessage = "List created!"
    private val failureMessage = "Creation failed!"

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { stringProvider.createListSuccess } returns successMessage
        every { stringProvider.createListFailed } returns failureMessage
    }

    @Test
    fun `onAddClicked on success updates state with success snackbar and emits Dismiss effect`() =
        runTest {
            val listTitle = "My New List"

            coEvery { manageSavedListsUseCase.createSavedList(listTitle) } just runs
            initViewModel()
            viewModel.onListTitleChanged(listTitle)

            viewModel.effect.test {
                viewModel.onAddClicked()
                advanceUntilIdle()

                assertThat(awaitItem()).isEqualTo(AddBookmarkEffect.Dismiss)

                coVerify(exactly = 1) { manageSavedListsUseCase.createSavedList(listTitle) }

                val state = viewModel.state.value
                assertThat(state.listTitle).isEmpty()
                assertThat(state.isLoading).isFalse()

                assertThat(state.snackBarData).isNotNull()
                assertThat(state.snackBarData?.message).isEqualTo(successMessage)
                assertThat(state.snackBarData?.isError).isFalse()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onAddClicked on failure updates state with error snackbar and emits no effect`() = runTest {
        val listTitle = "Failing List"
        val error = RuntimeException("Database error")

        coEvery { manageSavedListsUseCase.createSavedList(listTitle) } throws error
        initViewModel()
        viewModel.onListTitleChanged(listTitle)

        viewModel.effect.test {
            viewModel.onAddClicked()
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
    fun `onSnackBarDismiss clears the snackbar data from state`() = runTest {
        initViewModel()
        coEvery { manageSavedListsUseCase.createSavedList(any()) } throws RuntimeException()
        viewModel.onListTitleChanged("test")
        viewModel.onAddClicked()
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNotNull()

        viewModel.onSnackBarDismiss()
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNull()
    }

    private fun initViewModel() {
        viewModel = AddBookmarkListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }
}