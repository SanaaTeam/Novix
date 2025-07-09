package com.sanaa.search
import com.sanaa.search.dto.ActorSearchDto
import com.sanaa.search.dto.MovieSearchDto
import com.sanaa.search.dto.TvSearchDto
import com.sanaa.search.response.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body

import io.ktor.client.request.get
import io.ktor.client.request.parameter


class SearchRemoteDataSourceImpl(private val client: HttpClient) {
    private val BASE_URL = "https://api.themoviedb.org/3"
    private val API_KEY = "75d83fe5482f3d79987ef22e6dae9800"

    suspend fun searchPerson(query: String, page: Int = 1): SearchResponse<ActorSearchDto> {
        return client.get("$BASE_URL/search/person") {
            parameter("query", query)
            parameter("page", page)
            parameter("language", "en-US")
            parameter("include_adult", false)
            parameter("api_key", API_KEY)
        }.body()
    }

    suspend fun searchTv(query: String, page: Int = 1): SearchResponse<TvSearchDto> {
        return client.get("$BASE_URL/search/tv") {
            parameter("query", query)
            parameter("page", page)
            parameter("language", "en-US")
            parameter("api_key", API_KEY)
        }.body()
    }

    suspend fun searchMovie(query: String, page: Int = 1): SearchResponse<MovieSearchDto> {
        return client.get("$BASE_URL/search/movie") {
            parameter("query", query)
            parameter("page", page)
            parameter("language", "en-US")
            parameter("api_key", API_KEY)
        }.body()
    }
}
