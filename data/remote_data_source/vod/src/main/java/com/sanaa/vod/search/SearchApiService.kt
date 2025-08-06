package com.sanaa.vod.search

import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET("search/person")
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse<ActorSearchDto>

    @GET("search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse<TvShowSearchDto>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse<MovieSearchDto>
}
