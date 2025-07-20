package com.sanaa.movies.dataSource.remote

import com.sanaa.movies.dataSource.remote.dto.ActorDto
import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto

interface MovieDetailsRemoteDataSource {
    suspend fun fetchMovieDetails(id: Int): MovieDto
    suspend fun fetchImagesUrl(id: Int): List<MovieImagesDto>
    suspend fun fetchCast(id: Int): List<ActorDto>
    suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDto>
    suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto>
    suspend fun fetchMoviesByCategory(category: Int): List<MovieDto>
    suspend fun fetchMovieTrailerUrl(id: Int): List<MovieVideoDto>
}