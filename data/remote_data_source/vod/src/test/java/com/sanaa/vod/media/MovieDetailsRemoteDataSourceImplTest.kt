package com.sanaa.vod.media

import com.google.common.truth.Truth.assertThat
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.media.movie.MovieApiService
import com.sanaa.vod.media.movie.RemoteMovieDataSourceImpl
import com.sanaa.vod.media.movie.response.MovieApiResponse
import com.sanaa.vod.media.movie.response.MovieCastResponse
import com.sanaa.vod.media.movie.response.MovieImagesResponse
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MovieDetailsRemoteDataSourceImplTest {

    private lateinit var dataSource: RemoteMovieDataSource
    private val apiService: MovieApiService = mockk()

    @BeforeEach
    fun setup() {
        dataSource = RemoteMovieDataSourceImpl(apiService)
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
        coEvery { apiService.fetchSimilarMoviesByMovieId(1,1) } returns MovieApiResponse<MovieDto>(
            results = listOf(dummyMovie)
        )
        val dto = dataSource.fetchSimilarMoviesByMovieId(1,1)

        assertEquals(1, dto.size)
    }

    @Test
    fun `fetchReviewsByMovieId() should return list of reviews dto`() = runTest {
        coEvery { apiService.fetchReviewsByMovieId(1,1) } returns MovieApiResponse<ReviewDto>(
            results = dummyReviews
        )

        val dto = dataSource.fetchReviewsByMovieId(1,1)

        assertEquals(2, dto.size)
    }

    @Test
    fun `fetchMoviesByCategory() should return list of movies dto`() = runTest {
        coEvery { apiService.fetchMoviesByCategory(1,1) } returns MovieApiResponse<MovieDto>(
            results = listOf(dummyMovie)
        )
        val dto = dataSource.fetchMoviesByCategory(1,1)

        assertEquals(1, dto.size)
    }

    @Test
    fun `fetchMovieTrailerUrl() should return list of movies dto`() = runTest {
        coEvery { apiService.fetchMovieTrailerUrl(1) } returns MovieApiResponse<VideoDto>(
            results = dummyMovieVideo
        )
        val dto = dataSource.fetchMovieTrailerUrl(1)
        assertEquals(2, dto.size)
    }

    @Test
    fun `fetchMovieGenres() should return list of movies dto`() = runTest {
        coEvery { apiService.fetchMovieGenres() } returns MovieApiResponse<GenreDto>(genres = dummyGenreDto)

        val dto = dataSource.fetchMovieGenres()

        assertThat(dto.size).isEqualTo(2)
    }


    val dummyMovie = MovieDto(
        id = 1, title = "A", posterImagePath = "/p.jpg", duration = 100
    )
    val dummyMovieImage = listOf<ImageDto>(
        ImageDto(
            filePath = "/p.jpg"
        ), ImageDto(
            filePath = "/p.jpg"
        )
    )
    val dummyActors = listOf<ActorDto>(
        ActorDto(
            name = "A",
            id = 1,
        ), ActorDto(
            name = "A",
            id = 1,
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
    val dummyMovieVideo = listOf<VideoDto>(
        VideoDto(
            key = "B", type = "trailer", site = "youtube"
        ),
        VideoDto(
            key = "A", type = "trailer", site = "youtube"
        ),
    )

    val dummyGenreDto = listOf<GenreDto>(GenreDto(id = 1, name = "A"), GenreDto(id = 2, name = "B"))

}