package com.sanaa.vod.search

import com.sanaa.vod.dataSource.remote.search.dto.ActorSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.search.response.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApiService {

    @GET("3/search/person")
    suspend fun searchActors(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse<ActorSearchDto>

    @GET("3/search/tv")
    suspend fun searchTvShows(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse<TvShowSearchDto>

    @GET("3/search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): SearchResponse<MovieSearchDto>
}
