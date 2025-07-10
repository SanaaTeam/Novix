package com.sanaa.search.dataSource

import com.sanaa.search.dto.ActorSearchDto
import com.sanaa.search.dto.MovieSearchDto
import com.sanaa.search.dto.TvShowSearchDto
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