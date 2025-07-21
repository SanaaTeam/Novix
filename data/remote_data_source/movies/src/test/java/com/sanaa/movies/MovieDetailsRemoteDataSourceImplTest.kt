package com.sanaa.movies

import com.sanaa.movies.dataSource.remote.dto.ActorDto
import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.response.MovieApiResponse
import com.sanaa.movies.response.MovieCastResponse
import com.sanaa.movies.response.MovieImagesResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MovieDetailsRemoteDataSourceImplTest {

    private lateinit var dataSource: MovieDetailsRemoteDataSourceImpl
    private val apiService: MovieApiService = mockk()

    @BeforeEach
    fun setup() {
        dataSource = MovieDetailsRemoteDataSourceImpl(apiService)
    }

    @Test
    fun `fetchMovieDetails() should return movie dto`() = runTest {
        coEvery { apiService.fetchMovieDetails(1) } returns dummyMovie

        val dto = dataSource.fetchMovieDetails(1)

        assertEquals(dummyMovie.id, dto.id)
    }

    @Test
    fun `fetchImagesUrl() should return list of movie images dto`() = runTest {
        coEvery { apiService.fetchImagesUrl(1) } returns MovieImagesResponse(dummyMovieImage)

        val dto = dataSource.fetchImagesUrl(1)

        assertEquals(2, dto.size)
    }

    @Test
    fun `fetchCast() should return list of actors dto`() = runTest {
        coEvery { apiService.fetchCast(1) } returns MovieCastResponse(dummyActors)

        val dto = dataSource.fetchCast(1)

        assertEquals(2, dto.size)
    }

    @Test
    fun `fetchSimilarMoviesByMovieId() should return list of movies dto`() = runTest {
        coEvery { apiService.fetchSimilarMoviesByMovieId(1) } returns MovieApiResponse<MovieDto>(
            results = listOf(dummyMovie)
        )
        val dto = dataSource.fetchSimilarMoviesByMovieId(1)

        assertEquals(1, dto.size)
    }

    @Test
    fun `fetchReviewsByMovieId() should return list of reviews dto`() = runTest {
        coEvery { apiService.fetchReviewsByMovieId(1) } returns MovieApiResponse<ReviewDto>(
            results = dummyReviews
        )

        val dto = dataSource.fetchReviewsByMovieId(1)

        assertEquals(2, dto.size)
    }

    @Test
    fun `fetchMoviesByCategory() should return list of movies dto`() = runTest {
        coEvery { apiService.fetchMoviesByCategory(1) } returns MovieApiResponse<MovieDto>(
            results = listOf(dummyMovie)
        )
        val dto = dataSource.fetchMoviesByCategory(1)

        assertEquals(1, dto.size)
    }

    @Test
    fun `fetchMovieTrailerUrl() should return list of movies dto`() = runTest {
        coEvery { apiService.fetchMovieTrailerUrl(1) } returns MovieApiResponse<MovieVideoDto>(
            results = dummyMovieVideo
        )
        val dto = dataSource.fetchMovieTrailerUrl(1)
        assertEquals(2, dto.size)
    }


    val dummyMovie = MovieDto(
        id = 1, title = "A", posterImagePath = "/p.jpg", duration = 100
    )
    val dummyMovieImage = listOf<MovieImagesDto>(
        MovieImagesDto(
            filePath = "/p.jpg"
        ), MovieImagesDto(
            filePath = "/p.jpg"
        )
    )
    val dummyActors = listOf<ActorDto>(
        ActorDto(
            name = "A",
            id = 1,
            profilePath = "/p.jpg",
        ), ActorDto(
            name = "A",
            id = 1,
            profilePath = "/p.jpg",
        )
    )
    val dummyReviews = listOf<ReviewDto>(
        ReviewDto(
            author = "A",
            content = "A",
        ), ReviewDto(
            author = "A",
            content = "A",
        )
    )
    val dummyMovieVideo = listOf<MovieVideoDto>(
        MovieVideoDto(
            key = "B", type = "trailer", site = "youtube"
        ),
        MovieVideoDto(
            key = "A", type = "trailer", site = "youtube"
        ),
    )
}