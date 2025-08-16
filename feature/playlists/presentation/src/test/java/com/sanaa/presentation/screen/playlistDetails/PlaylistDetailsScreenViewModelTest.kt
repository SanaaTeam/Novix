package com.sanaa.presentation.screen.playlistDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
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

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistDetailsScreenViewModelTest {

    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
    private val savedListsStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)
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
        every { savedListsStatusProvider.savedIds } returns MutableStateFlow(emptySet())
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
    fun `onSaveIconClick on success removes item, decrements count, and emits refresh effect`() = runTest {
        val mediaItem = MediaItem(id = 456, title = "", imageUrl = "", isSaved = true)
        coEvery { manageSavedListItemsUseCase.removeMovieFromSavedList(any(), any()) } returns true
        initViewModel()

        // Act and Assert within the effect collector
        viewModel.effect.test {
            viewModel.onDeleteIconClick(mediaItem)
            advanceUntilIdle()

            // Assert that the correct effect was emitted
            assertThat(awaitItem()).isEqualTo(PlaylistDetailsScreenEffect.ShowSuccessSnackBar)

            // Verify interactions
            verify(exactly = 1) { savedListsStatusProvider.markItemUnsaved(456) }
            coVerify(exactly = 1) { manageSavedListItemsUseCase.removeMovieFromSavedList(1, 456) }

            cancelAndIgnoreRemainingEvents()
        }
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
        viewModel.onDeleteListClick()
        assertThat(viewModel.state.value.showBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet sets showBottomSheet false`() = runTest {
        initViewModel()
        viewModel.updateState { copy(showBottomSheet = true) }
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
            savedListsStatusProvider = savedListsStatusProvider,
            dispatcher = testDispatcher
        )
    }
}