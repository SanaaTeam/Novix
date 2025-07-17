package com.sanaa.search

import com.example.preferences.service.LanguageProvider
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import com.sanaa.search.dataSource.remote.response.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body

import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchRemoteDataSourceImpl(
    private val client: HttpClient,
    private val baseUrl: String,
    private val languageProvider: LanguageProvider
): SearchRemoteDataSource {

    private suspend inline fun <reified T> search(
        path: String,
        query: String,
    ): SearchResponse<T> {
        return client.get("$baseUrl/search/$path") {
            parameter("query", query)
            parameter("page", PAGE_NUMBER)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }

    override suspend fun searchActors(query: String): SearchResponse<ActorSearchDto> =
        search(path = "person", query)

    override suspend fun searchTv(query: String): SearchResponse<TvShowSearchDto> =
        search(path = "tv", query)

    override suspend fun searchMovies(query: String): SearchResponse<MovieSearchDto> =
        search(path = "movie", query)

    private companion object {
        const val PAGE_NUMBER = 1
    }

}