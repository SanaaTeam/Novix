package com.sanaa.search

import com.example.env_config.service.LanguageProvider
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
    private val languageProvider: LanguageProvider
): SearchRemoteDataSource {

    override suspend fun searchActors(query: String, page: Int): SearchResponse<ActorSearchDto> =
        search(path = "person", query, page)

    override suspend fun searchTvShows(query: String, page: Int): SearchResponse<TvShowSearchDto> =
        search(path = "tv", query, page)

    override suspend fun searchMovies(query: String, page: Int): SearchResponse<MovieSearchDto> =
        search(path = "movie", query, page)


    private suspend inline fun <reified T> search(
        path: String,
        query: String,
        page: Int = 1,
    ): SearchResponse<T> {
        return client.get("${BuildConfig.TMDB_URL}/search/$path") {
            parameter("query", query)
            parameter("page", page)
            parameter("language", languageProvider.getCurrentLanguage())
            parameter("api_key", BuildConfig.TMDB_API_KEY)
        }.body()
    }
}