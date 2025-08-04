package com.sanaa.presentation.screen.genreMovies

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import entity.Genre
import entity.Movie
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue
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
    fun resetDispatcher() {
        Dispatchers.resetMain()
    }

    @BeforeEach
    fun setUp() {
        manageMoviesDetailsUseCase = mockk(relaxed = true)
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest {
        givenHappyViewModel()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        givenHappyViewModel()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)

        viewModel.onBottomSheetDismiss()
        assertEquals(false, viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        givenHappyViewModel()

        viewModel.onBackClick()

        viewModel.effect.test {
            assertEquals(GenreMoviesEffects.NavigateBack, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClick should emit NavigateToMovieDetails effect`() = runTest {
        givenHappyViewModel()


        viewModel.effect.test {
            viewModel.onMovieClick(10)
            assertEquals(GenreMoviesEffects.NavigateToMovieDetails(10), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun givenHappyViewModel() {
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), any()) } returns movies

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to genreList[0].id,
                "categoryName" to genreList[0].name
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
                duration = 120.minutes,
                releaseDate = kotlinx.datetime.LocalDate(2020, 1, 1),
                overview = "",
                rating = 0
            ),
            Movie(
                id = 2,
                title = "Movie 2",
                posterImageUrl = "",
                genres = emptyList(),
                imdbRating = 7.5f,
                duration = 110.minutes,
                releaseDate = kotlinx.datetime.LocalDate(2019, 1, 1),
                overview = "",
                rating = 0
            )
        )
    }
}
