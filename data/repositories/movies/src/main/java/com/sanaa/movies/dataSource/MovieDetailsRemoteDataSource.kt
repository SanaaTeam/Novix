package com.sanaa.movies.dataSource

import com.sanaa.movies.dataSource.dto.ActorDto
import com.sanaa.movies.dataSource.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.dto.ReviewDto

interface MovieDetailsRemoteDataSource {
    suspend fun fetchMovieDetails(id: Int): MovieDetailsDto
    suspend fun fetchImages(id: Int): List<String>
    suspend fun fetchCast(id: Int): List<ActorDto>
    suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDetailsDto>
    suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto>
    suspend fun fetchMoviesByCategory(category: Int): List<MovieDetailsDto>
}