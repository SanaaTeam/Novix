package com.sanaa.movies

import com.example.env_config.service.LanguageProvider
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.remote.dto.CastDto
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter


class MovieDetailsRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
) : MovieDetailsRemoteDataSource {

    private suspend inline fun <reified T> fetch(
        path: String,
        withLang: Boolean = true,
    ): T = client.get("$baseUrl/$path") {
        if (withLang) parameter("language", languageProvider.getCurrentLanguage())
        parameter("api_key", BuildConfig.TMDB_API_KEY)
    }.body()


    override suspend fun fetchMovieDetails(id: Int): MovieDetailsDto =
        fetch("movie/$id")

    override suspend fun fetchImages(id: Int): MovieImagesDto  =
        fetch("movie/$id/images")


    override suspend fun fetchCast(id: Int): CastDto =
        fetch("movie/$id/credits")

    override suspend fun fetchSimilarMoviesByMovieId(id: Int): SimilarMoviesDto =
        fetch("movie/$id/similar")


    override suspend fun fetchReviewsByMovieId(id: Int): ReviewDto =
        fetch("movie/$id/reviews")


    override suspend fun fetchMoviesByCategory(category: Int): List<MovieDetailsDto> {
        return client.get("$baseUrl/movie") {
            parameter("with_genres", category)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }

}