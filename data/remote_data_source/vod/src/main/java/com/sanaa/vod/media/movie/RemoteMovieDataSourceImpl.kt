package com.sanaa.vod.media.movie

import android.util.Log
import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource
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
        apiService.fetchCast(id).cast
    }

    override suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDto> = wrapApiCall {
        apiService.fetchSimilarMoviesByMovieId(id).results
    }

    override suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto> = wrapApiCall {
        apiService.fetchReviewsByMovieId(id).results
    }


    override suspend fun fetchMoviesByCategory(category: Int): List<MovieDto> = wrapApiCall {
        apiService.fetchMoviesByCategory(category).results
    }

    override suspend fun fetchMovieTrailerUrl(id: Int): List<VideoDto> = wrapApiCall {
        apiService.fetchMovieTrailerUrl(id).results
    }

    override suspend fun fetchMovieGenres(): List<GenreDto> {
        return wrapApiCall {
            apiService.fetchMovieGenres().genres
        }
    }

    override suspend fun fetchPopularMovies(page: Int): List<MovieDto> {
        val list =         apiService.getPopularMovies(page).results
        Log.d("test99", "fetchPopularMovies: $list")
        return list
    }

    override suspend fun fetchTrendingMovies(page: Int, genreId: Int?): List<MovieDto> =
        apiService.fetchTrendingMovies(page, genreId?.toString()).results

    override suspend fun fetchTopRatedMovies(page: Int, genreId: Int?): List<MovieDto> =
        apiService.fetchTopRatingMovies(page, genreId?.toString()).results

    override suspend fun fetchUpcomingMovies(page: Int, genreId: Int?): List<MovieDto> =
        apiService.fetchUpcomingMovies(page, genreId?.toString()).results
}