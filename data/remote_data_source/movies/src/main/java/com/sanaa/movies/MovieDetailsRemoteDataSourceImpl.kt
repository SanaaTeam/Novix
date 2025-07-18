package com.sanaa.movies

import com.example.preferences.service.LanguageProvider
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.remote.dto.*
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class MovieDetailsRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
) : MovieDetailsRemoteDataSource {

    override suspend fun fetchMovieDetails(id: Int): MovieDetailsDto =
        fetch("movie/$id")

    override suspend fun fetchImagesUrl(id: Int): MovieImagesDto =
        fetch("movie/$id/images", withLang = false)

    override suspend fun fetchCast(id: Int): CastDto =
        fetch("movie/$id/credits")

    override suspend fun fetchSimilarMoviesByMovieId(id: Int): SimilarMoviesDto =
        fetch("movie/$id/similar")

    override suspend fun fetchReviewsByMovieId(id: Int): ReviewDto =
        fetch("movie/$id/reviews")

    override suspend fun fetchMoviesByCategory(category: Int): MoviesByCategoryDto =
        fetch("movie?with_genres=$category")

    private suspend inline fun <reified T> fetch(
        path: String,
        withLang: Boolean = true
    ): T = fetchInternal(path, withLang, typeInfo<T>())

    private suspend fun <T> fetchInternal(
        path: String,
        withLang: Boolean,
        type: TypeInfo
    ): T = client.get("$baseUrl/$path") {
        if (withLang) parameter("language", languageProvider.getCurrentLanguage())
        parameter("api_key", BuildConfig.TMDB_API_KEY)
    }.body(type)
}
