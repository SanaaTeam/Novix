package com.sanaa.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.mediaTabScreen.MediaTabScreenEffect
import com.sanaa.presentation.state.MediaItem
import com.sanaa.presentation.state.MediaTypeUi
import entity.Genre
import entity.MediaHistoryItem
import entity.User
import exceptions.NoLoggedInUserException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.history.ManageWatchingHistoryUseCase
import usecase.search.search_param.MediaType

@ExperimentalCoroutinesApi
class WatchingHistoryViewModelTest {

    private val manageWatchingHistoryUseCase: ManageWatchingHistoryUseCase = mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)

    private lateinit var viewModel: WatchingHistoryViewModel
    private val testDispatcher = StandardTestDispatcher()
    val dummyHistoryItems = listOf(dummyHistoryItem)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } returns dummyUser
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(any(), any()) } returns flowOf(dummyHistoryItems)
    }

    private fun initializeViewModel() {
        viewModel = WatchingHistoryViewModel(
            manageWatchingHistoryUseCase,
            getLoggedInUserUseCase,
            checkIfUserIsLoggedInUseCase
        )
    }

    @Test
    fun `init should load watching history and update state on success`() = runTest(testDispatcher) {
        // Given
        val historyItems = listOf(dummyHistoryItem.copy(id = 1), dummyHistoryItem.copy(id = 2))
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, null) } returns flowOf(historyItems)

        // When
        initializeViewModel()

        // Then
        viewModel.state.test {
            val successState = awaitItem().let {
                var current = it
                while (current.isLoading) {
                    current = awaitItem()
                }
                current
            }

            assertThat(successState.isLoading).isFalse()
            assertThat(successState.error).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `init should handle error when user is not logged in`() = runTest(testDispatcher) {
        // Given
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } throws NoLoggedInUserException()

        // When
        initializeViewModel()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.isLoading).isTrue()
            assertThat(state.error).isEqualTo("Failed to load watching history")
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMediaTabSelection should update selected media type and load filtered history`() = runTest(testDispatcher) {
        // Given
        val moviesHistory = listOf(dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE))
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, MediaType.MOVIE) } returns flowOf(moviesHistory)
        initializeViewModel()

        // When
        viewModel.onMediaTabSelection(MediaTypeUi.MOVIE)

        // Then
        viewModel.state.test {
            awaitItem() // Initial state
            val finalState = awaitItem()
            assertThat(finalState.selectedMediaTypeUi).isEqualTo(MediaTypeUi.MOVIE)
            assertThat(finalState.isLoading).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMediaTabSelection should load all history when null is passed`() = runTest(testDispatcher) {
        // Given
        val allHistory = listOf(
            dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE),
            dummyHistoryItem.copy(id = 2, mediaType = MediaType.TV_SERIES)
        )
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, null) } returns flowOf(allHistory)
        initializeViewModel()

        // When
        viewModel.onMediaTabSelection(null)

        // Then
        viewModel.state.test {
            awaitItem()
            val finalState = awaitItem()
            assertThat(finalState.selectedMediaTypeUi).isNull()
            assertThat(finalState.isLoading).isFalse()
            assertThat(finalState.error).isNull()
            cancelAndConsumeRemainingEvents()
        }

        coVerify { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, null) }
    }

    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect`() = runTest(testDispatcher) {
        // Given
        initializeViewModel()

        // When
        viewModel.onMediaClick(101, MediaTypeUi.MOVIE)

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MediaTabScreenEffect.NavigateToMediaDetails(101, MediaTypeUi.MOVIE))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect for TV show`() = runTest(testDispatcher) {
        // Given
        initializeViewModel()

        // When
        viewModel.onMediaClick(102, MediaTypeUi.TV_SHOW)

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MediaTabScreenEffect.NavigateToMediaDetails(102, MediaTypeUi.TV_SHOW))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSaveIconClick should handle save icon click`() = runTest(testDispatcher) {
        // Given
        val mediaItem = MediaItem(id = 1, title = "Test Movie", imageUrl = "", rating = "", mediaTypeUi = MediaTypeUi.MOVIE, isSaved = false)
        initializeViewModel()

        // When
        viewModel.onSaveIconClick(mediaItem)

    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest(testDispatcher) {
        // Given
        initializeViewModel()

        // When
        viewModel.onBackClick()

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MediaTabScreenEffect.NavigateBack)
            cancelAndConsumeRemainingEvents()
        }
    }

    private companion object {
        val dummyUser = User(id = 42L, name = "Test User", username = "testuser")
        val dummyGenre = Genre(id = 28, name = "Action")
        val dummyHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = listOf(dummyGenre),
            isSaved = false
        )
    }}