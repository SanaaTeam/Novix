package com.sanaa.presentation.screen.genreMovies

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import entity.Genre
import entity.Movie
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreMoviesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: GenreMoviesViewModel
    private lateinit var manageMoviesDetailsUseCase: ManageMovieUseCase
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)

    @BeforeAll
    fun setUpDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterAll
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @BeforeEach
    fun setup() {
        manageMoviesDetailsUseCase = mockk(relaxed = true)
        clearAllMocks()
    }

    private fun createViewModelWithMovies(movies: List<Movie>) {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), any()) } returns movies

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to genreList[0].id,
                "genreName" to genreList[0].name
            )
        )

        viewModel = GenreMoviesViewModel(
            savedStateHandle = savedStateHandle,
            manageMoviesDetailsUseCase = manageMoviesDetailsUseCase,
            dispatcher = testDispatcher,
            checkIfUserIsLoggedInUseCase = checkIfUserIsLoggedInUseCase
        )
        testDispatcher.scheduler.advanceUntilIdle()
    }


    @Test
    fun `onBackClick emits NavigateBack effect`() = runTest {
        createViewModelWithMovies(movies)

        viewModel.effect.test {
            viewModel.onBackClick()
            assertThat(awaitItem()).isEqualTo(GenreMoviesEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick emits NavigateToMovieDetails effect with correct id`() = runTest {
        createViewModelWithMovies(movies)

        viewModel.effect.test {
            viewModel.onMovieClick(10)
            assertThat(awaitItem()).isEqualTo(GenreMoviesEffects.NavigateToMovieDetails(10))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `updateUserLoggingStatus updates userIsLoggedIn state`() = runTest {
        coEvery { checkIfUserIsLoggedInUseCase.isLoggedIn() } returns flowOf(true)
        createViewModelWithMovies(movies)

        val state = viewModel.state.value
        assertThat(state.userIsLoggedIn).isTrue()
    }



    private companion object {
        val genreList = listOf(
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )

        val movies = listOf(
            Movie(
                id = 1,
                title = "Movie 1",
                posterImageUrl = "",
                genres = emptyList(),
                imdbRating = 8f,
                releaseDate = LocalDate(2020, 1, 1),
                overview = "",
                rating = 0,
                duration = 110L.minutes,
                trailerUrl = "",
            ),
            Movie(
                id = 2,
                title = "Movie 2",
                posterImageUrl = "",
                genres = emptyList(),
                imdbRating = 7.5f,
                duration = 110L.minutes,
                releaseDate = LocalDate(2019, 1, 1),
                overview = "",
                rating = 0,
                trailerUrl = ""
            )
        )
    }
}