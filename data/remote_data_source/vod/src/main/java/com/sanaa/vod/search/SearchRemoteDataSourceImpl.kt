package com.sanaa.vod.search

import com.sanaa.vod.dataSource.remote.search.dto.ActorSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.MovieSearchDto
import com.sanaa.vod.dataSource.remote.search.dto.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.search.response.SearchResponse
import com.sanaa.vod.dataSource.remote.search.search.SearchRemoteDataSource

class SearchRemoteDataSourceImpl(
    private val searchApiService: SearchApiService,
) : SearchRemoteDataSource {

    override suspend fun searchActors(query: String, page: Int): SearchResponse<ActorSearchDto> {
        return searchApiService.searchActors(
            query = query,
            page = page
        )
    }

    override suspend fun searchTvShows(query: String, page: Int): SearchResponse<TvShowSearchDto> {
        return searchApiService.searchTvShows(
            query = query,
            page = page
        )
    }

    override suspend fun searchMovies(query: String, page: Int): SearchResponse<MovieSearchDto> {
        return searchApiService.searchMovies(
            query = query,
            page = page
        )
    }
}