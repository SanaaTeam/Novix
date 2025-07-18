package com.sanaa.movies.dataSource.remote

import com.sanaa.movies.dataSource.remote.dto.CastDto
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.MoviesByCategoryResponse
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import com.sanaa.movies.dataSource.remote.dto.VideoResponseDto

interface MovieDetailsRemoteDataSource {
    suspend fun fetchMovieDetails(id: Int): MovieDetailsDto
    suspend fun fetchImagesUrl(id: Int): MovieImagesDto
    suspend fun fetchCast(id: Int): CastDto
    suspend fun fetchSimilarMoviesByMovieId(id: Int): SimilarMoviesDto
    suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto>
    suspend fun fetchMoviesByCategory(category: Int): MoviesByCategoryResponse
    suspend fun fetchMovieTrailerUrl(id: Int): VideoResponseDto
}