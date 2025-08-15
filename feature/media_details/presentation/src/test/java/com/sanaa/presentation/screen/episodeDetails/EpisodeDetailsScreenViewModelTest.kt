package com.sanaa.presentation.screen.episodeDetails

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.util.DateTimeUtils.defaultDate
import entity.Actor
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
import usecase.ManageTvShowUseCase
import usecase.manageEpisodeDetailsUseCase.AddTvEpisodeRateUseCase
import usecase.manageEpisodeDetailsUseCase.GetEpisodeDetailsUseCase
import usecase.manageEpisodeDetailsUseCase.GetEpisodeGuestsOfHonorUseCase
import usecase.manageEpisodeDetailsUseCase.GetEpisodeImagesUseCase


@OptIn(ExperimentalCoroutinesApi::class)
class EpisodeDetailsScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val getUser = mockk<GetLoggedInUserUseCase>(relaxed = true)
    private val checkUserLogin = mockk<CheckIfUserIsLoggedInUseCase>(relaxed = true)
    private val getEpisodeDetailsUseCase: GetEpisodeDetailsUseCase = mockk(relaxed = true)
    private val getEpisodeGuestsOfHonorUseCase: GetEpisodeGuestsOfHonorUseCase = mockk(relaxed = true)
    private val getEpisodeImagesUseCase: GetEpisodeImagesUseCase = mockk(relaxed = true)
    private val addTvEpisodeRateUseCase: AddTvEpisodeRateUseCase = mockk(relaxed = true)
    private val manageTvShowDetails: ManageTvShowUseCase = mockk(relaxed = true)
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
        viewModel.onSavedClick(tvShowId)
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
        viewModel.onRateClicked()
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }

    @Test
    fun `onDismissBottomSheet sets showLoginBottomSheet to false`() = runTest {
        givenHappyViewModel()
        viewModel.onSavedClick(tvShowId)
        viewModel.onDismissBottomSheet()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
    }

    @Test
    fun `onRatingChanged updates rating`() = runTest {
        givenHappyViewModel()
        viewModel.onRatingChanged(4)
        assertThat(viewModel.state.value.imdbRating).isEqualTo(4)
    }

    private fun givenHappyViewModel() {
        coEvery { getEpisodeDetailsUseCase(tvShowId, seasonNumber, episodeNumber) } returns dummyEpisode
        coEvery { getEpisodeGuestsOfHonorUseCase(tvShowId, seasonNumber, episodeNumber) } returns dummyGuests
        coEvery { getEpisodeImagesUseCase(tvShowId, seasonNumber, episodeNumber, any()) } returns dummyImages
        coEvery { addTvEpisodeRateUseCase(any(), any(), any(), any()) } returns true
        coEvery { manageTvShowDetails.getTvShowTrailer(tvShowId) } returns dummyTrailer
        coEvery { manageTvShowDetails.getTvShowImageUrls(tvShowId) } returns dummyImages
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(false)
        coEvery { getUser.getLoggedInUser() } returns flowOf(mockk())
        coEvery { manageTvShowDetails.getEpisodesRate(any(), any(), any()) } returns 0

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )

        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle,
            getUser,
            checkUserLogin,
            getEpisodeDetailsUseCase,
            getEpisodeGuestsOfHonorUseCase,
            getEpisodeImagesUseCase,
            addTvEpisodeRateUseCase,
            manageTvShowDetails,
            dispatcher = testDispatcher
        )
    }

    @Test
    fun `onLoginButtonClick emits NavigateToLogin and hides BottomSheet`() = runTest {
        givenHappyViewModel()
        viewModel.onSavedClick(tvShowId)
        viewModel.onLoginButtonClick()
        assertThat(viewModel.state.value.showLoginBottomSheet).isFalse()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(EpisodeDetailsEffects.NavigateToLogin)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRateClicked when user is logged in shows RateBottomSheet`() = runTest {
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(true)
        coEvery { manageTvShowDetails.getEpisodesRate(any(), any(), any()) } returns 0
        coEvery { getEpisodeDetailsUseCase(any(), any(), any()) } returns dummyEpisode
        coEvery { getEpisodeGuestsOfHonorUseCase(any(), any(), any()) } returns emptyList()
        coEvery { getEpisodeImagesUseCase(any(), any(), any(), any()) } returns emptyList()
        coEvery { manageTvShowDetails.getTvShowTrailer(any()) } returns "trailer"

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )

        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle,
            getUser,
            checkUserLogin,
            getEpisodeDetailsUseCase,
            getEpisodeGuestsOfHonorUseCase,
            getEpisodeImagesUseCase,
            addTvEpisodeRateUseCase,
            manageTvShowDetails,
            dispatcher = testDispatcher
        )

        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.onRateClicked()
        assertThat(viewModel.state.value.showRateBottomSheet).isTrue()
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
    fun `onRetryLoadDetails reloads episode and resets flags`() = runTest {
        givenHappyViewModel()
        viewModel.onRetryLoadDetails()
        testDispatcher.scheduler.advanceUntilIdle()
        assertThat(viewModel.state.value.noInternetConnection).isFalse()
        assertThat(viewModel.state.value.isLoading).isFalse()
        assertThat(viewModel.state.value.error).isNull()
    }

    @Test
    fun `loadEpisode handles NoNetworkException`() = runTest {
        coEvery { getEpisodeDetailsUseCase(any(), any(), any()) } throws NoNetworkException()
        coEvery { checkUserLogin.isLoggedIn() } returns flowOf(false)

        val savedStateHandle = SavedStateHandle(
            mapOf(
                "tvShowId" to tvShowId,
                "seasonNumber" to seasonNumber,
                "episodeNumber" to episodeNumber
            )
        )

        viewModel = EpisodeDetailsScreenViewModel(
            savedStateHandle,
            getUser,
            checkUserLogin,
            getEpisodeDetailsUseCase,
            getEpisodeGuestsOfHonorUseCase,
            getEpisodeImagesUseCase,
            addTvEpisodeRateUseCase,
            manageTvShowDetails,
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