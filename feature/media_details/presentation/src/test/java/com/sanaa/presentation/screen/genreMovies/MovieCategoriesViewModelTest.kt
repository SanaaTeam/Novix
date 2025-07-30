package com.sanaa.presentation.screen.genreMovies

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import entity.Genre
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import usecase.ManageMovieUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieCategoriesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GenreMoviesViewModel

    private val manageMoviesDetailsUseCase: ManageMovieUseCase = mockk()

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
        clearAllMocks()
    }


    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest {
        val category = genreList[0]
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), 1) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to category.id,
                "categoryName" to category.name
            )
        )

        viewModel = GenreMoviesViewModel(
            savedStateHandle,
            manageMoviesDetailsUseCase
        )

        advanceUntilIdle()

        viewModel.onSaveIconClick()

        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        val category = genreList[0]
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), 1) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to category.id,
                "categoryName" to category.name
            )
        )

        viewModel = GenreMoviesViewModel(
            savedStateHandle,
            manageMoviesDetailsUseCase
        )

        advanceUntilIdle()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)

        viewModel.onBottomSheetDismiss()
        assertEquals(false, viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        val category = genreList[0]
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), 1) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to category.id,
                "categoryName" to category.name
            )
        )

        viewModel = GenreMoviesViewModel(
            savedStateHandle,
            manageMoviesDetailsUseCase
        )

        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onBackClick()
            assertEquals(GenreMoviesEffects.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onMovieClick should emit NavigateToMovieDetails effect`() = runTest {
        val category = genreList[0]
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), 1) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to category.id,
                "categoryName" to category.name
            )
        )

        viewModel = GenreMoviesViewModel(
            savedStateHandle,
            manageMoviesDetailsUseCase
        )

        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onMovieClick(10)
            assertEquals(
                GenreMoviesEffects.NavigateToMovieDetails(10),
                awaitItem()
            )
        }
    }

//    @Test
//    fun `when getMoviesByCategory throws exception then should update state with error`() =
//        runTest(testDispatcher) {
//            val category = genreList[0]
//            val exception = RuntimeException()
//
//            coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), 1) } throws exception
//
//
//            val savedStateHandle = SavedStateHandle(
//                mapOf(
//                    "categoryId" to category.id,
//                    "categoryName" to category.name
//                )
//            )
//
//            viewModel = GenreMoviesViewModel(
//                savedStateHandle,
//                manageMoviesDetailsUseCase
//            )
//
//            viewModel.state.test {
//                var currentState = awaitItem()
//                while (currentState.isLoading) {
//                    currentState = awaitItem()
//                }
//
//                val expectedState = GenreMoviesScreenUiState(
//                    title = null,
//                    movies = emptyList(),
//                    isLoading = false,
//                    error = exception.message,
//                    showBottomSheet = false
//                )
//
//                assertEquals(expectedState, currentState)
//            }
//        }

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
    }
}
