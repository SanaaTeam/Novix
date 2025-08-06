package com.sanaa.presentation.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.MediaItemUiModel
import com.sanaa.presentation.screen.myRating.MediaTypeUi
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryScreenEffect
import com.sanaa.presentation.screen.watchingHistory.WatchingHistoryViewModel
import entity.Genre
import entity.MediaHistoryItem
import entity.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.GetLoggedInUserUseCase
import usecase.history.ManageWatchingHistoryUseCase
import usecase.search.search_param.MediaType

@ExperimentalCoroutinesApi
class WatchingHistoryViewModelTest {

    private val manageWatchingHistoryUseCase: ManageWatchingHistoryUseCase = mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)

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
            testDispatcher
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
    fun `onMediaTabSelection should update selected media type and load filtered history`() = runTest(testDispatcher) {
        // Given
        val moviesHistory = listOf(dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE))
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, MediaType.MOVIE) } returns flowOf(moviesHistory)
        initializeViewModel()

        // When
        viewModel.onMediaTabSelection(MediaTypeUi.MOVIE)

        // Then
        viewModel.state.test {
            var currentState = awaitItem()
            while (currentState.isLoading) {
                currentState = awaitItem()
            }

            assertThat(currentState.selectedMediaTypeUi).isEqualTo(MediaTypeUi.MOVIE)
            assertThat(currentState.isLoading).isFalse()
            assertThat(currentState.error).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect`() = runTest(testDispatcher) {
        // Given
        initializeViewModel()

        // When
        viewModel.onMediaClick(101, MediaTypeUi.MOVIE)

        // Then
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WatchingHistoryScreenEffect.NavigateToMediaDetails(101, MediaTypeUi.MOVIE))
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
            assertThat(awaitItem()).isEqualTo(WatchingHistoryScreenEffect.NavigateToMediaDetails(102, MediaTypeUi.TV_SHOW))
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onSaveIconClick should handle save icon click`() = runTest(testDispatcher) {
        // Given
        val mediaItem = MediaItemUiModel(id = 1, imageUrl = "", rating = "", mediaTypeUi = MediaTypeUi.MOVIE, isSaved = false)
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
            assertThat(awaitItem()).isEqualTo(WatchingHistoryScreenEffect.NavigateBack)
            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `init should handle error when loading watching history fails`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Network Error"
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, null) } throws RuntimeException(errorMessage)

        // When
        initializeViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        // Then
        viewModel.state.test {
            var currentState = awaitItem()
            while (currentState.isLoading) {
                currentState = awaitItem()
            }

            assertThat(currentState.watchingHistory).isNotNull()
            assertThat(currentState.error).isEqualTo(errorMessage)
            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `onMediaTabSelection should handle error when loading filtered history fails`() = runTest(testDispatcher) {
        // Given
        val errorMessage = "Filtered Error"
        coEvery { manageWatchingHistoryUseCase.getWatchingHistory(dummyUser.username, MediaType.MOVIE) } throws RuntimeException(errorMessage)
        initializeViewModel()

        // When
        viewModel.onMediaTabSelection(MediaTypeUi.MOVIE)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertThat(state.selectedMediaTypeUi).isEqualTo(MediaTypeUi.MOVIE)
            assertThat(state.error).isEqualTo(errorMessage)
            cancelAndConsumeRemainingEvents()
        }
    }

    private companion object {
        val dummyUser = User(
            id = 42L,
            name = "Test User",
            username = "testuser",
            profileImageUrl = "https://example.com/profile.jpg"
        )

        val dummyGenre = Genre(id = 28, name = "Action")
        val dummyHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = listOf(dummyGenre),
            isSaved = false
        )
    }}