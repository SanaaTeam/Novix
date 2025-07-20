package com.sanaa.movies

import com.sanaa.movies.dataSource.remote.MovieDetailsRemoteDataSource
import com.sanaa.movies.dataSource.remote.dto.ActorDto
import com.sanaa.movies.dataSource.remote.dto.MovieDto
import com.sanaa.movies.dataSource.remote.dto.MovieImagesDto
import com.sanaa.movies.dataSource.remote.dto.MovieVideoDto
import com.sanaa.movies.dataSource.remote.dto.ReviewDto
import com.sanaa.movies.response.MovieApiResponse
import com.sanaa.movies.response.MovieCastResponse
import com.sanaa.movies.response.MovieImagesResponse
import com.sanaa.preferences.service.LanguageProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo

class MovieDetailsRemoteDataSourceImpl(
    private val client: HttpClient, private val languageProvider: LanguageProvider
) : MovieDetailsRemoteDataSource {

    override suspend fun fetchMovieDetails(id: Int): MovieDto = fetch("movie/$id")

    override suspend fun fetchImagesUrl(id: Int): List<MovieImagesDto> =
        fetch<MovieImagesResponse>("movie/$id/images").backdrops


    override suspend fun fetchCast(id: Int): List<ActorDto> =
        fetch<MovieCastResponse>("movie/$id/credits").cast

    override suspend fun fetchSimilarMoviesByMovieId(id: Int): List<MovieDto> =
        fetch<MovieApiResponse<MovieDto>>("movie/$id/similar").results


    override suspend fun fetchReviewsByMovieId(id: Int): List<ReviewDto> =
        fetch<MovieApiResponse<ReviewDto>>("movie/$id/reviews").results


    override suspend fun fetchMoviesByCategory(category: Int): List<MovieDto> =
        fetch<MovieApiResponse<MovieDto>>("discover/movie?with_genres=$category").results


    override suspend fun fetchMovieTrailerUrl(id: Int): List<MovieVideoDto> =
        fetch<MovieApiResponse<MovieVideoDto>>("movie/$id/videos", withLang = false).results


    private suspend inline fun <reified T> fetch(
        path: String, withLang: Boolean = true
    ): T = fetchInternal(path, withLang, typeInfo<T>())

    private suspend fun <T> fetchInternal(
        path: String, withLang: Boolean, type: TypeInfo
    ): T = client.get("${BuildConfig.TMDB_URL}/$path") {
        if (withLang) parameter("language", languageProvider.getCurrentLanguage())
        parameter("api_key", BuildConfig.TMDB_API_KEY)
    }.body(type)
}
