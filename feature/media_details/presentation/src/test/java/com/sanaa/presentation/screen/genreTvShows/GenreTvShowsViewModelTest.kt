package com.sanaa.presentation.screen.genreTvShows

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

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to category.id,
                "genreName" to category.name
            )
        )

        viewModel = GenreTvShowsViewModel(
            savedStateHandle,
            manageTvSeriesUseCase
        )

        advanceUntilIdle()

        viewModel.onSaveIconClick()

        assertTrue(viewModel.state.value.showBottomSheet)
    }

    @Test
    fun `onBottomSheetDismiss should set showBottomSheet to false`() = runTest {
        val category = genreList[0]
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to category.id,
                "genreName" to category.name
            )
        )

        viewModel = GenreTvShowsViewModel(
            savedStateHandle,
            manageTvSeriesUseCase
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
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any()) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to category.id,
                "genreName" to category.name
            )
        )

        viewModel = GenreTvShowsViewModel(
            savedStateHandle,
            manageTvSeriesUseCase
        )

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

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to category.id,
                "genreName" to category.name
            )
        )

        viewModel = GenreTvShowsViewModel(
            savedStateHandle,
            manageTvSeriesUseCase
        )

        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onTvShowClick(101)
            assertEquals(
                GenreTvShowsEffects.NavigateToTvShowDetails(101),
                awaitItem()
            )
        }
    }

    private companion object {
        val genreList = listOf(
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )
    }
}