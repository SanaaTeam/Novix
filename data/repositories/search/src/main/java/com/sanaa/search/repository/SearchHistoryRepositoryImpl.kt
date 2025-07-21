package com.sanaa.search.repository

import com.sanaa.search.dataSource.local.LocalSearchHistoryDataSource
import com.sanaa.search.mapper.toDto
import com.sanaa.search.mapper.toEntity
import exceptions.FailedToAddException
import exceptions.FailedToDeleteException
import exceptions.NoNetworkException
import exceptions.RetrievingDataFailureException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import search.repository.SearchHistoryRepository
import search.usecase.ManageRecentViewedUseCase.RecentViewedMedia
import search.usecase.search_param.SearchHistory
import java.net.UnknownHostException

class SearchHistoryRepositoryImpl(
    private val local: LocalSearchHistoryDataSource
) : SearchHistoryRepository {

    override suspend fun getSearchHistory(sizeLimit: Int): Flow<List<SearchHistory>> = safeCall(
        errorMessage = "Failed to retrieve search history"
    ) {
        local.getQueries(sizeLimit).map {
            it.map { query -> query.toEntity() }
        }
    }

    override suspend fun addSearchHistory(query: String) = safeCall(
        errorMessage = "Failed to add search history",
        exceptionProvider = ::FailedToAddException
    ) {
        local.insertQuery(query)
    }


    override suspend fun clearSearchHistory() = safeCall(
        errorMessage = "Failed to clear search history",
        exceptionProvider = ::FailedToDeleteException
    ) {
        local.deleteAllQueries()
    }


    override suspend fun removeSearchHistoryById(id: Int) = safeCall(
        errorMessage = "Failed to remove search history",
        exceptionProvider = ::FailedToDeleteException
    ) {
        local.deleteQueryById(id)
    }


    override suspend fun getRecentViewed(sizeLimit: Int): Flow<List<RecentViewedMedia>> = safeCall(
        errorMessage = "Failed to retrieve recent viewed"
    ) {
        local.getAllRecentViewed(sizeLimit).map {
            it.map { recentViewed -> recentViewed.toEntity() }
        }
    }

    override suspend fun addRecentViewedMedia(item: RecentViewedMedia) = safeCall(
        errorMessage = "Failed to add recent viewed",
        exceptionProvider = ::FailedToAddException
    ) {
        local.insertRecentViewed(item.toDto())
    }

    override suspend fun clearRecentViewed() = safeCall(
        errorMessage = "Failed to clear recent viewed",
        exceptionProvider = ::FailedToDeleteException
    ) {
        local.deleteAllRecentViewed()
    }

    private inline fun <T> safeCall(
        errorMessage: String,
        exceptionProvider: (String) -> Exception = { msg -> RetrievingDataFailureException(msg) },
        block: () -> T
    ): T {
        try {
            return block()
        } catch (_: UnknownHostException) {
            throw NoNetworkException()
        } catch (e: Exception) {
            throw exceptionProvider("$errorMessage: ${e.message}")
        }
    }

}