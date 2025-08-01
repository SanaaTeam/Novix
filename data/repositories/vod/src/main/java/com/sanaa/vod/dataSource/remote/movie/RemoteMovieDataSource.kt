package com.sanaa.vod.dataSource.remote.movie

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.RatingResponse
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.dto.WatchlistActionDto
import com.sanaa.vod.dataSource.remote.dto.WatchlistRequestBody

interface RemoteMovieDataSource {
    suspend fun fetchMovieDetails(id: Int): MovieDto
    suspend fun fetchImagesUrl(id: Int): List<ImageDto>
    suspend fun fetchCast(id: Int): List<ActorDto>
    suspend fun fetchSimilarMoviesByMovieId(id: Int, page: Int): List<MovieDto>
    suspend fun fetchReviewsByMovieId(id: Int, page: Int): List<ReviewDto>
    suspend fun fetchMoviesByCategory(category: Int, page: Int): List<MovieDto>
    suspend fun fetchMovieTrailerUrl(id: Int): List<VideoDto>
    suspend fun fetchMovieGenres(): List<GenreDto>
    suspend fun fetchPopularMovies(page: Int): List<MovieDto>
    suspend fun fetchTrendingMovies(page: Int, genreId: Int?): List<MovieDto>
    suspend fun fetchTopRatedMovies(page: Int, genreId: Int?): List<MovieDto>
    suspend fun fetchUpcomingMovies(page: Int, genreId: Int?): List<MovieDto>
    suspend fun fetchWatchlistMovies(page: Int): List<MovieDto>
    suspend fun addToWatchlist(body: WatchlistRequestBody): WatchlistActionDto


    suspend fun fetchMoviesRate(accountId: Long, sessionId: String): List<MovieDto>
    suspend fun sendMovieRate(movieId: Int, sessionId: String, rating: Float): RatingResponse
}