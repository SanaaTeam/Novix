package com.sanaa.presentation.screen.episode_details

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.model.toActorUiModel
import com.sanaa.presentation.model.toEpisodeUiModel
import details.usecase.ManageEpisodeDetailsUseCase
import details.usecase.ManageTvSeriesDetailsUseCase
import entity.Actor
import entity.Actor.Gender
import entity.Episode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase = mockk(relaxed = true)
    private val manageTvSeriesDetails: ManageTvSeriesDetailsUseCase = mockk(relaxed = true)
    private lateinit var viewModel: EpisodeDetailsScreenViewModel
    private val seriesId = 5
    private val seasonNumber = 2
    private val episodeNumber = 3

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun `onBackClick emits NavigateBack`() = runTest {
        givenHappyViewModel()
        viewModel.onBackClick()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onGenreTypeClick emits NavigateToActorDetails`() = runTest {
        givenHappyViewModel()
        val id = 7
        viewModel.onGenreTypeClick(id)
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsEffects.NavigateToActorDetails(id))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onCastClick emits NavigateToActorDetails`() = runTest {
        givenHappyViewModel()
        val actorId = 11
        viewModel.onCastClick(actorId)
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsEffects.NavigateToActorDetails(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSavedClick and onRateClicked toggle login bottom sheet`() = runTest {
        givenHappyViewModel()
        viewModel.onSavedClick(seriesId)
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
        viewModel.onRateClicked()
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet sets showLoginBottomSheet to false`() = runTest {
        givenHappyViewModel()
        viewModel.onSavedClick(seriesId)
        viewModel.onDismissBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    private fun givenHappyViewModel() {
        coEvery { manageEpisodeDetails.getEpisodeDetails(seriesId, seasonNumber, episodeNumber) } returns dummyEpisode
        coEvery { manageEpisodeDetails.getEpisodeGuestsOfHonor(seriesId, seasonNumber, episodeNumber) } returns dummyGuests
        coEvery { manageTvSeriesDetails.getTvSeriesImages(seriesId) } returns dummyImages
        coEvery { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) } returns dummyTrailer
        viewModel = EpisodeDetailsScreenViewModel(
            seriesId, seasonNumber, episodeNumber,
            manageEpisodeDetails, manageTvSeriesDetails
        )
    }

    companion object {
        private val dummyEpisode = Episode(
            id = 100,
            title = "Episode Title",
            number = 3,
            seasonNumber = 2,
            imdbRating = 8.5f,
            overview = "Overview",
            durationMinutes = 45,
            releaseDate = LocalDate.parse("2021-06-01"),
            stillImagePath = "/path.jpg"
        )
        private val dummyGuests = listOf(
            Actor(
                id = 11,
                imageUrl = "/icon_placeholder_light.xml",
                name = "Guest One",
                region = "US",
                lastShow = "Show",
                gender = Gender.FEMALE,
                department = "Acting",
                character = "Role",
                birthDate = LocalDate.parse("1990-01-01"),
                deathDate = null,
                placeOfBirth = "City",
                biography = "Bio"
            )
        )
        private val dummyImages = listOf("/img1.jpg", "/img2.jpg")
        private const val dummyTrailer = "http://trailer.mp4"
    }
}
