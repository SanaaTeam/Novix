package com.sanaa.vod.dataSource.remote

import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.response.SearchResponse

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