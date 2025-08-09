package com.sanaa.presentation.bottomsheets.saveToListBottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
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
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class SaveToListViewModelTest {

    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
    private val savedListsStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)

    private lateinit var viewModel: SaveToListViewModel
    private val testDispatcher = StandardTestDispatcher()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `init on success loads playlists and updates state`() = runTest {
        val fakeLists = listOf(SavedList(id = 1, title = "Favorites", itemCount = 10))
        coEvery { manageSavedListsUseCase.getSavedLists() } returns fakeLists

        initViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.playlists).hasSize(1)
        assertThat(state.playlists.first().title).isEqualTo("Favorites")
    }

    @Test
    fun `init on failure updates state with error message`() = runTest {
        coEvery { manageSavedListsUseCase.getSavedLists() } throws RuntimeException("Network Error")

        initViewModel()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo("Failed to load lists.")
    }

    @Test
    fun `onPlaylistSelected updates state correctly`() {
        initViewModel()

        viewModel.onPlaylistSelected(123L)

        val state = viewModel.state.value
        assertThat(state.selectedListId).isEqualTo(123L)
        assertThat(state.isAddButtonEnabled).isTrue()
    }

    @Test
    fun `onAddClicked on success adds item, reloads, marks saved, and emits success effect`() = runTest {
        initViewModel()
        viewModel.onPlaylistSelected(1L)
        coEvery { manageSavedListItemsUseCase.addMovieToSavedList(any(), any()) } returns true

        viewModel.effect.test {
            viewModel.onAddClicked(mediaId = 789L)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(SaveToListEffect.AddedSuccessfully)

            coVerify(exactly = 1) { manageSavedListItemsUseCase.addMovieToSavedList(1, 789) }
            verify(exactly = 1) { savedListsStatusProvider.markItemSaved(789) }
            coVerify(exactly = 2) { manageSavedListsUseCase.getSavedLists() } // Once in init, once in reload

            assertThat(viewModel.state.value.isLoading).isFalse()
        }
    }

    @Test
    fun `onAddClicked on failure updates error state and emits failure effect`() = runTest {
        initViewModel()
        viewModel.onPlaylistSelected(1L)
        coEvery { manageSavedListItemsUseCase.addMovieToSavedList(any(), any()) } throws RuntimeException("API Error")

        viewModel.effect.test {
            viewModel.onAddClicked(mediaId = 789L)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(SaveToListEffect.FailedToAdd)

            val state = viewModel.state.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.errorMessage).isEqualTo("Failed to add item to list.")
        }
    }

    private fun initViewModel() {
        viewModel = SaveToListViewModel(
            manageSavedListsUseCase = manageSavedListsUseCase,
            manageSavedListItemsUseCase = manageSavedListItemsUseCase,
            savedListsStatusProvider = savedListsStatusProvider,
            dispatcher = testDispatcher
        )
    }
}