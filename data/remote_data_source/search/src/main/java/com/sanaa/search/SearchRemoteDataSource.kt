package com.sanaa.search

import com.sanaa.search.dto.ActorSearchDto
import com.sanaa.search.dto.MovieSearchDto
import com.sanaa.search.dto.TvSearchDto
import com.sanaa.search.response.SearchResponse

interface SearchRemoteDataSource {
    suspend fun searchPerson(query: String, page: Int = 1): SearchResponse<ActorSearchDto>
    suspend fun searchTv(query: String, page: Int = 1): SearchResponse<TvSearchDto>
    suspend fun searchMovie(query: String, page: Int = 1): SearchResponse<MovieSearchDto>
}