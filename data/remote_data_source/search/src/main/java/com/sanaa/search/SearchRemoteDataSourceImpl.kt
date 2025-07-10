package com.sanaa.search

import com.sanaa.search.dataSource.SearchRemoteDataSource
import com.sanaa.search.dto.ActorSearchDto
import com.sanaa.search.dto.MovieSearchDto
import com.sanaa.search.dto.TvShowSearchDto
import com.sanaa.search.response.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body

import io.ktor.client.request.get
import io.ktor.client.request.parameter

class SearchRemoteDataSourceImpl(
    private val client: HttpClient,
): SearchRemoteDataSource {

    private companion object {
        const val BASE_URL = "https://api.themoviedb.org/3"
        const val PAGE_NUMBER = 1
    }

    private suspend inline fun <reified T> search(
        path: String,
        query: String,
    ): SearchResponse<T> {
        return client.get("$BASE_URL/search/$path") {
            parameter("query", query)
            parameter("page", PAGE_NUMBER)
            parameter("language", "en")
            parameter("api_key", TmdbConfig.apiKey)
        }.body()
    }

    override suspend fun searchActors(query: String): SearchResponse<ActorSearchDto> =
        search(path = "person", query)

    override suspend fun searchTv(query: String): SearchResponse<TvShowSearchDto> =
        search(path = "tv", query)

    override suspend fun searchMovies(query: String): SearchResponse<MovieSearchDto> =
        search(path = "movie", query)
}