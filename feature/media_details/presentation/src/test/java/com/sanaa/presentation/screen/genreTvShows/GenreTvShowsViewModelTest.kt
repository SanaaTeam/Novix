package com.sanaa.presentation.screen.genreTvShows

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import entity.Genre
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.ManageTvSeriesUseCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreTvShowsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: GenreTvShowsViewModel

    private val manageTvSeriesUseCase: ManageTvSeriesUseCase = mockk()
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase = mockk(relaxed = true)


    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        clearAllMocks()
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSaveIconClick should set showBottomSheet to true`() = runTest {
        val category = genreList[0]
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any(), any()) } returns emptyList()
        coEvery { checkIfUserIsLoggedInUseCase.isLoggedIn() } returns flowOf(false)

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to category.id,
                "genreName" to category.name
            )
        )

        viewModel = GenreTvShowsViewModel(
            savedStateHandle,
            manageTvSeriesUseCase,
            checkIfUserIsLoggedInUseCase,
            testDispatcher
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onSaveIconClick()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(viewModel.state.value.showBottomSheet)
    }


    @Test
    fun `onTvShowClick should emit NavigateToTvShowDetails effect`() = runTest {
        val category = genreList[0]
        coEvery { manageTvSeriesUseCase.getTvSeriesByGenre(any(), 1) } returns emptyList()

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "genreId" to category.id,
                "genreName" to category.name
            )
        )

        viewModel = GenreTvShowsViewModel(
            savedStateHandle,
            manageTvSeriesUseCase,
            checkIfUserIsLoggedInUseCase
        )

        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onTvShowClick(101)

            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(
                GenreTvShowsEffects.NavigateToTvShowDetails(101),
                awaitItem()
            )

            cancelAndIgnoreRemainingEvents()
        }
    }


    private companion object {
        val genreList = listOf(
            Genre(id = 1, name = "Drama"),
            Genre(id = 2, name = "Action")
        )
    }
}