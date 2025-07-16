package com.sanaa.search.repository

import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.mapper.toSearchOutput
import repository.SearchPagingRepository
import usecase.search.MediaFilters
import usecase.search.MediaType
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

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
