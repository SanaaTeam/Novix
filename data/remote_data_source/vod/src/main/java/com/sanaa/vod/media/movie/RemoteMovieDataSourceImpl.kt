package com.sanaa.vod.media.movie

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
import com.sanaa.vod.media.movie.request.MovieRateRequest
import com.sanaa.vod.util.wrapApiCall

class RemoteMovieDataSourceImpl(
    private val apiService: MovieApiService,
) : RemoteMovieDataSource {

    override suspend fun fetchMovieDetails(id: Int): MovieDto =
        wrapApiCall { apiService.fetchMovieDetails(id) }

    override suspend fun fetchImagesUrl(id: Int): List<ImageDto> = wrapApiCall {
        apiService.fetchImagesUrl(id).backdrops
    }

    override suspend fun fetchCast(id: Int): List<ActorDto> = wrapApiCall {
        apiService.fetchCast(id).cast.distinctBy { it.id }
    }

    override suspend fun fetchSimilarMoviesByMovieId(id: Int, page: Int): List<MovieDto> = wrapApiCall {
        apiService.fetchSimilarMoviesByMovieId(id, page).results.distinctBy { it.id }
    }

    override suspend fun fetchReviewsByMovieId(id: Int, page: Int): List<ReviewDto> = wrapApiCall {
        apiService.fetchReviewsByMovieId(id, page).results.distinctBy { it.id }
    }


    override suspend fun fetchMoviesByCategory(category: Int, page: Int): List<MovieDto> =
        wrapApiCall {
            apiService.fetchMoviesByCategory(
                category = category,
                page = page
            ).results.distinctBy { it.id }
        }

    override suspend fun fetchMovieTrailerUrl(id: Int): List<VideoDto> = wrapApiCall {
        apiService.fetchMovieTrailerUrl(id).results
    }

    override suspend fun fetchMovieGenres(): List<GenreDto> {
        return wrapApiCall {
            apiService.fetchMovieGenres().genres
        }
    }

    override suspend fun fetchPopularMovies(page: Int): List<MovieDto> =
        apiService.getPopularMovies(page).results.distinctBy { it.id }

    override suspend fun fetchTrendingMovies(page: Int, genreId: Int?): List<MovieDto> =
        apiService.fetchTrendingMovies(page, genreId?.toString()).results.distinctBy { it.id }

    override suspend fun fetchTopRatedMovies(page: Int, genreId: Int?): List<MovieDto> =
        apiService.fetchTopRatingMovies(page, genreId?.toString()).results.distinctBy { it.id }

    override suspend fun fetchUpcomingMovies(page: Int, genreId: Int?): List<MovieDto> =
        apiService.fetchUpcomingMovies(page, genreId?.toString()).results.distinctBy { it.id }

    override suspend fun sendMovieRate(
        movieId: Int,
        sessionId: String,
        rating: Float
    ) =
        apiService.rateMovie(
            movieId = movieId,
            sessionId = sessionId,
            rating = MovieRateRequest(value = rating)
        )

}