package com.sanaa.search.repository

import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.mapper.toDto
import com.sanaa.search.mapper.toEntity
import exceptions.FailedToAddException
import exceptions.FailedToDeleteException
import exceptions.RetrievingDataFailureException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import repository.SearchHistoryRepository
import usecase.search.RecentViewedMedia
import usecase.search.SearchHistory

class SearchHistoryRepositoryImpl(
    private val local: LocalSearchHistoryDataSource
) : SearchHistoryRepository {
    override suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>> {
        try {
            return local.getAllQueries(sizeLimit).map {
                it.map { query -> query.toEntity() }
            }
        } catch (_: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve search history")
        }
    }

    override suspend fun addSearchHistory(query: String) {
        try {
            local.insertQuery(query = query)
        } catch (_: Exception) {
            throw FailedToAddException("Failed to add search history")
        }
    }

    override suspend fun clearSearchHistory() {
        try {
            local.deleteAllQueries()
        } catch (_: Exception) {
            throw FailedToDeleteException("Failed to clear search history")
        }
    }

    override suspend fun removeSearchHistoryById(id: Int) {
        try {
            local.deleteQueryById(id)
        } catch (_: Exception) {
            throw FailedToDeleteException("Failed to remove search history")
        }
    }

    override suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>> {
        try {
            return local.getAllRecentViewed(sizeLimit).map {
                it.map { recentViewed -> recentViewed.toEntity() }
            }
        } catch (_: Exception) {
            throw RetrievingDataFailureException("Failed to retrieve recent viewed")
        }
    }

    override suspend fun addRecentViewedMedia(item: RecentViewedMedia) {
        try {
            local.insertRecentViewed(item.toDto())
        } catch (_: Exception) {
            throw FailedToAddException("Failed to add recent viewed")
        }
    }

    override suspend fun clearRecentViewed() {
        try {
            local.deleteAllRecentViewed()
        } catch (_: Exception) {
            throw FailedToDeleteException("Failed to clear recent viewed")
        }
    }
}