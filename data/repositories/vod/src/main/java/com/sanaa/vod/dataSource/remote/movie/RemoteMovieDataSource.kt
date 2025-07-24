package com.sanaa.vod.dataSource.remote.movie

import com.sanaa.vod.dataSource.remote.dto.ActorDto
import com.sanaa.vod.dataSource.remote.dto.GenreDto
import com.sanaa.vod.dataSource.remote.dto.ImageDto
import com.sanaa.vod.dataSource.remote.dto.MovieDto
import com.sanaa.vod.dataSource.remote.dto.ReviewDto
import com.sanaa.vod.dataSource.remote.dto.VideoDto

interface RemoteMovieDataSource {
    suspend fun fetchMovieDetails(id: Int): MovieDto
    suspend fun fetchImagesUrl(id: Int): List<ImageDto>
    suspend fun fetchCast(id: Int): List<ActorDto>
    suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDto>
    suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto>
    suspend fun fetchMoviesByCategory(category: Int): List<MovieDto>
    suspend fun fetchMovieTrailerUrl(id: Int): List<VideoDto>
    suspend fun fetchMovieGenres(): List<GenreDto>
}