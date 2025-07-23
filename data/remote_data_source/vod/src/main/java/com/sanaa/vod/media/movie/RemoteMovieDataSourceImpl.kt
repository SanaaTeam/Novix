package com.sanaa.vod.media.movie

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource

class RemoteMovieDataSourceImpl(
    private val apiService: MovieApiService,
) : RemoteMovieDataSource {

    override suspend fun fetchMovieDetails(id: Int): MovieDto = apiService.fetchMovieDetails(id)

    override suspend fun fetchImagesUrl(id: Int): List<ImageDto> =
        apiService.fetchImagesUrl(id).backdrops

    override suspend fun fetchCast(id: Int): List<ActorDto> = apiService.fetchCast(id).cast


    override suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDto> =
        apiService.fetchSimilarMoviesByMovieId(id).results

    override suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto> =
        apiService.fetchReviewsByMovieId(id).results


    override suspend fun fetchMoviesByCategory(category: Int): List<MovieDto> =
        apiService.fetchMoviesByCategory(category).results

    override suspend fun fetchMovieTrailerUrl(id: Int): List<VideoDto> =
        apiService.fetchMovieTrailerUrl(id).results

    override suspend fun fetchPopularMovies(page: Int): List<MovieDto> =
        apiService.getPopularMovies(page).results

    override suspend fun fetchTrendingMovies(page: Int, genreId: String?): List<MovieDto> =
        apiService.fetchTrendingMovies(page, genreId).results

    override suspend fun fetchTopRatedMovies(page: Int, genreId: String?): List<MovieDto> =
        apiService.fetchTopRatingMovies(page).results

    override suspend fun fetchUpcomingMovies(page: Int, genreId: String?): List<MovieDto> =
        apiService.fetchUpcomingMovies(page).results
}