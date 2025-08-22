package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import entity.Actor
import entity.Episode
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import service.VodStringProvider
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvShowUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase = mockk(relaxed = true)
    private val manageTvShowDetails: ManageTvShowUseCase = mockk(relaxed = true)
    private val stringProvider: VodStringProvider = mockk(relaxed = true)
    private lateinit var viewModel: EpisodeDetailsScreenViewModel
    private val tvShowId = 5
    private val seasonNumber = 2
    private val episodeNumber = 3

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun givenHappyViewModel() {
        coEvery {
            manageEpisodeDetails.getEpisodeDetails(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        } returns dummyEpisode
        coEvery {
            manageEpisodeDetails.getEpisodeGuestsOfHonor(
                tvShowId,
                seasonNumber,
                episodeNumber
            )
        } returns dummyGuests
        coEvery { manageTvShowDetails.getTvShowImageUrls(tvShowId) } returns dummyImages
        coEvery { manageTvShowDetails.getTvShowTrailer(tvShowId) } returns dummyTrailer

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )

        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle,
            manageEpisodeDetails,
            manageTvShowDetails,
            stringProvider,
            dispatcher = testDispatcher
        )

        testDispatcher.scheduler.advanceUntilIdle()
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
            stillImagePath = "/path.jpg",
            rating = 0
        )
        private val dummyGuests = listOf(
            Actor(
                id = 11,
                imageUrl = "/icon_placeholder_light.xml",
                name = "Guest One",
                department = "Acting",
                character = "Role",
                birthDate = LocalDate.parse("1990-01-01"),
                deathDate = defaultDate,
                placeOfBirth = "City",
                biography = "Bio"
            )
        )
        private val dummyImages = listOf("/img1.jpg", "/img2.jpg")
        private const val dummyTrailer = "http://trailer.mp4"
    }
}