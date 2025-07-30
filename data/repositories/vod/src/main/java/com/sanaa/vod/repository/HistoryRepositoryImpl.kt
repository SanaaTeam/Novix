package com.sanaa.vod.repository

import com.sanaa.vod.dataSource.local.search.LocalSearchHistoryDataSource
import com.sanaa.vod.mapper.search.toDto
import com.sanaa.vod.mapper.search.toEntity
import com.sanaa.vod.util.safeCall
import entity.MediaType
import entity.Movie
import entity.TvSeries
import exceptions.FailedToAddException
import exceptions.FailedToDeleteException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import repository.HistoryRepository
import usecase.history.ManageWatchedMediaHistoryUseCase
import usecase.history.ManageWatchedMediaHistoryUseCase.MediaHistoryItem
import usecase.history.history_param.SearchHistory
import usecase.search.ManageRecentViewedUseCase.RecentViewedMedia

class HistoryRepositoryImpl(
    private val local: LocalSearchHistoryDataSource
) : HistoryRepository {

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

    override suspend fun addWatchedMediaHistory(media: MediaHistoryItem) {

    }

    override suspend fun getWatchedMediaHistory(
        mediaType: MediaType?,
        genreId: Int?
    ): List<ManageWatchedMediaHistoryUseCase.MediaHistoryItem> {
        return emptyList()
    }
}