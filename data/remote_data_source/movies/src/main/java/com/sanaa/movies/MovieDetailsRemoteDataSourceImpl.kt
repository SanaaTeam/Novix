package com.sanaa.movies

import com.sanaa.preferences.service.LanguageProvider
import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.remote.dto.CastDto
import com.sanaa.movies.dataSource.remote.dto.MovieDetailsDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.MoviesByCategoryResponse
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.dataSource.remote.dto.SimilarMoviesDto
import com.sanaa.movies.dataSource.remote.dto.VideoResponseDto
import com.sanaa.movies.response.MovieReviewsResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class MovieDetailsRemoteDataSourceImpl(
    private val client: HttpClient,
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

    override suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto> =
        fetch<MovieReviewsResponse>("movie/$id/reviews").results

    override suspend fun fetchMoviesByCategory(category: Int): MoviesByCategoryResponse =
        fetch("discover/movie?with_genres=$category")

    override suspend fun fetchMovieTrailerUrl(id: Int): VideoResponseDto =
        fetch("movie/$id/videos", withLang = false)

    private suspend inline fun <reified T> fetch(
        path: String,
        withLang: Boolean = true
    ): T = fetchInternal(path, withLang, typeInfo<T>())

    private suspend fun <T> fetchInternal(
        path: String,
        withLang: Boolean,
        type: TypeInfo
    ): T = client.get("${BuildConfig.TMDB_URL}/$path") {
        if (withLang) parameter("language", languageProvider.getCurrentLanguage())
        parameter("api_key", BuildConfig.TMDB_API_KEY)
    }.body(type)
}
