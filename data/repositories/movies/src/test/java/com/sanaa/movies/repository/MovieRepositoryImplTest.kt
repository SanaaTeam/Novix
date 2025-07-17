package com.sanaa.movies.repository

import com.google.common.truth.Truth.assertThat
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.remote.dto.CastDto
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.net.UnknownHostException

class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private val remote: MovieDetailsRemoteDataSource = mockk(relaxed = true)

    @BeforeEach
    fun setUp() {
        repository = MovieRepositoryImpl(remote)
    }

    @Test
    fun `getMovieDetails returns expected movie`() = runTest {
        coEvery { remote.fetchMovieDetails(1) } returns sampleMovieDto

        val result = repository.getMovieDetails(1)

        assertThat(result.title).isEqualTo("Sample Title")
    }

    @Test
    fun `getImages returns top 3 poster urls`() = runTest {
        coEvery { remote.fetchImagesUrl(1) } returns sampleImagesDto

        val result = repository.getImages(1)

        assertThat(result.size).isEqualTo(3)
    }

    @Test
    fun `getMovieCast returns cast list`() = runTest {
        coEvery { remote.fetchCast(1) } returns sampleCastDto

        val result = repository.getMovieCast(1)

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].name).isEqualTo("Actor A")
    }

    @Test
    fun `getSimilarMoviesByMovieId returns similar movies`() = runTest {
        coEvery { remote.fetchSimilarMoviesByMovieId(1) } returns sampleSimilarDto

        val result = repository.getSimilarMoviesByMovieId(1)

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getReviewsByMovieId returns reviews`() = runTest {
        coEvery { remote.fetchReviewsByMovieId(1) } returns sampleReviewDto

        val result = repository.getReviewsByMovieId(1)

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `getMovieDetails throws NoNetworkException on UnknownHostException`() = runTest {
        coEvery { remote.fetchMovieDetails(any()) } throws UnknownHostException()

        assertThrows<NoNetworkException> { repository.getMovieDetails(1) }
    }

    @Test
    fun `getImages throws RetrievingDataFailureException on unknown error`() = runTest {
        coEvery { remote.fetchImagesUrl(any()) } throws RuntimeException()

        assertThrows<RetrievingDataFailureException> { repository.getImages(1) }
    }

    companion object {
        private val sampleMovieDto = MovieDetailsDto(
            id = 1,
            title = "Sample Title",
        )

        private val sampleImagesDto = MovieImagesDto(
            id = 1,
            posters = listOf(
                MovieImagesDto.ImageItemDto(filePath = "/poster1.jpg"),
                MovieImagesDto.ImageItemDto(filePath = "/poster1.jpg"),
                MovieImagesDto.ImageItemDto(filePath = "/poster1.jpg"),
                MovieImagesDto.ImageItemDto(filePath = "/poster1.jpg"),
            ),
            backdrops = emptyList(),
            logos = emptyList()
        )

        private val sampleCastDto = CastDto(
            id = 1,
            cast = arrayListOf(
                CastDto.Cast(name = "Actor A", id = 1),
                CastDto.Cast(name = "Actor B", id = 2)
            ),
            crew = arrayListOf()
        )

        private val sampleSimilarDto = SimilarMoviesDto(
            page = 1,
            results = arrayListOf(
                SimilarMoviesDto.Results(title = "Similar A", id = 1),
                SimilarMoviesDto.Results(title = "Similar B", id = 2)
            ),
            totalPages = 1,
            totalResults = 2
        )

        private val sampleReviewDto = ReviewDto(
            id = 1,
            page = 1,
            totalPages = 1,
            totalResults = 2,
            results = arrayListOf(
                ReviewDto.Results(author = "Critic A", content = "Nice", id = "1"),
                ReviewDto.Results(author = "Critic B", content = "Meh", id = "2")
            )
        )
    }
}