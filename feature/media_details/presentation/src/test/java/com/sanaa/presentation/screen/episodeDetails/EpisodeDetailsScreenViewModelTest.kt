package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Actor.Gender
import entity.Episode
import exceptions.NoNetworkException
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.CheckIfUserIsLoggedInUseCase
import usecase.GetLoggedInUserUseCase
import usecase.ManageEpisodeDetailsUseCase
import usecase.ManageTvSeriesUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getUser = mockk<GetLoggedInUserUseCase>(relaxed = true)
    private val checkUserLogin = mockk<CheckIfUserIsLoggedInUseCase>(relaxed = true)
    private val manageEpisodeDetails: ManageEpisodeDetailsUseCase = mockk(relaxed = true)
    private val manageTvSeriesDetails: ManageTvSeriesUseCase = mockk(relaxed = true)
    private lateinit var viewModel: EpisodeDetailsScreenViewModel
    private val seriesId = 5
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
                seriesId,
                seasonNumber,
                episodeNumber
            )
        } returns dummyEpisode
        coEvery {
            manageEpisodeDetails.getEpisodeGuestsOfHonor(
                seriesId,
                seasonNumber,
                episodeNumber
            )
        } returns dummyGuests
        coEvery { manageTvSeriesDetails.getTvSeriesImages(seriesId) } returns dummyImages
        coEvery { manageTvSeriesDetails.getTvSeriesTrailer(seriesId) } returns dummyTrailer
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(false) // default login state

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "seriesId" to seriesId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )

        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle,
            getUser,
            checkUserLogin,
            manageEpisodeDetails,
            manageTvSeriesDetails,
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

    @Test
    fun `onRatingChanged updates rating`() = runTest {
        givenHappyViewModel()
        viewModel.onRatingChanged(4)
        assertThat(viewModel.state.value.imdbRating).isEqualTo(4)
    }

    @Test
    fun `onLoginButtonClick emits NavigateToLogin and hides BottomSheet`() = runTest {
        givenHappyViewModel()
        viewModel.onSavedClick(seriesId)
        viewModel.onLoginButtonClick()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `onSubmitRateBottomSheet hides sheet and calls addRate`() = runTest {
        givenHappyViewModel()
        viewModel.onRatingChanged(7)
        viewModel.onSubmitRateBottomSheet()
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `onSubmitRateBottomSheet handles error properly`() = runTest {
        val errorMessage = "Rating failed"
        coEvery {
            manageEpisodeDetails.addTvEpisodeRate(any(), any(), any(), any())
        } throws RuntimeException(errorMessage)

        givenHappyViewModel()
        viewModel.onRatingChanged(5)
        viewModel.onSubmitRateBottomSheet()
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.error).isEqualTo(errorMessage)
        assertThat(viewModel.state.value.showRateBottomSheet).isFalse()
    }

    @Test
    fun `onRetryLoadDetails reloads episode and resets flags`() = runTest {
        coEvery {
            manageEpisodeDetails.getEpisodeDetails(any(), any(), any())
        } throws NoNetworkException()
        val savedStateHandle = SavedStateHandle(
            mapOf(
                "seriesId" to seriesId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )
        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle, getUser, checkUserLogin, manageEpisodeDetails, manageTvSeriesDetails, dispatcher = testDispatcher
        )
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.noInternetConnection).isTrue()

        coEvery {
            manageEpisodeDetails.getEpisodeDetails(any(), any(), any())
        } returns dummyEpisode
        coEvery { manageEpisodeDetails.getEpisodeGuestsOfHonor(any(), any(), any()) } returns dummyGuests
        coEvery { manageTvSeriesDetails.getTvSeriesImages(any()) } returns dummyImages
        coEvery { manageTvSeriesDetails.getTvSeriesTrailer(any()) } returns dummyTrailer
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(false)

        viewModel.onRetryLoadDetails()
        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.noInternetConnection).isFalse()
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `loadEpisode handles NoNetworkException`() = runTest {
        coEvery {
            manageEpisodeDetails.getEpisodeDetails(
                any(),
                any(),
                any()
            )
        } throws NoNetworkException()
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(false)

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "seriesId" to seriesId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )

        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle,
            getUser,
            checkUserLogin,
            manageEpisodeDetails,
            manageTvSeriesDetails,
            dispatcher = testDispatcher
        )

        testDispatcher.scheduler.advanceUntilIdle()

        assertThat(viewModel.state.value.noInternetConnection).isTrue()
        assertThat(viewModel.state.value.isLoading).isFalse()
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