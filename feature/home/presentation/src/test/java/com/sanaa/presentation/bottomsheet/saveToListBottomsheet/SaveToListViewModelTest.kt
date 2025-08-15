package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListsStatusProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class SaveToListViewModelTest {

    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
    private val listsStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)

    private lateinit var viewModel: SaveToListViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakePlaylistsFlow = MutableStateFlow<List<SavedList>>(emptyList())

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { listsStatusProvider.savedLists } returns fakePlaylistsFlow
    }

    @Test
    fun `init on success loads playlists and updates state`() = runTest {
        initViewModel()
        val fakeLists = listOf(SavedList(id = 1, title = "Favorites", itemCount = 10))

        fakePlaylistsFlow.value = fakeLists
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.playlists).hasSize(1)
        assertThat(state.playlists.first().title).isEqualTo("Favorites")
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `onPlaylistSelected updates state correctly`() {
        initViewModel()

        viewModel.onPlaylistSelected(123L)

        val state = viewModel.state.value
        assertThat(state.selectedListsIds).isEqualTo(123L)
        assertThat(state.isAddButtonEnabled).isTrue()
    }

    @Test
    fun `onAddClicked on success adds item, refreshes, marks saved, and emits success effect`() = runTest {
//        coEvery { manageSavedListItemsUseCase.addMovieToSavedList(any(), any()) } returns true //todo
        initViewModel()
        viewModel.onPlaylistSelected(1L)

        viewModel.effect.test {
            viewModel.onAddClicked(mediaId = 789L)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(SaveToListEffect.AddedSuccessfully)

            coVerify(exactly = 1) { manageSavedListItemsUseCase.addMovieToSavedList(1, 789) }
            verify(exactly = 1) { listsStatusProvider.markItemSaved(789) }
            coVerify(exactly = 1) { listsStatusProvider.refreshLists() }

            assertThat(viewModel.state.value.isLoading).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddClicked on failure updates error state and emits failure effect`() = runTest {
        coEvery { manageSavedListItemsUseCase.addMovieToSavedList(any(), any()) } throws RuntimeException("API Error")
        initViewModel()
        viewModel.onPlaylistSelected(1L)

        viewModel.onAddClicked(mediaId = 789L)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isTrue()
        assertThat(state.errorMessage).isEqualTo(null)
    }

    private fun initViewModel() {
        viewModel = SaveToListViewModel(
            manageSavedListItemsUseCase = manageSavedListItemsUseCase,
            dispatcher = testDispatcher,
            mangeSavedListsUseCase = mockk(),//todo
        )
    }
}