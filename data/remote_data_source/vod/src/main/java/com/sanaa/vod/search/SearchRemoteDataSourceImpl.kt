package com.sanaa.vod.search

import com.sanaa.vod.dataSource.remote.SearchRemoteDataSource
import com.sanaa.vod.dataSource.remote.dto.search.ActorSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.MovieSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.TvShowSearchDto
import com.sanaa.vod.dataSource.remote.dto.search.response.SearchResponse
import com.sanaa.vod.util.wrapApiCall
import javax.inject.Inject

class SearchRemoteDataSourceImpl @Inject constructor(
    private val searchApiService: SearchApiService,
) : SearchRemoteDataSource {

    override suspend fun searchActors(query: String, page: Int): SearchResponse<ActorSearchDto> {
        return wrapApiCall {
            searchApiService.searchActors(
                query = query, page = page
            )
        }
    }

    override suspend fun searchTvShows(query: String, page: Int): SearchResponse<TvShowSearchDto> {
        return wrapApiCall {
            searchApiService.searchTvShows(
                query = query, page = page
            )
        }
    }

    override suspend fun searchMovies(query: String, page: Int): SearchResponse<MovieSearchDto> {
        return wrapApiCall {
            searchApiService.searchMovies(
                query = query, page = page
            )
        }
    }
}