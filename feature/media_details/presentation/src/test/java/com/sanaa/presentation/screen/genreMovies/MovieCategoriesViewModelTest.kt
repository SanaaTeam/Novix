package com.sanaa.presentation.screen.genreMovies

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import entity.Genre
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.*
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageMovieUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovieCategoriesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GenreMoviesViewModel

    private val manageMoviesDetailsUseCase: ManageMovieUseCase = mockk()
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)

    @BeforeAll
    fun setMainDispatcher() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterAll
    fun resetMainDispatcher() {
        Dispatchers.resetMain()
    }

    @BeforeEach
    fun setupMocks() {
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest {
        prepareViewModel()
        advanceUntilIdle()

        viewModel.onSaveIconClick()

        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        prepareViewModel()
        advanceUntilIdle()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)

        viewModel.onBottomSheetDismiss()
        assertEquals(false, viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        prepareViewModel()
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onBackClick()
            assertEquals(GenreMoviesEffects.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onMovieClick should emit NavigateToMovieDetails effect`() = runTest {
        prepareViewModel()
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onMovieClick(10)
            assertEquals(GenreMoviesEffects.NavigateToMovieDetails(10), awaitItem())
        }
    }


    private fun prepareViewModel() {
        val category = genreList.first()
        coEvery { manageMoviesDetailsUseCase.getMoviesByCategory(any(), any()) } returns emptyList()

        viewModel = createViewModel(category)
    }

    private fun createViewModel(category: Genre): GenreMoviesViewModel {
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "categoryId" to category.id,
                "categoryName" to category.name
            )
        )
        return GenreMoviesViewModel(
            savedStateHandle = savedStateHandle,
            manageMoviesDetailsUseCase = manageMoviesDetailsUseCase,
            checkIfUserIsLoggedInUseCase = checkIfUserIsLoggedInUseCase,
            dispatcher = testDispatcher
        )
    }

    private companion object {
        val genreList = listOf(
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )
    }
}