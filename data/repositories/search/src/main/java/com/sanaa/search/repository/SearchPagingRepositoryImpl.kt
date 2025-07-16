package com.sanaa.search.repository

import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toSearchOutput
import search.repository.SearchPagingRepository
import search.usecase.search_param.MediaFilters
import search.usecase.search_param.MediaType
import search.usecase.search_param.SearchActorOutput
import search.usecase.search_param.SearchMediaOutput

class SearchPagingRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource
) : SearchPagingRepository {
    override suspend fun searchMovies(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType,
    ): List<SearchMediaOutput> {
        return remoteDataSource.searchMovies(query, page).results.map {
            it.toSearchOutput(false)
        }
    }

    override suspend fun searchTvShows(
        query: String,
        page: Int,
        filters: MediaFilters?,
        mediaType: MediaType,
    ): List<SearchMediaOutput> {
        return remoteDataSource.searchMovies(query, page).results.map {
            it.toSearchOutput(false)
        }
    }

    override suspend fun searchActors(query: String, page: Int): List<SearchActorOutput> {
        return remoteDataSource.searchActors(query, page)
            .results
            .map { it.toSearchOutput() }
    }
}
