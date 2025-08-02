package com.sanaa.vod.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.identity.dataSoruce.local.dataStore.PreferencesManager
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.AuthorDetailsDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.RatingResponse
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.fake.FakeData.MoviesDtoList
import com.sanaa.vod.util.exceptions.ConnectionException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private val preferences: PreferencesManager = mockk()
    private val remoteMovieDataSource: RemoteMovieDataSource =
        mockk<RemoteMovieDataSource>(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = MovieRepositoryImpl(remoteMovieDataSource, preferences)
    }

    @Test
    fun `getMovieDetails returns expected movie`() = runTest {
        coEvery { remoteMovieDataSource.fetchMovieDetails(1) } returns sampleMovieDto

        val result = repository.getMovieDetails(1)

        assertThat(result.title).isEqualTo("Fight club")
    }

    @Test
    fun `getImages returns top images poster urls`() = runTest {
        coEvery { remoteMovieDataSource.fetchImagesUrl(1) } returns listOf(sampleImagesDto)

        val result = repository.getImageUrls(1, 2)

        assertThat(result.size).isEqualTo(1)
    }

    @Test
    fun `getMovieCast returns cast list`() = runTest {
        coEvery { remoteMovieDataSource.fetchCast(1) } returns sampleCastDto

        val result = repository.getMovieCast(1)

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getSimilarMoviesByMovieId returns similar movies`() = runTest {
        coEvery { remoteMovieDataSource.fetchSimilarMoviesByMovieId(1, 1) } returns sampleSimilarDto

        val result = repository.getSimilarMoviesByMovieId(1, 1)

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getReviewsByMovieId returns reviews`() = runTest {
        coEvery {
            remoteMovieDataSource.fetchReviewsByMovieId(
                1,
                1
            )
        } returns listOf(sampleReviewDto)

        val result = repository.getReviewsByMovieId(1, 1)

        assertThat(result.first().authorName).isEqualTo("Critic A")
    }

    @Test
    fun `getMovieDetails throws NoNetworkException on ConnectionException`() = runTest {
        coEvery { remoteMovieDataSource.fetchMovieDetails(any()) } throws ConnectionException()

        assertThrows<NoNetworkException> { repository.getMovieDetails(1) }
    }

    @Test
    fun `getImages throws RetrievingDataFailureException on unknown error`() = runTest {
        coEvery { remoteMovieDataSource.fetchImagesUrl(any()) } throws RuntimeException("some error")

        assertThrows<RetrievingDataFailureException> { repository.getImageUrls(1, 2) }
    }

    @Test
    fun `getMoviesByCategory should return list of MovieDto `() = runTest {
        coEvery {
            remoteMovieDataSource.fetchMoviesByCategory(
                any(),
                any()
            )
        } returns sampleSimilarDto

        val result = repository.getMoviesByCategory(1, 1)

        assertThat(result).isNotEmpty()
    }


    @Test
    fun `getMovieTrailer returns null when no YouTube trailer found`() = runTest {
        coEvery { remoteMovieDataSource.fetchMovieTrailerUrl(1) } returns trailers

        val result = repository.getMovieTrailer(1)

        assertThat(result).isNull()
    }

    @Test
    fun `getMovieTrailer returns null when empty list returned`() = runTest {
        coEvery { remoteMovieDataSource.fetchMovieTrailerUrl(1) } returns emptyList()

        val result = repository.getMovieTrailer(1)

        assertThat(result).isNull()
    }

    @Test
    fun `getPopularMovies returns empty list`() = runTest {
        val result = repository.getPopularMovies(1)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTopRatedMovies returns empty list`() = runTest {
        val result = repository.getTopRatedMovies(page = 1, genreId = 1)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getUpcomingMovies returns empty list`() = runTest {
        val result = repository.getUpcomingMovies(1, null)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getTrendingMovies returns empty list`() = runTest {
        val result = repository.getTrendingMovies(page = 1, genreId = 1)
        assertThat(result).isEmpty()
    }

    @Test
    fun `getMovieGenres returns list of genres`() = runTest {
        coEvery { remoteMovieDataSource.fetchMovieGenres() } returns dummyGenresDto

        val result = repository.getMovieGenres()

        assertThat(result).isNotEmpty()
    }


    @Test
    fun `getMoviesRate should returns empty list when there is no data return from sever`() =
        runTest {
            // Given
            val accountId = 1L
            val sessionId = "3434"
            coEvery {
                remoteMovieDataSource.fetchMoviesRate(
                    accountId = accountId,
                    sessionId = sessionId
                )
            } returns emptyList()

            coEvery { preferences.sessionId } returns flowOf(sessionId)

            // When
            val result = repository.getMoviesRate(accountId = accountId)

            // Then
            assertThat(result).isEmpty()
        }

    @Test
    fun `getMoviesRate should returns data as movies list when data return from server`() =
        runTest {
            // Given
            val accountId = 1L
            val sessionId = "43546"
            coEvery {
                remoteMovieDataSource.fetchMoviesRate(
                    accountId = accountId,
                    sessionId = sessionId
                )
            } returns MoviesDtoList

            coEvery { preferences.sessionId } returns flowOf(sessionId)

            // When
            val resultIds = repository.getMoviesRate(accountId = accountId).map { it.id }
            val expectedIds = MoviesDtoList.map { it.id }

            // Then
            assertThat(resultIds).isEqualTo(expectedIds)
        }

    @Test
    fun `addMovieRate should call RemoteMovieDataSource send movie rate when try to add movie rate`() =
        runTest {
            // Given
            val movieId = 4324
            val rating = 9f
            val sessionId = "sessionId"
            coEvery { preferences.sessionId } returns flowOf(sessionId)

            repository.addMovieRate(movieId, rating)

            coVerify { remoteMovieDataSource.sendMovieRate(any(), any(), any()) }
        }

    @Test
    fun `addMovieRate should return true when the rate sent to the server`() =
        runTest {
            // Given
            val movieId = 4324
            val rating = 9f
            val sessionId = "sessionId"
            coEvery { preferences.sessionId } returns flowOf(sessionId)
            coEvery {
                remoteMovieDataSource.sendMovieRate(
                    any(),
                    any(),
                    any()
                )
            } returns RatingResponse(true, 200, "Success")

            val result = repository.addMovieRate(movieId, rating)
            assertThat(result).isTrue()
        }

    @Test
    fun `addMovieRate should return flase when rate sent to the server failed`() =
        runTest {
            // Given
            val movieId = 4324
            val rating = 9f
            val sessionId = "sessionId"
            coEvery { preferences.sessionId } returns flowOf(sessionId)
            coEvery {
                remoteMovieDataSource.sendMovieRate(
                    any(),
                    any(),
                    any()
                )
            } returns RatingResponse(false, 402, "Failed")

            val result = repository.addMovieRate(movieId, rating)
            assertThat(result).isFalse()
        }

    companion object {
        private val sampleMovieDto = MovieDto(id = 1, title = "Fight club")
        private val sampleImagesDto = ImageDto(filePath = "/poster1.jpg")

        private val sampleCastDto = listOf<ActorDto>(
            ActorDto(name = "Actor A", id = 1), ActorDto(
                name = "Actor B", id = 2,
            )
        )

        private val sampleSimilarDto = listOf<MovieDto>(
            MovieDto(
                id = 1, title = "A"
            ), MovieDto(
                id = 2, title = "B"
            )
        )

        private val sampleReviewDto = ReviewDto(
            id = "1", content = "Nice movie!", authorDetails = AuthorDetailsDto(
                name = "Critic A", username = "critic_a", avatarPath = "/avatar.jpg", rating = 8.5f
            ), createdAt = "2024-01-01T00:00:00Z"
        )
        val trailers = listOf(
            VideoDto(type = "Teaser", site = "YouTube", key = "def456"),
            VideoDto(type = "Trailer", site = "Vimeo", key = "ghi789")
        )
        val dummyGenresDto = listOf(
            GenreDto(id = 1, name = "Action"),
            GenreDto(id = 2, name = "Drama")
        )

    }
}