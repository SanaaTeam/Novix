package com.sanaa.presentation.bottomsheets.addEditBookmark

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlist.SnackData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListsStatusProvider
import service.VodStringProvider
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class AddBookmarkListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val listsStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)
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
    fun `init calls refreshLists on the provider`() = runTest {
        initViewModel()
        advanceUntilIdle()

        coVerify(exactly = 1) { listsStatusProvider.refreshLists() }
    }

    @Test
    fun `onAddClicked on success updates state with success snackbar and emits Dismiss effect`() =
        runTest {
            val listTitle = "My New List"
            val mediaId = 123
            val fakeSavedList = SavedList(id = 1, title = listTitle, itemCount = 1)

            coEvery { manageSavedListsUseCase.createSavedList(listTitle) } returns fakeSavedList
            initViewModel()
            viewModel.onListTitleChanged(listTitle)

            viewModel.effect.test {
                viewModel.onAddClicked(mediaId)
                advanceUntilIdle()

                assertThat(awaitItem()).isEqualTo(AddBookMarksEffect.Dismiss)

                coVerify(exactly = 1) { manageSavedListsUseCase.createSavedList(listTitle) }
                verify(exactly = 1) { listsStatusProvider.markItemSaved(mediaId) }
                verify(exactly = 1) { listsStatusProvider.addList(fakeSavedList) }
                coVerify(exactly = 2) { listsStatusProvider.refreshLists() }

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
            viewModel.onAddClicked(456)
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
        viewModel.onAddClicked(1)
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNotNull()

        viewModel.onSnackBarDismiss()
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNull()
    }

    private fun initViewModel() {
        viewModel = AddBookmarkListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            listsStatusProvider = listsStatusProvider,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }
}