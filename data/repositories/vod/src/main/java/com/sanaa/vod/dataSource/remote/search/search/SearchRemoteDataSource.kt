package com.sanaa.vod.dataSource.remote.search.search

import com.sanaa.vod.dataSource.remote.search.dto.ActorSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.search.response.SearchResponse

interface SearchRemoteDataSource {

    suspend fun searchActors(
        query: String,
        page: Int = 1,
    ): SearchResponse<ActorSearchDto>

    suspend fun searchTvShows(
        query: String,
        page: Int = 1,
    ): SearchResponse<TvShowSearchDto>

    suspend fun searchMovies(
        query: String,
        page: Int = 1,
    ): SearchResponse<MovieSearchDto>

}