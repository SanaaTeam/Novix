package com.sanaa.presentation.screen.genreTvShows

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
import usecase.ManageTvSeriesUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreTvShowsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GenreTvShowsViewModel

    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk()

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
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } returns emptyList()

        viewModel = GenreTvShowsViewModel(category.id, category.name, manageTvSeriesUseCase)
        advanceUntilIdle()

        viewModel.onSaveIconClick()

        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        val category = genreList[0]
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } returns emptyList()

        viewModel = GenreTvShowsViewModel(category.id, category.name, manageTvSeriesUseCase)
        advanceUntilIdle()

        viewModel.onSaveIconClick()
        assertTrue(viewModel.state.value.showBottomSheet)

        viewModel.onBottomSheetDismiss()
        assertEquals(false, viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBackClick should emit NavigateBack effect`() = runTest {
        val category = genreList[0]
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } returns emptyList()

        viewModel = GenreTvShowsViewModel(category.id, category.name, manageTvSeriesUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onBackClick()
            assertEquals(GenreTvShowsEffects.NavigateBack, awaitItem())
        }
    }

    @Test
    fun `onTvShowClick should emit NavigateToTvShowDetails effect`() = runTest {
        val category = genreList[0]
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } returns emptyList()

        viewModel = GenreTvShowsViewModel(category.id, category.name, manageTvSeriesUseCase)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onTvShowClick(101)
            assertEquals(
                GenreTvShowsEffects.NavigateToTvShowDetails(101),
                awaitItem()
            )
        }
    }

//    @Test
//    fun `when getTvSeriesByGenre throws exception then should update state with error`() =
//        runTest(testDispatcher) {
//            val category = genreList[0]
//            val exception = RuntimeException("Something went wrong")
//
//            coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } throws exception
//
//            viewModel = GenreTvShowsViewModel(category.id, category.name, manageTvSeriesUseCase)
//
//            viewModel.state.test {
//                var currentState = awaitItem()
//                while (currentState.isLoading) {
//                    currentState = awaitItem()
//                }
//
//                val expectedState = GenreTvShowsScreenUiState(
//                    title = null,
//                    tvShows = emptyList(),
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
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )
    }
}