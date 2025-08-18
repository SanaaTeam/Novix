package com.sanaa.presentation.screen.tvShow

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.GenreUiModel
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import entity.Actor
import entity.Episode
import entity.Genre
import entity.Season
import entity.TvShow
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageTvShowUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class TvShowScreenViewModelTest {
    private val getUser = mockk<GetLoggedInUserUseCase>(relaxed = true)
    private val checkUserLogin = mockk<CheckIfUserIsLoggedInUseCase>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val manageTvShowDetails: ManageTvShowUseCase = mockk(relaxed = true)
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase =
        mockk(relaxed = true)
    private val getLoggedInUserUseCase: GetLoggedInUserUseCase = mockk(relaxed = true)
    private lateinit var viewModel: TvShowScreenViewModel

    private val tvShowId = 42

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackClicked emits NavigateBack`() = runTest {
        givenHappyViewModel()
        viewModel.onBackClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(TvShowScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onViewReviewsClicked emits NavigateToReviewsScreen`() = runTest {
        givenHappyViewModel()
        viewModel.onViewReviewsClicked(tvShowId)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(TvShowScreenEffects.NavigateToReviewsScreen(tvShowId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onActorClicked emits NavigateToActorScreen`() = runTest {
        givenHappyViewModel()
        val actorId = 99
        viewModel.onActorClicked(actorId)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(TvShowScreenEffects.NavigateToActorScreen(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSeasonNumberClicked does nothing when same season`() = runTest {
        coEvery { manageTvShowDetails.getTvShowDetails(tvShowId) } returns dummyTvShow
        coEvery { manageTvShowDetails.getTvShowCast(tvShowId) } returns dummyCast
        coEvery { manageTvShowDetails.getTvShowSeasonDetails(tvShowId, 1) } returns dummySeason
        coEvery { manageTvShowDetails.getTvShowImageUrls(tvShowId) } returns dummyImages
        coEvery { manageTvShowDetails.getTvShowTrailer(tvShowId) } returns dummyTrailer

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId
            )
        )

        viewModel = TvShowScreenViewModel(
            savedStateHandle,
            checkUserLogin,
            getUser,
            manageTvShowDetails,
            manageWatchedMediaHistoryUseCase,
            getLoggedInUserUseCase,
        )
        assertThat(viewModel.state.value.selectedSeason).isEqualTo(1)
        viewModel.onSeasonNumberClicked(1)

        assertThat(viewModel.state.value.selectedSeason).isEqualTo(1)
    }

    @Test
    fun `onEpisodeClicked emits NavigateToEpisodeDetailsScreen`() = runTest {
        givenHappyViewModel()
        viewModel.onEpisodeClicked(tvShowId, 1, 5)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(TvShowScreenEffects.NavigateToEpisodeDetailsScreen(tvShowId, 1, 5))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRateClicked sets showLoginBottomSheet to true`() = runTest {
        givenHappyViewModel()

        viewModel.onRateClicked()

        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }


    @Test
    fun `onGenreClicked emits NavigateToMovieCategoriesScreen`() = runTest {
        givenHappyViewModel()
        val genre = GenreUiModel(
            id = 1,
            name = "Drama"
        )
        viewModel.onGenreClicked(genre)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(TvShowScreenEffects.NavigateToMovieCategoriesScreen(genre))
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `loadTvShow handles error correctly when use case fails`() = runTest {
        coEvery { manageTvShowDetails.getTvShowDetails(tvShowId) } throws RuntimeException("Test failure")

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId
            )
        )

        viewModel = TvShowScreenViewModel(
            savedStateHandle,
            checkUserLogin,
            getUser,
            manageTvShowDetails,
            manageWatchedMediaHistoryUseCase,
            getLoggedInUserUseCase,
            dispatcher = testDispatcher
        )
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo("Test failure")
    }


    @Test
    fun `onPlayTrailerClicked emits PlayTrailer`() = runTest {
        givenHappyViewModel()
        advanceUntilIdle()
        viewModel.onPlayTrailerClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(TvShowScreenEffects.PlayTrailer(dummyTrailer))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRateClicked sets showRateBottomSheet true when user is logged in`() = runTest {
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(true)
        givenHappyViewModel()
        advanceUntilIdle()
        viewModel.onRateClicked()
        assertThat(viewModel.state.value.showRateBottomSheet).isTrue()
    }

    @Test
    fun `onDismissRateBottomSheet sets showRateBottomSheet to false`() = runTest {
        givenHappyViewModel()
        viewModel.onRateClicked()
        viewModel.onDismissRateBottomSheet()
        assertThat(viewModel.state.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `onDismissLoginBottomSheet sets showLoginBottomSheet to false`() = runTest {
        givenHappyViewModel()
        viewModel.onDismissLoginBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onLoginButtonClick hides sheet and emits NavigateToLogin`() = runTest {
        givenHappyViewModel()
        viewModel.onLoginButtonClick()

        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()

        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(TvShowScreenEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSubmitRateBottomSheet sets error when exception occurs`() = runTest {
        val errorMsg = "Failed to rate"
        coEvery { manageTvShowDetails.addTvShowRate(any(), any()) } throws RuntimeException(
            errorMsg
        )
        givenHappyViewModel()

        viewModel.onRatingChanged(6)
        viewModel.onSubmitRateBottomSheet()
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertThat(error).isEqualTo(errorMsg)
            assertThat(showRateBottomSheet).isFalse()
        }
    }

    @Test
    fun `onSubmitRateBottomSheet calls addRate successfully`() = runTest {
        coEvery { manageTvShowDetails.addTvShowRate(any(), any()) } returns true
        givenHappyViewModel()

        viewModel.onRatingChanged(8)
        viewModel.onSubmitRateBottomSheet()
        advanceUntilIdle()

        assertThat(viewModel.state.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `onDismissAnyBottomSheet hides both bottom sheets`() = runTest {
        givenHappyViewModel()
        viewModel.updateState {
            copy(showRateBottomSheet = true, showLoginBottomSheet = true)
        }

        viewModel.onDismissAnyBottomSheet()

        val state = viewModel.state.value
        assertThat(state.showRateBottomSheet).isFalse()
        assertThat(state.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onRetryLoadDetails updates loading state and calls loadTvShow`() = runTest {
        givenHappyViewModel()
        val errorMsg = "Some error"

        viewModel.updateState {
            copy(
                isLoading = false,
                error = errorMsg,
                noInternetConnection = true
            )
        }
        viewModel.onRetryLoadDetails()

        val state = viewModel.state.value
        assertThat(state.isLoading).isTrue()
        assertThat(state.error).isNull()
        assertThat(state.noInternetConnection).isFalse()
    }


    private fun givenHappyViewModel(dispatcher: CoroutineDispatcher = StandardTestDispatcher()) {
        coEvery { manageTvShowDetails.getTvShowDetails(tvShowId) } returns dummyTvShow
        coEvery { manageTvShowDetails.getTvShowCast(tvShowId) } returns dummyCast
        coEvery { manageTvShowDetails.getTvShowSeasonDetails(tvShowId, 1) } returns dummySeason
        coEvery { manageTvShowDetails.getTvShowImageUrls(tvShowId) } returns dummyImages
        coEvery { manageTvShowDetails.getTvShowTrailer(tvShowId) } returns dummyTrailer

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId
            )
        )

        viewModel = TvShowScreenViewModel(
            savedStateHandle,
            checkUserLogin,
            getUser,
            manageTvShowDetails,
            manageWatchedMediaHistoryUseCase,
            getLoggedInUserUseCase,
            dispatcher
        )
    }

    private companion object {
        val genreList = listOf(
            Genre(
                id = 1,
                name = "Drama"
            ),
            Genre(
                id = 2,
                name = "Action"
            )
        )
        val dummyTvShow = TvShow(
            id = 42,
            title = "My Show",
            overview = "Overview",
            releaseDate = LocalDate.parse("2021-07-01"),
            genres = genreList,
            imdbRating = 9.0f,
            posterImageUrl = "/tv.jpg",
            seasonsCount = 2,
            rating = 0
        )
        val dummyCast = listOf(
            Actor(
                id = 1,
                imageUrl = "/actor1.jpg",
                name = "Actor One",
                department = "Acting",
                character = "Role A",
                birthDate = LocalDate.parse("1980-01-01"),
                deathDate = defaultDate,
                placeOfBirth = "LA",
                biography = "Bio"
            )
        )
        val dummyEpisode = Episode(
            id = 10,
            title = "Ep One",
            number = 1,
            seasonNumber = 1,
            imdbRating = 8.0f,
            overview = "",
            durationMinutes = 0,
            releaseDate = defaultDate,
            stillImagePath = "",
            rating = 0
        )
        val dummySeason = Season(
            id = 100,
            title = "Season 1",
            overview = "S1 overview",
            number = 1,
            episodes = listOf(dummyEpisode)
        )
        val dummyImages = listOf("/img1.jpg", "/img2.jpg")
        const val dummyTrailer = "http://trailer.url/video.mp4"
    }
}
