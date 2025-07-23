package usecase.details

import com.google.common.truth.Truth.assertThat
import repository.ActorRepository
import entity.Actor
import entity.Genre
import entity.Movie
import entity.TvSeries
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import usecase.ManageActorUseCase

class ManageActorUseCaseTest {

    private val actorRepository: ActorRepository = mockk(relaxed = true)
    private lateinit var manageActorDetailsUseCase: ManageActorUseCase

    @BeforeEach
    fun setUp() {
        manageActorDetailsUseCase = ManageActorUseCase(actorRepository)
    }

    @Test
    fun `getActorDetails should call repository and return Actor`() = runTest {
        val actorId = 1
        coEvery { actorRepository.getActorDetails(actorId) } returns dummyActor
        val result = manageActorDetailsUseCase.getActorDetails(actorId)
        assertThat(result).isEqualTo(dummyActor)
    }

    @Test
    fun `getActorDetails should throw when repository fails`() = runTest {
        val actorId = 2
        coEvery { actorRepository.getActorDetails(actorId) } throws RetrievingDataFailureException("Error")
        assertThrows<RetrievingDataFailureException> {
            manageActorDetailsUseCase.getActorDetails(actorId)
        }
    }

    @Test
    fun `getActorTopMovies should call repository and return list`() = runTest {
        val actorId = 3
        coEvery { actorRepository.getActorTopMovies(actorId) } returns dummyMovies
        val result = manageActorDetailsUseCase.getActorTopMovies(actorId)
        assertThat(result).isEqualTo(dummyMovies)
    }

    @Test
    fun `getActorTopMovies should throw when repository fails`() = runTest {
        val actorId = 4
        coEvery { actorRepository.getActorTopMovies(actorId) } throws RetrievingDataFailureException(
            "Error"
        )
        assertThrows<RetrievingDataFailureException> {
            manageActorDetailsUseCase.getActorTopMovies(actorId)
        }
    }

    @Test
    fun `getActorTopTvSeries should call repository and return list`() = runTest {
        val actorId = 5
        coEvery { actorRepository.getActorTopTvShows(actorId) } returns dummySeries
        val result = manageActorDetailsUseCase.getActorTopTvSeries(actorId)
        assertThat(result).isEqualTo(dummySeries)
    }

    @Test
    fun `getActorTopTvSeries should throw when repository fails`() = runTest {
        val actorId = 6
        coEvery { actorRepository.getActorTopTvShows(actorId) } throws RetrievingDataFailureException(
            "Error"
        )
        assertThrows<RetrievingDataFailureException> {
            manageActorDetailsUseCase.getActorTopTvSeries(actorId)
        }
    }

    @Test
    fun `getGalleryImages should call repository and return list`() = runTest {
        val actorId = 7
        coEvery { actorRepository.getGalleryImageUrls(actorId) } returns dummyGallery
        val result = manageActorDetailsUseCase.getGalleryImages(actorId)
        assertThat(result).isEqualTo(dummyGallery)
    }

    @Test
    fun `getGalleryImages should throw when repository fails`() = runTest {
        val actorId = 8
        coEvery { actorRepository.getGalleryImageUrls(actorId) } throws RetrievingDataFailureException(
            "Error"
        )
        assertThrows<RetrievingDataFailureException> {
            manageActorDetailsUseCase.getGalleryImages(actorId)
        }
    }

    @Test
    fun `getProfileImages should call repository and return list`() = runTest {
        val actorId = 9
        coEvery { actorRepository.getProfileImageUrls(actorId, 10) } returns dummyProfiles
        val result = manageActorDetailsUseCase.getProfileImages(actorId)
        assertThat(result).isEqualTo(dummyProfiles)
    }

    @Test
    fun `getProfileImages should throw when repository fails`() = runTest {
        val actorId = 10
        coEvery {
            actorRepository.getProfileImageUrls(
                actorId,
                10
            )
        } throws RetrievingDataFailureException(
            "Error"
        )
        assertThrows<RetrievingDataFailureException> {
            manageActorDetailsUseCase.getProfileImages(actorId)
        }
    }
    @Test
    fun `getTrendingActors should call repository and return list of actors`() = runTest {
        val trendingActors = listOf(dummyActor, dummyActor.copy(id = 2, name = "Jane Smith"))
        coEvery { actorRepository.getTrendingActors() } returns trendingActors

        val result = manageActorDetailsUseCase.getTrendingActors()

        assertThat(result).isEqualTo(trendingActors)
    }

    @Test
    fun `getTrendingActors should throw when repository fails`() = runTest {
        coEvery { actorRepository.getTrendingActors() } throws RetrievingDataFailureException(
            "Error fetching trending actors"
        )

        assertThrows<RetrievingDataFailureException> {
            manageActorDetailsUseCase.getTrendingActors()
        }
    }

    companion object {
        private val dummyActor = Actor(
            id = 1,
            imageUrl = "https://image.tmdb.org/t/p/w500/xyz.jpg",
            name = "John Doe",
            region = "US",
            lastShow = "Some Movie (2024)",
            gender = Actor.Gender.MALE,
            character = "Acting",
            birthDate = LocalDate(1980, 1, 1),
            deathDate = null,
            placeOfBirth = "Somewhere, USA",
            biography = "A short bio text.",
            department = "Acting"
        )
        private val action = Genre.ACTION
        private val drama = Genre.DRAMA
        private val dummyMovies = listOf(
            Movie(
                id = 1,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster1.jpg",
                title = "Blockbuster One",
                genres = listOf(action),
                imdbRating = 8.6f,
                duration = 137,
                releaseDate = LocalDate(2023, 5, 12),
                overview = "A big summer action film."
            ),
            Movie(
                id = 2,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster2.jpg",
                title = "Critically-Acclaimed Drama",
                genres = listOf(drama),
                imdbRating = 8.2f,
                duration = 126,
                releaseDate = LocalDate(2022, 11, 3),
                overview = "An award-winning character study."
            )
        )
        private val sciFi = Genre.SCIENCE_FICTION
        private val crime = Genre.CRIME
        private val dummySeries = listOf(
            TvSeries(
                id = 101,
                title = "Future Worlds",
                overview = "High-concept science-fiction drama.",
                releaseDate = LocalDate(2021, 9, 10),
                genres = listOf(sciFi),
                imdbRating = 8.9f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series1.jpg",
                seasonsCount = 1
            ),
            TvSeries(
                id = 102,
                title = "City Shadows",
                overview = "Gritty crime thriller.",
                releaseDate = LocalDate(2020, 2, 1),
                genres = listOf(crime),
                imdbRating = 8.5f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series2.jpg",
                seasonsCount = 1
            )
        )
        private val dummyGallery = listOf(
            "https://image.tmdb.org/t/p/w780/backdrop1.jpg",
            "https://image.tmdb.org/t/p/w780/backdrop2.jpg",
            "https://image.tmdb.org/t/p/w780/backdrop3.jpg"
        )
        private val dummyProfiles = listOf(
            "https://image.tmdb.org/t/p/w500/profile1.jpg",
            "https://image.tmdb.org/t/p/w500/profile2.jpg",
            "https://image.tmdb.org/t/p/w500/profile3.jpg"
        )
    }
}
