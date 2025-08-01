package com.sanaa.presentation.screen.actors

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.sanaa.presentation.screen.actor.ActorScreenEffects
import com.sanaa.presentation.screen.actor.ActorViewModel
import entity.Actor
import entity.Actor.Gender
import entity.Movie
import entity.TvSeries
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import usecase.ManageActorUseCase
import kotlin.time.Duration.Companion.minutes

@OptIn(ExperimentalCoroutinesApi::class)
class ActorViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val manageActorDetailsUseCase: ManageActorUseCase = mockk(relaxed = true)
    private lateinit var viewModel: ActorViewModel
    private val actorId = 77

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `onBackClicked emits NavigateBack`() = runTest {
        givenHappyViewModel()
        viewModel.onBackClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorScreenEffects.NavigateBack)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTopMoviesClicked emits NavigateToTopMovies`() = runTest {
        givenHappyViewModel()
        viewModel.onTopMoviesClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorScreenEffects.NavigateToTopMovies(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onTopSeriesClicked emits NavigateToTopSeries`() = runTest {
        givenHappyViewModel()
        viewModel.onTopSeriesClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorScreenEffects.NavigateToTopSeries(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onViewAllGalleryClicked emits NavigateToGallery`() = runTest {
        givenHappyViewModel()
        viewModel.onViewAllGalleryClicked()
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorScreenEffects.NavigateToGallery(actorId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSeriesClicked emits NavigateToSeriesDetails`() = runTest {
        givenHappyViewModel()
        val seriesId = 123
        viewModel.onSeriesClicked(seriesId)
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorScreenEffects.NavigateToSeriesDetails(seriesId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onMovieClicked emits NavigateToMovieDetails`() = runTest {
        givenHappyViewModel()
        val movieId = 456
        viewModel.onMovieClicked(movieId)
        viewModel.effect.test {
            assertThat(awaitItem()).isEqualTo(ActorScreenEffects.NavigateToMovieDetails(movieId))
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSaveClicked sets showLoginBottomSheet to true`() = runTest {
        givenHappyViewModel()
        viewModel.onSaveClicked()
        assertThat(viewModel.state.value.showLoginBottomSheet).isTrue()
    }

    private fun givenHappyViewModel() {
        coEvery { manageActorDetailsUseCase.getActorDetails(actorId) } returns dummyActor
        coEvery { manageActorDetailsUseCase.getActorTopMovies(actorId) } returns dummyMovies
        coEvery { manageActorDetailsUseCase.getActorTopTvSeries(actorId) } returns dummySeries
        coEvery { manageActorDetailsUseCase.getGalleryImages(actorId) } returns dummyGallery
        coEvery { manageActorDetailsUseCase.getProfileImages(actorId) } returns dummyProfiles

        val savedStateHandle = SavedStateHandle(mapOf("actorId" to actorId))

        viewModel = ActorViewModel(savedStateHandle, manageActorDetailsUseCase)
    }

    companion object {
        private val dummyActor = Actor(
            id = 77,
            imageUrl = "/icon_placeholder_light.xml",
            name = "Jane Doe",
            region = "UK",
            lastShow = "Fabulous Show",
            gender = Gender.FEMALE,
            department = "Acting",
            character = "Detective",
            birthDate = LocalDate.parse("1990-01-01"),
            deathDate = null,
            placeOfBirth = "London",
            biography = "A very long biography…"
        )

        private val dummyMovies = listOf(
            Movie(
                id = 1,
                posterImageUrl = "/m1.jpg",
                title = "Movie One",
                genres = emptyList(),
                imdbRating = 8.3f,
                duration = 120.minutes,
                releaseDate = LocalDate.parse("2020-01-01"),
                overview = "Overview",
                rating = 0
            ),
            Movie(
                id = 2,
                posterImageUrl = "/m2.jpg",
                title = "Movie Two",
                genres = emptyList(),
                imdbRating = 7.9f,
                duration = 118.minutes,
                releaseDate = LocalDate.parse("2022-09-20"),
                overview = "Overview",
                rating = 0
            )
        )

        private val dummySeries = listOf(
            TvSeries(
                id = 3,
                title = "Series One",
                overview = "Overview",
                releaseDate = LocalDate.parse("2019-05-12"),
                genres = emptyList(),
                imdbRating = 8.1f,
                posterImageUrl = "/s1.jpg",
                seasonsCount = 2,
                rating = 0
            )
        )

        private val dummyGallery = listOf("/g1.jpg", "/g2.jpg")
        private val dummyProfiles = listOf("/p1.jpg", "/p2.jpg")
    }
}
