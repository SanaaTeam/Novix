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
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.VodStringProvider
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.search.search_param.MediaType

@ExperimentalCoroutinesApi
class WatchingHistoryViewModelTest {

    private val manageWatchingHistoryUseCase: ManageWatchedMediaHistoryUseCase = mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private val manageMovieUseCase: ManageMovieUseCase = mockk(relaxed = true)
    private val manageTvShowUseCase: ManageTvShowUseCase = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk(relaxed = true)
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
    }

    private fun initializeViewModel() {
        viewModel = WatchingHistoryViewModel(
            manageWatchedMediaHistoryUseCase = manageWatchingHistoryUseCase,
            getLoggedInUserUseCase = getLoggedInUserUseCase,
            manageMovieUseCase = manageMovieUseCase,
            manageTvShowUseCase = manageTvShowUseCase,
            stringProvider = stringProvider,
            dispatcher = testDispatcher
        )
    }


    @Test
    fun `init should load watching history and update state on success`() =
        runTest(testDispatcher) {
            val historyItems = listOf(dummyHistoryItem.copy(id = 1), dummyHistoryItem.copy(id = 2))
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    genreId = 1,
                    mediaType = MediaType.MOVIE,
                    username = "user"
                )
            } returns flowOf(historyItems)

            initializeViewModel()

            viewModel.state.test {
                val successState = awaitItem().let {
                    var current = it
                    while (current.isLoading) {
                        current = awaitItem()
                    }
                    current
                }

                assertThat(successState.isLoading).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onMediaTabSelection should update selected media type and load filtered history`() =
        runTest(testDispatcher) {
            val moviesHistory = listOf(dummyHistoryItem.copy(id = 1, mediaType = MediaType.MOVIE))
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    dummyUser.username,
                    MediaType.MOVIE,
                    dummyGenre.id
                )
            } returns flowOf(moviesHistory)
            initializeViewModel()

            viewModel.onMediaTabSelection(MediaTypeUi.MOVIE)

            viewModel.state.test {
                var currentState = awaitItem()
                while (currentState.isLoading) {
                    currentState = awaitItem()
                }

                assertThat(currentState.selectedMediaTypeUi).isEqualTo(MediaTypeUi.MOVIE)
                assertThat(currentState.isLoading).isFalse()
                cancelAndConsumeRemainingEvents()
            }
        }


    @Test
    fun `onMediaClick should emit NavigateToMediaDetails effect`() = runTest(testDispatcher) {
        initializeViewModel()

        viewModel.onMediaClick(101, MediaTypeUi.MOVIE)

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
            initializeViewModel()

            viewModel.onMediaClick(102, MediaTypeUi.TV_SHOW)

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
        val mediaItem =
            MediaItemUiModel(id = 1, imageUrl = "", rating = "", mediaTypeUi = MediaTypeUi.MOVIE)
        initializeViewModel()

        viewModel.onSaveIconClick(mediaItem)

    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest(testDispatcher) {
        initializeViewModel()

        viewModel.onBackClick()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(WatchingHistoryScreenEffect.NavigateBack)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onMovieGenreClick should update selected genre and fetch movies if genre is different`() =
        runTest(testDispatcher) {
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

            viewModel.onMovieGenreClick(newGenreId)

            testDispatcher.scheduler.advanceUntilIdle()

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
            val newGenreId = 77
            val tvShowHistory =
                listOf(dummyHistoryItem.copy(id = 2, mediaType = MediaType.TV_SHOW))

            coEvery { getLoggedInUserUseCase.getLoggedInUser() } returns flowOf(dummyUser)
            coEvery {
                manageWatchingHistoryUseCase.getMediaHistory(
                    genreId = newGenreId,
                    mediaType = MediaType.TV_SHOW,
                    username = dummyUser.username
                )
            } returns flowOf(tvShowHistory)

            initializeViewModel()

            viewModel.onTvShowGenreClick(newGenreId)

            advanceUntilIdle()

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
            val currentGenreId = 28
            initializeViewModel()

            viewModel.onMovieGenreClick(currentGenreId)

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.movieSelectedGenreId).isEqualTo(currentGenreId)
                cancelAndConsumeRemainingEvents()
            }
        }

    @Test
    fun `onTvShowGenreClick should do nothing if selected genre is same as current`() =
        runTest(testDispatcher) {
            val currentGenreId = 28
            initializeViewModel()

            viewModel.onTvShowGenreClick(currentGenreId)
            testDispatcher.scheduler.advanceUntilIdle()

            viewModel.state.test {
                val initialState = awaitItem()
                assertThat(initialState.tvShowSelectedGenreId).isEqualTo(currentGenreId)
                cancelAndConsumeRemainingEvents()
            }
        }
   @Test
    fun `onDismissSaveToListBottomSheet should update state correctly`() = runTest(testDispatcher) {
        val mediaItem = MediaItemUiModel(id = 1, imageUrl = "", rating = "", mediaTypeUi = MediaTypeUi.MOVIE)
        initializeViewModel()
        viewModel.onSaveIconClick(mediaItem)
        viewModel.onDismissSaveToListBottomSheet()
        advanceUntilIdle()

        viewModel.state.test {
            val updatedState = awaitItem()
            assertThat(updatedState.showSaveToListBottomSheet).isFalse()
            assertThat(updatedState.selectedMediaToSave).isNull()
            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `onCreateNewListClick should update state to show AddListBottomSheet`() = runTest(testDispatcher) {
        initializeViewModel()
        val mediaItem = MediaItemUiModel(id = 1, imageUrl = "", rating = "", mediaTypeUi = MediaTypeUi.MOVIE)
        viewModel.onSaveIconClick(mediaItem)

        viewModel.onCreateNewListClick()
        advanceUntilIdle()

        viewModel.state.test {
            val updatedState = awaitItem()
            assertThat(updatedState.showSaveToListBottomSheet).isFalse()
            assertThat(updatedState.showAddListBottomSheet).isTrue()
            cancelAndConsumeRemainingEvents()
        }
    }
    @Test
    fun `onDismissAddListBottomSheet should update state correctly`() = runTest(testDispatcher) {
        initializeViewModel()
        viewModel.onCreateNewListClick()

        viewModel.onDismissAddListBottomSheet()
        advanceUntilIdle()

        viewModel.state.test {
            val updatedState = awaitItem()
            assertThat(updatedState.showAddListBottomSheet).isFalse()
            cancelAndConsumeRemainingEvents()
        }
    }

    private companion object {
        val dummyUser = User(
            id = 42L,
            name = "Test User",
            username = "test user",
            profileImageUrl = "https://example.com/profile.jpg"
        )

        val dummyGenre = Genre(id = 28, name = "Action")
        val dummyHistoryItem = MediaHistoryItem(
            id = 1,
            posterImageUrl = "test_url",
            mediaType = MediaType.MOVIE,
            genres = listOf(dummyGenre),
            lastWatchedAt = 1L,
        )
    }
}