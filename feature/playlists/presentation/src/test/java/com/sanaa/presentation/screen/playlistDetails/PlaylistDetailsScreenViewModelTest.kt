package com.sanaa.presentation.screen.playlistDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.playlistDetails.state.MediaItem
import com.sanaa.presentation.screen.playlistDetails.state.MediaTypeUi
import exceptions.NoNetworkException
import io.mockk.coEvery
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
import usecase.MangeUserPreferenceUseCase
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.ManageSavedListsUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class PlaylistDetailsScreenViewModelTest {

    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
    private val manageSavedListsUseCase: ManageSavedListsUseCase = mockk(relaxed = true)
    private val mangeUserPreferenceUseCase: MangeUserPreferenceUseCase = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk(relaxed = true)
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
    fun `onMediaClick emits NavigateToMediaDetails effect`() = runTest {
        initViewModel()
        val mediaId = 123
        val mediaType = MediaTypeUi.MOVIE

        viewModel.onMediaClick(mediaId, mediaType)

        viewModel.effect.test {
            val expectedEffect =
                PlaylistDetailsScreenEffect.NavigateToMediaDetails(mediaId, mediaType)
            assertThat(awaitItem()).isEqualTo(expectedEffect)
            cancelAndIgnoreRemainingEvents()
        }
    }



    @Test
    fun `onDeleteListClick on failure with general error updates error message`() = runTest {
        val mediaItem = MediaItem(id = 456, title = "", imageUrl = "", isSaved = true)
        val errorMessage = "Database Error"
        val error = RuntimeException(errorMessage)
        coEvery { manageSavedListItemsUseCase.removeMovieFromSavedList(any(), any()) } throws error

        initViewModel()

        viewModel.onDeleteListClick()
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `onDeleteListClick on failure with NoNetworkException sets error message to null`() =
        runTest {
            val error = NoNetworkException()
            coEvery {
                manageSavedListItemsUseCase.removeMovieFromSavedList(
                    any(),
                    any()
                )
            } throws error
            initViewModel()

            viewModel.onDeleteListClick()
            advanceUntilIdle()

            val state = viewModel.state.value
            assertThat(state.isLoading).isFalse()
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
        assertThat(viewModel.state.value.showListDeletionConfirmationBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet sets showBottomSheet false`() = runTest {
        initViewModel()
        viewModel.updateState { copy(showListDeletionConfirmationBottomSheet = true) }
        viewModel.onDismissConfirmationBottomSheet()
        assertThat(viewModel.state.value.showListDeletionConfirmationBottomSheet).isFalse()
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
            manageSavedListsUseCase= manageSavedListsUseCase,
            manageUserPreferenceUseCase = mangeUserPreferenceUseCase,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }
}