package com.sanaa.search.dataSource.remote

import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import com.sanaa.search.dataSource.remote.response.SearchResponse

interface SearchRemoteDataSource {

    suspend fun searchActors(
        query: String,
        page: Int = 1
    ): SearchResponse<ActorSearchDto>

    suspend fun searchTv(
        query: String,
        page: Int = 1
    ): SearchResponse<TvShowSearchDto>

    suspend fun searchMovies(
        query: String,
        page: Int = 1
    ): SearchResponse<MovieSearchDto>

}