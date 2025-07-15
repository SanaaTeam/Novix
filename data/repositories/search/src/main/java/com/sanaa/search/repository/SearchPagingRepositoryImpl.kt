package com.sanaa.search.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.sanaa.search.dataSource.remote.SearchRemoteDataSource
import com.sanaa.search.paging.SearchActorsPagingSource
import com.sanaa.search.paging.SearchMoviesPagingSource
import com.sanaa.search.paging.SearchTvShowsPagingSource
import kotlinx.coroutines.flow.Flow
import repository.SearchPagingRepository
import usecase.search.MediaFilters
import usecase.search.SearchActorOutput
import usecase.search.SearchMediaOutput

class SearchPagingRepositoryImpl(
    private val remoteDataSource: SearchRemoteDataSource
) : SearchPagingRepository {

    override fun searchMovies(
        query: String,
        filters: MediaFilters?
    ): Flow<PagingData<SearchMediaOutput>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                SearchMoviesPagingSource(
                    remoteDataSource = remoteDataSource,
                    query = query,
                    filters = filters
                )
            }
        ).flow
    }

    override fun searchTvShows(
        query: String,
        filters: MediaFilters?
    ): Flow<PagingData<SearchMediaOutput>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                SearchTvShowsPagingSource(
                    remoteDataSource = remoteDataSource,
                    query = query,
                    filters = filters
                )
            }
        ).flow
    }

    override fun searchActors(query: String): Flow<PagingData<SearchActorOutput>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = PREFETCH_DISTANCE
            ),
            pagingSourceFactory = {
                SearchActorsPagingSource(
                    remoteDataSource = remoteDataSource,
                    query = query
                )
            }
        ).flow
    }

    companion object {
        private const val PAGE_SIZE = 10
        private const val PREFETCH_DISTANCE = 3
    }
} 