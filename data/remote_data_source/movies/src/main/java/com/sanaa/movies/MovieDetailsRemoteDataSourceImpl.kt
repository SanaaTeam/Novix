package com.sanaa.movies

import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.remote.dto.ActorDto
import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.preferences.service.LanguageProvider

class MovieDetailsRemoteDataSourceImpl(
    private val apiService: MovieApiService,
    private val languageProvider: LanguageProvider
) : MovieDetailsRemoteDataSource {

    override suspend fun fetchMovieDetails(id: Int): MovieDto = apiService.fetchMovieDetails(id)

    override suspend fun fetchImagesUrl(id: Int): List<MovieImagesDto> =
        apiService.fetchImagesUrl(id).backdrops

    override suspend fun fetchCast(id: Int): List<ActorDto> = apiService.fetchCast(id).cast


    override suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDto> =
        apiService.fetchSimilarMoviesByMovieId(id).results

    override suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto> =
        apiService.fetchReviewsByMovieId(id).results


    override suspend fun fetchMoviesByCategory(category: Int): List<MovieDto> =
        apiService.fetchMoviesByCategory(category).results

    override suspend fun fetchMovieTrailerUrl(id: Int): List<MovieVideoDto> =
        apiService.fetchMovieTrailerUrl(id).results

}
