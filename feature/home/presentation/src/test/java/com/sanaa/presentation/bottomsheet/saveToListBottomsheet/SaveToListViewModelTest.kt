package com.sanaa.presentation.bottomsheet.saveToListBottomsheet

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.components.SnackData
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
import service.VodStringProvider
import usecase.custom_list.ManageSavedListItemsUseCase
import usecase.custom_list.custom_list_param.SavedList

@OptIn(ExperimentalCoroutinesApi::class)
class SaveToListViewModelTest {

    private val manageSavedListItemsUseCase: ManageSavedListItemsUseCase = mockk(relaxed = true)
    private val listsStatusProvider: SavedListsStatusProvider = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk()

    private lateinit var viewModel: SaveToListViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakePlaylistsFlow = MutableStateFlow<List<SavedList>>(emptyList())
    private val successMessage = "Added successfully"
    private val failureMessage = "Failed to add"

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        every { listsStatusProvider.savedLists } returns fakePlaylistsFlow
        every { stringProvider.addToListSuccess } returns successMessage
        every { stringProvider.addToListFailed } returns failureMessage
    }

    @Test
    fun `init observes playlists and updates state`() = runTest {
        initViewModel()
        val fakeLists = listOf(SavedList(id = 1, title = "Favorites", itemCount = 10))

        fakePlaylistsFlow.value = fakeLists
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.playlists).hasSize(1)
        assertThat(state.playlists.first().title).isEqualTo("Favorites")
    }

    @Test
    fun `onPlaylistSelected updates state correctly`() = runTest {
        initViewModel()

        viewModel.onPlaylistSelected(123L)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertThat(state.selectedListId).isEqualTo(123L)
        assertThat(state.isAddButtonEnabled).isTrue()
    }

    @Test
    fun `onAddClicked on success updates state with snackbar and emits Dismiss effect`() = runTest {
        coEvery { manageSavedListItemsUseCase.addMovieToSavedList(any(), any()) } returns true
        initViewModel()
        viewModel.onPlaylistSelected(1L)

        viewModel.effect.test {
            viewModel.onAddClicked(mediaId = 789L)
            advanceUntilIdle()

            assertThat(awaitItem()).isEqualTo(SaveToListEffect.Dismiss)

            coVerify(exactly = 1) { manageSavedListItemsUseCase.addMovieToSavedList(1, 789) }
            verify(exactly = 1) { listsStatusProvider.markItemSaved(789) }
            coVerify(exactly = 1) { listsStatusProvider.refreshLists() }

            val state = viewModel.state.value
            assertThat(state.isLoading).isFalse()
            assertThat(state.snackBarData).isNotNull()
            assertThat(state.snackBarData?.message).isEqualTo(successMessage)
            assertThat(state.snackBarData?.isError).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAddClicked on failure updates state with error snackbar and emits no effect`() = runTest {
        coEvery {
            manageSavedListItemsUseCase.addMovieToSavedList(
                any(),
                any()
            )
        } throws RuntimeException("API Error")
        initViewModel()
        viewModel.onPlaylistSelected(1L)

        viewModel.effect.test {
            viewModel.onAddClicked(mediaId = 789L)
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
        coEvery {
            manageSavedListItemsUseCase.addMovieToSavedList(any(), any())
        } throws RuntimeException("API Error")
        initViewModel()
        viewModel.onPlaylistSelected(1L)
        viewModel.onAddClicked(mediaId = 789L)
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNotNull()

        viewModel.onSnackBarDismiss()
        advanceUntilIdle()

        assertThat(viewModel.state.value.snackBarData).isNull()
    }

    private fun initViewModel() {
        viewModel = SaveToListViewModel(
            manageSavedListItemsUseCase = manageSavedListItemsUseCase,
            listsStatusProvider = listsStatusProvider,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }
}