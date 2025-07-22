package com.sanaa.vod.media.movie

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto
import com.sanaa.vod.dataSource.remote.movie.RemoteMovieDataSource

class RemoteMovieDataSourceImp(
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

}