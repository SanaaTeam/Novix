package com.sanaa.actors.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.actors.dataSource.remote.ActorRemoteDataSource
import com.sanaa.actors.dataSource.remote.dto.ActorDto
import com.sanaa.actors.dataSource.remote.dto.ActorImagesDto
import com.sanaa.actors.dataSource.remote.dto.ActorMovieCastDto
import com.sanaa.actors.dataSource.remote.dto.ActorTvCastDto
import com.sanaa.preferences.service.LanguageProvider
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException

class ActorRepositoryImplTest {

    private lateinit var repository: ActorRepositoryImpl
    private val remoteDataSource: ActorRemoteDataSource = mockk(relaxed = true)
    private val languageProvider: LanguageProvider = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = ActorRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `getActorDetails returns expected Actor`() = runTest {
        coEvery { remoteDataSource.getActorDetails(1) } returns sampleActorDto
        coEvery { languageProvider.getCurrentLanguage() } returns "en"

        val result = repository.getActorDetails(1)

        assertThat(result.name).isEqualTo("Tom Hanks")
        assertThat(result.region).isEqualTo(null)
    }

    @Test
    fun `getActorDetails throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remoteDataSource.getActorDetails(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorDetails(1)
        }
    }

    @Test
    fun `getActorDetails throws RetrievingDataFailureException on Exception`() = runTest {
        coEvery { remoteDataSource.getActorDetails(any()) } throws RuntimeException()

        assertThrows<RetrievingDataFailureException> {
            repository.getActorDetails(1)
        }
    }

    @Test
    fun `getProfileImages returns top 3 image URLs`() = runTest {
        coEvery { remoteDataSource.getActorImages(1) } returns sampleImagesDto

        val result = repository.getProfileImages(1)

        assertThat(result.size).isEqualTo(3)
        assertThat(result[0]).startsWith("https://image.tmdb.org/t/p/w500")
    }

    @Test
    fun `getGalleryImages skips first image`() = runTest {
        coEvery { remoteDataSource.getActorImages(1) } returns sampleImagesDto

        val result = repository.getGalleryImages(1)

        assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `getActorTopMovies returns sorted top 20`() = runTest {
        val cast = (0..25).map {
            ActorMovieCastDto.MovieCastCreditDto(
                movieId = it,
                title = "Movie $it",
                voteAverage = it.toDouble()
            )
        }
        coEvery { remoteDataSource.getActorTopMovies(1) } returns ActorMovieCastDto(1, cast)

        val result = repository.getActorTopMovies(1)

        assertThat(result.size).isEqualTo(20)
        assertThat(result[0].title).isEqualTo("Movie 25")
    }

    @Test
    fun `getActorTopTvSeries returns sorted top 20`() = runTest {
        val cast = (0..25).map {
            ActorTvCastDto.TvCastCreditDto(
                tvId = it,
                name = "TV $it",
                voteAverage = it.toDouble()
            )
        }
        coEvery { remoteDataSource.getActorTopTvSeries(1) } returns ActorTvCastDto(1, cast)

        val result = repository.getActorTopTvSeries(1)

        assertThat(result.size).isEqualTo(20)
        assertThat(result[0].title).isEqualTo("TV 25")
    }

    @Test
    fun `getGalleryImages throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getGalleryImages(1)
        }
    }

    @Test
    fun `getActorTopMovies throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorTopMovies(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorTopMovies(1)
        }
    }

    @Test
    fun `getActorTopTvSeries throws NoNetworkException when network is down`() = runTest {
        coEvery { remoteDataSource.getActorTopTvSeries(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> {
            repository.getActorTopTvSeries(1)
        }
    }

    @Test
    fun `getProfileImages throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorImages(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getProfileImages(1)
            }
        }

    @Test
    fun `getActorTopMovies throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorTopMovies(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getActorTopMovies(1)
            }
        }

    @Test
    fun `getActorTopTvSeries throws RetrievingDataFailureException when an unknown error occurs`() =
        runTest {
            coEvery { remoteDataSource.getActorTopTvSeries(any()) } throws Exception()

            assertThrows<RetrievingDataFailureException> {
                repository.getActorTopTvSeries(1)
            }
        }

    @Test
    fun `getProfileImages propagates NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> { repository.getProfileImages(42) }
    }

    @Test
    fun `getGalleryImages propagates RetrievingDataFailureException on generic error`() = runTest {
        coEvery { remoteDataSource.getActorImages(any()) } throws IllegalStateException("boom")

        assertThrows<RetrievingDataFailureException> { repository.getGalleryImages(42) }
    }

    @Test
    fun `getActorTopMovies handles null voteAverage`() = runTest {
        val cast = listOf(
            ActorMovieCastDto.MovieCastCreditDto(movieId = 1, title = "M-A", voteAverage = null),
            ActorMovieCastDto.MovieCastCreditDto(movieId = 2, title = "M-B", voteAverage = null)
        )
        coEvery { remoteDataSource.getActorTopMovies(any()) } returns ActorMovieCastDto(42, cast)

        val movies = repository.getActorTopMovies(42)

        assertThat(movies.map { it.title }).containsExactly("M-A", "M-B")
    }

    @Test
    fun `getActorTopTvSeries handles null voteAverage`() = runTest {
        val cast = listOf(
            ActorTvCastDto.TvCastCreditDto(tvId = 1, name = "S-A", voteAverage = null),
            ActorTvCastDto.TvCastCreditDto(tvId = 2, name = "S-B", voteAverage = null)
        )
        coEvery { remoteDataSource.getActorTopTvSeries(any()) } returns ActorTvCastDto(99, cast)

        val series = repository.getActorTopTvSeries(99)

        assertThat(series.map { it.title }).containsExactly("S-A", "S-B")
    }

    companion object {
        private val sampleActorDto = ActorDto(
            id = 1,
            name = "Tom Hanks",
            profileImagePath = "/img.jpg",
            knownForDepartment = "Acting",
            biography = "Biography",
            birthDay = "1956-07-09",
            deathDay = null,
            gender = 2,
            placeOfBirth = "USA",
            alsoKnownAs = listOf("Thomas Hanks")
        )

        private val sampleImagesDto = ActorImagesDto(
            id = 1,
            profiles = listOf(
                ActorImagesDto.ProfileImageDto(1.0, 500, 500, null, "/img1.jpg", 8.0, 100),
                ActorImagesDto.ProfileImageDto(1.0, 500, 500, null, "/img2.jpg", 7.0, 80),
                ActorImagesDto.ProfileImageDto(1.0, 500, 500, null, "/img3.jpg", 6.0, 60),
                ActorImagesDto.ProfileImageDto(1.0, 500, 500, null, "/img4.jpg", 5.0, 40),
            )
        )
    }
}
