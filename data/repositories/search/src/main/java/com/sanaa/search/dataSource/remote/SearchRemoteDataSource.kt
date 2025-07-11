package com.sanaa.search.dataSource.remote

import com.sanaa.search.dataSource.remote.dto.ActorSearchDto
import com.sanaa.search.dataSource.remote.dto.MovieSearchDto
import com.sanaa.search.dataSource.remote.dto.TvShowSearchDto
import com.sanaa.search.response.SearchResponse

interface SearchRemoteDataSource {

    suspend fun searchActors(
        query: String
    ): SearchResponse<ActorSearchDto>

    suspend fun searchTv(
        query: String
    ): SearchResponse<TvShowSearchDto>

    suspend fun searchMovies(
        query: String
    ): SearchResponse<MovieSearchDto>

}