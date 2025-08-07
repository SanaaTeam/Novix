package com.sanaa.presentation.screen.bottomsheets.addEditBookmark

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.bottomsheets.addEditBookmark.AddBookmarkListViewModel
import io.mockk.coEvery
import io.mockk.coVerify
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
import repository.SavedMovieStatusProvider
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class AddBookmarkListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val savedMovieStatusProvider: SavedMovieStatusProvider = mockk(relaxed = true)

    private lateinit var viewModel: AddBookmarkListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        initViewModel()
    }

    @Test
    fun `onListTitleChanged with non-empty string updates state and enables button`() = runTest {
        viewModel.onListTitleChanged("New Playlist")

        val state = viewModel.state.value
        assertThat(state.listTitle).isEqualTo("New Playlist")
        assertThat(state.isAddButtonEnabled).isTrue()
    }

    @Test
    fun `onListTitleChanged with empty string updates state and disables button`() = runTest {
        viewModel.onListTitleChanged("An existing title")

        viewModel.onListTitleChanged("")

        val state = viewModel.state.value
        assertThat(state.listTitle).isEmpty()
        assertThat(state.isAddButtonEnabled).isFalse()
    }

    @Test
    fun `resetState clears title, loading, and error states`() = runTest {
        viewModel.updateState {
            it.copy(
                listTitle = "Some Title",
                isLoading = true,
                errorMessage = "Some Error"
            )
        }

        viewModel.resetState()

        val state = viewModel.state.value
        assertThat(state.listTitle).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `onAddClicked does nothing if button is not enabled`() = runTest {

        viewModel.onAddClicked(123)

        coVerify(exactly = 0) { manageSavedListsUseCase.createSavedList(any()) }
        verify(exactly = 0) { savedMovieStatusProvider.markSaved(any()) }
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onAddClicked on success creates list, resets state, marks saved, and emits effect`() =
        runTest {
            val listTitle = "My New List"
            val mediaId = 123
            viewModel.onListTitleChanged(listTitle)

            val fakeSavedList = SavedList(id = 1, title = listTitle, itemCount = 1)
            coEvery { manageSavedListsUseCase.createSavedList(listTitle) } returns fakeSavedList

            viewModel.effect.test {
                viewModel.onAddClicked(mediaId)

                assertThat(awaitItem()).isEqualTo(Unit)

                coVerify(exactly = 1) { manageSavedListsUseCase.createSavedList(listTitle) }
                verify(exactly = 1) { savedMovieStatusProvider.markSaved(mediaId) }

                val state = viewModel.state.value
                assertThat(state.listTitle).isEmpty()
                assertThat(state.isLoading).isFalse()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `onAddClicked on failure updates state with error message and does not emit effect`() =
        runTest {
            val listTitle = "Failing List"
            viewModel.onListTitleChanged(listTitle)

            val error = RuntimeException("Database error")
            coEvery { manageSavedListsUseCase.createSavedList(listTitle) } throws error

            viewModel.effect.test {
                viewModel.onAddClicked(456)

                advanceUntilIdle()

                val state = viewModel.state.value
                assertThat(state.isLoading).isFalse()
                assertThat(state.errorMessage).isEqualTo("Failed to create list. Please try again.")

                expectNoEvents()
            }
        }

    private fun initViewModel() {
        viewModel = AddBookmarkListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            savedMovieStatusProvider = savedMovieStatusProvider,
            dispatcher = testDispatcher
        )
    }
}