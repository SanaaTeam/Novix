package com.sanaa.presentation.screen.movieDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.GenreUiModel
import entity.Actor
import entity.Actor.Gender
import entity.Genre
import entity.Movie
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterEach
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageMovieUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {
    private val checkUserLogin = mockk<CheckIfUserIsLoggedInUseCase>(relaxed = true)
    private val getUser = mockk<GetLoggedInUserUseCase>(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()
    private val manageMovieDetails: ManageMovieUseCase = mockk(relaxed = true)
    private val manageWatchedMediaHistoryUseCase: ManageWatchedMediaHistoryUseCase =
        mockk(relaxed = true)
    private lateinit var viewModel: MovieDetailsViewModel
    private val movieId = 10

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onBackClick emits NavigateBack`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onWatchTrailerClick does nothing when trailer missing`() = runTest {
        coEvery { manageMovieDetails.getMovieDetails(movieId) } returns dummyMovie
        coEvery { manageMovieDetails.getMovieCast(movieId) } returns dummyCast
        coEvery { manageMovieDetails.getMovieImages(movieId) } returns dummyImages
        coEvery { manageMovieDetails.getSimilarMoviesByMovieId(movieId, 1) } returns dummySimilar
        coEvery { manageMovieDetails.getMovieTrailer(movieId) } returns null

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId))

        viewModel = MovieDetailsViewModel(
            savedStateHandle,
            manageMovieDetails,
            checkUserLogin,
            manageWatchedMediaHistoryUseCase,
            getUser
        )
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onWatchTrailerClick()
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onReadMoreClick does nothing`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onReadMoreClick()
    }

    @Test
    fun `onBookmarkClick and onRateMovieClick toggle login bottom sheet`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onBookmarkClick(movieId)
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()

        viewModel.onRateMovieClick()
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `onDismissLoginBottomSheet sets sheet false`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onBookmarkClick(movieId)
        viewModel.onDismissLoginBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onSimilarMovieClick emits NavigateToAnotherMovieDetails`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()
        val otherId = 99

        viewModel.effect.test {
            viewModel.onSimilarMovieClick(otherId)
            assertThat(awaitItem()).isEqualTo(
                MovieDetailsUiEffect.NavigateToAnotherMovieDetails(
                    otherId
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onActorCardClick emits NavigateToActorScreen`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()
        val actorId = 5

        viewModel.effect.test {
            viewModel.onActorCardClick(actorId)
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiEffect.NavigateToActorScreen(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onShowReviewsClick emits NavigateToReviewsScreen`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onShowReviewsClick(movieId)
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiEffect.NavigateToReviewsScreen(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreClicked emits NavigateToMovieCategoriesScreen`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()
        val genre = GenreUiModel(id = 1, name = "Drama")

        viewModel.effect.test {
            viewModel.onGenreClicked(genre)
            assertThat(awaitItem()).isEqualTo(
                MovieDetailsUiEffect.NavigateToMovieCategoriesScreen(genre.id, genre.name)
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRateMovieClick shows rate bottom sheet if user is logged in`() = runTest {
        coEvery { checkUserLogin.isLoggedIn() } returns true
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onRateMovieClick()

        assertThat(viewModel.state.value.showRateBottomSheet).isTrue()
    }


    @Test
    fun `onRatingChanged updates the rating`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onRatingChanged(8)
        assertThat(viewModel.state.value.imdbRating).isEqualTo(8)
    }

    @Test
    fun `onDismissRateBottomSheet sets sheet to false`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.updateState { it.copy(showRateBottomSheet = true) }
        viewModel.onDismissRateBottomSheet()
        assertThat(viewModel.state.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `onSubmitRateBottomSheet sets errorMessage on exception`() = runTest {
        val error = RuntimeException("Something went wrong")
        coEvery { manageMovieDetails.addMovieRate(any(), any()) } throws error
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onSubmitRateBottomSheet()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.errorMessage).isEqualTo("Something went wrong")
        assertThat(viewModel.state.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `onRetryLoadDetails updates state and retries fetch`() = runTest {
        givenHappy()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.onRetryLoadDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.errorMessage).isNull()
        assertThat(viewModel.state.value.noInternetConnection).isFalse()
    }

    private fun givenHappy() {
        coEvery { manageMovieDetails.getMovieDetails(movieId) } returns dummyMovie
        coEvery { manageMovieDetails.getMovieCast(movieId) } returns dummyCast
        coEvery { manageMovieDetails.getMovieImages(movieId) } returns dummyImages
        coEvery { manageMovieDetails.getSimilarMoviesByMovieId(movieId, 1) } returns dummySimilar
        coEvery { manageMovieDetails.getMovieTrailer(movieId) } returns dummyTrailer

        val savedStateHandle = SavedStateHandle(mapOf("movieId" to movieId))

        viewModel = MovieDetailsViewModel(
            savedStateHandle,
            manageMovieDetails,
            checkUserLogin,
            manageWatchedMediaHistoryUseCase,
            getUser,
            dispatcher = testDispatcher
        )
    }

    companion object {
        private val genreList = listOf(
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )
        private val dummyMovie = Movie(
            id = 10,
            posterImageUrl = "/poster.jpg",
            title = "Movie One",
            genres = genreList,
            imdbRating = 7.5f,
            duration = 100.minutes,
            releaseDate = LocalDate.parse("2020-05-20"),
            overview = "Overview1",
            rating = 0
        )
        private val dummyCast = listOf(
            Actor(
                id = 1, imageUrl = "/a.jpg", name = "Actor A", region = "US",
                lastShow = "ShowX", gender = Gender.FEMALE,
                department = "Acting", character = "Lead",
                birthDate = LocalDate.parse("1980-01-01"),
                deathDate = null, placeOfBirth = "LA", biography = "Bio"
            )
        )
        private val dummySimilar =
            listOf(dummyMovie.copy(id = 11, title = "Movie Two", overview = "Overview2"))
        private val dummyImages = listOf("/img1.png", "/img2.png")
        private const val dummyTrailer = "http://trailer.url"
    }
}
