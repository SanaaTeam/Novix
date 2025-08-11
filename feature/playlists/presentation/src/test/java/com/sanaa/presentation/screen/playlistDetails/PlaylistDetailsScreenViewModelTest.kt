package com.sanaa.presentation.screen.playlistDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import com.sanaa.presentation.screen.playlistDetails.state.SavedDetailsScreenUiState
import exceptions.NoNetworkException
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
import usecase.custom_list.ManageSavedListItemsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistDetailsScreenViewModelTest {

    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: PlaylistDetailsScreenViewModel
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var savedStateHandle: SavedStateHandle

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        savedStateHandle = SavedStateHandle(
            mapOf(
                "listId" to 1,
                "title" to "My Playlist"
            )
        )
    }

    @Test
    fun `init loads items and sets initial state correctly`() = runTest {
        coEvery { manageSavedListItemsUseCase.getAllItemsInSavedList(any(), any()) } returns emptyList()

        initViewModel()

        viewModel.state.test {
            val initialState = awaitItem()
            assertThat(initialState.title).isNull()

            val finalState = awaitItem()

            assertThat(finalState.movieList).isNotEqualTo(SavedDetailsScreenUiState().movieList)
            assertThat(finalState.title).isEqualTo("My Playlist")
            assertThat(finalState.listId).isEqualTo(1)
            assertThat(finalState.isLoading).isFalse()

            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        initViewModel()
        val mediaId = 123
        val mediaType = MediaTypeUi.MOVIE

        viewModel.onMediaClick(mediaId, mediaType)

        viewModel.effect.test {
            val expectedEffect = PlaylistDetailsScreenEffect.NavigateToMediaDetails(mediaId, mediaType)
            assertThat(awaitItem()).isEqualTo(expectedEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSaveIconClick on success calls use case and reloads items`() = runTest {
        val mediaItem = MediaItem(id = 456, title = "", imageUrl = "", isSaved = true,)
        coEvery { manageSavedListItemsUseCase.removeMovieFromSavedList(any(), any()) } returns true
        initViewModel()

        viewModel.onSaveIconClick(mediaItem)
        advanceUntilIdle()

        coVerify(exactly = 1) { manageSavedListItemsUseCase.removeMovieFromSavedList(1, 456) }
        assertThat(viewModel.state.value.isLoading).isFalse()
    }

    @Test
    fun `onSaveIconClick on failure with general error updates error message`() = runTest {
        val mediaItem = MediaItem(id = 456, title = "", imageUrl = "", isSaved = true)
        val errorMessage = "Database Error"
        val error = RuntimeException(errorMessage)
        coEvery { manageSavedListItemsUseCase.removeMovieFromSavedList(any(), any()) } throws error

        initViewModel()

        viewModel.onSaveIconClick(mediaItem)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
    }

    @Test
    fun `onSaveIconClick on failure with NoNetworkException sets error message to null`() = runTest {
        val mediaItem = MediaItem(id = 456, title = "", imageUrl = "", isSaved = true)
        val error = NoNetworkException()
        coEvery { manageSavedListItemsUseCase.removeMovieFromSavedList(any(), any()) } throws error
        initViewModel()

        viewModel.onSaveIconClick(mediaItem)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `onBackClick emits NavigateBack`() = runTest {
        initViewModel()
        viewModel.onBackClick()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(PlaylistDetailsScreenEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onDeleteListClicked sets showBottomSheet true`() = runTest {
        initViewModel()
        viewModel.onDeleteListClicked()
        assertThat(viewModel.state.value.showBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet sets showBottomSheet false`() = runTest {
        initViewModel()
        viewModel.updateState { it.copy(showBottomSheet = true) }
        viewModel.onDismissBottomSheet()
        assertThat(viewModel.state.value.showBottomSheet).isFalse()
    }

    @Test
    fun `onListDeletedSuccessfully emits NavigateBackAfterDelete`() = runTest {
        initViewModel()
        viewModel.onListDeletedSuccessfully()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(PlaylistDetailsScreenEffect.NavigateBackAfterDelete)
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun initViewModel() {
        viewModel = PlaylistDetailsScreenViewModel(
            savedStateHandle = savedStateHandle,
            manageSavedListItemsUseCase = manageSavedListItemsUseCase,
            dispatcher = testDispatcher
        )
    }
}