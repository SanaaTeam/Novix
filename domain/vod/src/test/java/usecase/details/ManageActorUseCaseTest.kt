package usecase.details

import com.google.common.truth.Truth.assertThat
import entity.Actor
import entity.Genre
import entity.Movie
import entity.TvSeries
import exceptions.NovixAppException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import repository.ActorRepository
import usecase.ManageActorUseCase
import kotlin.time.Duration.Companion.minutes

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
        coEvery { actorRepository.getActorDetails(actorId) } throws NovixAppException("Error")
        assertThrows<NovixAppException> {
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
        coEvery { actorRepository.getActorTopMovies(actorId) } throws NovixAppException(
            "Error"
        )
        assertThrows<NovixAppException> {
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
        coEvery { actorRepository.getActorTopTvShows(actorId) } throws NovixAppException(
            "Error"
        )
        assertThrows<NovixAppException> {
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
        coEvery { actorRepository.getGalleryImageUrls(actorId) } throws NovixAppException(
            "Error"
        )
        assertThrows<NovixAppException> {
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
                actorId, 10
            )
        } throws NovixAppException(
            "Error"
        )
        assertThrows<NovixAppException> {
            manageActorDetailsUseCase.getProfileImages(actorId)
        }
    }

    @Test
    fun `getTrendingActors should call repository and return list of actors`() = runTest {
        val trendingActors = listOf(dummyActor, dummyActor.copy(id = 2, name = "Jane Smith"))
        coEvery { actorRepository.getTrendingActors(1) } returns trendingActors

        val result = manageActorDetailsUseCase.getTrendingActors(1)

        assertThat(result).isEqualTo(trendingActors)
    }

    @Test
    fun `getTrendingActors should throw when repository fails`() = runTest {
        coEvery { actorRepository.getTrendingActors(1) } throws NovixAppException(
            "Error fetching trending actors"
        )

        assertThrows<NovixAppException> {
            manageActorDetailsUseCase.getTrendingActors(1)
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
            deathDate = LocalDate(1, 1, 1),
            placeOfBirth = "Somewhere, USA",
            biography = "A short bio text.",
            department = "Acting"
        )
        private val action = Genre(
            id = 1, name = "Action"
        )
        private val drama = Genre(
            id = 2, name = "Drama"
        )
        private val dummyMovies = listOf(
            Movie(
                id = 1,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster1.jpg",
                title = "Blockbuster One",
                genres = listOf(action),
                imdbRating = 8.6f,
                duration = 137.minutes,
                releaseDate = LocalDate(2023, 5, 12),
                overview = "A big summer action film.",
                rating = 0
            ), Movie(
                id = 2,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/poster2.jpg",
                title = "Critically-Acclaimed Drama",
                genres = listOf(drama),
                imdbRating = 8.2f,
                duration = 126.minutes,
                releaseDate = LocalDate(2022, 11, 3),
                overview = "An award-winning character study.",
                rating = 0
            )
        )
        private val sciFi = Genre(
            id = 3, name = "Science Fiction"
        )
        private val crime = Genre(
            id = 4, name = "Crime"
        )
        private val dummySeries = listOf(
            TvSeries(
                id = 101,
                title = "Future Worlds",
                overview = "High-concept science-fiction drama.",
                releaseDate = LocalDate(2021, 9, 10),
                genres = listOf(sciFi),
                imdbRating = 8.9f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series1.jpg",
                seasonsCount = 1,
                rating = 0
            ), TvSeries(
                id = 102,
                title = "City Shadows",
                overview = "Gritty crime thriller.",
                releaseDate = LocalDate(2020, 2, 1),
                genres = listOf(crime),
                imdbRating = 8.5f,
                posterImageUrl = "https://image.tmdb.org/t/p/w500/series2.jpg",
                seasonsCount = 1,
                rating = 0
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
