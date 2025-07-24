package com.sanaa.presentation.screen.movie_details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.GenreUiModel
import usecase.ManageMovieUseCase
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
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieDetailsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val manageMovieDetails: ManageMovieUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MovieDetailsViewModel
    private val movieId = 10

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `onBackClick emits NavigateBack`() = runTest {
        givenHappy()
        viewModel.onBackClick()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(MovieDetailsUiEffect.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onWatchTrailerClick does nothing when trailer missing`() = runTest {
        coEvery { manageMovieDetails.getMovieDetails(movieId) } returns dummyMovie
        coEvery { manageMovieDetails.getMovieCast(movieId) } returns dummyCast
        coEvery { manageMovieDetails.getMovieImages(movieId) } returns dummyImages
        coEvery { manageMovieDetails.getSimilarMoviesByMovieId(movieId) } returns dummySimilar
        coEvery { manageMovieDetails.getMovieTrailer(movieId) } returns null

        viewModel = MovieDetailsViewModel(movieId, manageMovieDetails)
        viewModel.onWatchTrailerClick()

        viewModel.effect.test {
            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onReadMoreClick does nothing`() {
        givenHappy()
        viewModel.onReadMoreClick()
    }

    @Test
    fun `onBookmarkClick and onRateMovieClick toggle login bottom sheet`() = runTest {
        givenHappy()
        viewModel.onBookmarkClick(movieId)
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
        viewModel.onRateMovieClick()
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `onDismissLoginBottomSheet sets sheet false`() = runTest {
        givenHappy()
        viewModel.onBookmarkClick(movieId)
        viewModel.onDismissLoginBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onSimilarMovieClick emits NavigateToAnotherMovieDetails`() = runTest {
        givenHappy()
        val otherId = 99
        viewModel.onSimilarMovieClick(otherId)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(MovieDetailsUiEffect.NavigateToAnotherMovieDetails(otherId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onActorCardClick emits NavigateToActorScreen`() = runTest {
        givenHappy()
        val actorId = 5
        viewModel.onActorCardClick(actorId)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(MovieDetailsUiEffect.NavigateToActorScreen(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onShowReviewsClick emits NavigateToReviewsScreen`() = runTest {
        givenHappy()
        viewModel.onShowReviewsClick(movieId)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(MovieDetailsUiEffect.NavigateToReviewsScreen(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreClicked emits NavigateToMovieCategoriesScreen`() = runTest {
        givenHappy()
        val genre =
            GenreUiModel(
            id = 1,
            name = "Drama"
        )

        viewModel.onGenreClicked(genre)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(MovieDetailsUiEffect.NavigateToMovieCategoriesScreen(genre.id, genre.name))
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun givenHappy() {
        coEvery { manageMovieDetails.getMovieDetails(movieId) } returns dummyMovie
        coEvery { manageMovieDetails.getMovieCast(movieId) } returns dummyCast
        coEvery { manageMovieDetails.getMovieImages(movieId) } returns dummyImages
        coEvery { manageMovieDetails.getSimilarMoviesByMovieId(movieId) } returns dummySimilar
        coEvery { manageMovieDetails.getMovieTrailer(movieId) } returns dummyTrailer
        viewModel = MovieDetailsViewModel(movieId, manageMovieDetails)
    }

    companion object {
        private val genreList = listOf(
            Genre(
                id = 1,
                name = "Drama"
            ),
            Genre(
                id = 2,
                name = "Action"
            )
        )
        private val dummyMovie = Movie(
            id = 10,
            posterImageUrl = "/poster.jpg",
            title = "Movie One",
            genres = genreList,
            imdbRating = 7.5f,
            duration = 100,
            releaseDate = LocalDate.parse("2020-05-20"),
            overview = "Overview1"
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
        private val dummySimilar = listOf(
            dummyMovie.copy(id = 11, title = "Movie Two", overview = "Overview2")
        )
        private val dummyImages = listOf("/img1.png", "/img2.png")
        private const val dummyTrailer = "http://trailer.url"
    }
}
