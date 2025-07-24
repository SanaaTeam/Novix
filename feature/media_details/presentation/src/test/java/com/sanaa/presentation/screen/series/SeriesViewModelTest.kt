package com.sanaa.presentation.screen.series

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.GenreUiModel
import entity.Actor
import entity.Actor.Gender
import entity.Episode
import entity.Genre
import entity.Season
import entity.TvSeries
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageTvSeriesUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class SeriesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val manageTvSeriesDetails: ManageTvSeriesUseCase = mockk(relaxed = true)
    private lateinit var viewModel: SeriesViewModel

    private val seriesId = 42

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `onBackClicked emits NavigateBack`() = runTest {
        givenHappyViewModel()
        viewModel.onBackClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(SeriesScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onViewReviewsClicked emits NavigateToReviewsScreen`() = runTest {
        givenHappyViewModel()
        viewModel.onViewReviewsClicked(seriesId)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(SeriesScreenEffects.NavigateToReviewsScreen(seriesId))
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
                .isEqualTo(SeriesScreenEffects.NavigateToActorScreen(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSeasonNumberClicked does nothing when same season`() = runTest {
        coEvery { manageTvSeriesDetails.getTvSeriesDetails(seriesId) } returns dummyTvSeries
        coEvery { manageTvSeriesDetails.getTvSeriesCast(seriesId) } returns dummyCast
        coEvery { manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, 1) } returns dummySeason
        coEvery { manageTvSeriesDetails.getTvSeriesImages(seriesId) } returns dummyImages
        coEvery { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) } returns dummyTrailer

        viewModel = SeriesViewModel(seriesId, manageTvSeriesDetails)
        assertThat(viewModel.state.value.selectedSeason).isEqualTo(1)
        viewModel.onSeasonNumberClicked(1)

        assertThat(viewModel.state.value.selectedSeason).isEqualTo(1)
    }

    @Test
    fun `onEpisodeClicked emits NavigateToEpisodeDetailsScreen`() = runTest {
        givenHappyViewModel()
        viewModel.onEpisodeClicked(seriesId, 1, 5)
        viewModel.effect.test {
            assertThat(awaitItem())
                .isEqualTo(SeriesScreenEffects.NavigateToEpisodeDetailsScreen(seriesId, 1, 5))
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
    fun `onDismissRateBottomSheet sets showLoginBottomSheet to false`() = runTest {
        givenHappyViewModel()
        viewModel.onRateClicked()
        viewModel.onDismissRateBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onSaveSeriesClicked sets showLoginBottomSheet to true`() = runTest {
        givenHappyViewModel()
        viewModel.onSaveSeriesClicked()
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
                .isEqualTo(SeriesScreenEffects.NavigateToMovieCategoriesScreen(genre))
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `loadSeries handles error correctly when use case fails`() = runTest {
        coEvery { manageTvSeriesDetails.getTvSeriesDetails(seriesId) } throws RuntimeException("Test failure")

        viewModel = SeriesViewModel(seriesId, manageTvSeriesDetails, dispatcher = testDispatcher)
        advanceUntilIdle()

        assertThat(viewModel.state.value.error).isEqualTo("Test failure")
    }

    @Test
    fun `onSeasonNumberClicked updates state when selecting new season`() = runTest {
        coEvery { manageTvSeriesDetails.getTvSeriesDetails(seriesId) } returns dummyTvSeries
        coEvery { manageTvSeriesDetails.getTvSeriesCast(seriesId) } returns dummyCast
        coEvery { manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, 2) } returns dummySeason2
        coEvery { manageTvSeriesDetails.getTvSeriesImages(seriesId) } returns dummyImages
        coEvery { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) } returns dummyTrailer
        viewModel = SeriesViewModel(seriesId, manageTvSeriesDetails, testDispatcher)

        viewModel.onSeasonNumberClicked(2)
        advanceUntilIdle()

        with(viewModel.state.value) {
            assertThat(selectedSeason).isEqualTo(2)
            assertThat(season.episodes.first().seasonNumber).isEqualTo(2)
            assertThat(isLoadingEpisodes).isFalse()
        }
    }

    private fun givenHappyViewModel(dispatcher: CoroutineDispatcher = StandardTestDispatcher()) {
        coEvery { manageTvSeriesDetails.getTvSeriesDetails(seriesId) } returns dummyTvSeries
        coEvery { manageTvSeriesDetails.getTvSeriesCast(seriesId) } returns dummyCast
        coEvery { manageTvSeriesDetails.getTvSeriesSeasonDetails(seriesId, 1) } returns dummySeason
        coEvery { manageTvSeriesDetails.getTvSeriesImages(seriesId) } returns dummyImages
        coEvery { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) } returns dummyTrailer

        viewModel = SeriesViewModel(seriesId, manageTvSeriesDetails, dispatcher)
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
        val dummyTvSeries = TvSeries(
            id = 42,
            title = "My Series",
            overview = "Overview",
            releaseDate = LocalDate.parse("2021-07-01"),
            genres = genreList,
            imdbRating = 9.0f,
            posterImageUrl = "/series.jpg",
            seasonsCount = 2
        )
        val dummyCast = listOf(
            Actor(
                id = 1,
                imageUrl = "/actor1.jpg",
                name = "Actor One",
                region = "US",
                lastShow = "Show A",
                gender = Gender.MALE,
                department = "Acting",
                character = "Role A",
                birthDate = LocalDate.parse("1980-01-01"),
                deathDate = null,
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
            overview = null,
            durationMinutes = null,
            releaseDate = null,
            stillImagePath = null
        )
        val dummySeason = Season(
            id = 100,
            title = "Season 1",
            overview = "S1 overview",
            number = 1,
            episodes = listOf(dummyEpisode)
        )
        val dummySeason2 = Season(
            id = 101,
            title = "Season 2",
            overview = "S2 overview",
            number = 2,
            episodes = listOf(dummyEpisode.copy(number = 1, seasonNumber = 2))
        )
        val dummyImages = listOf("/img1.jpg", "/img2.jpg")
        const val dummyTrailer = "http://trailer.url/video.mp4"
    }
}
