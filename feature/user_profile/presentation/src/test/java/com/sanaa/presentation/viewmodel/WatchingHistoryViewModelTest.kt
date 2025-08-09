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
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import repository.SavedListsStatusProvider
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvSeriesUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType

@ExperimentalCoroutinesApi
class WatchingHistoryViewModelTest {

    private val manageWatchingHistoryUseCase: ManageWatchedMediaHistoryUseCase =
        mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk(relaxed = true)
    private lateinit var savedListsStatusProvider: SavedListsStatusProvider

    private lateinit var viewModel: WatchingHistoryViewModel
    private val testDispatcher = StandardTestDispatcher()
    val dummyHistoryItems = listOf(dummyHistoryItem)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        coEvery { getLoggedInUserUseCase.getLoggedInUser() } returns flowOf(dummyUser)
        coEvery {
            manageWatchingHistoryUseCase.getMediaHistory(
                any(),
                any(),
                any()
            )
        } returns flowOf(dummyHistoryItems)
        savedListsStatusProvider = mockk(relaxed = true) {
            every { savedIds } returns MutableStateFlow(emptySet())
        }
    }

    private fun initializeViewModel() {
        viewModel = WatchingHistoryViewModel(
            manageWatchedMediaHistoryUseCase = manageWatchingHistoryUseCase,
            getLoggedInUserUseCase = getLoggedInUserUseCase,
            manageMovieUseCase = manageMovieUseCase,
            manageTvSeriesUseCase = manageTvSeriesUseCase,
            savedListsStatusProvider = savedListsStatusProvider,

            dispatcher = testDispatcher
        )
    }


    @Test
    fun `init should load watching history and update state on success`() =
        runTest(testDispatcher) {
            // Given
            val historyItems = listOf(dummyHistoryItem.copy(id = 1), dummyHistoryItem.copy(id = 2))
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    genreId = 1,
                    mediaType = MediaType.MOVIE,
                    username = "user"
                )
            } returns flowOf(historyItems)

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
    fun `onMediaTabSelection should update selected media type and load filtered history`() =
        runTest(testDispatcher) {
            // Given
            val moviesHistory = listOf(dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE))
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    dummyUser.username,
                    MediaType.MOVIE,
                    dummyGenre.id
                )
            } returns flowOf(moviesHistory)
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
            assertThat(awaitItem()).isEqualTo(
                WatchingHistoryScreenEffect.NavigateToMediaDetails(
                    101,
                    MediaTypeUi.MOVIE
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect for TV show`() =
        runTest(testDispatcher) {
            // Given
            initializeViewModel()

            // When
            viewModel.onMediaClick(102, MediaTypeUi.TV_SHOW)

            // Then
            viewModel.effect.test {
                assertThat(awaitItem()).isEqualTo(
                    WatchingHistoryScreenEffect.NavigateToMediaDetails(
                        102,
                        MediaTypeUi.TV_SHOW
                    )
                )
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onSaveIconClick should handle save icon click`() = runTest(testDispatcher) {
        // Given
        val mediaItem =
            MediaItemUiModel(id = 1, imageUrl = "", rating = "", mediaTypeUi = MediaTypeUi.MOVIE)
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
    fun `onMovieGenreClick should update selected genre and fetch movies if genre is different`() =
        runTest(testDispatcher) {
            // Given
            val newGenreId = 99
            val movieHistory = listOf(dummyHistoryItem.copy(id = 1))

            coEvery { getLoggedInUserUseCase.getLoggedInUser() } returns flowOf(dummyUser)
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    genreId = newGenreId,
                    mediaType = MediaType.MOVIE,
                    username = dummyUser.username
                )
            } returns flowOf(movieHistory)

            initializeViewModel()

            // When
            viewModel.onMovieGenreClick(newGenreId)

            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.state.test {
                val state = awaitItem()
                assertThat(state.movieSelectedGenreId).isEqualTo(newGenreId)
                assertThat(state.movieList).hasSize(1)
                cancelAndConsumeRemainingEvents()
            }
        }


    @Test
    fun `onTvShowGenreClick should update selected genre and fetch tv shows if genre is different`() =
        runTest(testDispatcher) {
            // Given
            val newGenreId = 77
            val tvShowHistory =
                listOf(dummyHistoryItem.copy(id = 2, mediaType = MediaType.TV_SERIES))

            coEvery { getLoggedInUserUseCase.getLoggedInUser() } returns flowOf(dummyUser)
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    genreId = newGenreId,
                    mediaType = MediaType.TV_SERIES,
                    username = dummyUser.username
                )
            } returns flowOf(tvShowHistory)

            initializeViewModel()

            // When
            viewModel.onTvShowGenreClick(newGenreId)

            advanceUntilIdle()

            // Then
            viewModel.state.test {
                val state = awaitItem()
                assertThat(state.tvShowSelectedGenreId).isEqualTo(newGenreId)
                assertThat(state.tvShowList).hasSize(1)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onMovieGenreClick should do nothing if selected genre is same as current`() =
        runTest(testDispatcher) {
            // Given
            val currentGenreId = 28
            initializeViewModel()

            // When
            viewModel.onMovieGenreClick(currentGenreId)

            // Then
            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.movieSelectedGenreId).isEqualTo(currentGenreId)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onTvShowGenreClick should do nothing if selected genre is same as current`() =
        runTest(testDispatcher) {
            // Given
            val currentGenreId = 28
            initializeViewModel()

            // When
            viewModel.onTvShowGenreClick(currentGenreId)
            testDispatcher.scheduler.advanceUntilIdle()

            // Then
            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.tvShowSelectedGenreId).isEqualTo(currentGenreId)
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
        )
    }
}